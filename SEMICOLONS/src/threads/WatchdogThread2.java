package threads;

import engine.Runphase;
import engine.sys;
import libraries.Global;
import main.Main;
import awtcomponents.AWTANSI;
import components.ProtectedTextComponent;
import components.Shell;

public class WatchdogThread2 implements InternalThread {
	private Thread watchdogThread2;

	protected WatchdogThread2() {
		watchdogThread2 = new Thread(null, new Runnable() {
			public void run() {
				Thread.currentThread().setPriority(4); //1 is MIN_PRIORITY, 10 is MAX_PRIORITY
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				while (true) {
					if (Global.getCurrentPhase().equals(Runphase.RUN)) {
						if (ThreadAllocation.isWDTActive()) {
							
							try {
								if (Main.javafxEnabled && Main.jfxWinloader.getCmdLine() != null)
									sys.log("WDT2", 1, "Not setting caret to last position");
									// set jfx caret to last pos
								else
									Main.mainFrameAWT.getCmdLine()
											.setCaretPosition(Main.mainFrameAWT.getCmdLine().getText().length());
							} catch (NullPointerException npe) {
								sys.log("WDT2", 3, "Setting cursor to last text position threw an error. Main.mainFrame probably is null.");
							} catch (IllegalArgumentException iae) {
								sys.log("WDT2", 3, "Setting cursor to last text position threw an error, because the set position was out of range.");
							}
							
							// This check often fails for no reason, so it is left out.
							/*if (Main.javafxEnabled
									&& Global.getCurrentPhase().equals(Runphase.RUN)
									&& Main.cmdLine.getText().equals("SHELL INIT")) {
								System.err.println("What have I done to you?!");
								WatchdogThread.stopWithError(1, 15000, "The shell didn't initialize properly. Try again.");
							}*/
						} else {
							Shell.print(AWTANSI.B_Yellow, "Init fail.\n");
							Global.setErrorRunphase();
						}
						
						try {
							if (!Main.javafxEnabled) {
								Main.mainFrameAWT.getCmdLine().setEditable(true);
								new ProtectedTextComponent(Main.mainFrameAWT.getCmdLine()).unprotectAllText();
							}
						} catch (NullPointerException npe) {
							npe.printStackTrace();
						}
						
						break;
					}
				}
				while (!ThreadAllocation.isShutdownSignalActive()) {
					try { Thread.sleep(2000); } catch (InterruptedException ie) { ie.printStackTrace(); }
					if (!ThreadAllocation.isWDTActive()) {
						WatchdogThread.stopWithError(2, 15000, "[WDT2] Watchdog Thread 1 (WDT) is found inactive.\n"
								+ "Because it cannot detect further errors, Vexus will be terminated.\n"
								+ "This is probably an internal error or bug. If this issue continues to persist\n"
								+ "and is reproducible, try restarting or reinstalling, and if that still doesn't help,\n"
								+ "contact me and I'll try to fix the problem.");
					}
					
					try {
						if (!Main.javafxEnabled) {
							Main.mainFrameAWT.getCmdLine().setEditable(true);
							new ProtectedTextComponent(Main.mainFrameAWT.getCmdLine()).unprotectAllText();
						}
					} catch (NullPointerException npe) {
						npe.printStackTrace();
					}
				}
			}
		}, "WDT2");
	}
	
	@Override
	public void start() {
		watchdogThread2.start();
	}

	@Override
	public void suspend() {
		sys.log("WDT2", 2, "WatchdogThread2 cannot be suspended.");
	}

	@Override
	public boolean isRunning() {
		return watchdogThread2.isAlive();
	}
}

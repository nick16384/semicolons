package threads;

import commandProcessing.CommandManager;
import engine.sys;
import jfxcomponents.JFxGUIThread;

/**
 * This class is responsible for managing threads. (not AWT ones!)
 */
public class ThreadAllocation {
	private static final WatchdogThread WDT = new WatchdogThread();
	private static final WatchdogThread2 WDT2 = new WatchdogThread2();
	private static final ShellWriteThread SWT = new ShellWriteThread();
	private static final CheckUserInputThread CUIT = new CheckUserInputThread();
	private static final CommandManager CMGR = new CommandManager();
	private static final JFxGUIThread JFXT = new JFxGUIThread();
	
	public static void launchAll() {
		sys.log("THREAD-ALLOC", 1, "Launching all internal threads...");
		if (WDT.isRunning())  { sys.log("THREAD-ALLOC", 2, "WatchdogThread already running."); }
		else { WDT.start(); }
		if (WDT2.isRunning()) { sys.log("THREAD-ALLOC", 2, "WatchdogThread2 already running."); }
		else { WDT2.start(); }
		if (SWT.isRunning())  { sys.log("THREAD-ALLOC", 2, "ShellWriteThread already running."); }
		else { SWT.start(); }
		if (CUIT.isRunning()) { sys.log("THREAD-ALLOC", 2, "CheckUserInputThread already running."); }
		else { CUIT.start(); }
		if (CMGR.isRunning()) { sys.log("THREAD-ALLOC", 2, "Command Manager Thread already running."); }
		else { CMGR.start(); }
		if (JFXT.isRunning()) { sys.log("THREAD-ALLOC", 2, "JavaFX GUI Thread already running."); }
		else { JFXT.start(); }
	}
	
	public static WatchdogThread getWDT()        { return WDT; }
	public static WatchdogThread2 getWDT2()      { return WDT2; }
	public static ShellWriteThread getSWT()      { return SWT; }
	public static CheckUserInputThread getCUIT() { return CUIT; }
	public static CommandManager getCMGR()       { return CMGR; }
	public static JFxGUIThread getJFXT()         { return JFXT; }
	
	public static boolean isWDTActive()  { return WDT.isRunning(); }
	public static boolean isWDT2Active() { return WDT2.isRunning(); }
	public static boolean isSWTActive()  { return SWT.isRunning(); }
	public static boolean isCUITActive() { return CUIT.isRunning(); }
	public static boolean isCMGRActive() { return CMGR.isRunning(); }
	public static boolean isJFXTActive() { return JFXT.isRunning(); }
	
	public static void shutdownVexus(int exitCode) {
		sys.log("THREAD-ALLOC", 1, "Calling WDT thread(s) shutdown.");
		WDT.shutdownVexus(exitCode);
	}
	
	public static boolean isShutdownSignalActive() {
		return WDT.isShutdownSignalActive();
	}
}

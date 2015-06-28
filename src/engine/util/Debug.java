package engine.util;

public class Debug {
	public static final boolean IGNORE_FRAME_CAP = true;
	private static final boolean LOG_TO_CONSOLE = true;

	public static void log(Object str) {
		if(LOG_TO_CONSOLE) {
			System.out.println(str.toString());
		}
	}
}

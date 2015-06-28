package engine.util;

public class IDAssigner {
	private static int currentId = 0;
	public static int getId() {
		return currentId++;
	}
}

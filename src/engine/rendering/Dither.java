package engine.rendering;

public class Dither {
	private static final int[] dither = new int[] { 1, 49, 13, 61, 4, 52, 16,
		64, 33, 17, 45, 29, 36, 20, 48, 32, 9, 57, 5, 53, 12, 60, 8, 56,
		41, 25, 37, 21, 44, 28, 40, 24, 3, 51, 15, 63, 2, 50, 14, 62, 35,
		19, 47, 31, 34, 18, 46, 30, 11, 59, 7, 55, 10, 58, 6, 54, 43, 27,
		39, 23, 42, 26, 38, 22, };

	public static double getDither(int i, int j) {
		return dither[(i & 7) + (j & 7) * 8] / 65.0;
	}
}

package engine.util;

import java.util.StringTokenizer;

public class Util {
	public static int[] toInt(Integer[] src) {
		int[] result = new int[src.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = src[i];
		}
		return result;
	}
	
	public static double saturate(double val) {
		return clamp(val, 0.0, 1.0);
	}
	
	public static int clamp(int val, int min, int max) {
		if(val < min) {
			val = min;
		} else if(val > max) {
			val = max;
		}
		return val;
	}

	public static double clamp(double val, double min, double max) {
		if(val < min) {
			val = min;
		} else if(val > max) {
			val = max;
		}
		return val;
	}

	public static int floorMod(int num, int den) {
		if(den < 0) {
			throw new IllegalArgumentException(
					"floorMod does not currently support negative" +
					"denominators");
		}
		if(num > 0) {
			return num % den;
		} else {
			int mod = (-num) % den;
			if(mod != 0) {
				mod = den - mod;
			}
			return mod;
		}
	}
	
	public static double floorMod(double num, double den) {
		if(den < 0) {
			throw new IllegalArgumentException(
					"floorMod does not currently support negative" +
					"denominators");
		}
		if(num > 0) {
			return num % den;
		} else {
			double mod = (-num) % den;
			if(mod != 0) {
				mod = den - mod;
			}
			return mod;
		}
	}

	public static void boundsCheck(int index, int min, int max) {
		if(index > max) {
			throw new IndexOutOfBoundsException(index + " is more than " + max);
		}
		if(index < min) {
			throw new IndexOutOfBoundsException(index + " is less than " + min);
		}
	}

	public static String wrapString(String str, double maxLength) {
		StringTokenizer st=new StringTokenizer(str);
		double spaceLeft=maxLength;
		int spaceWidth=1;
		StringBuilder sb = new StringBuilder();
		while(st.hasMoreTokens())
		{
			String word=st.nextToken();
			if((word.length()+spaceWidth)>spaceLeft)
			{
				sb.append("\n"+word+" ");
				spaceLeft=maxLength-word.length();
			}
			else
			{
				sb.append(word+" ");
				spaceLeft-=(word.length()+spaceWidth);
			}
		}
		return sb.toString();
	}

//	public static int floorDiv(int num, int den) {
//		if(num > 0) {
//			return num / den;
//		} else {
//			int floor = -((-num)/den);
//			int mod = (-num) % den;
//			if(mod != 0) {
//				floor--;
//			}
//			return floor;
//		}
//	}
//
}

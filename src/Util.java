public class Util
{
	public static float Clamp(float val, float min, float max)
	{
		if(val < min)
		{
			return min;
		}
		if(val > max)
		{
			return max;
		}

		return val;
	}

	public static float VectorLength(float x, float y)
	{
		return (float)Math.sqrt(x * x + y * y);
	}
}

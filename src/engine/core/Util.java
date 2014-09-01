package engine.core;

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

	public static float VectorReflect(float vec, float normal, float dot)
	{
		return vec - (normal * dot * 2);
		//return *this - (normal * (this->Dot(normal) * 2));
	}

	public static float Lerp(float a, float b, float amt)
	{
		return (b - a) * amt + a;
	}
}

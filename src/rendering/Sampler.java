package rendering;

public abstract class Sampler
{
	byte m_a;
	byte m_b;
	byte m_g;
	byte m_r;

	public byte GetA() { return m_a; }
	public byte GetB() { return m_b; }
	public byte GetG() { return m_g; }
	public byte GetR() { return m_r; }

	public void SetColor(byte a, byte b, byte g, byte r)
	{
		m_a = a;
		m_b = b;
		m_g = g;
		m_r = r;
	}

	public abstract void Sample(Bitmap texture, float srcX, float srcY);
}

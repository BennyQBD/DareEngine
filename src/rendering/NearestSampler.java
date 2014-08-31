package rendering;

public class NearestSampler extends Sampler
{
	public void Sample(Bitmap texture, float srcXFloat, float srcYFloat)
	{
		int srcX = (int)(srcXFloat * (texture.GetWidth()-1));
		int srcY = (int)(srcYFloat * (texture.GetHeight()-1));

		int srcIndex = (srcX+srcY*texture.GetWidth())*4;

		SetColor(texture.GetComponent(srcIndex),
		         texture.GetComponent(srcIndex + 1),
		         texture.GetComponent(srcIndex + 2),
		         texture.GetComponent(srcIndex + 3));

//		int destIndex = (destX+destY*dest.GetWidth())*4;
		//
//		dest.SetComponent(destIndex, 
//				m_components[srcIndex]); 
//		dest.SetComponent(destIndex + 1, 
//				m_components[srcIndex + 1]); 
//		dest.SetComponent(destIndex + 2, 
//				m_components[srcIndex + 2]); 
//		dest.SetComponent(destIndex + 3, 
//				m_components[srcIndex + 3]);
	}
}

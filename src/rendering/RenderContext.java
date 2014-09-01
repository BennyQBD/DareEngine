package rendering;

import core.*;
import physics.*;

public class RenderContext extends Bitmap
{
	public static final int TRANSPARENCY_NONE = 0;
	public static final int TRANSPARENCY_BASIC = 1;
	public static final int TRANSPARENCY_FULL = 2;

	private float m_cameraX;
	private float m_cameraY;
	private Bitmap m_font;
	private Bitmap m_fontColor;
	
	public RenderContext(int width, int height)
	{
		super(width, height);
		m_cameraX = 0.0f;
		m_cameraY = 0.0f;
		m_font = new Bitmap("./res/monospace.png");
		m_fontColor = new Bitmap(1, 1);
	}

	public void DrawString(String text, float x, float y, float size,
			byte b, byte g, byte r)
	{
		float spacingFactor = m_font.GetAspect();
		float aspect = GetAspect();
		m_fontColor.DrawPixel(0, 0, (byte)0x00, b, g, r);
		
		float halfWidth   = GetWidth()/2.0f;
		float halfHeight  = GetHeight()/2.0f;

		float currentPosX = x;
		float currentPosY = y;

		float sizeX = size;
		float sizeY = size;

		for(int i = 0; i < text.length(); i++)
		{
			char current = text.charAt(i);
			int imgX = current & 0x0F;
			int imgY = (current >> 4) & 0x0F;

			float imgXStart = (float)imgX/16.0f;
			float imgYStart = (float)imgY/16.0f + 0.01f;

			float xStart = currentPosX;
			float yStart = currentPosY;
			float xEnd = currentPosX + sizeX;
			float yEnd = currentPosY + sizeY;

			DrawImageDispatcher(m_font, m_fontColor, 
				xStart, yStart, 
				xEnd, yEnd, 
				imgXStart, imgYStart,
				(spacingFactor)/16.0f, 1.0f/16.0f,
				TRANSPARENCY_BASIC);

			currentPosX += sizeX * spacingFactor;
		}
	}

	public void SetCameraPosition(float x, float y)
	{
		m_cameraX = x;
		m_cameraY = y;
	}

	public AABB GetRenderArea()
	{
		float aspect = GetAspect();
		return 
			new AABB(-aspect + m_cameraX, -1 + m_cameraY, 
					aspect + m_cameraX, 1 + m_cameraY);
	}

	private void DrawImageDispatcher(Bitmap bitmap, Bitmap source,
		   	float xStart, float yStart, 
			float xEnd, float yEnd, 
			float imageXStart, float imageYStart,
			float scaleXStep, float scaleYStep,
			int transparencyType)
	{
		float aspect = GetAspect();
		float halfWidth   = GetWidth()/2.0f;
		float halfHeight  = GetHeight()/2.0f;

		xStart -= m_cameraX;
		xEnd   -= m_cameraX;
		yStart -= m_cameraY;
		yEnd   -= m_cameraY;

		xStart /= aspect;
		xEnd   /= aspect;
		
		float imageYStep  = scaleYStep/(((yEnd * halfHeight) + halfHeight)
			   	-((yStart * halfHeight) + halfHeight));
		float imageXStep  = scaleXStep/(((xEnd * halfWidth) + halfWidth)
			   	-((xStart * halfWidth) + halfWidth));

		if(xStart < -1.0f)
		{
			imageXStart = -((xStart + 1.0f)/(xEnd - xStart));
			xStart = -1.0f;
		}
		if(xStart > 1.0f)
		{
			imageXStart = -((xStart + 1.0f)/(xEnd - xStart));
			xStart = 1.0f;
		}
		if(yStart < -1.0f)
		{
			imageYStart = -((yStart + 1.0f)/(yEnd - yStart));
			yStart = -1.0f;
		}
		if(yStart > 1.0f)
		{
			imageYStart = -((yStart + 1.0f)/(yEnd - yStart));
			yStart = 1.0f;
		}

		xEnd = Util.Clamp(xEnd, -1.0f, 1.0f);
		yEnd = Util.Clamp(yEnd, -1.0f, 1.0f);
		
		xStart = (xStart * halfWidth) + halfWidth;
		yStart = (yStart * halfHeight) + halfHeight;
		xEnd   = (xEnd * halfWidth) + halfWidth;
		yEnd   = (yEnd * halfHeight) + halfHeight;

		switch(transparencyType)
		{
			case TRANSPARENCY_NONE:
				DrawImageInternal(bitmap, 
						(int)xStart, (int)yStart, 
						(int)xEnd, (int)yEnd,
						imageXStart, imageYStart, 
						imageXStep, imageYStep);
				break;
			case TRANSPARENCY_BASIC:
				DrawImageBasicTransparencyInternal(bitmap, source,
						(int)xStart, (int)yStart, 
						(int)xEnd, (int)yEnd,
						imageXStart, imageYStart, 
						imageXStep, imageYStep);
				break;
			case TRANSPARENCY_FULL:
				DrawImageAlphaBlendedInternal(bitmap, 
						(int)xStart, (int)yStart, 
						(int)xEnd, (int)yEnd,
						imageXStart, imageYStart, 
						imageXStep, imageYStep);				
				break;
			default:
				System.err.println("You used an invalid transparency value >:(");
				System.exit(1);
		}

	}

	public void DrawImage(Bitmap bitmap, float xStart, float yStart, 
			float xEnd, float yEnd, int transparencyType)
	{
		DrawImageDispatcher(bitmap, bitmap,
				xStart, yStart, xEnd, yEnd,
				0.0f, 0.0f, 1.0f, 1.0f,
				transparencyType);
	}

	private void DrawImageAlphaBlendedInternal(Bitmap bitmap, 
			int xStart, int yStart, 
			int xEnd, int yEnd,
			float texStartX, float texStartY,
			float srcXStep, float srcYStep)
	{
		int destIndex = (xStart+yStart*GetWidth())*4;
		int destYInc = (GetWidth() - (xEnd - xStart)) * 4;

		float srcY = texStartY;
		float srcIndexFloatStep = (srcXStep * (float)(bitmap.GetWidth() - 1));
		for(int j = yStart; j < yEnd; j++)
		{
//			float srcX = texStartX;
			float srcIndexFloat = ((texStartX * (bitmap.GetWidth()-1))
					+(int)(srcY * (bitmap.GetHeight()-1))*bitmap.GetWidth());

			for(int i = xStart; i < xEnd; i++)
			{	
				int srcIndex = (int)(srcIndexFloat) * 4;
				
				//The destIndex logic is equivalent to this
//				int destIndex = (i+j*GetWidth())*4;

//				//The srcIndex logic is equivalent to this
//				int srcIndex = ((int)(srcX * (bitmap.GetWidth()-1))
//						+(int)(srcY * (bitmap.GetHeight()-1))*bitmap.GetWidth())*4;
 
				int a = bitmap.GetComponent(srcIndex + 0) & 0xFF;
				int b = bitmap.GetComponent(srcIndex + 1) & 0xFF;
				int g = bitmap.GetComponent(srcIndex + 2) & 0xFF;
				int r = bitmap.GetComponent(srcIndex + 3) & 0xFF;

				int thisB = GetComponent(destIndex + 1) & 0xFF;
				int thisG = GetComponent(destIndex + 2) & 0xFF;
				int thisR = GetComponent(destIndex + 3) & 0xFF;

				//This is performed using 0.8 fixed point mulitplication
				//rather than floating point.
				int otherAmt = a;
				int thisAmt  = 255 - a; 
				byte newB = (byte)((thisB*thisAmt + b*otherAmt) >> 8);
				byte newG = (byte)((thisG*thisAmt + g*otherAmt) >> 8);
				byte newR = (byte)((thisR*thisAmt + r*otherAmt) >> 8);

				SetComponent(destIndex + 1, newB);
				SetComponent(destIndex + 2, newG);
				SetComponent(destIndex + 3, newR);

				destIndex += 4; 
				srcIndexFloat += srcIndexFloatStep;
//				srcX += srcXStep;
			}
			srcY += srcYStep;
			destIndex += destYInc;
		}
	}
	
	private void DrawImageBasicTransparencyInternal(Bitmap bitmap, Bitmap source,
			int xStart, int yStart, 
			int xEnd, int yEnd,
			float texStartX, float texStartY,
			float srcXStep, float srcYStep)
	{

		//Note: The two bitmaps/srcIndices are a trick to reuse this function for
		//drawing fonts. Under normal usage, the same bitmap should be given to
		//both. However, when drawing fonts, the font bitmap should be supplied
		//as "bitmap," and the font color bitmap should be supplied as "source."
		int destIndex = (xStart+yStart*GetWidth())*4;
		int destYInc = (GetWidth() - (xEnd - xStart)) * 4;

		float srcY = texStartY;
		float srcIndexFloatStep1 = (srcXStep * (float)(source.GetWidth() - 1));
		float srcIndexFloatStep2 = (srcXStep * (float)(bitmap.GetWidth() - 1));
		for(int j = yStart; j < yEnd; j++)
		{
//			float srcX = texStartX;
			float srcIndexFloat1 = ((texStartX * (source.GetWidth()-1))
					+(int)(srcY * (source.GetHeight()-1))*source.GetWidth());
			float srcIndexFloat2 = ((texStartX * (bitmap.GetWidth()-1))
					+(int)(srcY * (bitmap.GetHeight()-1))*bitmap.GetWidth());


			for(int i = xStart; i < xEnd; i++)
			{	
				int srcIndex1 = (int)(srcIndexFloat1) * 4;
				int srcIndex2 = (int)(srcIndexFloat2) * 4;
				
				//The destIndex logic is equivalent to this
//				int destIndex = (i+j*GetWidth())*4;

//				//The srcIndex logic is equivalent to this
//				int srcIndex2 = ((int)(srcX * (bitmap.GetWidth()-1))
//						+(int)(srcY * (bitmap.GetHeight()-1))*bitmap.GetWidth())*4;
 
				byte a = bitmap.GetComponent(srcIndex2 + 0);
				
				if(a < (byte)0)
				{
					SetComponent(destIndex + 1, source.GetComponent(srcIndex1 + 1));
					SetComponent(destIndex + 2, source.GetComponent(srcIndex1 + 2));
					SetComponent(destIndex + 3, source.GetComponent(srcIndex1 + 3));
				}

				destIndex += 4; 
				srcIndexFloat1 += srcIndexFloatStep1;
				srcIndexFloat2 += srcIndexFloatStep2;
//				srcX += srcXStep;
			}
			srcY += srcYStep;
			destIndex += destYInc;
		}
	}


	private void DrawImageInternal(Bitmap bitmap, int xStart, int yStart, 
			int xEnd, int yEnd,
			float texStartX, float texStartY,
			float srcXStep, float srcYStep)
	{
		float srcY = texStartY;
		for(int j = yStart; j < yEnd; j++)
		{
			float srcX = texStartX;
			for(int i = xStart; i < xEnd; i++)
			{
				bitmap.CopyNearest(this, i, j, srcX, srcY);
				srcX += srcXStep;
			}
			srcY += srcYStep;
		}
	}

//	public void DrawLine(float xStart, float yStart, float xEnd, 
//			float yEnd, byte a, byte b, byte g, byte r)
//	{
//		float halfWidth   = GetWidth()/2.0f;
//		float halfHeight  = GetHeight()/2.0f;
//		float scaleFactor = halfWidth < halfHeight ? halfWidth : halfHeight;		
//		xStart = (xStart * scaleFactor) + halfWidth;
//		yStart = (yStart * scaleFactor) + halfHeight;
//		xEnd   = (xEnd * scaleFactor) + halfWidth;
//		yEnd   = (yEnd * scaleFactor) + halfHeight;
//
//		int x0 = (int)xStart;
//		int x1 = (int)xEnd;
//		int y0 = (int)yStart;
//		int y1 = (int)yEnd;
//
//		int deltaX = Math.abs(x1 - x0);
//		int deltaY = Math.abs(y1 - y0);
//		int error = deltaX - deltaY;
//		int xStep;
//		int yStep;
//		if(x0 < x1)
//			xStep = 1;
//		else
//			xStep = -1;
//		if(y0 < y1)
//			yStep = 1;
//		else
//			yStep = -1;
//		while(true)
//		{
//			SafeDrawPixel(x0, y0, a, b, g, r);
//			if(x0 == x1 && y0 == y1)
//				break;
//			int error2 = error * 2;
//			if(error2 > -deltaY)
//			{
//				error -= deltaY;
//				x0 += xStep;
//			}
//			if(x0 == x1 && y0 == y1)
//			{
//				SafeDrawPixel(x0, y0, a, b, g, r);
//				break;
//			}
//			if(error2 < deltaX)
//			{
//				error += deltaX;
//				y0 += yStep;
//			}
//		}
//	}
}

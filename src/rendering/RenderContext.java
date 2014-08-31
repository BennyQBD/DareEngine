package rendering;

import core.*;
import physics.*;

public class RenderContext extends Bitmap
{
	public static final int TRANSPARENCY_NONE = 0;
	public static final int TRANSPARENCY_BASIC = 1;
	public static final int TRANSPARENCY_FULL = 2;

//	public static final int SAMPLER_NEAREST = 0;
//	public static final int SAMPLER_LINEAR = 1;

	private float m_cameraX;
	private float m_cameraY;
	
	public RenderContext(int width, int height)
	{
		super(width, height);
		m_cameraX = 0.0f;
		m_cameraY = 0.0f;
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

	public void DrawImage(Bitmap bitmap, float xStart, float yStart, 
			float xEnd, float yEnd, int transpencyType)
	{
		float aspect = GetAspect();

		xStart -= m_cameraX;
		xEnd   -= m_cameraX;
		yStart -= m_cameraY;
		yEnd   -= m_cameraY;

		xStart /= aspect;
		xEnd   /= aspect;

		float halfWidth   = GetWidth()/2.0f;
		float halfHeight  = GetHeight()/2.0f;

		float imageXStart = 0.0f;
		float imageYStart = 0.0f;
		float imageYStep  = 1.0f/(((yEnd * halfHeight) + halfHeight)
			   	-((yStart * halfHeight) + halfHeight));
		float imageXStep  = 1.0f/(((xEnd * halfWidth) + halfWidth)
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

//		switch(samplerType)
//		{
//			case SAMPLER_NEAREST:
			switch(transpencyType)
			{
				case TRANSPARENCY_NONE:
					DrawImageInternal(bitmap, 
							(int)xStart, (int)yStart, 
							(int)xEnd, (int)yEnd,
							imageXStart, imageYStart, 
							imageXStep, imageYStep);
					break;
				case TRANSPARENCY_BASIC:
					DrawImageBasicTransparencyInternal(bitmap, 
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
//			break;
//			case SAMPLER_LINEAR:
//				switch(transpencyType)
//				{
//					case TRANSPARENCY_NONE:
//						DrawImageInternalLinear(bitmap, 
//								(int)xStart, (int)yStart, 
//								(int)xEnd, (int)yEnd,
//								imageXStart, imageYStart, 
//								imageXStep, imageYStep);
//						break;
//					case TRANSPARENCY_BASIC:
//						DrawImageBasicTransparencyInternal(bitmap, 
//								(int)xStart, (int)yStart, 
//								(int)xEnd, (int)yEnd,
//								imageXStart, imageYStart, 
//								imageXStep, imageYStep);
//						break;
//					case TRANSPARENCY_FULL:
//						DrawImageAlphaBlendedInternal(bitmap, 
//								(int)xStart, (int)yStart, 
//								(int)xEnd, (int)yEnd,
//								imageXStart, imageYStart, 
//								imageXStep, imageYStep);				
//						break;
//					default:
//						System.err.println("You used an invalid transparency value >:(");
//						System.exit(1);
//				}
//			break;
//			default:
//					System.err.println("You used an invalid sampler value >:(");
//					System.exit(1);
//		}
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
	
	private void DrawImageBasicTransparencyInternal(Bitmap bitmap, 
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
 
				byte a = bitmap.GetComponent(srcIndex + 0);
//				byte b = bitmap.GetComponent(srcIndex + 1);
//				byte g = bitmap.GetComponent(srcIndex + 2);
//				byte r = bitmap.GetComponent(srcIndex + 3);
//
//				byte thisB = GetComponent(destIndex + 1);
//				byte thisG = GetComponent(destIndex + 2);
//				byte thisR = GetComponent(destIndex + 3);
//
//				byte mask = (byte)(a >> 7);
//				byte newB = (byte)((b & (mask)) | (thisB & (~mask)));
//				byte newG = (byte)((g & (mask)) | (thisG & (~mask)));
//				byte newR = (byte)((r & (mask)) | (thisR & (~mask)));
//
//				SetComponent(destIndex + 1, newB);
//				SetComponent(destIndex + 2, newG);
//				SetComponent(destIndex + 3, newR);
				
				if(a < (byte)0)
				{
					SetComponent(destIndex + 1, bitmap.GetComponent(srcIndex + 1));
					SetComponent(destIndex + 2, bitmap.GetComponent(srcIndex + 2));
					SetComponent(destIndex + 3, bitmap.GetComponent(srcIndex + 3));
				}

				destIndex += 4; 
				srcIndexFloat += srcIndexFloatStep;
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

//	private void DrawImageInternalLinear(Bitmap bitmap, 
//			int xStart, int yStart, 
//			int xEnd, int yEnd,
//			float texStartX, float texStartY,
//			float srcXStep, float srcYStep)
//	{
//		float srcY = texStartY;
//		for(int j = yStart; j < yEnd; j++)
//		{
//			float srcX = texStartX;
//			for(int i = xStart; i < xEnd; i++)
//			{
//				bitmap.CopyNearest(this, i, j, srcX, srcY);
//				srcX += srcXStep;
//			}
//			srcY += srcYStep;
//		}
//	}


	public void FillRect(float xStart, float yStart, 
			float xEnd, float yEnd,
			byte a, byte b, byte g, byte r)
	{
//		float xStart = xCenter - width/2.0f;
//		float yStart = yCenter - height/2.0f;
//		float xEnd = xStart + width;
//		float yEnd = yStart + height;

		float halfWidth   = GetWidth()/2.0f;
		float halfHeight  = GetHeight()/2.0f;

		float imageXStart = 0.0f;
		float imageYStart = 0.0f;
		float imageYStep  = 1.0f/(((yEnd * halfHeight) + halfHeight)
			   	-((yStart * halfHeight) + halfHeight));
		float imageXStep  = 1.0f/(((xEnd * halfWidth) + halfWidth)
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

		FillRectInternal((int)xStart, (int)yStart, 
				(int)xEnd, (int)yEnd,
				a, b, g, r);
	}
	
	private void FillRectInternal(int xStart, int yStart, 
			int xEnd, int yEnd,
			byte a, byte b, byte g, byte r)
	{
		for(int j = yStart; j < yEnd; j++)
		{
			for(int i = xStart; i < xEnd; i++)
			{
				DrawPixel(i, j, a, b, g, r);
			}
		}
	}

//	public void DrawImage(Bitmap image, float xCenter, float yCenter, float width, float height)
//	{
//		//Begin clipping logic
//		float xStart = xCenter - width/2.0f;
//		float yStart = yCenter - height/2.0f;
//		float xEnd = xStart + width;
//		float yEnd = yStart + height;
//
//		float halfWidth   = GetWidth()/2.0f;
//		float halfHeight  = GetHeight()/2.0f;
//		float scaleFactor = halfWidth < halfHeight ? halfWidth : halfHeight;
//
//		float imageXStart = 0.0f;
//		float imageYStart = 0.0f;
//		float imageYStep  = 1.0f/(((yEnd * scaleFactor) + halfHeight)
//			   	-((yStart * scaleFactor) + halfHeight));
//		float imageXStep  = 1.0f/(((xEnd * scaleFactor) + halfWidth)
//			   	-((xStart * scaleFactor) + halfWidth));
//
//		if(xStart < -1.0f)
//		{
//			imageXStart = -((xStart + 1.0f)/(xEnd - xStart));
//			xStart = -1.0f;
//		}
//		if(xStart > 1.0f)
//		{
//			imageXStart = -((xStart + 1.0f)/(xEnd - xStart));
//			xStart = 1.0f;
//		}
//		if(yStart < -1.0f)
//		{
//			imageYStart = -((yStart + 1.0f)/(yEnd - yStart));
//			yStart = -1.0f;
//		}
//		if(yStart > 1.0f)
//		{
//			imageYStart = -((yStart + 1.0f)/(yEnd - yStart));
//			yStart = 1.0f;
//		}
//
//		xEnd = Util.Clamp(xEnd, -1.0f, 1.0f);
//		yEnd = Util.Clamp(yEnd, -1.0f, 1.0f);
//		
//		xStart = (xStart * scaleFactor) + halfWidth;
//		yStart = (yStart * scaleFactor) + halfHeight;
//		xEnd   = (xEnd * scaleFactor) + halfWidth;
//		yEnd   = (yEnd * scaleFactor) + halfHeight;
//		//End clipping logic
//
//		float imageY = imageYStart;
//
//		for(int y = (int)yStart; y < (int)yEnd; y++)
//		{
//			float imageX = imageXStart;
//			for(int x = (int)xStart; x < (int)xEnd; x++)
//			{
//				image.CopyNearest(this, x, y, imageX, imageY);
//				imageX += imageXStep;
//			}
//			imageY += imageYStep;
//		}
//	}

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

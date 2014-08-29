package rendering;

import core.*;

public class RenderContext extends Bitmap
{
	public static final int TRANSPARENCY_NONE = 0;
	public static final int TRANSPARENCY_BASIC = 1;
	public static final int TRANSPARENCY_FULL = 2;

	public RenderContext(int width, int height)
	{
		super(width, height);
	}

	public void DrawImage(Bitmap bitmap, float xStart, float yStart, 
			float xEnd, float yEnd, int transpencyType)
	{
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
	}

	public void DrawImageAlphaBlendedInternal(Bitmap bitmap, 
			int xStart, int yStart, 
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
				int a = bitmap.GetNearestComponent(srcX, srcY, 0) & 0xFF;
				int b = bitmap.GetNearestComponent(srcX, srcY, 1) & 0xFF;
				int g = bitmap.GetNearestComponent(srcX, srcY, 2) & 0xFF;
				int r = bitmap.GetNearestComponent(srcX, srcY, 3) & 0xFF;

				int thisB = GetComponent(i, j, 1) & 0xFF;
				int thisG = GetComponent(i, j, 2) & 0xFF;
				int thisR = GetComponent(i, j, 3) & 0xFF;

				//This is performed using 0.8 fixed point mulitplication
				//rather than floating point.
				int otherAmt = a;
				int thisAmt  = 255 - a; 
				byte newB = (byte)((thisB*thisAmt + b*otherAmt) >> 8);
				byte newG = (byte)((thisG*thisAmt + g*otherAmt) >> 8);
				byte newR = (byte)((thisR*thisAmt + r*otherAmt) >> 8);

				int index = (i + j * GetWidth()) * 4;
				SetComponent(index + 1, newB);
				SetComponent(index + 2, newG);
				SetComponent(index + 3, newR);

				srcX += srcXStep;
			}
			srcY += srcYStep;
		}
	}
	
	public void DrawImageBasicTransparencyInternal(Bitmap bitmap, 
			int xStart, int yStart, 
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
				if(bitmap.GetNearestComponent(srcX, srcY, 0) > (byte)0)
				{
					bitmap.CopyNearest(this, i, j, srcX, srcY);
				}
				srcX += srcXStep;
			}
			srcY += srcYStep;
		}
	}


	public void DrawImageInternal(Bitmap bitmap, int xStart, int yStart, 
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

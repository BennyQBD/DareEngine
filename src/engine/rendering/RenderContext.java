/*
 * Copyright (c) 2014, Benny Bobaganoosh
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package engine.rendering;

import engine.core.Util;
import engine.physics.AABB;

public class RenderContext extends Bitmap {
	public static final int TRANSPARENCY_NONE = 0;
	public static final int TRANSPARENCY_BASIC = 1;
	public static final int TRANSPARENCY_FULL = 2;

	private float xCamera;
	private float yCamera;
	private Bitmap font;
	private Bitmap fontColor;

	public RenderContext(int width, int height) {
		super(width, height);
		xCamera = 0.0f;
		yCamera = 0.0f;
		font = new Bitmap("./res/monospace.png");
		fontColor = new Bitmap(1, 1);
	}

	public void drawString(String text, float x, float y,
			float size, byte b, byte g, byte r) {
		float spacingFactor = font.getAspect();
		fontColor.drawPixel(0, 0, (byte) 0x00, b, g, r);

		float currentPosX = x;
		float currentPosY = y;

		float sizeX = size;
		float sizeY = size;

		for (int i = 0; i < text.length(); i++) {
			char current = text.charAt(i);
			int imgX = current & 0x0F;
			int imgY = (current >> 4) & 0x0F;

			float imgXStart = (float) imgX / 16.0f;
			float imgYStart = (float) imgY / 16.0f + 0.01f;

			float xStart = currentPosX;
			float yStart = currentPosY;
			float xEnd = currentPosX + sizeX;
			float yEnd = currentPosY + sizeY;

			drawImageDispatcher(font, fontColor, xStart, yStart,
					xEnd, yEnd, imgXStart, imgYStart,
					(spacingFactor) / 16.0f, 1.0f / 16.0f,
					TRANSPARENCY_BASIC);

			currentPosX += sizeX * spacingFactor;
		}
	}

	public void setCameraPosition(float x, float y) {
		xCamera = x;
		yCamera = y;
	}

	public AABB getRenderArea() {
		float aspect = getAspect();
		return new AABB(-aspect + xCamera, -1 + yCamera, aspect
				+ xCamera, 1 + yCamera);
	}

	private void drawImageDispatcher(Bitmap bitmap,
			Bitmap source, float xStart, float yStart,
			float xEnd, float yEnd, float imageXStart,
			float imageYStart, float scaleXStep,
			float scaleYStep, int transparencyType) {
		float aspect = getAspect();
		float halfWidth = getWidth() / 2.0f;
		float halfHeight = getHeight() / 2.0f;

		xStart -= xCamera;
		xEnd -= xCamera;
		yStart -= yCamera;
		yEnd -= yCamera;

		xStart /= aspect;
		xEnd /= aspect;

		float imageYStep = scaleYStep
				/ (((yEnd * halfHeight) + halfHeight) - ((yStart * halfHeight) + halfHeight));
		float imageXStep = scaleXStep
				/ (((xEnd * halfWidth) + halfWidth) - ((xStart * halfWidth) + halfWidth));

		if (xStart < -1.0f) {
			imageXStart = -((xStart + 1.0f) / (xEnd - xStart));
			xStart = -1.0f;
		}
		if (xStart > 1.0f) {
			imageXStart = -((xStart + 1.0f) / (xEnd - xStart));
			xStart = 1.0f;
		}
		if (yStart < -1.0f) {
			imageYStart = -((yStart + 1.0f) / (yEnd - yStart));
			yStart = -1.0f;
		}
		if (yStart > 1.0f) {
			imageYStart = -((yStart + 1.0f) / (yEnd - yStart));
			yStart = 1.0f;
		}

		xEnd = Util.clamp(xEnd, -1.0f, 1.0f);
		yEnd = Util.clamp(yEnd, -1.0f, 1.0f);

		xStart = (xStart * halfWidth) + halfWidth;
		yStart = (yStart * halfHeight) + halfHeight;
		xEnd = (xEnd * halfWidth) + halfWidth;
		yEnd = (yEnd * halfHeight) + halfHeight;

		switch (transparencyType) {
		case TRANSPARENCY_NONE:
			drawImageInternal(bitmap, (int) xStart,
					(int) yStart, (int) xEnd, (int) yEnd,
					imageXStart, imageYStart, imageXStep,
					imageYStep);
			break;
		case TRANSPARENCY_BASIC:
			drawImageBasicTransparencyInternal(bitmap, source,
					(int) xStart, (int) yStart, (int) xEnd,
					(int) yEnd, imageXStart, imageYStart,
					imageXStep, imageYStep);
			break;
		case TRANSPARENCY_FULL:
			drawImageAlphaBlendedInternal(bitmap, (int) xStart,
					(int) yStart, (int) xEnd, (int) yEnd,
					imageXStart, imageYStart, imageXStep,
					imageYStep);
			break;
		default:
			System.err
					.println("You used an invalid transparency value >:(");
			System.exit(1);
		}

	}

	public void drawImage(Bitmap bitmap, float xStart,
			float yStart, float xEnd, float yEnd,
			int transparencyType) {
		drawImageDispatcher(bitmap, bitmap, xStart, yStart,
				xEnd, yEnd, 0.0f, 0.0f, 1.0f, 1.0f,
				transparencyType);
	}

	private void drawImageAlphaBlendedInternal(Bitmap bitmap,
			int xStart, int yStart, int xEnd, int yEnd,
			float texStartX, float texStartY, float srcXStep,
			float srcYStep) {
		int destIndex = (xStart + yStart * getWidth()) * 4;
		int destYInc = (getWidth() - (xEnd - xStart)) * 4;

		float srcY = texStartY;
		float srcIndexFloatStep = (srcXStep * (float) (bitmap
				.getWidth() - 1));
		for (int j = yStart; j < yEnd; j++) {
			// float srcX = texStartX;
			float srcIndexFloat = ((texStartX * (bitmap
					.getWidth() - 1)) + (int) (srcY * (bitmap
					.getHeight() - 1)) * bitmap.getWidth());

			for (int i = xStart; i < xEnd; i++) {
				int srcIndex = (int) (srcIndexFloat) * 4;

				// The destIndex logic is equivalent to this
				// int destIndex = (i+j*GetWidth())*4;

				// //The srcIndex logic is equivalent to this
				// int srcIndex = ((int)(srcX * (bitmap.GetWidth()-1))
				// +(int)(srcY * (bitmap.GetHeight()-1))*bitmap.GetWidth())*4;

				int a = bitmap.getComponent(srcIndex + 0) & 0xFF;
				int b = bitmap.getComponent(srcIndex + 1) & 0xFF;
				int g = bitmap.getComponent(srcIndex + 2) & 0xFF;
				int r = bitmap.getComponent(srcIndex + 3) & 0xFF;

				int thisB = getComponent(destIndex + 1) & 0xFF;
				int thisG = getComponent(destIndex + 2) & 0xFF;
				int thisR = getComponent(destIndex + 3) & 0xFF;

				// This is performed using 0.8 fixed point mulitplication
				// rather than floating point.
				int otherAmt = a;
				int thisAmt = 255 - a;
				byte newB = (byte) ((thisB * thisAmt + b
						* otherAmt) >> 8);
				byte newG = (byte) ((thisG * thisAmt + g
						* otherAmt) >> 8);
				byte newR = (byte) ((thisR * thisAmt + r
						* otherAmt) >> 8);

				setComponent(destIndex + 1, newB);
				setComponent(destIndex + 2, newG);
				setComponent(destIndex + 3, newR);

				destIndex += 4;
				srcIndexFloat += srcIndexFloatStep;
				// srcX += srcXStep;
			}
			srcY += srcYStep;
			destIndex += destYInc;
		}
	}

	private void drawImageBasicTransparencyInternal(
			Bitmap bitmap, Bitmap source, int xStart,
			int yStart, int xEnd, int yEnd, float texStartX,
			float texStartY, float srcXStep, float srcYStep) {
		// Note: The two bitmaps/srcIndices are a trick to reuse this function
		// for drawing fonts. Under normal usage, the same bitmap should be
		// given to both. However, when drawing fonts, the font bitmap should be
		// supplied as "bitmap," and the font color bitmap should be supplied as
		// "source."
		int destIndex = (xStart + yStart * getWidth()) * 4;
		int destYInc = (getWidth() - (xEnd - xStart)) * 4;

		float srcY = texStartY;
		float srcIndexFloatStep1 = (srcXStep * (float) (source
				.getWidth() - 1));
		float srcIndexFloatStep2 = (srcXStep * (float) (bitmap
				.getWidth() - 1));
		for (int j = yStart; j < yEnd; j++) {
			// float srcX = texStartX;
			float srcIndexFloat1 = ((texStartX * (source
					.getWidth() - 1)) + (int) (srcY * (source
					.getHeight() - 1)) * source.getWidth());
			float srcIndexFloat2 = ((texStartX * (bitmap
					.getWidth() - 1)) + (int) (srcY * (bitmap
					.getHeight() - 1)) * bitmap.getWidth());

			for (int i = xStart; i < xEnd; i++) {
				int srcIndex1 = (int) (srcIndexFloat1) * 4;
				int srcIndex2 = (int) (srcIndexFloat2) * 4;

				// The destIndex logic is equivalent to this
				// int destIndex = (i+j*GetWidth())*4;

				// //The srcIndex logic is equivalent to this
				// int srcIndex2 = ((int)(srcX * (bitmap.GetWidth()-1))
				// +(int)(srcY * (bitmap.GetHeight()-1))*bitmap.GetWidth())*4;

				byte a = bitmap.getComponent(srcIndex2 + 0);

				if (a < (byte) 0) {
					setComponent(destIndex + 1,
							source.getComponent(srcIndex1 + 1));
					setComponent(destIndex + 2,
							source.getComponent(srcIndex1 + 2));
					setComponent(destIndex + 3,
							source.getComponent(srcIndex1 + 3));
				}

				destIndex += 4;
				srcIndexFloat1 += srcIndexFloatStep1;
				srcIndexFloat2 += srcIndexFloatStep2;
				// srcX += srcXStep;
			}
			srcY += srcYStep;
			destIndex += destYInc;
		}
	}

	private void drawImageInternal(Bitmap bitmap, int xStart,
			int yStart, int xEnd, int yEnd, float texStartX,
			float texStartY, float srcXStep, float srcYStep) {
		float srcY = texStartY;
		for (int j = yStart; j < yEnd; j++) {
			float srcX = texStartX;
			for (int i = xStart; i < xEnd; i++) {
				bitmap.copyNearest(this, i, j, srcX, srcY);
				srcX += srcXStep;
			}
			srcY += srcYStep;
		}
	}
}

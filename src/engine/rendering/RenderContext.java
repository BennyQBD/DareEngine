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

	private float cameraX;
	private float cameraY;
	private Bitmap font;
	private Bitmap fontColor;

	public RenderContext(int width, int height) {
		super(width, height);
		cameraX = 0.0f;
		cameraY = 0.0f;
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

			float imgStartX = (float) imgX / 16.0f;
			float imgStartY = (float) imgY / 16.0f + 0.01f;

			float startX = currentPosX;
			float startY = currentPosY;
			float endX = currentPosX + sizeX;
			float endY = currentPosY + sizeY;

			drawImageDispatcher(font, fontColor, startX, startY,
					endX, endY, imgStartX, imgStartY,
					(spacingFactor) / 16.0f, 1.0f / 16.0f,
					TRANSPARENCY_BASIC);

			currentPosX += sizeX * spacingFactor;
		}
	}

	public void setCameraPosition(float x, float y) {
		cameraX = x;
		cameraY = y;
	}

	public AABB getRenderArea() {
		float aspect = getAspect();
		return new AABB(-aspect + cameraX, -1 + cameraY, aspect
				+ cameraX, 1 + cameraY);
	}

	private void drawImageDispatcher(Bitmap bitmap,
			Bitmap source, float startX, float startY,
			float endX, float endY, float imageStartX,
			float imageStartY, float scaleStepX,
			float scaleStepY, int transparencyType) {
		float aspect = getAspect();
		float halfWidth = getWidth() / 2.0f;
		float halfHeight = getHeight() / 2.0f;

		startX -= cameraX;
		endX -= cameraX;
		startY -= cameraY;
		endY -= cameraY;

		startX /= aspect;
		endX /= aspect;

		float imageYStep = scaleStepY
				/ (((endY * halfHeight) + halfHeight) - ((startY * halfHeight) + halfHeight));
		float imageXStep = scaleStepX
				/ (((endX * halfWidth) + halfWidth) - ((startX * halfWidth) + halfWidth));

		if (startX < -1.0f) {
			imageStartX = -((startX + 1.0f) / (endX - startX));
			startX = -1.0f;
		}
		if (startX > 1.0f) {
			imageStartX = -((startX + 1.0f) / (endX - startX));
			startX = 1.0f;
		}
		if (startY < -1.0f) {
			imageStartY = -((startY + 1.0f) / (endY - startY));
			startY = -1.0f;
		}
		if (startY > 1.0f) {
			imageStartY = -((startY + 1.0f) / (endY - startY));
			startY = 1.0f;
		}

		endX = Util.clamp(endX, -1.0f, 1.0f);
		endY = Util.clamp(endY, -1.0f, 1.0f);

		startX = (startX * halfWidth) + halfWidth;
		startY = (startY * halfHeight) + halfHeight;
		endX = (endX * halfWidth) + halfWidth;
		endY = (endY * halfHeight) + halfHeight;

		switch (transparencyType) {
		case TRANSPARENCY_NONE:
			drawImageInternal(bitmap, (int) startX,
					(int) startY, (int) endX, (int) endY,
					imageStartX, imageStartY, imageXStep,
					imageYStep);
			break;
		case TRANSPARENCY_BASIC:
			drawImageBasicTransparencyInternal(bitmap, source,
					(int) startX, (int) startY, (int) endX,
					(int) endY, imageStartX, imageStartY,
					imageXStep, imageYStep);
			break;
		case TRANSPARENCY_FULL:
			drawImageAlphaBlendedInternal(bitmap, (int) startX,
					(int) startY, (int) endX, (int) endY,
					imageStartX, imageStartY, imageXStep,
					imageYStep);
			break;
		default:
			System.err
					.println("You used an invalid transparency value >:(");
			System.exit(1);
		}

	}

	public void drawImage(Bitmap bitmap, float startX,
			float startY, float endX, float endY,
			int transparencyType) {
		drawImageDispatcher(bitmap, bitmap, startX, startY,
				endX, endY, 0.0f, 0.0f, 1.0f, 1.0f,
				transparencyType);
	}

	private void drawImageAlphaBlendedInternal(Bitmap bitmap,
			int startX, int startY, int endX, int endY,
			float texStartX, float texStartY, float srcStepX,
			float srcStepY) {
		int destIndex = (startX + startY * getWidth()) * 4;
		int destStepY = (getWidth() - (endX - startX)) * 4;

		float srcY = texStartY;
		float srcIndexFloatStep = (srcStepX * (float) (bitmap
				.getWidth() - 1));
		for (int j = startY; j < endY; j++) {
			// float srcX = texStartX;
			float srcIndexFloat = ((texStartX * (bitmap
					.getWidth() - 1)) + (int) (srcY * (bitmap
					.getHeight() - 1)) * bitmap.getWidth());

			for (int i = startX; i < endX; i++) {
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
			srcY += srcStepY;
			destIndex += destStepY;
		}
	}

	private void drawImageBasicTransparencyInternal(
			Bitmap bitmap, Bitmap source, int startX,
			int startY, int endX, int endY, float texStartX,
			float texStartY, float srcStepX, float srcStepY) {
		// Note: The two bitmaps/srcIndices are a trick to reuse this function
		// for drawing fonts. Under normal usage, the same bitmap should be
		// given to both. However, when drawing fonts, the font bitmap should be
		// supplied as "bitmap," and the font color bitmap should be supplied as
		// "source."
		int destIndex = (startX + startY * getWidth()) * 4;
		int destYInc = (getWidth() - (endX - startX)) * 4;

		float srcY = texStartY;
		float srcIndexFloatStep1 = (srcStepX * (float) (source
				.getWidth() - 1));
		float srcIndexFloatStep2 = (srcStepX * (float) (bitmap
				.getWidth() - 1));
		for (int j = startY; j < endY; j++) {
			// float srcX = texStartX;
			float srcIndexFloat1 = ((texStartX * (source
					.getWidth() - 1)) + (int) (srcY * (source
					.getHeight() - 1)) * source.getWidth());
			float srcIndexFloat2 = ((texStartX * (bitmap
					.getWidth() - 1)) + (int) (srcY * (bitmap
					.getHeight() - 1)) * bitmap.getWidth());

			for (int i = startX; i < endX; i++) {
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
			srcY += srcStepY;
			destIndex += destYInc;
		}
	}

	private void drawImageInternal(Bitmap bitmap, int startX,
			int startY, int endX, int endY, float texStartX,
			float texStartY, float srcStepX, float srcStepY) {
		float srcY = texStartY;
		for (int j = startY; j < endY; j++) {
			float srcX = texStartX;
			for (int i = startX; i < endX; i++) {
				bitmap.copyNearest(this, i, j, srcX, srcY);
				srcX += srcStepX;
			}
			srcY += srcStepY;
		}
	}
}

package engine.util.preprocessing;

import java.io.IOException;

import engine.rendering.ArrayBitmap;

public class SpriteSheetBorderMaker {
	public static void generateBorder(String inputFileName,
			String outputFileName, String outputFormat, int spritesX,
			int spritesY, final int spriteSheetBorder) throws IOException {
		final ArrayBitmap srcImage = new ArrayBitmap(inputFileName);
		final int spriteWidth = srcImage.getWidth() / spritesX;
		final int spriteHeight = srcImage.getHeight() / spritesY;
		final int borderedSpriteWidth = spriteWidth + spriteSheetBorder * 2;
		final int borderedSpriteHeight = spriteHeight + spriteSheetBorder * 2;
		final ArrayBitmap destImage = new ArrayBitmap(borderedSpriteWidth
				* spritesX, borderedSpriteHeight * spritesY);

		destImage.visitAll(new ArrayBitmap.IVisitor() {
			@Override
			public void visit(int x, int y, int pixel) {
				int spriteNumX = x / borderedSpriteWidth;
				int spritePosX = x % borderedSpriteWidth;
				int spriteNumY = y / borderedSpriteHeight;
				int spritePosY = y % borderedSpriteHeight;

				boolean isLeftBorder = spritePosX < spriteSheetBorder;
				boolean isRightBorder = spritePosX >= borderedSpriteWidth
						- spriteSheetBorder;
				boolean isTopBorder = spritePosY < spriteSheetBorder;
				boolean isBottomBorder = spritePosY >= borderedSpriteHeight
						- spriteSheetBorder;
				int srcStartX = spriteNumX * spriteWidth;
				int srcStartY = spriteNumY * spriteHeight;
				int srcEndX = (spriteNumX + 1) * spriteWidth - 1;
				int srcEndY = (spriteNumY + 1) * spriteHeight - 1;
				int srcX = srcStartX + (spritePosX - spriteSheetBorder);
				int srcY = srcStartY + (spritePosY - spriteSheetBorder);

				if (isLeftBorder) {
					if (isTopBorder) {
						destImage.set(x, y, srcImage.get(srcStartX, srcStartY));
					} else if (isBottomBorder) {
						destImage.set(x, y, srcImage.get(srcStartX, srcEndY));
					} else {
						destImage.set(x, y, srcImage.get(srcStartX, srcY));
					}
				} else if (isRightBorder) {
					if (isTopBorder) {
						destImage.set(x, y, srcImage.get(srcEndX, srcStartY));
					} else if (isBottomBorder) {
						destImage.set(x, y, srcImage.get(srcEndX, srcEndY));
					} else {
						destImage.set(x, y, srcImage.get(srcEndX, srcY));
					}
				} else if (isTopBorder) {
					destImage.set(x, y, srcImage.get(srcX, srcStartY));
				} else if (isBottomBorder) {
					destImage.set(x, y, srcImage.get(srcX, srcEndY));
				} else {
					destImage.set(x, y, srcImage.get(srcX, srcY));
				}
			}
		});

		destImage.save(outputFileName, outputFormat);
	}
}

package dy.edmundson.game.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Loads sprite sheet data and makes accessible to user
 */
public class SpriteSheet {
    private final int pixelsPerTile;
    private int width;
    private int height;
    private int[] spriteSheetPixels;


    public SpriteSheet(String spriteSheetPath, int pixelsPerTile) {
        this.pixelsPerTile = pixelsPerTile;
        BufferedImage spriteSheetImg;
        try {
            spriteSheetImg = ImageIO.read(new File(spriteSheetPath));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.width = spriteSheetImg.getWidth();
        this.height = spriteSheetImg.getHeight();
        spriteSheetPixels = spriteSheetImg.getRGB(0, 0, width,
                height, null, 0, width);
    }

    /**
     * gets the pixels from a tile in the sprite sheet (initial indexing is at 0)
     * @param xTileStart starting x tile data request
     * @param xTileEnd ending x tile data request
     * @param yTileStart starting y tile data request
     * @param yTileEnd ending y tile data request
     * @param inputArray array pixel data is input into (if null function will return an array)
     * @throws IllegalArgumentException if inputArray size doesn't match size of requested output or request
     *                  is outside of bounds
     * @return array of pixel values if input array is not null, else null
     */
    public int[] getSpriteSheetTiles(int xTileStart, int xTileEnd, int yTileStart,
                                    int yTileEnd, int[] inputArray) {
        int inputSize = (xTileEnd - xTileStart + 1) * pixelsPerTile * (yTileEnd - yTileStart + 1) * pixelsPerTile;
        if (inputArray != null && inputArray.length != inputSize) {
            throw new IllegalArgumentException("Improper input for size of input array");
        }
        if ((yTileEnd + 1) * pixelsPerTile > height ||  (xTileEnd + 1) * pixelsPerTile > width) {
            throw new IllegalArgumentException("Request outside of sprite sheet range");
        }
        int[] pixels;
        if (inputArray == null) {
            pixels = new int[inputSize];
        } else {
            pixels = inputArray;
        }

        int i = 0;
        for (int row = yTileStart * pixelsPerTile; row < (yTileEnd + 1) * pixelsPerTile; row++) {
            for (int col = xTileStart * pixelsPerTile; col < (xTileEnd + 1) * pixelsPerTile; col++) {
                pixels[i] = (spriteSheetPixels[row * width + col] & 0xff) / 16;
                i++;
            }
        }
        return pixels;
    }
}

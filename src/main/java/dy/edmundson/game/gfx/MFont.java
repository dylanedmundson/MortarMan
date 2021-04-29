package dy.edmundson.game.gfx;

import dy.edmundson.game.Game;
import dy.edmundson.utilites.GrayScaleList;
import dy.edmundson.utilites.PixelArrayHandler;

public class MFont {
    public static final byte HORIZ_CENTER = 0x0000;
    public static final byte HORIZ_LEFT = 0x0001;
    public static final byte HORIZ_RIGHT = 0x0002;

    public static final int MARGIN = 4;

    private static final String chars = "abcdefghijklmnopqrstuvwxyz      " +
            "0123456789.,:;'\"!?$%()-=+/>     ";

    public static void drawStringToPixels(String input, int[] outputPixels, int outputPixelsWidth,
                                          int xStart, int yStart, int scale, int color) {
        GrayScaleList grayScaleList = new GrayScaleList();
        grayScaleList.add(mColour.BLACK, mColour.get(-1, -1, -1, -1));
        grayScaleList.add(mColour.WHITE, color);
        input = input.toLowerCase();
        int y = yStart;
        int x = xStart;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '\n') {
                y += Game.PIXELS_PER_TILE * scale + MARGIN;
                x = xStart;
                if (y > outputPixels.length / outputPixelsWidth) {
                    return;
                }
            } else {
                int val = chars.indexOf(input.charAt(i));
                if (val == -1) {
                    throw new IllegalArgumentException("char not supported");
                }
                int xIndex = val % 32;
                int yIndex = val / 32 + 30;
                int[] pixels = Game.spriteSheet.getSpriteSheetTiles(xIndex, xIndex, yIndex, yIndex, null);
                pixels = PixelArrayHandler.scalePixels(pixels, Game.PIXELS_PER_TILE, Game.PIXELS_PER_TILE, scale);
                if (i > 0) {
                    x += Game.PIXELS_PER_TILE * scale;
                }
                if (x + Game.PIXELS_PER_TILE * scale >= outputPixelsWidth) {
                    y += Game.PIXELS_PER_TILE * scale + MARGIN;
                    x = xStart;
                    if (y > outputPixels.length / outputPixelsWidth) {
                        return;
                    }
                }
                mColour.changeColours(pixels, grayScaleList);
                PixelArrayHandler.renderImageOnTop(pixels, Game.PIXELS_PER_TILE * scale,
                        Game.PIXELS_PER_TILE * scale, x, y, outputPixels, outputPixelsWidth);
            }
        }
    }
    public static void drawStringToPixels(String input, int[] outputPixels, int outputPixelsWidth,
                                          byte horizontalAlignment, int yStart, int scale, int color) {
        String[] inputs = input.split("\n");
        if (horizontalAlignment == HORIZ_CENTER) {
            int y = yStart;
            for (int i = 0; i < inputs.length; i++) {
                int xStart = (outputPixelsWidth / 2) - (inputs[i].length() / 2 * Game.PIXELS_PER_TILE * scale);
                drawStringToPixels(inputs[i], outputPixels, outputPixelsWidth, xStart, y, scale, color);
                y += Game.PIXELS_PER_TILE * scale + MARGIN;
            }
        } else if (horizontalAlignment == HORIZ_LEFT) {
            int y = yStart;
            for (int i = 0; i < inputs.length; i++) {
                int xStart = MARGIN;
                drawStringToPixels(inputs[i], outputPixels, outputPixelsWidth, xStart, y, scale, color);
                y += Game.PIXELS_PER_TILE * scale + MARGIN;
            }
        } else if (horizontalAlignment == HORIZ_RIGHT) {
            int y = yStart;
            for (int i = 0; i < inputs.length; i++) {
                int xStart = outputPixelsWidth - inputs[i].length() * Game.PIXELS_PER_TILE * scale -  MARGIN;
                drawStringToPixels(inputs[i], outputPixels, outputPixelsWidth, xStart, y, scale, color);
                y += Game.PIXELS_PER_TILE * scale + MARGIN;
            }
        } else {
            throw new IllegalArgumentException("illegal horizontal alignment");
        }
    }
}

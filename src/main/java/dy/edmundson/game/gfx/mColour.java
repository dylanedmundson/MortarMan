package dy.edmundson.game.gfx;

import dy.edmundson.utilites.GrayScaleList;

public class mColour {
    public static final int BLACK = 0;
    public static final int GRAY_60 = 6;
    public static final int GRAY_40 = 9;
    public static final int WHITE = 15;
    public static final int GRAY_80 = 3;



    public static void changeColours(int[] pixels, GrayScaleList grayScaleList) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = grayScaleList.getNewColour(grayScaleList.getGrayScaleIndex(pixels[i]));
        }
    }

    public static int get(int red, int green, int blue, int aa) {
        return (get(aa) << 24) + (get(red) << 16) + (get(green) << 8) + get(blue);
    }


    public static int get(int colour) {
        if (colour < 0) return 255;
        int r = colour / 100 % 10;
        int g = colour / 10 % 10;
        int b = colour% 10;
        return r * 36 + g * 6 + b;
    }
}

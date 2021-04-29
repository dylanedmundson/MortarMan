package dy.edmundson.utilites;

import org.junit.Assert;
import org.junit.Test;

public class TestPixelArrayHandler {

    @Test
    public void testScalePixels() {
        int[] pixels = new int[] {
                0, 1, 2,
                3, 4, 5,
                6, 7, 8
        };

        int[] scaled = PixelArrayHandler.scalePixels(pixels, 3, 3, 2);
        int[] scaledExpected = new int[] {
                0, 0, 1, 1, 2, 2,
                0, 0, 1, 1, 2, 2,
                3, 3, 4, 4, 5, 5,
                3, 3, 4, 4, 5, 5,
                6, 6, 7, 7, 8, 8,
                6, 6, 7, 7, 8, 8
        };
        Assert.assertArrayEquals(scaled, scaledExpected);

        scaled = PixelArrayHandler.scalePixels(pixels, 3, 3, 3);
        scaledExpected = new int[] {
                0, 0, 0, 1, 1, 1, 2, 2, 2,
                0, 0, 0, 1, 1, 1, 2, 2, 2,
                0, 0, 0, 1, 1, 1, 2, 2, 2,
                3, 3, 3, 4, 4, 4, 5, 5, 5,
                3, 3, 3, 4, 4, 4, 5, 5, 5,
                3, 3, 3, 4, 4, 4, 5, 5, 5,
                6, 6, 6, 7, 7, 7, 8, 8, 8,
                6, 6, 6, 7, 7, 7, 8, 8, 8,
                6, 6, 6, 7, 7, 7, 8, 8, 8
        };
        Assert.assertArrayEquals(scaled, scaledExpected);
    }
}

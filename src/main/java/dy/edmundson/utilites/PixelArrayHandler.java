package dy.edmundson.utilites;

public class PixelArrayHandler {

    /**
     * puts the input pixels into the output pixels given a tile row and column as the pixel index
     * @param inputPixels pixels to be input (smaller than output)
     * @param tileRowStart the row of the tile input tiles will be placed
     * @param tileCol the col of the tile input tiles will be copied
     * @param outputPixels pixels tiles are being input into.
     */
    public static void putGraphicsTile(int[] inputPixels, int tileRowStart, int tileCol, int[] outputPixels) {
        int j = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int pixelIndex = (tileRowStart * 8 * 512) + (row * 512) + (tileCol * 8) + col;
                outputPixels[pixelIndex] = inputPixels[j];
                j++;
            }
        }
    }

    /**
     * used for filling the entire output pixels array with a portion of the input array
     * assumes input array is larger
     * @param inputPixels
     * @param inputWidth
     * @param pixelRowStart
     * @param pixelRowEnd
     * @param pixelColStart
     * @param pixelColEnd
     * @param outputPixels
     */
    public static void putGraphicsPixel(int[] inputPixels, int inputWidth, int pixelRowStart, int pixelRowEnd,
                                        int pixelColStart, int pixelColEnd, int[] outputPixels) {
        int i = 0;
        for (int y = pixelRowStart; y < pixelRowEnd; y++) {
            for (int x = pixelColStart; x < pixelColEnd; x++) {
                int val = getXYCoord(inputPixels, x, y, inputWidth);
                if (val != -1) {
                    outputPixels[i] = val;
                }
                i++;
            }
        }
    }

    /**
     * get the x y coordinate in an array of row major order
     * @param pixelArray array value is being extracted from
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of each row
     * @return integer pixel value
     */
    public static int getXYCoord(int[] pixelArray, int x, int y, int width) {
        return pixelArray[y * width + x];
    }

    /**
     * replaces x y coordinate of an array with a new value
     * @param newValue value being input into array
     * @param pixelArray pixel array being altered
     * @param width width of each row of the pixel array
     * @param x x coordinate
     * @param y y coordinate
     */
    public static void replaceXYCoord(int newValue, int[] pixelArray, int width, int x, int y) {
        pixelArray[y * width + x] = newValue;
    }

    /**
     * renders an image on top of an array
     * @param inputArray row major order input array
     * @param inputWidth width of input array
     * @param inputHeight height of input array
     * @param xStart starting x position
     * @param yStart starting y position
     * @param outputArray array image is rendered on top of
     * @param outputWidth width of output array rows
     */
    public static void renderImageOnTop(int[] inputArray, int inputWidth, int inputHeight, int xStart, int yStart, int[] outputArray, int outputWidth) {
        for (int row = 0; row < inputHeight; row++) {
            for (int col = 0; col < inputWidth; col++) {
                int val = getXYCoord(inputArray, col, row, inputWidth);
                if (val != -1) {
                    replaceXYCoord(val, outputArray,
                            outputWidth, xStart + col, yStart + row);
                }
            }
        }
    }

    /**
     * enlarges a pixelated image
     * @param pixelArray pixel array to be enlarged (1D row major order)
     * @param width width of each row
     * @param height number of rows
     * @param scale scale image is enlarged by
     * @return new scaled array
     */
    public static int[] scalePixels(int[] pixelArray, int width, int height, int scale) {
        int[] scaled = new int[width * height * scale * scale];
        int scaledIndex = 0;
        for (int row = 0; row < height; row++) {
            for (int i = 0; i < scale; i++) { //repeat row 3 times
                for (int col = row * width; col < row * width + width; col ++) {
                    for (int j = 0; j < scale; j++) { //repeat index 3 times
                            scaled[scaledIndex] = pixelArray[col];
                            scaledIndex++;
                    }
                }
            }
        }
        return scaled;
    }

    /**
     * mirrors image over x axis
     * @param entityPixels pixels being mirrored
     * @param width width of entity pixels row
     * @param height height of entity pixels
     * @return new array of mirrored image
     */
    public static int[] mirror(int[] entityPixels, int width, int height) {
        int[] newPixels = new int[entityPixels.length];
        for (int row = 0; row < height; row++) {
            for (int col = width - 1, newCol = 0; col >= 0; col--, newCol++) {
                int val = getXYCoord(entityPixels, col, row, width);
                replaceXYCoord(val, newPixels, width, newCol, row);
            }
        }
        return newPixels;
    }

    /**
     * rotates image 90 degrees clockwise
     * @param entityPixels pixel array being rotated (row major order)
     * @param width of 1 row
     * @param height of 2D image (number of rows)
     * @return rotated array
     */
    public static int[] rotate90(int[] entityPixels, int width, int height) {
        int[] newArr = new int[width * height];
        int i = 0;
        for (int col = height - 1; col >= 0; col--) {
            for (int row = 0; row < width; row++) {
                replaceXYCoord(entityPixels[i], newArr, height, col, row);
                i++;
            }
        }
        return newArr;
    }

    public static int[] getPortionPixelArray(int inputWidth, int inputHeight, int xStart, int yStart,
                                             int[] outputArray, int outputWidth) {
        int[] arr = new int[inputWidth * inputHeight];
        for (int row = yStart; row < yStart + inputHeight; row++) {
            for (int col = xStart; col < xStart + inputWidth; col++) {
                int val = getXYCoord(outputArray, col, row, outputWidth);
                replaceXYCoord(val, arr, inputWidth, col - xStart, row - yStart);
            }
        }
        return arr;
    }
}

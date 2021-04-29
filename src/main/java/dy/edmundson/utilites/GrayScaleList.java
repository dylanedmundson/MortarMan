package dy.edmundson.utilites;

/**
 * a list holding gray scale integer color values and their associated new colour that
 * will be rended instead of the grayscale
 */
public class GrayScaleList {
    private static final int BASE_SIZE = 3;
    private GrayScalePair[] elements;
    int size;

    public GrayScaleList() {
        elements = new GrayScalePair[BASE_SIZE];
        size = 0;
    }

    /**
     * adds a new grayScale colour and its associated new colour
     * @param grayScale 32 bit integer value indicating pixel colour (gray scale referenced in sprite sheet)
     * @param newColour 32 bit integer value indicating new pixel colour to be rendered to screen
     */
    public void add(int grayScale, int newColour) {
        if (size >= elements.length) {
            resize();
        }
        elements[size] = new GrayScalePair(grayScale, newColour);
        size++;
    }

    /**
     * get index of a grayscale
     * @param grayScale 32 bit grayscale value being serached for
     * @return index of grayscale
     */
    public int getGrayScaleIndex(int grayScale) {
        for (int i = 0; i < size; i++) {
            if (elements[i].grayScale == grayScale) {
                return i;
            }
        }
        return -1;
    }

    /**
     * get the new colour to be rendered in place of the grayscale sprite sheet pixel
     * @param grayScaleIndex index of gray scale value
     * @return 32 bit new colour to be rendered in place of grayscale value
     */
    public int getNewColour(int grayScaleIndex) {
        return elements[grayScaleIndex].newColour;
    }

    private void resize() {
        GrayScalePair[] newArr = new GrayScalePair[size * 2];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[i];
        }
        elements = newArr;
    }

    private class GrayScalePair {
        public int grayScale;
        public int newColour;

        public GrayScalePair(int grayScale, int newColour) {
            this.grayScale = grayScale;
            this.newColour = newColour;
        }
    }
}

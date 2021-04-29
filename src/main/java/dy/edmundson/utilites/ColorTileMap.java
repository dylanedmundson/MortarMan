package dy.edmundson.utilites;

/**
 * maps pixel colors to a tile ID
 */
public class ColorTileMap {
    private static final int BASE_SIZE = 3;
    private ColorTilePair[] elements;
    int size;

    public ColorTileMap() {
        elements = new ColorTilePair[BASE_SIZE];
        size = 0;
    }

    /**
     * adds a new tile ID and its associated colour
     * @param tileID id of map tile type
     * @param associatedColor the color of a tile that will be replaced by associated tile ID
     */
    public void add(int tileID, int associatedColor) {
        if (size >= elements.length) {
            resize();
        }
        elements[size] = new ColorTilePair(tileID, associatedColor);
        size++;
    }

    /**
     * get tile ID of associated color
     * @param associatedTileColor color of tile to be overwritten by tileID
     * @return id of tile
     */
    public int getTileID(int associatedTileColor) {
        for (int i = 0; i < size; i++) {
            if (associatedTileColor == elements[i].associatedColor) {
                return elements[i].tileID;
            }
        }
        return -1;
    }

    private void resize() {
        ColorTilePair[] newArr = new ColorTilePair[size * 2];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[i];
        }
        elements = newArr;
    }

    private class ColorTilePair{
        public int tileID;
        public int associatedColor;

        public ColorTilePair(int tileID, int associatedColor) {
            this.tileID = tileID;
            this.associatedColor = associatedColor;
        }
    }
}

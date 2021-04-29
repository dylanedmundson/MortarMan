package dy.edmundson.level.tiles;

import dy.edmundson.game.Game;

public abstract class Tile {
    private int tileId;
    private boolean isSolid;
    private boolean isEmitter;
    private int associatedLevelColour;
    protected int[] tilePixelData;

    public static final int GRASS_ID = 0;
    public static final int STONE_ID = 1;
    public static final int VOID_ID = 2;
    public static final int MORTAR_ID = 3;

    public static final Tile GRASS_TILE = new GrassTile();
    public static final Tile STONE_TILE = new StoneTile();
    public static final Tile VOID_TILE = new VoidTile();

    public static final int GRASS_REF_COLOR = -16711936; //lime inkscape 00ff00ff
    public static final int STONE_REF_COLOR = -6710887; //40% gray inkscape 999999ff
    public static final int MORTAR_REF_COLOR = -16775424; //Black ink scape

    public Tile(int tileId, boolean isSolid, boolean isEmitter, int associatedLevelColour) {
        this.tileId = tileId;
        this.isSolid = isSolid;
        this.isEmitter = isEmitter;
        this.associatedLevelColour = associatedLevelColour;
        tilePixelData = new int[Game.PIXELS_PER_TILE * Game.PIXELS_PER_TILE];
    }

    public int tileId() {
        return tileId;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public boolean isEmitter() {
        return isEmitter;
    }

    public abstract void tick();

    public int getAssociatedLevelColour() {
        return associatedLevelColour;
    }

    public int[] getTilePixelData() {
        return tilePixelData;
    }


    public abstract void loadTileData();
    public abstract void changeColourData();
    public static Tile getTile(int tileId) {
        switch (tileId) {
            case GRASS_ID:
                return GRASS_TILE;
            case STONE_ID:
                return STONE_TILE;
            default:
                return VOID_TILE;
        }
    }
}

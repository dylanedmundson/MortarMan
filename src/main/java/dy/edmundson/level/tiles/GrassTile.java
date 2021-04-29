package dy.edmundson.level.tiles;

import dy.edmundson.game.Game;
import dy.edmundson.game.gfx.mColour;
import dy.edmundson.utilites.GrayScaleList;

public class GrassTile extends Tile {
    public GrassTile() {
        super(Tile.GRASS_ID, false, false, GRASS_REF_COLOR);
        loadTileData();
        changeColourData();
    }

    @Override
    public void tick() {

    }

    @Override
    public void loadTileData() {
        Game.spriteSheet.getSpriteSheetTiles(0, 0, 1, 1, this.tilePixelData);
    }

    @Override
    public void changeColourData() {
        GrayScaleList list = new GrayScaleList();
        list.add(mColour.BLACK, mColour.get(0, 255, 0, 100));
        list.add(mColour.GRAY_60, mColour.get(0, 150, 0, 255));
        mColour.changeColours(tilePixelData, list);
    }
}

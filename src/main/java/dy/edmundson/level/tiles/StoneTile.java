package dy.edmundson.level.tiles;

import dy.edmundson.game.Game;
import dy.edmundson.game.gfx.mColour;
import dy.edmundson.utilites.GrayScaleList;

public class StoneTile extends Tile {
    public StoneTile() {
        super(Tile.GRASS_ID, true, false, STONE_REF_COLOR);
        loadTileData();
        changeColourData();
    }

    @Override
    public void tick() {

    }

    @Override
    public void loadTileData() {
        Game.spriteSheet.getSpriteSheetTiles(1, 1, 1, 1, this.tilePixelData);
    }

    @Override
    public void changeColourData() {
        GrayScaleList list = new GrayScaleList();
        list.add(mColour.BLACK, mColour.get(102, 102, 102, 100));
        list.add(mColour.GRAY_60, mColour.get(153, 153, 153, 100));
        mColour.changeColours(tilePixelData, list);
    }

}

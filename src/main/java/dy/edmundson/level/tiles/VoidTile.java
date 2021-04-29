package dy.edmundson.level.tiles;

import java.awt.*;

public class VoidTile extends Tile{
    public VoidTile() {
        super(VOID_ID, false, false, 0);
        loadTileData();
    }

    @Override
    public void tick() {

    }

    @Override
    public void loadTileData() {
        for (int i = 0; i < tilePixelData.length; i++) {
            tilePixelData[i] = (new Color(0,0,0,255)).getRGB();
        }
    }

    @Override
    public void changeColourData() {

    }
}

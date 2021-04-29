package dy.edmundson.level;

import dy.edmundson.entities.MortarLaunch;
import dy.edmundson.game.Game;
import dy.edmundson.game.InputHandler;
import dy.edmundson.game.gfx.ScreenRenderer;
import dy.edmundson.level.tiles.Tile;
import dy.edmundson.utilites.ColorTileMap;
import dy.edmundson.utilites.PixelArrayHandler;
import dy.edmundson.entities.Entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Level {
    public int [] tileIDs;
    protected int[] levelPixels;
    protected int width;
    protected int height;
    public int tileIDWidth;
    protected List<Entity> entities;
    public ScreenRenderer screenRenderer;
    public InputHandler inputHandler;

    public Level() {

    }

    public Level(String levelPath, ColorTileMap colorTileMap, int tilesWidth, int tilesHeight,
                 ScreenRenderer screenRenderer, InputHandler inputHandler) {
        this.tileIDWidth = tilesWidth;
        this.screenRenderer = screenRenderer;
        this.inputHandler = inputHandler;
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(levelPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.width = tilesWidth * Game.PIXELS_PER_TILE;
        this.height = tilesHeight * Game.PIXELS_PER_TILE;
        entities = new CopyOnWriteArrayList<>();
        if (image != null) {
            tileIDs = new int[tilesWidth * tilesHeight];
            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(),
                    null, 0, image.getWidth());
            recordTileIDs(pixels, colorTileMap);
            levelPixels = new int[width * height * Game.PIXELS_PER_TILE * Game.PIXELS_PER_TILE *
                    Game.SCALE * Game.SCALE];
            replace();
        }
    }

    private void recordTileIDs(int[] pixels, ColorTileMap map) {
        int i = 0;
        for (int row = 0; row < 64; row++) {
            for (int col = 0; col < 64; col++) {
                tileIDs[i] = map.getTileID(PixelArrayHandler.getXYCoord(pixels, col, row, 64));
                i++;
            }
        }
    }
    // private void printIDs() {
    //     System.out.print(" ");
    //     for (int i = 0; i < tileIDs.length; i++) {
    //         if (i % 64 == 0) {
    //             System.out.println();
    //             System.out.print("" + (i / 64) + ": ");
    //         }
    //         System.out.print(tileIDs[i]);
    //     }
    // }

    public void replace() {
        List<Integer> mortarRows = new ArrayList<>();
        List<Integer> mortarCols = new ArrayList<>();
        for (int i = 0; i < tileIDs.length; i++) {
            int[] tilePix = Tile.getTile(tileIDs[i]).getTilePixelData();
            if (tileIDs[i] == Tile.MORTAR_ID) {
                mortarRows.add(i / 64);
                mortarCols.add(i % 64);
                tilePix = Tile.getTile(tileIDs[i + 1]).getTilePixelData();
            }
            int tileRow = i / 64;
            int tileCol = i % 64;
            PixelArrayHandler.putGraphicsTile(tilePix, tileRow, tileCol, levelPixels);
        }

        levelPixels = PixelArrayHandler.scalePixels(levelPixels, width, height, Game.SCALE);
        width = width * Game.SCALE;
        height = height *Game.SCALE;

        for (int i = 0; i < mortarRows.size(); i++) {
           Entity mortar = new MortarLaunch(this, mortarCols.get(i) * Game.PIXELS_PER_TILE * Game.SCALE,
                   mortarRows.get(i) * Game.PIXELS_PER_TILE * Game.SCALE, screenRenderer, inputHandler);
           entities.add(mortar);
            PixelArrayHandler.renderImageOnTop(mortar.entityPixels, mortar.getWidth(), mortar.getHeight(),
                    mortar.x, mortar.y, this.levelPixels, this.width);
        }
        mortarRows.clear();
        mortarCols.clear();
    }

    public int[] getPixels() {
        return levelPixels;
    }

    public void tick() {

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public int getTileID(int x, int y) {
        if (y >= height || x >= width) {
            return -1;
        } else {
            return tileIDs[(y / Game.SCALE / Game.PIXELS_PER_TILE * tileIDWidth) +
                    (x / Game.SCALE / Game.PIXELS_PER_TILE)];
        }
    }

}

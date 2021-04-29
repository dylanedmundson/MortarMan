package dy.edmundson.utilites;

import dy.edmundson.game.gfx.ScreenRenderer;
import dy.edmundson.entities.Entity;

public class CollisionBox {
    private int width;
    private int height;
    private int xIndex;
    private int yIndex;
    private Entity entity;
    private ScreenRenderer screenRenderer;

    public CollisionBox(int xIndex, int yIndex, int width, int height, Entity entity, ScreenRenderer screenRenderer) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.width = width;
        this.height = height;
        this.entity = entity;
        this.screenRenderer = screenRenderer;
    }

    public int getXMin() {
        return screenRenderer.xOffset + entity.x + xIndex;
    }

    public int getXMax() {
        return screenRenderer.xOffset + entity.x + xIndex + width;
    }

    public int getYMin() {
        return screenRenderer.yOffset + entity.y + yIndex;
    }

    public int getYMax() {
        return screenRenderer.yOffset + entity.y + yIndex + height;
    }

    // public int boxesCollide(CollisionBox other) {
    //     int xMin = other.getXMin();
    //     int xMax = other.getXMax();
    //     int yMin = other.getYMin();
    //     int yMax = other.getYMax();
    //
    //     //top
    //     for (int x = xMin; x < xMax - 1; x++) {
    //         int id = level.getTileID(x, yMin);
    //         if (id != -1 && Tile.getTile(id).isSolid()) {
    //             return true;
    //         }
    //     }
    //     //bot
    //     for (int x = xMin; x < xMax - 1; x++) {
    //         int id = level.getTileID(x, yMax);
    //         if (id != -1 && Tile.getTile(id).isSolid()) {
    //             return true;
    //         }
    //     }
    //     //Left
    //     for (int y = yMin; y < yMax - 1; y++) {
    //         int id = level.getTileID(xMin, y);
    //         if (id != -1 && Tile.getTile(id).isSolid()) {
    //             return true;
    //         }
    //     }
    //     //right
    //     for (int y = yMin; y < yMax - 1; y++) {
    //         int id = level.getTileID(xMax, y);
    //         if (id != -1 && Tile.getTile(id).isSolid()) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }
}

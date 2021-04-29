package dy.edmundson.entities;

import dy.edmundson.game.gfx.ScreenRenderer;
import dy.edmundson.level.Level;
import dy.edmundson.level.tiles.Tile;

public abstract class Mob extends Entity{
    protected static final int COLLISION_DEBOUNCE = 1000;
    public boolean isAlive = true;
    public int speed;
    public boolean isMoving;
    long moveCount;
    private long lastTime;

    public Mob(Level level, int startXPos, int startYPos, int speed, ScreenRenderer screenRenderer) {
        super(level);
        this.x = startXPos;
        this.y = startYPos;
        this.speed = speed;
        this.screenRenderer = screenRenderer;
        lastTime = System.currentTimeMillis();
        moveCount = 0;
    }

    @Override
    public void tick(Player player) {
        move();
        if (isMoving) {
            long currTime = System.currentTimeMillis();
            if (currTime - lastTime >= 250) {
                lastTime = currTime;
                moveCount++;
            }
        }
    }


    public abstract void move();

    @Override
    public boolean hasCollided() {
        if (collisionBox == null) {
            return false;
        }
        int xMin = collisionBox.getXMin();
        int xMax = collisionBox.getXMax();
        int yMin = collisionBox.getYMin();
        int yMax = collisionBox.getYMax();

        //top
        for (int x = xMin; x < xMax - 1; x++) {
            int id = level.getTileID(x, yMin);
            if (id != -1 && Tile.getTile(id).isSolid()) {
                return true;
            }
        }
        //bot
        for (int x = xMin; x < xMax - 1; x++) {
            int id = level.getTileID(x, yMax);
            if (id != -1 && Tile.getTile(id).isSolid()) {
                return true;
            }
        }
        //Left
        for (int y = yMin; y < yMax - 1; y++) {
            int id = level.getTileID(xMin, y);
            if (id != -1 && Tile.getTile(id).isSolid()) {
                return true;
            }
        }
        //right
        for (int y = yMin; y < yMax - 1; y++) {
            int id = level.getTileID(xMax, y);
            if (id != -1 && Tile.getTile(id).isSolid()) {
                return true;
            }
        }
        return false;
    }

}

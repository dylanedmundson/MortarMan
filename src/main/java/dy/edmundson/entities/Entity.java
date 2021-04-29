package dy.edmundson.entities;

import dy.edmundson.game.gfx.ScreenRenderer;
import dy.edmundson.level.Level;
import dy.edmundson.utilites.CollisionBox;
import dy.edmundson.utilites.PixelArrayHandler;

public abstract class Entity {
    //TODO: either this game or next need to come up with state manager that manages state of board
    //so we only make changes to pixels that are going to be seen
    //TODO: add enemy
    //TODO: add enemy collision with player and with mortars and enemy life system
    //TODO: add health bar or hearts to track health
    //TODO: add knife attack for player
    //TODO: add levels and level changes
    //TODO: add boss level and boss entity
    //TODO: add victory screen
    public enum MOVING_DIR {NORTH, SOUTH, EAST, WEST};
    public static final int PLAYER_SPEED = 2;
    public static final int PLAYER_SCALE = 3;
    public static final int MORTAR_LAUNCH_SCALE = 3;
    public static final int TILES_PER_MOB = 4;
    public static final int MORTAR_SCALE = 3;
    public static final int EXPLOSION_SCALE = 3;
    protected static final int MORTAR_ANIMATION_DIST = 40;

    public int x, y;
    public int[] entityPixels;
    public Level level;
    protected int width;
    protected int height;
    public MOVING_DIR movingDir;
    public ScreenRenderer screenRenderer;
    protected CollisionBox collisionBox;
    public int scale;

    public Entity(Level level) {
        this.level = level;
        level.addEntity(this);
        this.width = 2 * 8 * MORTAR_LAUNCH_SCALE;
        this.height = 2 * 8 * MORTAR_LAUNCH_SCALE;
    }
    public CollisionBox getCollisionBox() {
        return collisionBox;
    }
    public abstract void tick(Player player);
    public abstract void loadEntityPixels();
    public abstract boolean hasCollided();
    public int getWidth() {
        return width;
    }
    public abstract void collide();
    public int getHeight() {
        return height;
    }
    public int[] getEntityPixels() {
        return entityPixels;
    }
    public void render(int[] pixels, int width) {
        loadEntityPixels();
        PixelArrayHandler.renderImageOnTop(this.entityPixels, this.width, this.height, this.x, this.y, pixels, width);
    };

}

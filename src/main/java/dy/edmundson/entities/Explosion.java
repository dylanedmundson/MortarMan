package dy.edmundson.entities;

import dy.edmundson.game.Game;
import dy.edmundson.game.gfx.ScreenRenderer;
import dy.edmundson.game.gfx.mColour;
import dy.edmundson.level.Level;
import dy.edmundson.utilites.CollisionBox;
import dy.edmundson.utilites.GrayScaleList;
import dy.edmundson.utilites.PixelArrayHandler;

public class Explosion extends Entity{
    private static final int EXPLOSION_ANIMATION_LENGTH = 1000;
    private static final int EXPLOSION_ANIMATION_TIME = 200;
    private GrayScaleList grayScaleList;
    private int xSpirteStart;
    private int[] backgroundPixels;
    private Player player;
    private long lastTime;
    private long lastPixelChange;
    private boolean isAlive;

    public Explosion(Level level, int startXPos, int startYPos, ScreenRenderer screenRenderer) {
        super(level);
        this.level = level;
        this.x = startXPos;
        this.y = startYPos;
        this.scale = EXPLOSION_SCALE;
        this.screenRenderer = screenRenderer;
        width =  8 * MORTAR_SCALE;
        height = 8 * MORTAR_SCALE;
        collisionBox = new CollisionBox(0, 0, width, height, this, screenRenderer);
        grayScaleList = new GrayScaleList();
        grayScaleList.add(mColour.BLACK, mColour.get(-1, -1, -1, -1));
        grayScaleList.add(mColour.GRAY_40, mColour.get(-1, -1, -1, 255));
        grayScaleList.add(mColour.WHITE, mColour.get(1000, -1, -1, 255));
        xSpirteStart = 0;
        loadEntityPixels();
        backgroundPixels = PixelArrayHandler.getPortionPixelArray(width, height,
                x, y, level.getPixels(), level.getWidth());
        lastTime = System.currentTimeMillis();
        isAlive = true;
        lastPixelChange = lastTime;
    }

    @Override
    public void tick(Player player) {
        if (this.player == null) {
            this.player = player;
        }
        long nextTime = System.currentTimeMillis();
        if (nextTime - lastTime > EXPLOSION_ANIMATION_LENGTH) {
            isAlive = false;
        }
        if (nextTime - lastPixelChange > EXPLOSION_ANIMATION_TIME) {
            lastPixelChange = nextTime;
            xSpirteStart++;
            if (xSpirteStart >= 3) {
                xSpirteStart = 0;
            }
        }
        hasCollided();
    }

    @Override
    public void loadEntityPixels() {
        entityPixels = new int[Game.PIXELS_PER_TILE * Game.PIXELS_PER_TILE];
        Game.spriteSheet.getSpriteSheetTiles(xSpirteStart, xSpirteStart, 2, 2,
                this.entityPixels);
        mColour.changeColours(this.entityPixels, this.grayScaleList);
        if (scale > 1) {
            entityPixels = PixelArrayHandler.scalePixels(this.entityPixels, 8, 8, scale);
        }
    }

    @Override
    public void render(int[] pixels, int width) {
        if (isAlive) {
            loadEntityPixels();
            PixelArrayHandler.renderImageOnTop(this.backgroundPixels, this.width, this.height,
                    this.x, this.y, level.getPixels(), level.getWidth());
            PixelArrayHandler.renderImageOnTop(this.entityPixels, this.width, this.height, this.x, this.y,
                    level.getPixels(), level.getWidth());
        } else {
            PixelArrayHandler.renderImageOnTop(this.backgroundPixels, this.width, this.height,
                    this.x, this.y, level.getPixels(), level.getWidth());
            screenRenderer.entityRemoveQueue.add(this);
        }
    }

    @Override
    public boolean hasCollided() {
        for (Entity e : level.getEntities()) {
            if (hasCollieded(e)) e.collide();
        }
        return false;
    }

    private boolean hasCollieded(Entity e) {
        if (e.getCollisionBox() == null) {
            return false;
        }
        int xMin = e.getCollisionBox().getXMin();
        int xMax = e.getCollisionBox().getXMax();
        int yMin = e.getCollisionBox().getYMin();
        int yMax = e.getCollisionBox().getYMax();

        //top
        for (int x = xMin; x < xMax; x++) {
            if (x >= this.x && x <= this.x + width && yMin >= this.y && yMin <= this.y + height) {
                return true;
            }
        }
        //bot
        for (int x = xMin; x < xMax; x++) {
            if (x >= this.x && x <= this.x + width && yMax >= this.y && yMax <= this.y + height) {
                return true;
            }
        }
        //left
        for (int y = yMin; y < yMax; y++) {
            if (y >= this.y && y <= this.y + height && xMin >= this.x && xMin <= this.x + width) {
                return true;
            }
        }
        //right
        for (int y = yMin; y < yMax; y++) {
            if (y >= this.y && y <= this.y + height && xMax >= this.x && xMax <= this.x + width) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void collide() {

    }
}

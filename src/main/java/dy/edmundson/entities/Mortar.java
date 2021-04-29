package dy.edmundson.entities;

import dy.edmundson.game.Game;
import dy.edmundson.game.gfx.ScreenRenderer;
import dy.edmundson.game.gfx.mColour;
import dy.edmundson.level.Level;
import dy.edmundson.utilites.GrayScaleList;
import dy.edmundson.utilites.PixelArrayHandler;

public class Mortar extends Entity {

    private GrayScaleList grayScaleList;
    private int xSpirteStart;
    private Player player;
    private boolean isAlive;
    private int detonatePos;
    private int[] BackgroundPixels;
    private int backgroundX;
    private int backgroundY;

    public Mortar(Level level, int startXPos, int startYPos, ScreenRenderer screenRenderer) {
        super(level);
        this.level = level;
        this.x = startXPos;
        this.y = startYPos;
        this.scale = MORTAR_SCALE;
        this.screenRenderer = screenRenderer;
        width =  8 * MORTAR_SCALE;
        height = 2 * 8 * MORTAR_SCALE;
        grayScaleList = new GrayScaleList();
        grayScaleList.add(mColour.BLACK, mColour.get(-1, -1, -1, -1));
        grayScaleList.add(mColour.GRAY_60, mColour.get(0, 0, 0, 255));
        grayScaleList.add(mColour.GRAY_40, mColour.get(255, 255, 255, 255));
        grayScaleList.add(mColour.WHITE, mColour.get(1000, -1, -1, 255));
        xSpirteStart = 0;
        loadEntityPixels();
        isAlive = true;
        detonatePos = this.y + MORTAR_ANIMATION_DIST;
        BackgroundPixels = PixelArrayHandler.getPortionPixelArray(width, height + MORTAR_ANIMATION_DIST,
                x, y, level.getPixels(), level.getWidth());
        backgroundX = x;
        backgroundY = y;
    }

    @Override
    public void tick(Player player) {
        if (this.player == null) {
            this.player = player;
        }
        if (y != detonatePos) {
            y++;
        } else {
            isAlive = false;
        }
    }

    @Override
    public void loadEntityPixels() {
        entityPixels = new int[2 * Game.PIXELS_PER_TILE * Game.PIXELS_PER_TILE];
        Game.spriteSheet.getSpriteSheetTiles(xSpirteStart, xSpirteStart + 1, 0, 0,
                this.entityPixels);
        entityPixels = PixelArrayHandler.rotate90(entityPixels, 2 * 8, 8);
        mColour.changeColours(this.entityPixels, this.grayScaleList);
        if (scale > 1) {
            entityPixels = PixelArrayHandler.scalePixels(this.entityPixels, 8, 16, scale);
        }
    }
    

    @Override
    public void render(int[] pixels, int width) {
        if (isAlive) {
            PixelArrayHandler.renderImageOnTop(this.BackgroundPixels, this.width, this.height + MORTAR_ANIMATION_DIST,
                    this.backgroundX, this.backgroundY, level.getPixels(), level.getWidth());
            PixelArrayHandler.renderImageOnTop(this.entityPixels, this.width, this.height, this.x, this.y,
                    level.getPixels(), level.getWidth());
        } else {
            detonate();
        }
    }

    private void detonate() {
        PixelArrayHandler.renderImageOnTop(this.BackgroundPixels, this.width, this.height + MORTAR_ANIMATION_DIST,
                this.backgroundX, this.backgroundY, level.getPixels(), level.getWidth());
        screenRenderer.entityQueue.add(new Explosion(level, x, detonatePos, screenRenderer));
        screenRenderer.entityRemoveQueue.add(this);
    }

    @Override
    public boolean hasCollided() {
        return false;
    }

    @Override
    public void collide() {

    }
}

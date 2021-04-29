package dy.edmundson.entities;

import dy.edmundson.game.Game;
import dy.edmundson.game.InputHandler;
import dy.edmundson.game.gfx.ScreenRenderer;
import dy.edmundson.game.gfx.mColour;
import dy.edmundson.level.Level;
import dy.edmundson.level.tiles.Tile;
import dy.edmundson.utilites.CollisionBox;
import dy.edmundson.utilites.GrayScaleList;
import dy.edmundson.utilites.PixelArrayHandler;

public class MortarLaunch extends Entity{
    private static final int DEBOUNCE = 300;
    private static final int ANIMATION_TIMEOUT = 50;
    private static final int MORTAR_LAUNCH_DIST = 100;
    private static final int[] backgroundRestore = PixelArrayHandler.scalePixels(Tile.getTile(Tile.GRASS_ID).getTilePixelData(),
            Game.PIXELS_PER_TILE, Game.PIXELS_PER_TILE, Game.SCALE);

    private InputHandler inputHandler;
    private GrayScaleList grayScaleList;
    private Player player;
    private long lastLaunchTime;
    private int animationTimeoutCount;
    private boolean isLaunching = false;
    private int xSpirteStart;
    private MOVING_DIR launchDir;
    public MortarLaunch(Level level, int startXPos, int startYPos, ScreenRenderer screenRenderer, InputHandler inputHandler) {
        super(level);
        this.level = level;
        this.x = startXPos;
        this.y = startYPos;
        this.scale = MORTAR_LAUNCH_SCALE;
        this.inputHandler = inputHandler;
        this.screenRenderer = screenRenderer;
        width = 2 * 8 * MORTAR_LAUNCH_SCALE;
        height = 2 * 8 * MORTAR_LAUNCH_SCALE;
        collisionBox = new CollisionBox(0, 0, width, height, this, screenRenderer);
        grayScaleList = new GrayScaleList();
        grayScaleList.add(mColour.BLACK, mColour.get(-1, -1, -1, -1));
        grayScaleList.add(mColour.GRAY_60, mColour.get(0, 0, 0, 255));
        grayScaleList.add(mColour.GRAY_40, mColour.get(255, 255, 255, 255));
        grayScaleList.add(mColour.WHITE, mColour.get(1000, -1, -1, 255));
        xSpirteStart = 8;
        loadEntityPixels();
        // render(null, 0);
        lastLaunchTime = System.currentTimeMillis();
    }

    @Override
    public void tick(Player player) {
        if (this.player == null) {
            this.player = player;
        }
        if (hasCollided()) {
            long t = System.currentTimeMillis();
            if (inputHandler.space.isPressed() && t - lastLaunchTime > DEBOUNCE) {
                lastLaunchTime = t;
                isLaunching = true;
                launchDir = player.movingDir;
            }
        }
        if (isLaunching) {
            animationTimeoutCount++;
            if (animationTimeoutCount < ANIMATION_TIMEOUT) {
                xSpirteStart = 10;
                loadEntityPixels();
            } else if (animationTimeoutCount < 2 * ANIMATION_TIMEOUT) {
                xSpirteStart = 12;
                loadEntityPixels();
            } else if (animationTimeoutCount < 3 * ANIMATION_TIMEOUT) {
                xSpirteStart = 14;
                loadEntityPixels();
            } else {
                xSpirteStart = 8;
                loadEntityPixels();
                render(null, 0);
                launch();
                isLaunching = false;
            }
        }
    }

    private void restoreBackground() {
        PixelArrayHandler.renderImageOnTop(backgroundRestore, Game.PIXELS_PER_TILE * Game.SCALE,
                Game.PIXELS_PER_TILE * Game.SCALE, x, y, level.getPixels(), level.getWidth());
        PixelArrayHandler.renderImageOnTop(backgroundRestore, Game.PIXELS_PER_TILE * Game.SCALE,
                Game.PIXELS_PER_TILE * Game.SCALE, x + Game.PIXELS_PER_TILE * Game.SCALE,
                y, level.getPixels(), level.getWidth());
        PixelArrayHandler.renderImageOnTop(backgroundRestore, Game.PIXELS_PER_TILE * Game.SCALE,
                Game.PIXELS_PER_TILE * Game.SCALE, x, y + Game.PIXELS_PER_TILE * Game.SCALE,
                level.getPixels(), level.getWidth());
        PixelArrayHandler.renderImageOnTop(backgroundRestore, Game.PIXELS_PER_TILE * Game.SCALE,
                Game.PIXELS_PER_TILE * Game.SCALE, x + Game.PIXELS_PER_TILE * Game.SCALE,
                y + Game.PIXELS_PER_TILE * Game.SCALE, level.getPixels(), level.getWidth());
    }

    private void launch() {
        animationTimeoutCount = 0;
        int mortarXStart;
        int mortarYStart;
        if (launchDir == MOVING_DIR.NORTH) {
            mortarXStart = x;
            mortarYStart = y - MORTAR_LAUNCH_DIST - MORTAR_ANIMATION_DIST;;
            if (mortarYStart < 0) {
                return;
            }
        } else if (launchDir == MOVING_DIR.SOUTH) {
            mortarXStart = x;
            mortarYStart = y + height + MORTAR_LAUNCH_DIST - MORTAR_ANIMATION_DIST;;
            if (mortarYStart >= level.getHeight() || mortarYStart < 0) {
                return;
            }
        } else if (launchDir == MOVING_DIR.EAST) {
            mortarXStart = x + width + MORTAR_LAUNCH_DIST;
            mortarYStart = y - MORTAR_ANIMATION_DIST;;
            if (mortarXStart >= level.getWidth() || mortarYStart < 0) {
                return;
            }
        } else {
            mortarXStart = x - MORTAR_LAUNCH_DIST;
            mortarYStart = y - MORTAR_ANIMATION_DIST;;
            if(mortarXStart < 0 || mortarYStart < 0) {
                return;
            }
        }
        screenRenderer.entityQueue.add(new Mortar(level, mortarXStart, mortarYStart, screenRenderer));
    }

    @Override
    public void loadEntityPixels() {
        entityPixels = new int[2 * 2 * Game.PIXELS_PER_TILE * Game.PIXELS_PER_TILE];
        Game.spriteSheet.getSpriteSheetTiles(xSpirteStart, xSpirteStart + 1, 28, 29, this.entityPixels);
        mColour.changeColours(this.entityPixels, this.grayScaleList);
        if (scale > 1) {
            entityPixels = PixelArrayHandler.scalePixels(this.entityPixels, 16, 16, scale);
        }
    }

    @Override
    public void render(int[] pixels, int width) {
        if (isLaunching) {
            restoreBackground();
            PixelArrayHandler.renderImageOnTop(this.entityPixels, this.width, this.height, this.x, this.y,
                    level.getPixels(), level.getWidth());
        }
    }

    @Override
    public boolean hasCollided() {
        int xMin = player.collisionBox.getXMin();
        int xMax = player.collisionBox.getXMax();
        int yMin = player.collisionBox.getYMin();
        int yMax = player.collisionBox.getYMax();

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
            if (y >= this.y && y <= this.y + height && xMax  >= this.x && xMax <= this.x + width) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void collide() {

    }
}

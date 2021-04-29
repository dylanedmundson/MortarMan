package dy.edmundson.entities;

import dy.edmundson.game.Game;
import dy.edmundson.game.InputHandler;
import dy.edmundson.game.gfx.ScreenRenderer;
import dy.edmundson.game.gfx.mColour;
import dy.edmundson.level.Level;
import dy.edmundson.utilites.CollisionBox;
import dy.edmundson.utilites.GrayScaleList;
import dy.edmundson.utilites.PixelArrayHandler;

public class Player extends Mob{

    private InputHandler inputHandler;
    private GrayScaleList grayScaleList;
    private long lastCollisionTime = 0;
    private int health;

    public Player(Level level, int startXPos, int startYPos, ScreenRenderer screenRenderer, InputHandler inputHandler) {
        super(level, startXPos, startYPos, PLAYER_SPEED, screenRenderer);
        this.inputHandler = inputHandler;
        scale = PLAYER_SCALE;
        this.width = 8 * 2 * scale;
        this.height = 8 * 2 * scale;
        movingDir = MOVING_DIR.SOUTH;
        grayScaleList = new GrayScaleList();
        grayScaleList.add(mColour.BLACK, mColour.get(-1, -1, -1, -1));
        grayScaleList.add(mColour.GRAY_60, mColour.get(0, 0, 0, 255));
        grayScaleList.add(mColour.GRAY_40, mColour.get(100, 200, 100, 255));
        grayScaleList.add(mColour.WHITE, mColour.get(300, 150, 100, 100));
        grayScaleList.add(mColour.GRAY_80, mColour.get(120, 68, 38, 100));
        this.collisionBox = new CollisionBox(2 * PLAYER_SCALE, 8 * PLAYER_SCALE,
                width - 4 * PLAYER_SCALE, height - 8 * PLAYER_SCALE - 1, this, screenRenderer);
        health = 1;
    }

    @Override
    public void move() {
        if (inputHandler.right.isPressed()) {
            x += speed;
            isMoving = true;
            movingDir = MOVING_DIR.EAST;
            if (hasCollided()) {
                x -= speed;
            }
        }
        if (inputHandler.left.isPressed()) {
            x -= speed;
            isMoving = true;
            movingDir = MOVING_DIR.WEST;
            if (hasCollided()) {
                x += speed;
            }
        }
        if (inputHandler.down.isPressed()) {
            y += speed;
            isMoving = true;
            movingDir = MOVING_DIR.SOUTH;
            if (hasCollided()) {
                y -= speed;
            }
        }
        if (inputHandler.up.isPressed()) {
            y -= speed;
            isMoving = true;
            movingDir = MOVING_DIR.NORTH;
            if (hasCollided()) {
                y += speed;
            }
        }
        if (!inputHandler.up.isPressed() && !inputHandler.down.isPressed() &&
        !inputHandler.left.isPressed() && !inputHandler.right.isPressed()) {
            isMoving = false;
        }

        if (y < 0) {
            screenRenderer.yOffset -= speed;
            y = 0;
            if (screenRenderer.yOffset < 0) {
                screenRenderer.yOffset = 0;
            }
        }
        if (x < 0) {
            x = 0;
            screenRenderer.xOffset -= speed;
            if (screenRenderer.xOffset < 0) {
                screenRenderer.xOffset = 0;
            }
        }
        if (y > screenRenderer.height - height - 1) {
            screenRenderer.yOffset += speed;
            y = screenRenderer.height - height - 1;
            if (screenRenderer.yOffset >= level.getHeight() - screenRenderer.height) {
                screenRenderer.yOffset = level.getHeight() - screenRenderer.height;
            }
        }
        if (x > screenRenderer.width - width - 1) {
            screenRenderer.xOffset += speed;
            x = screenRenderer.width - width - 1;
            if (screenRenderer.xOffset >= level.getWidth() - screenRenderer.width) {
                screenRenderer.xOffset = level.getWidth() - screenRenderer.width;
            }
        }
        moveCount++;
    }

    @Override
    public void loadEntityPixels() {
        this.entityPixels = new int[Game.PIXELS_PER_TILE * Game.PIXELS_PER_TILE * TILES_PER_MOB];
        if (movingDir == MOVING_DIR.SOUTH) {
            Game.spriteSheet.getSpriteSheetTiles(0, 1, 28, 29, this.entityPixels);
            if (moveCount % 2 == 1) {
                this.entityPixels = PixelArrayHandler.mirror(this.entityPixels, width / PLAYER_SCALE,
                        height / PLAYER_SCALE);
            }
        } else if (movingDir == MOVING_DIR.NORTH) {
            Game.spriteSheet.getSpriteSheetTiles(2, 3, 28, 29, this.entityPixels);
            if (moveCount % 2 == 1) {
                this.entityPixels = PixelArrayHandler.mirror(this.entityPixels, width / PLAYER_SCALE,
                        height / PLAYER_SCALE);
            }
        } else if (movingDir == MOVING_DIR.EAST) {
            Game.spriteSheet.getSpriteSheetTiles(4, 5, 28, 29, this.entityPixels);
            if (moveCount % 2 == 1) {
                Game.spriteSheet.getSpriteSheetTiles(6, 7, 28, 29, this.entityPixels);
            }
        } else if (movingDir == MOVING_DIR.WEST) {
            Game.spriteSheet.getSpriteSheetTiles(4, 5, 28, 29, this.entityPixels);
            if (moveCount % 2 == 1) {
                Game.spriteSheet.getSpriteSheetTiles(6, 7, 28, 29, this.entityPixels);
            }
            this.entityPixels = PixelArrayHandler.mirror(this.entityPixels, width / PLAYER_SCALE,
                    height / PLAYER_SCALE);
        }
        if (scale > 1) {
            entityPixels = PixelArrayHandler.scalePixels(this.entityPixels, 16, 16, scale);
        }
        mColour.changeColours(this.entityPixels, grayScaleList);
    }

    @Override
    public void collide() {
        long nextTime = System.currentTimeMillis();
        if (nextTime - lastCollisionTime > COLLISION_DEBOUNCE) {
            lastCollisionTime = nextTime;
            health--;
            System.out.println("Collide, heath: " + health);
            if (health == 0) {
                screenRenderer.gameOver();
            }
        }
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}

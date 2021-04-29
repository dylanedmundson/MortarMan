package dy.edmundson.level;

import dy.edmundson.game.Game;
import dy.edmundson.game.InputHandler;
import dy.edmundson.game.gfx.MFont;
import dy.edmundson.game.gfx.ScreenRenderer;
import dy.edmundson.game.gfx.mColour;

public class GameOverScreen extends Level{
    private static final int ANIMATION_DELAY = 80;
    public static final int PLAY_AGAIN = 0;
    public static final int FONT_COLOR = mColour.get(0, 0, 0, 0);
    public static final int HIGHLIGHT_COLOR = mColour.get(255, 0, 0, 1000);
    public static final int EXIT = 1;
    private long lastTime;
    private int choice;
    private Game game;
    private int playAgainColor;
    private int exitColor;

    public GameOverScreen(ScreenRenderer screenRenderer, InputHandler inputHandler, Game game) {
        width = screenRenderer.width;
        height = screenRenderer.height;
        this.inputHandler = inputHandler;
        lastTime = System.currentTimeMillis();
        choice = 0;
        this.game = game;
        playAgainColor = HIGHLIGHT_COLOR;
        exitColor = FONT_COLOR;
        this.levelPixels = screenRenderer.getScreenPixels();
        for (int i = 0; i < levelPixels.length; i++) {
            levelPixels[i] = ~((~levelPixels[i] >> 1) & 0x7F7F7F7F);
        }
        initPixels();
    }

    private void initPixels() {
        MFont.drawStringToPixels("Game Over!", this.levelPixels, this.width,
                MFont.HORIZ_CENTER, 50, 5, FONT_COLOR);
        int playAgainY = 50 + Game.PIXELS_PER_TILE * 5 + MFont.MARGIN + 50;
        MFont.drawStringToPixels("play again", this.levelPixels, this.width, 120,
                playAgainY,3, playAgainColor);
        int exitY = playAgainY + Game.PIXELS_PER_TILE * 3 + MFont.MARGIN;
        MFont.drawStringToPixels("exit", this.levelPixels, this.width,
                120, exitY, 3, exitColor);
    }

    @Override
    public void tick() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastTime > ANIMATION_DELAY) {
            lastTime = curTime;
            if (inputHandler.up.isPressed() || inputHandler.down.isPressed()) {
                choice++;
            }
            if (inputHandler.space.isPressed()) {
                if (choice % 2 == PLAY_AGAIN) {
                    game.init();
                } else {
                    game.endGame();
                }
            }
        }
    }

    public void updateInput() {
        if (choice % 2 == PLAY_AGAIN) {
            playAgainColor = HIGHLIGHT_COLOR;
            exitColor = FONT_COLOR;
        } else {
            exitColor = HIGHLIGHT_COLOR;
            playAgainColor = FONT_COLOR;
        }
        initPixels();
    }
}

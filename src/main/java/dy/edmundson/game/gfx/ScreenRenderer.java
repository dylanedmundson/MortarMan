package dy.edmundson.game.gfx;

import dy.edmundson.entities.Player;
import dy.edmundson.game.Game;
import dy.edmundson.level.GameOverScreen;
import dy.edmundson.level.Level;
import dy.edmundson.utilites.PixelArrayHandler;
import dy.edmundson.entities.Entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class ScreenRenderer {

    public int width;
    public int height;
    public int xOffset = 0;
    public int yOffset = 0;
    private int[] screenPixels;
    private Level level;
    private List<Entity> entities;
    public List<Entity> entityQueue;
    public List<Entity> entityRemoveQueue;
    private Player player;
    private boolean gameOver;
    private Game game;

    public ScreenRenderer(int width, int height, int[] screenPixels, Game game) {
        this.width = width;
        this.height = height;
        entityQueue = new CopyOnWriteArrayList<>();
        entityRemoveQueue = new CopyOnWriteArrayList<>();

        this.screenPixels = screenPixels;

        gameOver = false;
        this.game = game;
    }

    public void tick() {
        for (Entity e : entities) {
            e.tick(player);
        }
        level.tick();
    }


    /**
     * renders the screen background tiles (entity rendering will be handled in the entity class)
     */
    public void render() {
        if (entityQueue.size() != 0) {
            entities.addAll(entityQueue);
            entityQueue.clear();
        }
        if (entityRemoveQueue.size() != 0) {
            for (Entity e : entityRemoveQueue) {
                entities.remove(e);
            }
            entityRemoveQueue.clear();
        }
        if (gameOver) {
            ((GameOverScreen)level).updateInput();
        }
        PixelArrayHandler.putGraphicsPixel(level.getPixels(), level.getWidth(), yOffset, yOffset + height,
                xOffset, xOffset + width, this.screenPixels);
        for (Entity e : entities) {
            e.render(this.screenPixels, Game.WIDTH);
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addPlayer(Player player) {
        this.player = player;
    }

    public void setLevel(Level level) {
        this.level = level;
        entities = level.getEntities();
    }

    public void gameOver() {
        entityQueue.clear();
        entityRemoveQueue.clear();
        entities.clear();
        xOffset = 0;
        yOffset = 0;
        level = new GameOverScreen(this, level.inputHandler, game);
        gameOver = true;
    }

    public int[] getScreenPixels() {
        return this.screenPixels;
    }
}

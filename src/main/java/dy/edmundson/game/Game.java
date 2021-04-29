package dy.edmundson.game;

import dy.edmundson.game.gfx.ScreenRenderer;
import dy.edmundson.game.gfx.SpriteSheet;
import dy.edmundson.game.gfx.mColour;
import dy.edmundson.level.Level;
import dy.edmundson.level.tiles.Tile;
import dy.edmundson.utilites.ColorTileMap;
import dy.edmundson.utilites.GrayScaleList;
import dy.edmundson.entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Creates the MortarMan game
 */
public class Game extends Canvas implements Runnable {
    public static final int WIDTH = 480; //width of game screen in pixels
    public static final int HEIGHT = 360; //height of screen in pixels
    public static final int SCALE = 3;
    public static final String NAME = "Mortar Man";
    public static final String RES_PATH = "src/main/res/";
    public static final int PIXELS_PER_TILE = 8;
    public static final int SCREEN_TILES = 64;
    private static final int ICON_IMAGE_WIDTH = 16;
    private static final int ICON_IMAGE_HEIGHT = 8;
    private static final double TICKS_PER_SECOND = 1000000000D/60D; //60 ticks per 1000000000 nanoseconds (1 second)
    public static final SpriteSheet spriteSheet =
            new SpriteSheet(RES_PATH + "sprite_sheet.png", PIXELS_PER_TILE);


    //fields
    private JFrame frame;
    private boolean running = false;
    private ScreenRenderer screenRenderer;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    private InputHandler inputHandler;

    public Game () {
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        frame = new JFrame(NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        //set image icon of frame
        //get pixels from sprite sheet tiles
        BufferedImage gameIconImage =
                new BufferedImage(ICON_IMAGE_WIDTH, ICON_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        int[] iconImagePixels = ((DataBufferInt)gameIconImage.getRaster().getDataBuffer()).getData();
        spriteSheet.getSpriteSheetTiles(4, 5, 0, 0, iconImagePixels);

        //change grayscale colours to desired colours
        GrayScaleList iconImageGrayScaleList = new GrayScaleList();
        iconImageGrayScaleList.add(mColour.BLACK, mColour.get(-1, -1, -1, -1));
        iconImageGrayScaleList.add(mColour.GRAY_60, mColour.get(-1, -1, -1, 255));
        iconImageGrayScaleList.add(mColour.GRAY_40, mColour.get(100, 100, 100, 255));
        iconImageGrayScaleList.add(mColour.WHITE, mColour.get(255, -1, -1, 255));
        mColour.changeColours(iconImagePixels, iconImageGrayScaleList);

        frame.setIconImage(gameIconImage);

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void init() {
        screenRenderer = new ScreenRenderer(WIDTH,
                HEIGHT, this.pixels, this);
        inputHandler = new InputHandler();
        addKeyListener(inputHandler);
        ColorTileMap colorTileMap = new ColorTileMap();
        colorTileMap.add(Tile.GRASS_ID, Tile.GRASS_REF_COLOR);
        colorTileMap.add(Tile.STONE_ID, Tile.STONE_REF_COLOR);
        colorTileMap.add(Tile.MORTAR_ID, Tile.MORTAR_REF_COLOR);
        Level level1 = new Level(RES_PATH + "testCollisionLevel.png", colorTileMap, 64, 64,
                screenRenderer, inputHandler);
        screenRenderer.setLevel(level1);
        Player player = new Player(level1, 0, 0, screenRenderer, inputHandler);
        level1.addEntity(player);
        screenRenderer.addPlayer(player);
    }

    /**
     * runs the game until the player quits or dies
     */
    @Override
    public void run() {
        init(); //initialize game

        //handle frame and tick updating
        long lastTimeNano = System.nanoTime();
        long lastTimeMillis =  System.currentTimeMillis();
        long now;
        int tick = 0;
        int frames = 0;
        double delta = 0; //need delta to handle debouncing
        while (running) {
            now = System.nanoTime();
            delta += (now - lastTimeNano) / TICKS_PER_SECOND;
            lastTimeNano = now;
            while(delta >= 1) {
                tick++;
                tick();
                delta--;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            render();
            frames++;
            now = System.currentTimeMillis();
            if (now - lastTimeMillis >= 1000) {
                lastTimeMillis = now;
                System.out.println("Frames: " + frames + " tick: " + tick);
                tick = 0;
                frames = 0;
            }
        }
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void tick() {
        screenRenderer.tick();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screenRenderer.render();

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth(), getHeight());
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        new Game().start();
    }

    public void endGame() {
        running = false;
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}

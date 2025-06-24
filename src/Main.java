import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    JFrame displayZoneFrame;
    RenderEngine renderEngine;
    GameEngine gameEngine;
    PhysicEngine physicEngine;
    StartScreen startScreen;
    GameOverScreen gameOverScreen;
    GameCompleteScreen gameCompleteScreen;

    private Timer renderTimer, gameTimer, physicTimer;
    private long gameStartTime;
    private int currentLevel = 1;
    private boolean gameOverDisplayed = false;
    private static final int MAX_LEVELS = 10; // Total number of levels

    public Main() throws Exception {
        displayZoneFrame = new JFrame("Dungeon_Crawler");
        displayZoneFrame.setSize(800, 600);
        displayZoneFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        startScreen = new StartScreen(displayZoneFrame, this);
        displayZoneFrame.getContentPane().add(startScreen);
        displayZoneFrame.setVisible(true);
    }

    public void loadLevel(String levelFile, int levelNumber) throws Exception {
        gameOverDisplayed = false;
        currentLevel = levelNumber;
        displayZoneFrame.getContentPane().removeAll();

        // Only update start time when starting level 1
        if (levelNumber == 1) {
            gameStartTime = System.currentTimeMillis();
        }

        // hero.
        DynamicSprite hero = new DynamicSprite(100, 350,
                ImageIO.read(new File("./img/heroTileSheetLowRes.png")), 48, 50);

        // Create list of enemies
        ArrayList<DynamicSprite> enemies = new ArrayList<>();

        // Add multiple enemies based on level
        if (levelNumber <= 3) {
            // Early levels: 2-3 enemies
            DynamicSprite enemy1 = new DynamicSprite(500, 440,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy1.setEnemy(true);
            enemies.add(enemy1);

            DynamicSprite enemy2 = new DynamicSprite(260, 90,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy2.setEnemy(true);
            enemies.add(enemy2);

            if (levelNumber == 3) {
                DynamicSprite enemy3 = new DynamicSprite(400, 200,
                        ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
                enemy3.setEnemy(true);
                enemies.add(enemy3);
            }
        } else if (levelNumber == 4) {
            // Mid levels: 3-4 enemies
            DynamicSprite enemy1 = new DynamicSprite(500, 440,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy1.setEnemy(true);
            enemies.add(enemy1);

            DynamicSprite enemy2 = new DynamicSprite(260, 90,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy2.setEnemy(true);
            enemies.add(enemy2);

            DynamicSprite enemy3 = new DynamicSprite(400, 200,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy3.setEnemy(true);
            enemies.add(enemy3);

            DynamicSprite enemy4 = new DynamicSprite(300, 300,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy4.setEnemy(true);
            enemies.add(enemy4);
        } else {
            // Late levels: 5 enemies
            DynamicSprite enemy1 = new DynamicSprite(500, 440,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy1.setEnemy(true);
            enemies.add(enemy1);

            DynamicSprite enemy2 = new DynamicSprite(260, 90,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy2.setEnemy(true);
            enemies.add(enemy2);

            DynamicSprite enemy3 = new DynamicSprite(400, 200,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy3.setEnemy(true);
            enemies.add(enemy3);

            DynamicSprite enemy4 = new DynamicSprite(300, 300,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy4.setEnemy(true);
            enemies.add(enemy4);

            DynamicSprite enemy5 = new DynamicSprite(150, 150,
                    ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
            enemy5.setEnemy(true);
            enemies.add(enemy5);
        }

        renderEngine = new RenderEngine(displayZoneFrame, hero, enemies, gameStartTime, this);
        physicEngine = new PhysicEngine();
        gameEngine = new GameEngine(hero, enemies, this, physicEngine, renderEngine);

        Playground level = new Playground(levelFile);
        gameEngine.setEnvironment(level.getSpriteList());

        renderTimer = new Timer(50, (time) -> renderEngine.update());
        gameTimer = new Timer(50, (time) -> gameEngine.update());
        physicTimer = new Timer(50, (time) -> physicEngine.update());

        renderTimer.start();
        gameTimer.start();
        physicTimer.start();

        displayZoneFrame.getContentPane().add(renderEngine);
        displayZoneFrame.setVisible(true);

        renderEngine.addToRenderList(level.getSpriteList());
        renderEngine.addToRenderList(hero);

        // Add all enemies to render and physics engines
        for (DynamicSprite enemy : enemies) {
            renderEngine.addToRenderList(enemy);
        }

        physicEngine.addToMovingSpriteList(hero);
        physicEngine.setEnvironment(level.getSolidSpriteList());

        displayZoneFrame.addKeyListener(gameEngine);
    }

    public void startGame() throws Exception {
        loadLevel("./data/level1.txt", 1);
    }

    public void loadNextLevel() throws Exception {
        if (renderTimer != null) renderTimer.stop();
        if (gameTimer != null) gameTimer.stop();
        if (physicTimer != null) physicTimer.stop();
        for (KeyListener kl : displayZoneFrame.getKeyListeners()) {
            displayZoneFrame.removeKeyListener(kl);
        }

        int nextLevel = currentLevel + 1;

        // Check if game is complete
        if (nextLevel > MAX_LEVELS) {
            gameComplete();
            return;
        }

        loadLevel("./data/level" + nextLevel + ".txt", nextLevel);
    }

    public void gameOver() {
        if (gameOverDisplayed) return;
        gameOverDisplayed = true;

        if (renderTimer != null) renderTimer.stop();
        if (gameTimer != null) gameTimer.stop();
        if (physicTimer != null) physicTimer.stop();

        for (KeyListener kl : displayZoneFrame.getKeyListeners()) {
            displayZoneFrame.removeKeyListener(kl);
        }

        SwingUtilities.invokeLater(() -> {
            displayZoneFrame.getContentPane().removeAll();
            gameOverScreen = new GameOverScreen(displayZoneFrame, this);
            displayZoneFrame.setContentPane(gameOverScreen);
            displayZoneFrame.revalidate();
            displayZoneFrame.repaint();
        });
    }

    public void gameComplete() {
        if (renderTimer != null) renderTimer.stop();
        if (gameTimer != null) gameTimer.stop();
        if (physicTimer != null) physicTimer.stop();

        for (KeyListener kl : displayZoneFrame.getKeyListeners()) {
            displayZoneFrame.removeKeyListener(kl);
        }

        SwingUtilities.invokeLater(() -> {
            displayZoneFrame.getContentPane().removeAll();
            gameCompleteScreen = new GameCompleteScreen(displayZoneFrame, this);
            displayZoneFrame.setContentPane(gameCompleteScreen);
            displayZoneFrame.revalidate();
            displayZoneFrame.repaint();
        });
    }

    public void restartGame() throws Exception {
        // Stop any running timers
        if (renderTimer != null) renderTimer.stop();
        if (gameTimer != null) gameTimer.stop();
        if (physicTimer != null) physicTimer.stop();

        // Remove key listeners
        for (KeyListener kl : displayZoneFrame.getKeyListeners()) {
            displayZoneFrame.removeKeyListener(kl);
        }

        // Reset game state
        currentLevel = 1;
        gameOverDisplayed = false;

        // Clear the display and start fresh
        displayZoneFrame.getContentPane().removeAll();

        // Recreate and show start screen
        startScreen = new StartScreen(displayZoneFrame, this);
        displayZoneFrame.getContentPane().add(startScreen);
        displayZoneFrame.revalidate();
        displayZoneFrame.repaint();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public long getGameStartTime() {
        return gameStartTime;
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
    }
}

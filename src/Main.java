import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.io.File;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    JFrame displayZoneFrame;
    RenderEngine renderEngine;
    GameEngine gameEngine;
    PhysicEngine physicEngine;
    StartScreen startScreen;
    GameOverScreen gameOverScreen;

    private Timer renderTimer, gameTimer, physicTimer;
    private long gameStartTime;
    private int currentLevel = 1;
    private boolean gameOverDisplayed = false;

    public Main() throws Exception {
        displayZoneFrame = new JFrame("Java Labs");
        displayZoneFrame.setSize(400, 600);
        displayZoneFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        startScreen = new StartScreen(displayZoneFrame, this);
        displayZoneFrame.getContentPane().add(startScreen);
        displayZoneFrame.setVisible(true);
    }

    public void loadLevel(String levelFile, int levelNumber) throws Exception {
        gameOverDisplayed = false;
        currentLevel = levelNumber;
        displayZoneFrame.getContentPane().removeAll();
        gameStartTime = System.currentTimeMillis();

        // hero.
        DynamicSprite hero = new DynamicSprite(100, 350,
                ImageIO.read(new File("./img/heroTileSheetLowRes.png")), 48, 50);

        // enemy.
        DynamicSprite enemy = new DynamicSprite(260, 90,
                ImageIO.read(new File("./img/enemytilesheet.png")), 48, 50);
        enemy.setEnemy(true);

        renderEngine = new RenderEngine(displayZoneFrame, hero, enemy, gameStartTime, this);
        physicEngine = new PhysicEngine();
        gameEngine = new GameEngine(hero, enemy, this, physicEngine, renderEngine);

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
        renderEngine.addToRenderList(enemy);
        physicEngine.addToMovingSpriteList(hero);
        // physicEngine.addToMovingSpriteList(enemy);
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

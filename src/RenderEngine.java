import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class RenderEngine extends JPanel implements Engine {
    private JFrame frame;
    private ArrayList<Displayable> renderList;
    private DynamicSprite hero;
    private ArrayList<DynamicSprite> enemies; // Changed to ArrayList
    private long gameStartTime;
    private Main main;

    private long lastFrameTime = System.currentTimeMillis();
    private double fps = 0;

    // Constructor now accepts a list of enemies
    public RenderEngine(JFrame jFrame, DynamicSprite hero, ArrayList<DynamicSprite> enemies, long gameStartTime, Main main) {
        this.frame = jFrame;
        this.hero = hero;
        this.enemies = enemies != null ? enemies : new ArrayList<>();
        this.gameStartTime = gameStartTime;
        this.main = main;
        renderList = new ArrayList<>();
    }

    public void addToRenderList(Displayable displayable) {
        if (!renderList.contains(displayable)) {
            renderList.add(displayable);
        }
    }

    public void addToRenderList(ArrayList<Displayable> displayables) {
        for (Displayable d : displayables) {
            if (!renderList.contains(d)) {
                renderList.add(d);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Displayable renderObject : renderList) {
            renderObject.draw(g);
        }

        hero.draw(g);

        // Draw all enemies
        for (DynamicSprite enemy : enemies) {
            if (!enemy.isDead()) {
                enemy.draw(g);
            }
        }


        // FPS
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - lastFrameTime;
        if (delta > 0) {
            fps = 1000.0 / delta;
        }
        lastFrameTime = currentTime;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 18));
        g.drawString(String.format("FPS: %.1f", fps), 10, 20);

        // HP bar
        double healthRatio = hero.getCurrentHealth() / hero.getMaxHealth();
        int barWidth = 200, barHeight = 20, x = 10, y = 30;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, barWidth, barHeight);
        g.setColor(Color.RED);
        g.fillRect(x, y, (int)(barWidth * healthRatio), barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, barWidth, barHeight);

        // game duration
        long elapsedSeconds = (currentTime - gameStartTime) / 1000;
        g.drawString("Temps: " + elapsedSeconds + " s", 10, 80);
        g.drawString("Level: " + main.getCurrentLevel(), 10, 100);
    }

    @Override
    public void update() {
        repaint();
    }
}

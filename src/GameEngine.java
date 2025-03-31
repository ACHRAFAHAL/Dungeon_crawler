import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class GameEngine implements KeyListener, Engine {
    private DynamicSprite hero;
    private DynamicSprite enemy;
    private Main main;
    private PhysicEngine physicEngine;
    private RenderEngine renderEngine;
    private ArrayList<Displayable> environment;

    private static final long GAME_DURATION = 60 * 1000;

    // Constructor accepts both hero and enemy.
    public GameEngine(DynamicSprite hero, DynamicSprite enemy, Main main,
                      PhysicEngine physicEngine, RenderEngine renderEngine) {
        this.hero = hero;
        this.enemy = enemy;
        this.main = main;
        this.physicEngine = physicEngine;
        this.renderEngine = renderEngine;
    }

    public void setEnvironment(ArrayList<Displayable> environment) {
        this.environment = environment;
    }

    @Override
    public void update() {
        hero.updateInvincibility();

        if (enemy != null && !enemy.isDead()) {
            enemy.updateFollow(hero);
        }

        // Check collision between enemy and hero
        if (enemy != null && !enemy.isDead()) {
            Rectangle2D heroBox = hero.getHitBox();
            Rectangle2D enemyBox = enemy.getHitBox();
            if (heroBox.intersects(enemyBox)) {
                hero.takeDamage(hero.getMaxHealth());
                SoundManager.playSound("./sound/MarioFall.wav");
                main.gameOver();
                return;
            }
        }

        // collisions with environment
        if (environment != null) {
            for (Displayable d : environment) {
                if (d instanceof SolidSprite) {
                    SolidSprite s = (SolidSprite) d;
                    if (s.isPortal()) {
                        Rectangle2D heroBox = hero.getHitBox();
                        Rectangle2D portalBox = s.getHitBox();
                        if (heroBox.intersects(portalBox)) {
                            System.out.println("Portal collision detected!");
                            try {
                                main.loadNextLevel();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return;
                        }
                    }
                    if (s.isTrap()) {
                        System.out.println("Checking trap at: " + s.getHitBox());
                        Rectangle2D heroBox = hero.getHitBox();
                        Rectangle2D trapBox = s.getHitBox();
                        if (heroBox.intersects(trapBox)) {
                            System.out.println("Trap collision detected!");
                            if (!hero.isInvincible() ) {
                                SoundManager.playSound("./sound/ooF.wav");
                            }
                            hero.takeDamage(hero.getMaxHealth() / 2);
                        }
                    }
                }
            }
        }

        if (hero.isDead()) {
            SoundManager.playSound("./sound/MarioFall.wav");
            main.gameOver();
            return;
        }

        long elapsed = System.currentTimeMillis() - main.getGameStartTime();
        if (elapsed >= GAME_DURATION) {
            main.gameOver();
            return;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                hero.setDirection(Direction.NORTH);
                break;
            case KeyEvent.VK_DOWN:
                hero.setDirection(Direction.SOUTH);
                break;
            case KeyEvent.VK_LEFT:
                hero.setDirection(Direction.WEST);
                break;
            case KeyEvent.VK_RIGHT:
                hero.setDirection(Direction.EAST);
                break;
            case KeyEvent.VK_CONTROL:
                hero.setSpeed(15);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            hero.setSpeed(5);
        }
    }
}

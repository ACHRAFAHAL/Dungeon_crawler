import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DynamicSprite extends SolidSprite {
    private Direction direction = Direction.EAST;
    private double speed = 5;
    private double timeBetweenFrame = 250;
    private boolean isWalking = true;
    private final int spriteSheetNumberOfColumn = 10;

    private double maxHealth = 100;
    private double currentHealth = 100;
    private boolean invincible = false;
    private long invincibleStartTime;
    private boolean enemy = false;


    public DynamicSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height);
    }

    public void setEnemy(boolean enemy) {
        this.enemy = enemy;
    }

    public boolean isEnemy() {
        return enemy;
    }

    public void updateFollow(DynamicSprite hero) {
        if (!enemy) return;
        double dx = hero.x - this.x;
        double dy = hero.y - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double threshold = 150;
        double followSpeed = 5.5;
        if (distance < threshold && distance > 0) {
            dx /= distance;
            dy /= distance;
            this.x += dx * followSpeed;
            this.y += dy * followSpeed;
        }

    }


    public boolean isInvincible() {
        return invincible;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void takeDamage(double damage) {
        if (!invincible) {
            System.out.println("Hero took damage: " + damage);
            currentHealth -= damage;
            if (currentHealth < 0) {
                currentHealth = 0;
            }
            invincible = true;
            invincibleStartTime = System.currentTimeMillis();
        }
    }

    public void updateInvincibility() {
        if (invincible && (System.currentTimeMillis() - invincibleStartTime) >= 2000) {
            invincible = false;
        }
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    private boolean isMovingPossible(ArrayList<Sprite> environment) {
        Rectangle2D.Double moved = new Rectangle2D.Double();
        switch (direction) {
            case EAST:
                moved.setRect(getHitBox().getX() + speed, getHitBox().getY(),
                        getHitBox().getWidth(), getHitBox().getHeight());
                break;
            case WEST:
                moved.setRect(getHitBox().getX() - speed, getHitBox().getY(),
                        getHitBox().getWidth(), getHitBox().getHeight());
                break;
            case NORTH:
                moved.setRect(getHitBox().getX(), getHitBox().getY() - speed,
                        getHitBox().getWidth(), getHitBox().getHeight());
                break;
            case SOUTH:
                moved.setRect(getHitBox().getX(), getHitBox().getY() + speed,
                        getHitBox().getWidth(), getHitBox().getHeight());
                break;
        }

        for (Sprite s : environment) {
            if ((s instanceof SolidSprite) && (s != this)) {
                SolidSprite ss = (SolidSprite) s;
                if (ss.isTrap() || ss.isPortal()) {     // walk on portals and traps is possible.
                    continue;
                }
                if (ss.intersect(moved)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private void move() {
        switch (direction) {
            case NORTH -> this.y -= speed;
            case SOUTH -> this.y += speed;
            case EAST -> this.x += speed;
            case WEST -> this.x -= speed;
        }
    }

    public void moveIfPossible(ArrayList<Sprite> environment) {
        if (isMovingPossible(environment)) {
            move();
        }
    }

    @Override
    public void draw(Graphics g) {
        int index = (int) (System.currentTimeMillis() / timeBetweenFrame % spriteSheetNumberOfColumn);
        g.drawImage(image, (int) x, (int) y, (int) (x + width), (int) (y + height),
                (int) (index * width), (int) (direction.getFrameLineNumber() * height),
                (int) ((index + 1) * width), (int) ((direction.getFrameLineNumber() + 1) * height), null);
    }
}

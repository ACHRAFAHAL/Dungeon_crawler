import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SolidSprite extends Sprite {
    private boolean trap = false;
    private boolean portal = false;

    public SolidSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height);
    }

    public SolidSprite(double x, double y, Image image, double width, double height, boolean trap, boolean portal) {
        super(x, y, image, width, height);
        this.trap = trap;
        this.portal = portal;
    }

    public Rectangle2D getHitBox() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public boolean intersect(Rectangle2D.Double hitBox) {
        return this.getHitBox().intersects(hitBox);
    }

    public boolean isTrap() {
        return trap;
    }

    public boolean isPortal() {
        return portal;
    }
}

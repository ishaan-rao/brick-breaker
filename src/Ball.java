import java.awt.*;

/**
 * A basic game object starting in the center of the game court. It is displayed as a
 * circle of a specified color.
 */
public abstract class Ball extends GameObj {
    private Color color;

    public Ball(int initVelX, int initVelY, int posX, int posY, int width, int height, int courtWidth, int courtHeight, Color color) {
        super(initVelX, initVelY, posX, posY, width, height, courtWidth, courtHeight);
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
    
    public abstract void rebound();
}
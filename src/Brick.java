import java.awt.*;

/**
 * A basic game object. It is displayed as a rectangle of a specified color, which does not move.
 */
public abstract class Brick extends GameObj {
    public Color color; //color of brick
    
    private int hits; //number of hits needed to destroy brick
    
    /**
    * Note that, because we don't need to do anything special when constructing a Brick, we simply
    * use the superclass constructor called with the correct parameters.
    */
    public Brick(int initVelX, int initVelY, int posX, int posY, int width, int height, int courtWidth, int courtHeight, Color color) {
        super(initVelX, initVelY, posX, posY, width, height, courtWidth, courtHeight);
        this.color = color;
    }

    /* GETTERS */
    public Color getColor() {
        return this.color;
    }
        
    public abstract int getHits();
    
    /* SETTERS */
    
    public abstract void setHits(int h);
    
    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    } 
}
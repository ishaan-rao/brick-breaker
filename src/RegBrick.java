import java.awt.*;

/**
 * A basic game object. It is displayed as a rectangle of a specified color, which does not move.
 */
public class RegBrick extends Brick {
    //CONSTANTS
    public static final int WIDTH = 40;
    public static final int HEIGHT = 20;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    
    private int hits = 1; //number of hits needed to destroy brick
    
    public RegBrick(int courtWidth, int courtHeight, Color color, int posX, int posY) {
        super(INIT_VEL_X, INIT_VEL_Y, posX, posY, WIDTH, HEIGHT, courtWidth, courtHeight, color);
    }   
    
    /* GETTERS */
    
    @Override
    public int getHits() {
        return this.hits;
    }
    
    /* SETTERS */
    
    @Override
    public void setHits(int h) {
        this.hits = h;
    }
}
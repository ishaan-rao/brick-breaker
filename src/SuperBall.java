import java.awt.*;

/**
 * A basic game object starting in the center of the game court. It is displayed as a
 * circle of a specified color.
 */

public class SuperBall extends Ball {
    public static final int SIZE = 10;

    public SuperBall(int posX, int posY, int velX, int velY, int courtWidth, int courtHeight, Color color) {
        super(velX, velY, posX, posY, SIZE, SIZE, courtWidth, courtHeight, color);
    }

    //determines the behavior when the ball hits a brick 
    
    @Override
    public void rebound() {
        for (int i = 0; i < GameCourt.bricks.length; i++) {
            for (int j = 0; j < GameCourt.bricks[i].length; j++) {
                if (GameCourt.ball.willIntersect(GameCourt.bricks[i][j])) {
                    if (GameCourt.bricks[i][j].getHits() > 1) {
                            GameCourt.ball.bounce(GameCourt.ball.hitObj(GameCourt.bricks[i][j]));
                    }
                    GameCourt.bricks[i][j].setHits(GameCourt.bricks[i][j].getHits() - 1);
                    if (GameCourt.bricks[i][j].getHits() == 0) {
                              GameCourt.bricks[i][j] = new RegBrick(GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT, Color.BLACK, 
                                   -40, -20);
                     }
                    GameCourt.score++;
                    GameCourt.status.setText("Lives: " + GameCourt.lives + " | Score: " + GameCourt.score + " | Name : " + GameCourt.name);
                }
            }
        }
    }
}
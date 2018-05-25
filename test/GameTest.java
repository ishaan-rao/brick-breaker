import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.awt.*;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Collections;
import java.util.ArrayList;

public class GameTest {
    
    /* GameObj Tests */
       
    Brick b = new RegBrick(300, 300, Color.YELLOW, 0, 0); 
    Brick c = new SuperBrick(300, 300, Color.YELLOW, 0, 0);
    Ball ball = new RegBall(300, 300, Color.BLUE);
    Ball superb = new SuperBall(0, 0, 5, 5, 300, 300, Color.BLUE);

    @Test
    public void widthTest() {
      assertEquals(b.getWidth(), 40);
    }

    @Test
    public void heightTest() {
      assertEquals(b.getHeight(), 20);
    }

    @Test
    public void colorTest() {
      assertEquals(b.getColor(), Color.YELLOW);
    }

    @Test
    public void pxTest() {
      assertEquals(b.getPx(), 0);
    }

    @Test
    public void pyTest() {
      assertEquals(b.getPy(), 0);
    }

    @Test
    public void vxTest() {
      assertEquals(b.getVx(), 0);
    }

    @Test
    public void vyTest() {
      assertEquals(b.getVy(), 0);
    }
    
    @Test
    public void moveTest() {
        ball.move();
        ball.move();
        assertEquals(b.getPx(), 0);
        assertEquals(b.getPy(), 0);
    }
    
    @Test
    public void clipTest() {
        ball.setPx(259);
        ball.setPy(279);
        ball.move();
        ball.move();
        ball.move();
        assertEquals(ball.getPx(), 253);
        assertEquals(ball.getPy(), 288);
    }
    
    @Test
    public void intersectTest() {
        b.setPx(50);
        b.setPy(50);
        ball.setPx(45);
        ball.setPy(45);
        assertTrue(ball.willIntersect(b));
    }
    
    @Test
    public void bounceTest() {
        b.setPx(45);
        b.setPy(50);
        ball.setPx(45);
        ball.setPy(45);
        ball.setVx(5);
        ball.setVy(5);
        ball.bounce(ball.hitObj(b));
        ball.move();
        assertEquals(ball.getPx(), 40);
        assertEquals(ball.getPy(), 50);
    }
    
    @Test
    public void superBrickTest() {
        int originalHits = c.getHits();
        c.setPx(50);
        c.setPy(50);
        ball.setPx(45);
        ball.setPy(45);
        if (ball.willIntersect(c)) {
            c.setHits(c.getHits() - 1);
        }
        int endHits = c.getHits();
        assertEquals(originalHits, 3);
        assertEquals(endHits, 2);
    }
    
    @Test
    public void superBallBounceTest() {
        c.setPx(45);
        c.setPy(50);
        c.setHits(1);
        superb.setPx(45);
        superb.setPy(45);
        superb.setVx(5);
        superb.setVy(5);
        if (ball.willIntersect(c)) {
            c.setHits(c.getHits() - 1);
        }
        if (c.getHits() > 1) {
            superb.bounce(superb.hitObj(b));
        }
        superb.move();
        assertEquals(superb.getPx(), 50);
        assertEquals(superb.getPy(), 50);
    }
    
    
    /* Player Tests */
    Player testPlayer = new Player("test", 5);
    
    @Test
    public void playerName() {
        assertEquals(testPlayer.getName(), "test");
    }
    
    @Test
    public void playerScore() {
        assertEquals(testPlayer.getScore(), 5);
    }   
}
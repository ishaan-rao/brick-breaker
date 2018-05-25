import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;
import java.util.Map;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    private Paddle paddle; // the paddle, keyboard control
    public static Ball ball; // the ball, bounces off walls and bricks
    public static Brick[][] bricks; //2-D array of bricks
    
    public static int score = 0; //game score
    public static String name; //player name
    public static int lives = 3; //player lives
    
    public boolean playing = false; // whether the game is running 
    public static JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int COURT_WIDTH = 500;
    public static final int COURT_HEIGHT = 300;
    public static final int PADDLE_VELOCITY = 4;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    
    //Readers and Writers for File I/O
    public FileWriter fw = null;
    public BufferedWriter bw = null;
    public FileReader fr = null;
    public BufferedReader br = null;

    //number of super bricks
    public int numSuperBricks = 0;
    
    //List to track player scores
    public ArrayList<Player> highscores = new ArrayList<Player>(); 
        
    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the paddle to move as long as an arrow key is pressed, by
        // changing the paddle's velocity accordingly. (The tick method below actually moves the
        // paddle.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    paddle.setVx(-PADDLE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    paddle.setVx(PADDLE_VELOCITY);
                }
            }

            public void keyReleased(KeyEvent e) {
                paddle.setVx(0);
                paddle.setVy(0);
            }
        });

        this.status = status;
    }
    
    //Makes the game start
    public void play() {
        playing = true;
        requestFocusInWindow();
    }

    //Displays the How to Play menu
    public void instructions() {
        JFrame instructions = new JFrame("Instructions");
        instructions.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel header = new JLabel("Welcome to Brick Breaker!");
        JLabel htp = new JLabel("How to Play: ");
        JLabel contents = new JLabel("<html>This is a rendition of the classic arcade game.<br>" + 
                                     "Use the left and right arrow keys to move the paddle to hit the ball.<br>" + 
                                     "If the ball hits the bottom wall, you lose a life. You<br>" +
                                     "have three lives. If the ball hits a brick, the brick loses a life.<br>" + 
                                     "Black bricks have 1 life, green bricks have 3 lives. <br>" +
                                     "If your score is greater than 25, you get a super ball, which " +
                                     "will not bounce off bricks and instead break through them." +
                                     "To win the game you must eliminate all the bricks.</html>"
                                    );
        panel.add(header);
        panel.add(htp);
        panel.add(contents);
        
        instructions.setPreferredSize(new Dimension(500, 200));
        instructions.getContentPane().add(panel, BorderLayout.CENTER);
        instructions.setLocation(100, 100);
        instructions.pack();
        instructions.setVisible(true);
        requestFocusInWindow();
    }
    
    //Displays the leaderboard (top 3)
    public void leaderboard() {
        highscores = new ArrayList<Player>();
        JFrame lb = new JFrame("Leaderboard");
        lb.getContentPane().removeAll();
        lb.getContentPane().repaint();
        lb.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel board = new JPanel();
        board.removeAll();
        board.repaint();
        board.setLayout(new BoxLayout(board, BoxLayout.Y_AXIS));
        JLabel leader = new JLabel("<html><body>High Scores</body></html>");
        lb.setPreferredSize(new Dimension(200, 100));
        board.add(leader);
        
        try {
            fr = new FileReader("files/scores.txt");
            br = new BufferedReader(fr);
            
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(": ");
                String name = parts[0];
                Integer score = Integer.parseInt(parts[1]); 
                highscores.add(new Player(name, score));                           
            } 
            
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }    
        
        Collections.sort(highscores);
        
        int count = 0;
        for(Player p : highscores){
            if (count < 3) {
                JLabel s = new JLabel((count + 1) + ". " + p.getName() + ": " + p.getScore());
                board.add(s);
            }
            count++;
		}
        
            
        lb.getContentPane().add(board, BorderLayout.CENTER);
        lb.setLocation(100, 100);
        lb.pack();
        lb.setVisible(true);
        requestFocusInWindow();
    }
    
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        paddle = new Paddle(COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
        ball = new RegBall(COURT_WIDTH, COURT_HEIGHT, Color.RED);
        bricks = new Brick[11][3];
        
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                Random rand = new Random();
                int x = rand.nextInt(100) + 1;
                if (x < 10) {
                        bricks[i][j] = new SuperBrick(COURT_WIDTH, COURT_HEIGHT, Color.GREEN, 
                                      -20 + 40 * (i + 1) + 2 * i, 20 * (j + 1) + 2 * j);
                        numSuperBricks++;
                }
                else {
                        bricks[i][j] = new RegBrick(COURT_WIDTH, COURT_HEIGHT, Color.BLACK, 
                                      -20 + 40 * (i + 1) + 2 * i, 20 * (j + 1) + 2 * j);
                }
            }
        }
        
        playing = false;
        lives = 3;
        score = 0;
        
        name = JOptionPane.showInputDialog("Name: ");
        
        status.setText("Lives: " + lives + " | Score: " + score + " | Name : " + name);
        
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
            // advance the paddle and ball in their current direction.
            paddle.move();
            ball.move();
            
            if (score > 25) {
                int posX = ball.getPx();
                int posY = ball.getPy();
                int velX = ball.getVx();
                int velY = ball.getVy();
                
                ball = new SuperBall(posX, posY, velX, velY, COURT_WIDTH, COURT_HEIGHT, Color.BLUE);
            }
            
            if (score == ((bricks.length * bricks[0].length) + 2 * numSuperBricks)) {
                playing = false;
                status.setText("You Win! Score: " + score);
            }
            
            if (ball.hitWall() == Direction.DOWN) {
                lives--;
                status.setText("Lives: " + lives + " | Score: " + score + " | Name : " + name);
                if (lives == 0) {
                    playing = false;
                    status.setText("You lose! Score: " + score);
                    
                    
                    try {
                        fw = new FileWriter("files/scores.txt", true);
                        bw = new BufferedWriter(fw);
                        bw.write(name + ": " + score);
                        bw.newLine();
                        bw.flush();
                        bw.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }                   
                }
                else {
                    ball.setPx(200);
                    ball.setPy(200);
                    ball.setVx(-2);
                    ball.setVy(3);
                    paddle.setPx(120);
                }
            }
            
            // make the ball bounce off walls
            ball.bounce(ball.hitWall());
            
            // make the ball bounce off the paddle
            ball.bounce(ball.hitObj(paddle));
            
            //determines the interaction between brick and ball
            ball.rebound();

            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paddle.draw(g);
        ball.draw(g);
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                bricks[i][j].draw(g);
            }
        }
        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
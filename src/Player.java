import java.awt.*;

/**
 * A class that stores the name and score of each player that plays the game.
 */
public class Player implements Comparable<Player>{
    private String name;
    private Integer score;

    public Player(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    /* GETTERS */

    public int getScore() {
        return ((int) this.score);
    }

    /* SETTERS */

    public String getName() {
        return this.name;
    }

    /* Orders players in descending order by score */
    @Override
    public int compareTo(Player other) {
		int comp = ((Player) other).getScore();
		return comp - ((int) this.score);
	}
}

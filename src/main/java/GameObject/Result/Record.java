package GameObject.Result;

import GameObject.Player.PlayerEnum.Color;

/**
 * Created by Arnob on 10/10/2014.
 * This class defines a structure for all players' record.
 */
public class Record {
    private Color color;
    private int score = 0;

    public Record(Color color) {
        this.color = color;
    }

    public int increaseScore() {
        return ++score;
    }

    public int getScore() {
        return score;
    }

    public Color getColor() {
        return color;
    }

}

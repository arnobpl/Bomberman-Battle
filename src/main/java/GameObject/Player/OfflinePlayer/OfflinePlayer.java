package GameObject.Player.OfflinePlayer;

import GameObject.Player.OnilnePlayer.Player;
import GameObject.Player.PlayerEnum.Color;

/**
 * Created by Arnob on 17/02/2015.
 * This class is the specialized version of <code>Player</code> class to be used for the single human player in offline game.
 * The key combination is same as OnlinePlayer. This is intended when only single human player plays the game.
 */
public class OfflinePlayer extends Player {
    public OfflinePlayer(Color color) {
        super(color);
    }

    public OfflinePlayer() {
        super(Color.White);
    }

    protected byte getInputData() {
        return inputData;
    }
}

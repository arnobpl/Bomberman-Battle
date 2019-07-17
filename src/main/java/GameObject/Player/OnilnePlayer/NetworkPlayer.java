package GameObject.Player.OnilnePlayer;

import GameObject.Player.PlayerEnum.Color;
import Network.BaseNetwork.BaseNetwork;

/**
 * Created by Arnob on 06/10/2014.
 * This class, based on <code>Player</code>, has an additional feature of receiving data over network.
 */
public class NetworkPlayer extends Player {
    public NetworkPlayer(Color color) {
        super(color);
    }

    protected byte getInputData() {
        return BaseNetwork.networkConnection.receiveFromPlayer(getID());
    }

    protected void vibrateWhenKilled() {   // this function only does vibration for this PC-controlled single human player
    }

    protected void resetVibrationAfterDeath() {    // this function only does vibration for this PC-controlled single human player
    }

    protected void playDyingSoundWithReverberation() { // this is the special sound effect which only works for this PC-controlled player in online games
    }
}

package GameObject.Player.OnilnePlayer;

import GameObject.Player.OfflinePlayer.AI_Player.AI_Player;
import GameObject.Player.PlayerEnum.Color;
import Network.BaseNetwork.BaseNetwork;

/**
 * Created by Arnob on 27/06/2015.
 * This class is similar to <code>AI_Player</code> but has an additional feature of sending data over network.
 */
public class AI_NetworkPlayer extends AI_Player {
    public AI_NetworkPlayer(Color color) {
        super(color);
    }

    protected byte getInputData() {
        byte inputDataBuffer = super.getInputData();
        BaseNetwork.networkConnection.sendToOthers(inputDataBuffer);

        return inputDataBuffer;
    }
}

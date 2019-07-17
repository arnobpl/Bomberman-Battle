package Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.Component;

import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.CreateGame;

/**
 * Created by Arnob on 30/01/2015.
 * User input to get the type of connections: Peer-to-Peer (P2P) or Client/Server (CS)
 */
public class Option_ConnectionType extends OptionChooser {
    public Option_ConnectionType(int positionIndex) {
        super(Strings.CreateGame.ConnectionType, positionIndex, 2, 0);

        addOption(Strings.CreateGame.P2P);  // 0
        addOption(Strings.CreateGame.CS);   // 1
    }

    public void performAction(int selectedIndex) {
        CreateGame.networkConnectionType = selectedIndex;
    }
}

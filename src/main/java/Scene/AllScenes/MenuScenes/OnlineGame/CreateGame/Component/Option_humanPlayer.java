package Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;
import Network.BaseNetwork.BaseNetwork;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.CreateGame;

/**
 * Created by Arnob on 27/06/2015.
 * User input to get how many human players to play
 */
public class Option_humanPlayer extends OptionChooser {
    private static final int selectedIndexOffsetWithOption = 2;

    public Option_humanPlayer(int positionIndex) {
        super(Strings.CreateGame.TotalHumanPlayers, positionIndex, 3, 2);

        int totalOptionsPlusOffset = getTotalOptions() + selectedIndexOffsetWithOption;
        for (int i = totalOptionsPlusOffset - getTotalOptions(); i < totalOptionsPlusOffset; i++)
            addOption(Integer.toString(i));

        // Since same 'totalHumanPlayers' variable is used for both online and offline game but it has different valid bounds,
        // bound checking is performed here for online mode.
        MainGame.totalHumanPlayers = AllSettings.userSettings.totalHumanPlayers;
        if (MainGame.totalHumanPlayers >= 2) {
            setSelectedIndex(MainGame.totalHumanPlayers - selectedIndexOffsetWithOption);
        } else {
            MainGame.totalHumanPlayers = BaseNetwork.maxPlayers;
        }
    }

    public void valueChanged(int selectedIndex) {
        int totalHumanPlayers = selectedIndex + selectedIndexOffsetWithOption;
        int totalPlayers = CreateGame.option_totalPlayer.getSelectedIndex() + selectedIndexOffsetWithOption;
        if (totalPlayers < totalHumanPlayers) {
            CreateGame.option_totalPlayer.setSelectedIndex(selectedIndex);
        }
    }

    public void performAction(int selectedIndex) {
        MainGame.totalHumanPlayers = selectedIndex + selectedIndexOffsetWithOption;
    }
}

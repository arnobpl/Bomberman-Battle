package Scene.AllScenes.MenuScenes.OfflineGame.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.GameScenes.MainGame.MainGame;

/**
 * Created by Arnob on 17/04/2014.
 * User input to get how many players to play
 */
public class Option_totalPlayer extends OptionChooser {
    private static final int selectedIndexOffsetWithOption = 2;

    public Option_totalPlayer(int positionIndex) {
        super(Strings.CreateGame.TotalPlayers, positionIndex, 3, AllSettings.userSettings.totalPlayers - selectedIndexOffsetWithOption);

        int totalOptionsPlusOffset = getTotalOptions() + selectedIndexOffsetWithOption;
        for (int i = totalOptionsPlusOffset - getTotalOptions(); i < totalOptionsPlusOffset; i++)
            addOption(Integer.toString(i));

        performAction(getSelectedIndex());
    }

    public void performAction(int selectedIndex) {
        MainGame.totalPlayers = selectedIndex + selectedIndexOffsetWithOption;
    }
}

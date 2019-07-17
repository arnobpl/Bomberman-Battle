package Scene.AllScenes.MenuScenes.OfflineGame.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.GameScenes.MainGame.MainGame;

/**
 * Created by Arnob on 17/04/2014.
 * User input to get how many human players to play
 */
public class Option_humanPlayer extends OptionChooser {
    private static final int selectedIndexOffsetWithOption = 0;

    public Option_humanPlayer(int positionIndex) {
        super(Strings.CreateGame.TotalHumanPlayers, positionIndex, 3, 2);

        int totalOptionsPlusOffset = getTotalOptions() + selectedIndexOffsetWithOption;
        for (int i = totalOptionsPlusOffset - getTotalOptions(); i < totalOptionsPlusOffset; i++)
            addOption(Integer.toString(i));

        // Since same 'totalHumanPlayers' variable is used for both online and offline game but it has different valid bounds,
        // bound checking is performed here for offline mode.
        MainGame.totalHumanPlayers = AllSettings.userSettings.totalHumanPlayers;
        if (MainGame.totalHumanPlayers <= 2) {
            setSelectedIndex(MainGame.totalHumanPlayers - selectedIndexOffsetWithOption);
        } else {
            MainGame.totalHumanPlayers = 2;
        }
    }

    public void performAction(int selectedIndex) {
        MainGame.totalHumanPlayers = selectedIndex + selectedIndexOffsetWithOption;
    }
}

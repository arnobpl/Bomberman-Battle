package Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.CreateGame;

/**
 * Created by Arnob on 07/11/2014.
 * User input to get how many players to play
 */
public class Option_totalPlayer extends OptionChooser {
    private static final int selectedIndexOffsetWithOption = 2;

    public Option_totalPlayer(int positionIndex) {
        super(Strings.CreateGame.TotalPlayers, positionIndex, 3, AllSettings.userSettings.totalPlayers - selectedIndexOffsetWithOption);

        int totalOptionsPlusOffset = getTotalOptions() + selectedIndexOffsetWithOption;
        for (int i = totalOptionsPlusOffset - getTotalOptions(); i < totalOptionsPlusOffset; i++)
            addOption(Integer.toString(i));

        valueChanged(getSelectedIndex());
        performAction(getSelectedIndex());
    }

    public void valueChanged(int selectedIndex) {
        int option_humanPlayer_selectedIndex = CreateGame.option_humanPlayer.getSelectedIndex();
        int totalHumanPlayers = option_humanPlayer_selectedIndex + selectedIndexOffsetWithOption;
        int totalPlayers = selectedIndex + selectedIndexOffsetWithOption;
        if (totalPlayers < totalHumanPlayers) {
            setSelectedIndex(option_humanPlayer_selectedIndex);
        }
    }

    public void performAction(int selectedIndex) {
        MainGame.totalPlayers = selectedIndex + selectedIndexOffsetWithOption;
    }
}

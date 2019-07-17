package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GameplayScene.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;

/**
 * Created by Arnob on 06/02/2015.
 * User input to get the time of match to win
 */
public class Option_MatchToWin extends OptionChooser {
    private static final int selectedIndexOffsetWithOption = 1;

    public Option_MatchToWin(int positionIndex) {
        super(Strings.GameplaySettings.MatchToWin, positionIndex, 5, AllSettings.userSettings.matchToWin - selectedIndexOffsetWithOption);

        int totalOptionsPlusOffset = getTotalOptions() + selectedIndexOffsetWithOption;
        for (int i = totalOptionsPlusOffset - getTotalOptions(); i < totalOptionsPlusOffset; i++)
            addOption(Integer.toString(i));
    }

    public void performAction(int selectedIndex) {
        AllSettings.userSettings.matchToWin = selectedIndex + selectedIndexOffsetWithOption;
    }
}

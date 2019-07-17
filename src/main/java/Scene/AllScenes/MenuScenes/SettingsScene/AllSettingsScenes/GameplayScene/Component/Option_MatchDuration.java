package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GameplayScene.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;

/**
 * Created by Arnob on 06/02/2015.
 * User input to get match duration
 */
public class Option_MatchDuration extends OptionChooser {
    private static final int selectedIndexOffsetWithOption = 1;

    public Option_MatchDuration(int positionIndex) {
        super(Strings.GameplaySettings.MatchDuration, positionIndex, 7, AllSettings.userSettings.matchDurationMinute - selectedIndexOffsetWithOption);

        int totalOptionsPlusOffset = getTotalOptions() + selectedIndexOffsetWithOption;
        for (int i = totalOptionsPlusOffset - getTotalOptions(); i < totalOptionsPlusOffset; i++)
            addOption(Integer.toString(i));
    }

    public void performAction(int selectedIndex) {
        AllSettings.userSettings.matchDurationMinute = selectedIndex + selectedIndexOffsetWithOption;
    }
}

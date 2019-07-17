package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.GraphicsScene;

/**
 * Created by Arnob on 27/05/2015.
 * User input to get desired FPS.
 * This class is not currently used since it does not work in network multiplayer mode for different FPS.
 */
public class Option_DesiredFPS extends OptionChooser {
    private static final int selectedIndexOffsetWithOption = 15;

    public Option_DesiredFPS(int positionIndex) {
        super(Strings.GraphicsSettings.DesiredFPS, positionIndex, 106, AllSettings.userSettings.desiredFPS - selectedIndexOffsetWithOption);

        int totalOptionsPlusOffset = getTotalOptions() + selectedIndexOffsetWithOption;
        for (int i = totalOptionsPlusOffset - getTotalOptions(); i < totalOptionsPlusOffset; i++)
            addOption(Integer.toString(i));
    }

    public void valueChanged(int selectedIndex) {
        GraphicsScene.restartNeeded = true;
    }

    public void performAction(int selectedIndex) {
        AllSettings.userSettings.desiredFPS = selectedIndex + selectedIndexOffsetWithOption;
    }
}

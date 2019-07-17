package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.Component;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.GraphicsScene;

/**
 * Created by Arnob on 05/02/2015.
 * User input to get display scale
 */
public class Option_DisplayScale extends OptionChooser {
    public Option_DisplayScale(int positionIndex) {
        super(Strings.GraphicsSettings.DisplayScale, positionIndex, 5, (int) ((AllSettings.userSettings.displayScale - 2.0) * 2.0));

        int totalOptionsPlusOffset = getTotalOptions() + 1;
        for (int i = totalOptionsPlusOffset - getTotalOptions(); i < totalOptionsPlusOffset; i++)
            addOption(Integer.toString(i));
    }

    public void valueChanged(int selectedIndex) {
        BaseWindow.setDisplayScale(selectedIndex / 2.0 + 2.0);   // lowest should be 2.0, highest should be 4.0
        GraphicsScene.sceneForm.resetMouseAlignment();
    }
}

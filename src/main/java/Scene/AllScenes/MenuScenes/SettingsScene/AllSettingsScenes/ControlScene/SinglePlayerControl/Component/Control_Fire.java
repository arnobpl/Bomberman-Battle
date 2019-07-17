package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.SinglePlayerControl.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.ControlChooser;

/**
 * Created by Arnob on 07/02/2015.
 * User input to get Fire key
 */
public class Control_Fire extends ControlChooser {
    public Control_Fire(int positionIndex) {
        super(Strings.ControlSettings.Fire, positionIndex, AllSettings.userSettings.Key_Fire);
    }

    public void performAction(Integer keyCode) {
        AllSettings.userSettings.Key_Fire = keyCode;
    }
}

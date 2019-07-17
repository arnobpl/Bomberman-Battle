package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Player1_Control.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.ControlChooser;

/**
 * Created by Arnob on 07/02/2015.
 * User input to get Fire key
 */
public class Control_Fire extends ControlChooser {
    public Control_Fire(int positionIndex) {
        super(Strings.ControlSettings.Fire, positionIndex, AllSettings.userSettings.Player1_Key_Fire);
    }

    public void performAction(Integer keyCode) {
        AllSettings.userSettings.Player1_Key_Fire = keyCode;
    }
}

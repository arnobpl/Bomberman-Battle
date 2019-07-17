package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Player0_Control.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.ControlChooser;

/**
 * Created by Arnob on 07/02/2015.
 * User input to get Up key
 */
public class Control_Up extends ControlChooser {
    public Control_Up(int positionIndex) {
        super(Strings.ControlSettings.Up, positionIndex, AllSettings.userSettings.Player0_Key_Up);
    }

    public void performAction(Integer keyCode) {
        AllSettings.userSettings.Player0_Key_Up = keyCode;
    }

}

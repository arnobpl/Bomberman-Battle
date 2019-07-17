package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Player0_Control.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.ControlChooser;

/**
 * Created by Arnob on 07/02/2015.
 * User input to get Right key
 */
public class Control_Right extends ControlChooser {
    public Control_Right(int positionIndex) {
        super(Strings.ControlSettings.Right, positionIndex, AllSettings.userSettings.Player0_Key_Right);
    }

    public void performAction(Integer keyCode) {
        AllSettings.userSettings.Player0_Key_Right = keyCode;
    }
}

package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Player1_Control.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.ControlChooser;

/**
 * Created by Arnob on 07/02/2015.
 * User input to get Left key
 */
public class Control_Left extends ControlChooser {
    public Control_Left(int positionIndex) {
        super(Strings.ControlSettings.Left, positionIndex, AllSettings.userSettings.Player1_Key_Left);
    }

    public void performAction(Integer keyCode) {
        AllSettings.userSettings.Player1_Key_Left = keyCode;
    }
}

package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.SinglePlayerControl.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.ControlChooser;

/**
 * Created by Arnob on 07/02/2015.
 * User input to get Right key
 */
public class Control_Right extends ControlChooser {
    public Control_Right(int positionIndex) {
        super(Strings.ControlSettings.Right, positionIndex, AllSettings.userSettings.Key_Right);
    }

    public void performAction(Integer keyCode) {
        AllSettings.userSettings.Key_Right = keyCode;
    }
}

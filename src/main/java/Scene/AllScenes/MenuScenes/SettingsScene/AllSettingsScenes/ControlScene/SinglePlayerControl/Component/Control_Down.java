package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.SinglePlayerControl.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.ControlChooser;

/**
 * Created by Arnob on 07/02/2015.
 * User input to get Down key
 */
public class Control_Down extends ControlChooser {
    public Control_Down(int positionIndex) {
        super(Strings.ControlSettings.Down, positionIndex, AllSettings.userSettings.Key_Down);
    }

    public void performAction(Integer keyCode) {
        AllSettings.userSettings.Key_Down = keyCode;
    }
}

package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Player1_Control.Player1_Control;

/**
 * Created by Arnob on 17/02/2015.
 * Button for 2nd player control
 */
public class Button_Player1_Control extends Button {
    public Button_Player1_Control(int positionIndex) {
        super(Strings.ControlSettings.Player1, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new Player1_Control();
    }
}

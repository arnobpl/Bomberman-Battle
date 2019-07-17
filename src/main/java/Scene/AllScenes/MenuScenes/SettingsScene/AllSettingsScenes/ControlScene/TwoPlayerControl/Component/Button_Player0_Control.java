package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Player0_Control.Player0_Control;

/**
 * Created by Arnob on 17/02/2015.
 * Button for 1st player control
 */
public class Button_Player0_Control extends Button {
    public Button_Player0_Control(int positionIndex) {
        super(Strings.ControlSettings.Player0, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new Player0_Control();
    }
}

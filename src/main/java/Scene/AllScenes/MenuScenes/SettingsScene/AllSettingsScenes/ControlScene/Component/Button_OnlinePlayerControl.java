package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.SinglePlayerControl.SinglePlayerControl;

/**
 * Created by Arnob on 16/02/2015.
 * Button for online player control
 */
public class Button_OnlinePlayerControl extends Button {
    public Button_OnlinePlayerControl(int positionIndex) {
        super(Strings.ControlSettings.OneHumanPlayer, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new SinglePlayerControl();
    }
}

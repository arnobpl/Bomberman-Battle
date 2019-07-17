package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.TwoPlayerControl;

/**
 * Created by Arnob on 16/02/2015.
 * Button for offline player control
 */
public class Button_OfflinePlayerControl extends Button {
    public Button_OfflinePlayerControl(int positionIndex) {
        super(Strings.ControlSettings.TwoHumanPlayer, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new TwoPlayerControl();
    }
}

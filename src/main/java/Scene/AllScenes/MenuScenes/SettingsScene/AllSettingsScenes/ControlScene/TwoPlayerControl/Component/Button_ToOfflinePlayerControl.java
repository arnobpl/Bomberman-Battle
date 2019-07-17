package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.TwoPlayerControl;

/**
 * Created by Arnob on 17/02/2015.
 * Back button to Offline Player Control AllSettings
 */
public class Button_ToOfflinePlayerControl extends Button {
    public Button_ToOfflinePlayerControl(int positionIndex) {
        super(Strings.MainMenu.Back, positionIndex, true);
    }

    public void performAction() {
        BaseWindow.scene = new TwoPlayerControl();
    }
}

package Scene.AllScenes.MenuScenes.SettingsScene.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.ControlScene;

/**
 * Created by Arnob on 05/02/2015.
 * Button for "Control"
 */
public class Button_Control extends Button {
    public Button_Control(int positionIndex) {
        super(Strings.Settings.Control, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new ControlScene();
    }
}

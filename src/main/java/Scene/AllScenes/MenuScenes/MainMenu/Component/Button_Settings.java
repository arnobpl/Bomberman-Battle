package Scene.AllScenes.MenuScenes.MainMenu.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.SettingsScene;

/**
 * Created by Arnob on 03/11/2014.
 * Button for "Settings"
 */
public class Button_Settings extends Button {
    public Button_Settings(int positionIndex) {
        super(Strings.MainMenu.Settings, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new SettingsScene();
    }
}

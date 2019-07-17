package Scene.AllScenes.MenuScenes.SettingsScene.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.SettingsScene;

/**
 * Created by Arnob on 05/02/2015.
 * Back button to AllSettings
 */
public class Button_ToSettingsScene extends Button {
    public Button_ToSettingsScene(int positionIndex) {
        super(Strings.MainMenu.Back, positionIndex, true);
    }

    public void performAction() {
        BaseWindow.scene = new SettingsScene();
    }
}

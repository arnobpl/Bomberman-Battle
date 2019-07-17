package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.ControlScene;

/**
 * Created by Arnob on 16/02/2015.
 * Back button to Control settings
 */
public class Button_ToControlScene extends Button {
    public Button_ToControlScene(int positionIndex) {
        super(Strings.MainMenu.Back, positionIndex, true);
    }

    public void performAction() {
        BaseWindow.scene = new ControlScene();
    }
}

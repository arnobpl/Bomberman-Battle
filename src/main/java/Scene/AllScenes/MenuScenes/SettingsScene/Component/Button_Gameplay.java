package Scene.AllScenes.MenuScenes.SettingsScene.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GameplayScene.GameplayScene;

/**
 * Created by Arnob on 05/02/2015.
 * Button for "Gameplay"
 */
public class Button_Gameplay extends Button {
    public Button_Gameplay(int positionIndex) {
        super(Strings.Settings.Gameplay, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new GameplayScene();
    }
}

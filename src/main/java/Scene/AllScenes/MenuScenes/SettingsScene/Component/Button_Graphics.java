package Scene.AllScenes.MenuScenes.SettingsScene.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.GraphicsScene;

/**
 * Created by Arnob on 05/02/2015.
 * Button for "Graphics"
 */
public class Button_Graphics extends Button {
    public Button_Graphics(int positionIndex) {
        super(Strings.Settings.Graphics, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new GraphicsScene();
    }
}

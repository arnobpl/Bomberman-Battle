package Scene.AllScenes.MenuScenes.SettingsScene.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.AudioScene.AudioScene;

/**
 * Created by Arnob on 05/02/2015.
 * Button for "Audio"
 */
public class Button_Audio extends Button {
    public Button_Audio(int positionIndex) {
        super(Strings.Settings.Audio, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new AudioScene();
    }
}

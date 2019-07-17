package Scene.AllScenes.MenuScenes.MainMenu.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.HowToPlayScene;

/**
 * Created by Arnob on 03/11/2014.
 * Button for "How to Play"
 */
public class Button_HowToPlay extends Button {
    public Button_HowToPlay(int positionIndex) {
        super(Strings.MainMenu.HowToPlay, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new HowToPlayScene();
    }
}

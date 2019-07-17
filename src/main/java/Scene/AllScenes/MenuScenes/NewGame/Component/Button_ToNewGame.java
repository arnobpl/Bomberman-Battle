package Scene.AllScenes.MenuScenes.NewGame.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.NewGame.NewGame;

/**
 * Created by Arnob on 07/11/2014.
 * Back button to NewGame
 */
public class Button_ToNewGame extends Button {
    public Button_ToNewGame(int positionIndex) {
        super(Strings.MainMenu.Back, positionIndex, true);
    }

    public void performAction() {
        BaseWindow.scene = new NewGame();
    }
}

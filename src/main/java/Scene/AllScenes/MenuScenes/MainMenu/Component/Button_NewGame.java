package Scene.AllScenes.MenuScenes.MainMenu.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.NewGame.NewGame;

/**
 * Created by Arnob on 03/11/2014.
 * Button for "New Game"
 */
public class Button_NewGame extends Button {
    public Button_NewGame(int positionIndex) {
        super(Strings.MainMenu.NewGame, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new NewGame();
    }
}

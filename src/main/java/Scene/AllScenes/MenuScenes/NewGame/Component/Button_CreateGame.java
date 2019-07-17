package Scene.AllScenes.MenuScenes.NewGame.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.CreateGame;

/**
 * Created by Arnob on 06/11/2014.
 * Button for "Create Game"
 */
public class Button_CreateGame extends Button {
    public Button_CreateGame(int positionIndex) {
        super(Strings.NewGame.CreateGame, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new CreateGame();
    }
}

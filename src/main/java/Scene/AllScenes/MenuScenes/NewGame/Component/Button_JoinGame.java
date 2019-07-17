package Scene.AllScenes.MenuScenes.NewGame.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.OnlineGame.JoinGame.JoinGame;

/**
 * Created by Arnob on 06/11/2014.
 * Button for "Join Game"
 */
public class Button_JoinGame extends Button {
    public Button_JoinGame(int positionIndex) {
        super(Strings.NewGame.JoinGame, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new JoinGame();
    }
}

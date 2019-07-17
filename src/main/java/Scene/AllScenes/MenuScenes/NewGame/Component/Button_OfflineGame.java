package Scene.AllScenes.MenuScenes.NewGame.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.OfflineGame.OfflineGame;

/**
 * Created by Arnob on 17/02/2015.
 * Button for "Offline Game"
 */
public class Button_OfflineGame extends Button {
    public Button_OfflineGame(int positionIndex) {
        super(Strings.NewGame.OfflineGame, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new OfflineGame();
    }
}

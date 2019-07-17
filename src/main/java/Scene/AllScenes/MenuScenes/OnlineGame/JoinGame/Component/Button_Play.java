package Scene.AllScenes.MenuScenes.OnlineGame.JoinGame.Component;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Network.BaseNetwork.InitializeGameNet.InitializeJoinGameNet;
import Scene.AllScenes.MenuScenes.OnlineGame.JoinGame.JoinGame;
import Scene.AllScenes.MenuScenes.OnlineGame.JoinGame.WaitingScene;

/**
 * Created by Arnob on 07/11/2014.
 * This button invoke a method which performs all network task to join and play the game.
 */
public class Button_Play extends Button {
    public Button_Play(int positionIndex) {
        super(Strings.NewGame.Play, positionIndex, false);
    }

    public void performAction() {
        if (!JoinGame.invalidInput) {
            AllSettings.userSettings.UserIP = JoinGame.textInput_getIP.getText();
            AllSettings.saveUserData();
            new InitializeJoinGameNet();
            BaseWindow.scene = new WaitingScene();
        }
    }

}

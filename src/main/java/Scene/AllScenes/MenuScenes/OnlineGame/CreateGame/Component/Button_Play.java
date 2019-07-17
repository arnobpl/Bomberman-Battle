package Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.Component;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Network.BaseNetwork.InitializeGameNet.InitializeCreateGameNet;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.WaitingScene;

/**
 * Created by Arnob on 07/11/2014.
 * This button invoke a method which performs all network task to create and play the game.
 */
public class Button_Play extends Button {
    public Button_Play(int positionIndex) {
        super(Strings.NewGame.Play, positionIndex, false);
    }

    public void performAction() {
        AllSettings.userSettings.totalHumanPlayers = MainGame.totalHumanPlayers;
        AllSettings.userSettings.totalPlayers = MainGame.totalPlayers;
        AllSettings.saveUserData();
        MainGame.matchToWin = AllSettings.userSettings.matchToWin;
        MainGame.matchDurationMinute = AllSettings.userSettings.matchDurationMinute;
        new InitializeCreateGameNet();
        BaseWindow.scene = new WaitingScene();
    }
}

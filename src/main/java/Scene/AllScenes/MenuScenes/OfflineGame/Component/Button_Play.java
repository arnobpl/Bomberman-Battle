package Scene.AllScenes.MenuScenes.OfflineGame.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.OfflineGame.OfflineGame;

/**
 * Created by Arnob on 17/04/2014.
 * This button creates an offline game.
 */
public class Button_Play extends Button {
    public Button_Play(int positionIndex) {
        super(Strings.NewGame.Play, positionIndex, false);
    }

    public void performAction() {
        AllSettings.userSettings.totalPlayers = MainGame.totalPlayers;
        AllSettings.userSettings.totalHumanPlayers = MainGame.totalHumanPlayers;
        AllSettings.userSettings.AI_playerDifficulty = MainGame.AI_playerDifficulty;
        AllSettings.saveUserData();
        MainGame.matchToWin = AllSettings.userSettings.matchToWin;
        MainGame.matchDurationMinute = AllSettings.userSettings.matchDurationMinute;
        OfflineGame.transitionAnimation.start();
    }
}

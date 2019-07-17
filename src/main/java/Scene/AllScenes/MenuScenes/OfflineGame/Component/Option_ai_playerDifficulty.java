package Scene.AllScenes.MenuScenes.OfflineGame.Component;

import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.GameScenes.MainGame.MainGame;

/**
 * Created by Arnob on 18/04/2014.
 * User input to get how many human players to play
 */
public class Option_ai_playerDifficulty extends OptionChooser {
    public Option_ai_playerDifficulty(int positionIndex) {
        super(Strings.OfflineGame.AI_PlayerDifficulty, positionIndex, 2, MainGame.AI_playerDifficulty);

        addOption(Strings.OfflineGame.Newbie);
        addOption(Strings.OfflineGame.Normal);
    }

    public void performAction(int selectedIndex) {
        MainGame.AI_playerDifficulty = selectedIndex;
    }
}

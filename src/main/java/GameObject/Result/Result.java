package GameObject.Result;

import AppInfo.BaseWindow;
import GameObject.Player.PlayerEnum.Color;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.GameScenes.ResultScene;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnob on 10/10/2014.
 * This class tracks all players' records to judge the winner.
 */
public class Result {
    public static List<Record> records;

    public Result(int totalPlayer) {
        records = new ArrayList<>(totalPlayer);
        for (int i = 0; i < totalPlayer; i++) {
            records.add(new Record(Color.valuesCached()[i]));
        }
    }

    public int getRecord(Color color) {
        return records.get(color.ordinal()).getScore();
    }

    public void setWinner(Color color) {
        if (records.get(color.ordinal()).increaseScore() == MainGame.matchToWin) {
            // Result scene (champion found)
            BaseWindow.scene = new ResultScene(color, true);
        } else {
            // Result scene (no champion)
            BaseWindow.scene = new ResultScene(color, false);
        }
    }

    public void drawGame() {
        // call Result scene without parameter to invoke draw game animation
        BaseWindow.scene = new ResultScene();
    }

}

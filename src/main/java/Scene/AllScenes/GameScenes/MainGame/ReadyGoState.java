package Scene.AllScenes.GameScenes.MainGame;

import AppInfo.Customization.AllSettings;
import AppInfo.Initialized;
import GraphicsRenderer.GfxFromFile;
import Scene.Subscene;

import java.awt.*;

/**
 * Created by Arnob on 13/12/2014.
 * Initial ReadyGoState of MainGame
 */
public class ReadyGoState implements Subscene {

    private static final int readyShowElapsed = 2000;
    private static final int goShowElapsed = 1000;

    private static final int readyStringImage_left = (AllSettings.unscaledWidth - GfxFromFile.readyGoHurry_Width) >> 1;
    private static final int readyStringImage_top = (AllSettings.unscaledHeight - GfxFromFile.readyGoHurry_Height) >> 1;

    private int time = 0;
    private boolean readyShow = true;

    public void update() {
        if (readyShow) {
            time += AllSettings.userSettings.timeStep;
            if (time > readyShowElapsed) {
                readyShow = false;
            }
        } else {
            MainGame.time += AllSettings.userSettings.timeStep;
            RunningGameState.runGame();
            if (MainGame.time > goShowElapsed) {
                MainGame.subscene = new RunningGameState();
            }
        }
    }

    public void render(Graphics2D g) {
        if (readyShow) {
            g.drawImage(Initialized.gfx.readyGoHurry(0), readyStringImage_left, readyStringImage_top, null);
        } else {
            g.drawImage(Initialized.gfx.readyGoHurry(1), readyStringImage_left, readyStringImage_top, null);
        }
    }
}

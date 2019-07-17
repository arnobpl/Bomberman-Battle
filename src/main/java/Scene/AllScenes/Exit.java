package Scene.AllScenes;

import AppInfo.BaseWindow;
import Scene.Scene;

/**
 * Created by Arnob on 03/11/2014.
 * This scene is shown while exiting from the game.
 */
public class Exit extends Scene {
    // TODO: do some animations before exiting
    public Exit() {
        BaseWindow.running = false;
    }
}

package Scene.AllScenes.GameScenes.MainGame;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import GameObject.Player.InputController.InputController;
import Scene.AllScenes.MenuScenes.ConnectionErrorScene;
import Scene.Subscene;

import java.awt.*;

/**
 * Created by Arnob on 14/12/2014.
 * RunningGameState of MainGame
 */
public class RunningGameState implements Subscene {

    private static final int drawGameAllDyingTimeoutDelay = 3000;

    private static boolean allDyingTimeAssigned;
    private static int allDyingTime;

    public RunningGameState() {
        allDyingTimeAssigned = false;
    }

    public static void runGame() {
        // game is draw at the frame accuracy
        if (MainGame.inputControllersList.isEmpty()) {
            MainGame.drawGame = true;
            MainGame.subscene = new TransitionState();
        } else {
            // playing state
            if (MainGame.inputControllersList.size() > 1 || allDyingTimeAssigned) {
                boolean allDying = true;
                try {
                    for (InputController i : MainGame.inputControllersList) {
                        i.processInput();
                        if (allDying) allDying = i.isKilled();
                    }
                } catch (NullPointerException e) {  // processInput() may throw NullPointerException when network error
                    e.printStackTrace();
                    BaseWindow.scene = new ConnectionErrorScene(e);
                    return;
                }
                // game is draw (by either frame accuracy or not)
                if (allDying) {
                    // needs a delay, game is draw
                    if (!allDyingTimeAssigned) {
                        allDyingTime = MainGame.time;
                        allDyingTimeAssigned = true;
                    } else if (MainGame.time - allDyingTime > drawGameAllDyingTimeoutDelay) {
                        MainGame.drawGame = true;
                        MainGame.subscene = new TransitionState();
                    }
                }
            }
            // winner is found
            else {
                MainGame.drawGame = false;
                MainGame.winnerPlayerColor = MainGame.inputControllersList.get(0).getColor();
                MainGame.subscene = new TransitionState();

            }
        }
    }

    public void update() {
        MainGame.time += AllSettings.userSettings.timeStep;

        if (MainGame.time > MainGame.timeoutWarningElapsed) {
            MainGame.subscene = new HurryUpState();
        }

        runGame();
    }

    public void render(Graphics2D g) {
    }
}

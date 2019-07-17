package Scene.AllScenes.GameScenes.MainGame;

import AppInfo.Customization.AllSettings;
import Scene.Subscene;
import SoundSystem.BaseSound;

import java.awt.*;

/**
 * Created by Arnob on 15/12/2014.
 * TransitionState of MainGame
 */
public class TransitionState implements Subscene {

    public TransitionState() {
        if (!MainGame.resultFoundSoundPlayed) {
            BaseSound.playSound(MainGame.resultFoundSound, AllSettings.userSettings.soundEffectVolume);
            MainGame.resultFoundSoundPlayed = true;
        }
    }

    public void update() {
        MainGame.transitionAnimation.tick();
    }

    public void render(Graphics2D g) {
        if (MainGame.transitionAnimation.render(g)) {
            if (MainGame.drawGame) MainGame.gameResult.drawGame();
            else MainGame.gameResult.setWinner(MainGame.winnerPlayerColor);
        }
    }
}

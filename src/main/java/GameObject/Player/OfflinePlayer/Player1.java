package GameObject.Player.OfflinePlayer;

import AppInfo.Customization.AllSettings;
import GameObject.Player.PlayerEnum.Color;

import java.awt.event.KeyEvent;

/**
 * Created by Arnob on 17/02/2015.
 * This class is the specialized version of <code>OfflinePlayer</code> class to be used for 2nd player in offline game.
 */
public class Player1 extends OfflinePlayer {
    public Player1() {
        super(Color.Black);
    }

    protected boolean isKeyUp(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Player1_Key_Up;
    }

    protected boolean isKeyDown(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Player1_Key_Down;
    }

    protected boolean isKeyLeft(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Player1_Key_Left;
    }

    protected boolean isKeyRight(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Player1_Key_Right;
    }

    protected boolean isKeyFire(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Player1_Key_Fire;
    }


    protected void vibrateWhenKilled() {   // this function only does vibration for this PC-controlled single human player
    }

    protected void resetVibrationAfterDeath() {    // this function only does vibration for this PC-controlled single human player
    }

    protected void playDyingSoundWithReverberation() { // this is the special sound effect which only works for this PC-controlled player in online games
    }
}

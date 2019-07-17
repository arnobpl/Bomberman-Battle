package GameObject.Player.OfflinePlayer;

import AppInfo.Customization.AllSettings;
import GameObject.Player.PlayerEnum.Color;

import java.awt.event.KeyEvent;

/**
 * Created by Arnob on 16/04/2015.
 * This class is the specialized version of <code>OfflinePlayer</code> class to be used for 1st player in offline game.
 */
public class Player0 extends OfflinePlayer {
    public Player0() {
        super(Color.White);
    }

    protected boolean isKeyUp(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Player0_Key_Up;
    }

    protected boolean isKeyDown(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Player0_Key_Down;
    }

    protected boolean isKeyLeft(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Player0_Key_Left;
    }

    protected boolean isKeyRight(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Player0_Key_Right;
    }

    protected boolean isKeyFire(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Player0_Key_Fire;
    }


    protected void vibrateWhenKilled() {   // this function only does vibration for this PC-controlled single human player
    }

    protected void resetVibrationAfterDeath() {    // this function only does vibration for this PC-controlled single human player
    }

    protected void playDyingSoundWithReverberation() { // this is the special sound effect which only works for this PC-controlled player in online games
    }
}

package Network.BaseNetwork;

import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;
import SoundSystem.BaseSound;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arnob on 14/11/2014.
 * This class contains some methods for network (such as IP address).
 */
public class NetworkTask {
    private static final String validIP4Pattern = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final Pattern pattern = Pattern.compile(validIP4Pattern);

    public static boolean isValid_IP4_address(String ip4Address) {
        Matcher matcher = pattern.matcher(ip4Address);
        return matcher.matches();
    }

    public static void doTaskAfterClosingConnection() {
        MainMenu.menuMusic.stop(BaseSound.fadeoutElapsed);
        MainGame.gameMusic.stop(BaseSound.fadeoutElapsed);
    }
}

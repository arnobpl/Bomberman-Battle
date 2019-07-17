package AppInfo.Customization;

import AppInfo.FilePath;

import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Arnob on 28/09/2014.
 * This class contains all the graphics, audio, game, control and other fixed settings.
 */
public class AllSettings {

    public static UserSettings userSettings = new UserSettings();


    public static final int unscaledWidth = 304;
    public static final int unscaledHeight = 224;

    public static final int unscaledWidth_half = unscaledWidth >> 1;
    public static final int unscaledHeight_half = unscaledHeight >> 1;


    public static final int motionBlurAlpha = 127;


    public static final int timeoutWarningStartingTimeBeforeTimeout = 30 * 1000;


    public static final int Key_MenuLeft = KeyEvent.VK_LEFT;
    public static final int Key_MenuRight = KeyEvent.VK_RIGHT;
    public static final int Key_MenuUp = KeyEvent.VK_UP;
    public static final int Key_MenuDown = KeyEvent.VK_DOWN;
    public static final int Key_MenuEnter = KeyEvent.VK_ENTER;
    public static final int Key_Backspace = KeyEvent.VK_BACK_SPACE;
    public static final int Key_Delete = KeyEvent.VK_DELETE;
    public static final int Key_Escape = KeyEvent.VK_ESCAPE;


    public static void saveUserData() {
        new Thread(() -> {
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FilePath.UserFilePath.UserData)));
                userSettings.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

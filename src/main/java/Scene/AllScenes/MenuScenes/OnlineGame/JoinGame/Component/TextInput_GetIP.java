package Scene.AllScenes.MenuScenes.OnlineGame.JoinGame.Component;

import AppInfo.Customization.AllSettings;
import MenuSystem.Form.MenuItems.TextInput;
import Network.BaseNetwork.NetworkTask;
import Scene.AllScenes.MenuScenes.OnlineGame.JoinGame.JoinGame;

import java.awt.event.KeyEvent;

/**
 * Created by Arnob on 08/11/2014.
 * TextInput for IP address
 */
public class TextInput_GetIP extends TextInput {
    public TextInput_GetIP(int positionIndex) {
        super(AllSettings.userSettings.UserIP, positionIndex, true);
        setMaxTextLength(15);
    }

    public boolean filterThisText(KeyEvent e, String text) {
        if (('0' <= e.getKeyChar() && e.getKeyChar() <= '9') || e.getKeyCode() == AllSettings.Key_Backspace || e.getKeyCode() == AllSettings.Key_Delete) {
            JoinGame.invalidInput = false;
            return false;
        } else if (e.getKeyChar() == '.') {
            if (text.length() == 0) return true;    // first char cannot be '.'
            if (text.charAt(text.length() - 1) == '.') return true; // ".." situation is filtered
            int dotCount = 0; // counts '.'
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == '.') dotCount++;
            }
            // if text has already 3 dots, it will be filtered;
            // here "JoinGame.invalidInput = false;" statement not needed;
            return dotCount >= 3;
        }
        return true;
    }

    public void doThisAfterTextFiltered(KeyEvent e, String text) {
        JoinGame.invalidInput = true;
    }

    public void performAction(String text) {
        JoinGame.invalidInput = !NetworkTask.isValid_IP4_address(text);
    }
}

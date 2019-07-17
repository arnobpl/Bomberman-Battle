package Scene.AllScenes.MenuScenes.MainMenu.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.Exit;

/**
 * Created by Arnob on 03/11/2014.
 * Button for "Exit"
 */
public class Button_Exit extends Button {
    public Button_Exit(int positionIndex) {
        super(Strings.MainMenu.Exit, positionIndex, true);
    }

    public void performAction() {
        BaseWindow.scene = new Exit();
    }
}

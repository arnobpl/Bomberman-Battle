package Scene.AllScenes.MenuScenes.MainMenu.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;

/**
 * Created by Arnob on 02/11/2014.
 * Class for button that can only go to main menu when action performed
 */
public class Button_ToMenu extends Button {
    public Button_ToMenu(int positionIndex) {
        super(Strings.MainMenu.Back, positionIndex, true);
    }

    public void performAction() {
        BaseWindow.scene = new MainMenu();
    }
}

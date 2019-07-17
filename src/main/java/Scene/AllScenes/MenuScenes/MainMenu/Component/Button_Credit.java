package Scene.AllScenes.MenuScenes.MainMenu.Component;

import AppInfo.BaseWindow;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.Button;
import Scene.AllScenes.MenuScenes.CreditScene;

/**
 * Created by Arnob on 03/11/2014.
 * Button for "Credit"
 */
public class Button_Credit extends Button {
    public Button_Credit(int positionIndex) {
        super(Strings.MainMenu.Credit, positionIndex, false);
    }

    public void performAction() {
        BaseWindow.scene = new CreditScene();
    }

}

package MenuSystem.Form.MenuItems;

import AppInfo.Customization.AllSettings;
import FontRenderer.FontFromFile;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Arnob on 02/11/2014.
 * This interface class creates a common environment for different menu items.
 */
public interface MenuItem {
    int height = 28;   // by default, 6 menuItems can be drawn (224 / 32 - 1 = 7), "- 1" for title

    int textTop = (height - FontFromFile.bigFontHeight) >> 1;
    int maxIndex = AllSettings.unscaledHeight / height - 1;    // by default, it is 7

    void update();

    void render(Graphics2D g);

    void keyPressed(KeyEvent e);

    void keyReleased(KeyEvent e);

    void focusEntered();

    void focusLost();

}

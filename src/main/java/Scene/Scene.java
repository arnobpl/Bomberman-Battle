package Scene;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Arnob on 01/10/2014.
 * This abstract class creates a common environment for different game scenes.
 */
public abstract class Scene {
    public void update() {
    }

    public void render(Graphics2D g) {
    }


    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }


    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }


    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
    }

    /**
     * This is used for resetting mouse alignment when calling the parent scene that is already constructed.
     */
    public void reset() {
    }

}

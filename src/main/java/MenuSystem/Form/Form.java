package MenuSystem.Form;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Resource.ResourceLoader;
import GraphicsRenderer.Animation.TitleString_Animation;
import GraphicsRenderer.GfxFromFile;
import MenuSystem.Form.MenuItems.Button;
import MenuSystem.Form.MenuItems.MenuItem;
import Scene.Scene;
import SoundSystem.BaseSound;
import javafx.scene.media.Media;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Arnob on 06/11/2014.
 * This class creates a form for menu system which contains all other menu items.
 * This class does not handle background image or any extra animations.
 */
public class Form {
    private static final double titleStringMoveStep = -2 * AllSettings.userSettings.motionConst;
    private static final int titleStringTop = 5;
    private static final int arrowMinDistance = Button.width - GfxFromFile.arrowL_Width;

    private TitleString_Animation titleStringAnimation;

    private int totalMenuItems = 1;
    private MenuSystem.Form.MenuItems.MenuItem[] menuComponents = new MenuItem[totalMenuItems];

    private int indexToAdd = 0;

    private int menuItemPositionIndexToAddOffset = Button.maxIndex - menuComponents.length + 1;

    private MenuArrow_Animation arrowAnimation;

    private Scene parentScene;

    private boolean sceneShouldBeChanged = false;

    private static final Media goToParentSceneSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InMenu.MenuSelect);


    /**
     * @param parentScene              the parent scene to go back with Key_Escape
     * @param titleString              title string
     * @param totalMenuItems           total menu items to be added
     * @param arrowMinDistance         minimum distance between the two arrow peeks
     * @param topIndexOffsetFromMiddle 0 for middle (default), -1 for 1 <code>MenuItem</code> top from the middle, +1 for  1 <code>MenuItem</code> bottom from the middle and such that
     */
    public Form(Scene parentScene, String titleString, int totalMenuItems, int arrowMinDistance, int topIndexOffsetFromMiddle) {
        this.parentScene = parentScene;
        titleStringAnimation = new TitleString_Animation(titleString, titleStringMoveStep, titleStringTop);

        if (1 < totalMenuItems && totalMenuItems <= MenuItem.maxIndex) {
            menuComponents = new MenuItem[totalMenuItems];
            this.totalMenuItems = totalMenuItems;
        }

        int topIndexOffsetFromBottom = ((MenuItem.maxIndex - totalMenuItems) >> 1) - topIndexOffsetFromMiddle;

        arrowAnimation = new MenuArrow_Animation(menuComponents, arrowMinDistance, topIndexOffsetFromBottom);

        menuItemPositionIndexToAddOffset = Button.maxIndex - menuComponents.length - topIndexOffsetFromBottom + 1;
    }

    /**
     * @param parentScene      the parent scene to go back with Key_Escape
     * @param titleString      title string
     * @param totalMenuItems   total menu items to be added
     * @param arrowMinDistance minimum distance between the two arrow peeks
     */
    public Form(Scene parentScene, String titleString, int totalMenuItems, int arrowMinDistance) {
        this(parentScene, titleString, totalMenuItems, arrowMinDistance, 0);
    }

    /**
     * @param parentScene    the parent scene to go back with Key_Escape
     * @param titleString    title string
     * @param totalMenuItems total menu items to be added
     */
    public Form(Scene parentScene, String titleString, int totalMenuItems) {
        this(parentScene, titleString, totalMenuItems, arrowMinDistance, 0);
    }

    /**
     * This allows to override the current <code>MenuItem</code> components.
     */
    public void clearMenuItems() {
        indexToAdd = 0;
    }

    public void resetSelectedIndex() {
        arrowAnimation.setSelectedIndex(0);
    }

    public void resetMouseAlignment() {
        arrowAnimation.resetMouseAlignment();
    }

    /**
     * This sets the <code>selectedIndex</code> to 0 and resets the alignment of mouse pointer.
     * Resetting the alignment of mouse pointer is necessary if display scale is changed.
     */
    public void reset() {
        resetSelectedIndex();
        resetMouseAlignment();
    }


    public void setSelectedIndex(int selectedIndex) {
        arrowAnimation.setSelectedIndex(selectedIndex);
    }

    public int getSelectedIndex() {
        return arrowAnimation.getSelectedIndex();
    }

    public int getPositionIndex() {
        return indexToAdd + menuItemPositionIndexToAddOffset;
    }

    public void addMenuItem(MenuItem menuItem) {
        if (indexToAdd < totalMenuItems) {
            menuComponents[indexToAdd++] = menuItem;
        }
    }

    public MenuItem getMenuItem(int index) {
        if (0 <= index && index < totalMenuItems) return menuComponents[index];
        return null;
    }

    /**
     * Before <code>update()</code>, all <code>MenuItem</code> need to be added.
     * Otherwise <code>NullPointerException</code> will be thrown.
     */
    public void update() {
        titleStringAnimation.animate();

        for (MenuItem i : menuComponents) {
            i.update();
        }

        arrowAnimation.update();
    }

    /**
     * Before <code>render(Graphics2D g)</code>, all <code>MenuItem</code> need to be added.
     * Otherwise <code>NullPointerException</code> will be thrown.
     */
    public void render(Graphics2D g) {
        titleStringAnimation.render(g);

        for (MenuItem i : menuComponents) {
            i.render(g);
        }

        arrowAnimation.render(g);

        if (sceneShouldBeChanged) {
            BaseWindow.scene = parentScene; // scene is changed here to maintain "first update, then render" in the new scene
            parentScene.reset();
            sceneShouldBeChanged = false;
        }
    }

    /**
     * Before <code>keyPressed(KeyEvent e)</code>, all <code>MenuItem</code> need to be added.
     * Otherwise <code>NullPointerException</code> will be thrown.
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == AllSettings.Key_Escape) {
            return;
        }
        arrowAnimation.keyPressed(e);
    }

    /**
     * Before <code>keyReleased(KeyEvent e)</code>, all <code>MenuItem</code> need to be added.
     * Otherwise <code>NullPointerException</code> will be thrown.
     */
    public void keyReleased(KeyEvent e) {
        if (parentScene != null && e.getKeyCode() == AllSettings.Key_Escape) {
            sceneShouldBeChanged = true;    // no  actual  change here because scene change must be done in 'render' method
            BaseSound.playSound(goToParentSceneSound, AllSettings.userSettings.soundEffectVolume);
            return;
        }
        arrowAnimation.keyReleased(e);
    }

    public void mousePressed(MouseEvent e) {
        arrowAnimation.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        arrowAnimation.mouseReleased(e);
    }

    public void mouseMoved(MouseEvent e) {
        arrowAnimation.mouseMoved(e);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        arrowAnimation.mouseWheelMoved(e);
    }

    public void mouseEntered(MouseEvent e) {
        arrowAnimation.mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        arrowAnimation.mouseExited(e);
    }

    public void mouseDragged(MouseEvent e) {
        arrowAnimation.mouseDragged(e);
    }

    public void focusGained(FocusEvent e) {
        arrowAnimation.focusGained(e);
    }

    public void focusLost(FocusEvent e) {
        arrowAnimation.focusLost(e);
    }
}

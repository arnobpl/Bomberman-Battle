package MenuSystem.Form;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import GraphicsRenderer.Animation.PingPong_Animation;
import GraphicsRenderer.GfxFromFile;
import GraphicsRenderer.GradientColor;
import MenuSystem.Form.MenuItems.Button;
import MenuSystem.Form.MenuItems.MenuItem;
import SoundSystem.BaseSound;
import javafx.scene.media.Media;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 06/11/2014.
 * This class handles the animation of menu arrow.
 * This class also responses for key inputs and scrolling sound.
 */

public class MenuArrow_Animation {
    private final MenuItem[] menuComponents;

    private static final int arrowLeftRight_amplitude = 10;
    private static final double arrowLeftRight_AnimationStep = 1.2 * AllSettings.userSettings.motionConst;
    private static final double upDown_AnimationStep = 10 * AllSettings.userSettings.motionConst;

    private int arrowRight_Left = Button.left - GfxFromFile.arrowL_Width;
    private int arrowLeft_Left = Button.left + Button.width;
    private int arrow_TopIndexOffset = MenuItem.maxIndex;

    private PingPong_Animation arrowLeftRightAnimation;

    private int selectedIndex = 0;  // range [0, menuComponents.length - 1]
    int previousSelectedIndex = 0;
    private double arrow_Top = (selectedIndex + arrow_TopIndexOffset) * MenuItem.height;
    private boolean arrowUpDownAnimationNeeded = false;

    private static final int mouseHighlightedMenuItemImageSameColorWidth = (int) (AllSettings.unscaledWidth * 0.8);
    private static final int mouseHighlightedMenuItemImageMouseLeft = (AllSettings.unscaledWidth - mouseHighlightedMenuItemImageSameColorWidth) >> 1;
    private static final int mouseHighlightedMenuItemImageMouseRight = mouseHighlightedMenuItemImageMouseLeft + mouseHighlightedMenuItemImageSameColorWidth;

    private static final BufferedImage mouseHighlightedMenuItemImage = new BufferedImage(AllSettings.unscaledWidth, MenuItem.height, BufferedImage.TYPE_INT_ARGB);
    private static final Color mouseHighlightedMenuItemImageColor = new Color(0, 162, 232, 31);

    private static final BufferedImage mouseClickedMenuItemImage = new BufferedImage(AllSettings.unscaledWidth, MenuItem.height, BufferedImage.TYPE_INT_ARGB);
    private static final Color mouseClickedMenuItemImageColor = new Color(0, 162, 232, 63);

    static {
        Graphics2D g;

        g = mouseHighlightedMenuItemImage.createGraphics();
        g.setColor(mouseHighlightedMenuItemImageColor);
        g.fillRect(mouseHighlightedMenuItemImageMouseLeft, 0, mouseHighlightedMenuItemImageSameColorWidth, mouseHighlightedMenuItemImage.getHeight());
        GradientColor.createGradientBackground_alongX(mouseHighlightedMenuItemImage, GradientColor.transparentColor, mouseHighlightedMenuItemImageColor, 0, mouseHighlightedMenuItemImageMouseLeft);
        GradientColor.createGradientBackground_alongX(mouseHighlightedMenuItemImage, mouseHighlightedMenuItemImageColor, GradientColor.transparentColor, mouseHighlightedMenuItemImageMouseRight, AllSettings.unscaledWidth);
        g.dispose();

        g = mouseClickedMenuItemImage.createGraphics();
        g.setColor(mouseClickedMenuItemImageColor);
        g.fillRect(mouseHighlightedMenuItemImageMouseLeft, 0, mouseHighlightedMenuItemImageSameColorWidth, mouseClickedMenuItemImage.getHeight());
        GradientColor.createGradientBackground_alongX(mouseClickedMenuItemImage, GradientColor.transparentColor, mouseClickedMenuItemImageColor, 0, mouseHighlightedMenuItemImageMouseLeft);
        GradientColor.createGradientBackground_alongX(mouseClickedMenuItemImage, mouseClickedMenuItemImageColor, GradientColor.transparentColor, mouseHighlightedMenuItemImageMouseRight, AllSettings.unscaledWidth);
        g.dispose();
    }

    private final int[] mouseHighlightedMenuItemImageTopList;
    private final int[] mouseHighlightedMenuItemImageTopWithMouseScaleList;
    private boolean showMouseHighlightedMenuItemImage = false;
    private boolean showMouseClickedMenuItemImage = false;
    private int mouseHighlightedMenuItemImageLeftWithMouseScale = (int) (mouseHighlightedMenuItemImageMouseLeft * AllSettings.userSettings.displayScale);
    private int mouseHighlightedMenuItemImageRightWithMouseScale = (int) (mouseHighlightedMenuItemImageMouseRight * AllSettings.userSettings.displayScale);
    private int mousePressedSelectedIndex = -1;


    private static final Media menuScrollSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InMenu.MenuScroll);


    public MenuArrow_Animation(MenuItem[] menuComponents, int minDistance, int topIndexOffsetFromBottom) {
        this.menuComponents = menuComponents;

        arrowLeftRightAnimation = new PingPong_Animation(arrowLeftRight_amplitude, arrowLeftRight_AnimationStep);

        int minDistance_half = Math.abs(minDistance) >> 1;
        arrowRight_Left = AllSettings.unscaledWidth_half - minDistance_half - GfxFromFile.arrowR_Width;
        arrowLeft_Left = AllSettings.unscaledWidth_half + minDistance_half;
        arrow_TopIndexOffset = MenuItem.maxIndex - menuComponents.length + 1 - Math.abs(topIndexOffsetFromBottom);
        arrow_Top = (selectedIndex + arrow_TopIndexOffset) * MenuItem.height;

        mouseHighlightedMenuItemImageTopList = new int[menuComponents.length];
        mouseHighlightedMenuItemImageTopWithMouseScaleList = new int[menuComponents.length + 1];
        for (int i = 0; i < mouseHighlightedMenuItemImageTopList.length; i++) {
            mouseHighlightedMenuItemImageTopList[i] = (i + arrow_TopIndexOffset) * MenuItem.height;
        }
        resetMouseAlignment();
    }

    public void setSelectedIndex(int selectedIndex) {
        if (0 <= selectedIndex && selectedIndex < menuComponents.length) {
            this.selectedIndex = selectedIndex;
            arrow_Top = (selectedIndex + arrow_TopIndexOffset) * MenuItem.height;
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void update() {
        arrowLeftRightAnimation.animate();
        if (arrowUpDownAnimationNeeded) {
            arrowUpDownAnimate();
        }
    }

    private void arrowUpDownAnimate() {
        int expectedArrowTop = (selectedIndex + arrow_TopIndexOffset) * MenuItem.height;
        if (arrow_Top < expectedArrowTop) {
            if (arrow_Top + upDown_AnimationStep < expectedArrowTop) {
                arrow_Top += upDown_AnimationStep;
            } else {
                arrow_Top = expectedArrowTop;
                arrowUpDownAnimationNeeded = false;
            }
        } else {
            if (expectedArrowTop < arrow_Top - upDown_AnimationStep) {
                arrow_Top -= upDown_AnimationStep;
            } else {
                arrow_Top = expectedArrowTop;
                arrowUpDownAnimationNeeded = false;
            }
        }
    }

    public void render(Graphics2D g) {
        if (showMouseHighlightedMenuItemImage) {
            if (showMouseClickedMenuItemImage)
                g.drawImage(mouseClickedMenuItemImage, 0, mouseHighlightedMenuItemImageTopList[selectedIndex], null);
            else
                g.drawImage(mouseHighlightedMenuItemImage, 0, mouseHighlightedMenuItemImageTopList[selectedIndex], null);
        }
        g.drawImage(Initialized.gfx.arrowRight(), arrowRight_Left - arrowLeftRightAnimation.getAnimateValue(), (int) Math.round(arrow_Top), null);
        g.drawImage(Initialized.gfx.arrowLeft(), arrowLeft_Left + arrowLeftRightAnimation.getAnimateValue(), (int) Math.round(arrow_Top), null);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == AllSettings.Key_MenuDown) {
            previousSelectedIndex = selectedIndex++;
            arrowUpDownAnimationNeeded = true;
            if (selectedIndex >= menuComponents.length) selectedIndex = menuComponents.length - 1;
            else {
                menuComponents[previousSelectedIndex].keyPressed(e);
                menuComponents[previousSelectedIndex].keyReleased(e);
                menuComponents[previousSelectedIndex].focusLost();
                menuComponents[selectedIndex].focusEntered();
                BaseSound.playSound(menuScrollSound, AllSettings.userSettings.soundEffectVolume);
            }
        } else if (e.getKeyCode() == AllSettings.Key_MenuUp) {
            previousSelectedIndex = selectedIndex--;
            arrowUpDownAnimationNeeded = true;
            if (selectedIndex < 0) selectedIndex = 0;
            else {
                menuComponents[previousSelectedIndex].keyPressed(e);
                menuComponents[previousSelectedIndex].keyReleased(e);
                menuComponents[previousSelectedIndex].focusLost();
                menuComponents[selectedIndex].focusEntered();
                BaseSound.playSound(menuScrollSound, AllSettings.userSettings.soundEffectVolume);
            }
        }
        menuComponents[selectedIndex].keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        menuComponents[selectedIndex].keyReleased(e);
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            showMouseClickedMenuItemImage = true;
            mousePressedSelectedIndex = getSelectedIndexFromMouseXY(e.getX(), e.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && showMouseClickedMenuItemImage) {
            showMouseClickedMenuItemImage = false;
            if (mousePressedSelectedIndex != -1) {
                int mouseReleasedSelectedIndex = getSelectedIndexFromMouseXY(e.getX(), e.getY());
                if (mousePressedSelectedIndex == mouseReleasedSelectedIndex) {
                    menuComponents[selectedIndex].keyPressed(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, AllSettings.Key_MenuEnter, (char) AllSettings.Key_MenuEnter));
                    menuComponents[selectedIndex].keyReleased(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, AllSettings.Key_MenuEnter, (char) AllSettings.Key_MenuEnter));
                }
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        int testSelectedIndex = getSelectedIndexFromMouseXY(e.getX(), e.getY());

        if (testSelectedIndex == -1) {
            showMouseHighlightedMenuItemImage = false;
            showMouseClickedMenuItemImage = false;
            return;
        }

        showMouseHighlightedMenuItemImage = true;
        selectedIndex = testSelectedIndex;
        arrowUpDownAnimationNeeded = true;
        if (previousSelectedIndex != selectedIndex) {
            showMouseClickedMenuItemImage = false;
            menuComponents[previousSelectedIndex].focusLost();
            menuComponents[selectedIndex].focusEntered();
        }
        previousSelectedIndex = selectedIndex;
    }

    private int getSelectedIndexFromMouseXY(int x, int y) {  // returns -1 if nothing selected
        if (mouseHighlightedMenuItemImageLeftWithMouseScale <= x && x < mouseHighlightedMenuItemImageRightWithMouseScale) {
            for (int i = 0; i < menuComponents.length; i++) {
                if (mouseHighlightedMenuItemImageTopWithMouseScaleList[i] <= y && y < mouseHighlightedMenuItemImageTopWithMouseScaleList[i + 1]) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (getSelectedIndexFromMouseXY(e.getX(), e.getY()) != -1) {
            if (e.getWheelRotation() < 0) {
                menuComponents[selectedIndex].keyPressed(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, AllSettings.Key_MenuLeft, (char) AllSettings.Key_MenuLeft));
                menuComponents[selectedIndex].keyReleased(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, AllSettings.Key_MenuLeft, (char) AllSettings.Key_MenuLeft));
            } else {
                menuComponents[selectedIndex].keyPressed(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, AllSettings.Key_MenuRight, (char) AllSettings.Key_MenuRight));
                menuComponents[selectedIndex].keyReleased(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, AllSettings.Key_MenuRight, (char) AllSettings.Key_MenuRight));
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
        int testSelectedIndex = getSelectedIndexFromMouseXY(e.getX(), e.getY());

        if (testSelectedIndex == -1) {
            showMouseHighlightedMenuItemImage = false;
            showMouseClickedMenuItemImage = false;
            return;
        }

        showMouseHighlightedMenuItemImage = true;
        selectedIndex = testSelectedIndex;
        arrowUpDownAnimationNeeded = true;

        menuComponents[selectedIndex].focusEntered();
        previousSelectedIndex = selectedIndex;
    }

    public void mouseExited(MouseEvent e) {
        showMouseHighlightedMenuItemImage = false;
        showMouseClickedMenuItemImage = false;
        menuComponents[selectedIndex].focusLost();
    }

    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    public void resetMouseAlignment() {
        mouseHighlightedMenuItemImageLeftWithMouseScale = (int) (mouseHighlightedMenuItemImageMouseLeft * AllSettings.userSettings.displayScale);
        mouseHighlightedMenuItemImageRightWithMouseScale = (int) (mouseHighlightedMenuItemImageMouseRight * AllSettings.userSettings.displayScale);
        for (int i = 0; i < mouseHighlightedMenuItemImageTopList.length; i++) {
            mouseHighlightedMenuItemImageTopWithMouseScaleList[i] = (int) (mouseHighlightedMenuItemImageTopList[i] * AllSettings.userSettings.displayScale);
        }
        mouseHighlightedMenuItemImageTopWithMouseScaleList[mouseHighlightedMenuItemImageTopWithMouseScaleList.length - 1] = (int) ((mouseHighlightedMenuItemImageTopWithMouseScaleList.length - 1 + arrow_TopIndexOffset) * MenuItem.height * AllSettings.userSettings.displayScale);

        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        try {
            SwingUtilities.convertPointFromScreen(mousePoint, BaseWindow.canvas);
            int testSelectedIndex = getSelectedIndexFromMouseXY(mousePoint.x, mousePoint.y);
            if (testSelectedIndex != -1) {
                showMouseHighlightedMenuItemImage = true;
                selectedIndex = testSelectedIndex;
                arrowUpDownAnimationNeeded = true;
                //menuComponents[previousSelectedIndex].focusLost();
                menuComponents[selectedIndex].focusEntered();
                previousSelectedIndex = selectedIndex;
            } else {
                showMouseHighlightedMenuItemImage = false;
                showMouseClickedMenuItemImage = false;
            }
        } catch (NullPointerException e) {  // this is done to prevent startup failure
            //e.printStackTrace();
        }
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
        showMouseHighlightedMenuItemImage = false;
    }
}

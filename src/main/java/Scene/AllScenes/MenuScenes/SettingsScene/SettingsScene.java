package Scene.AllScenes.MenuScenes.SettingsScene;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import MenuSystem.Form.Form;
import Scene.AllScenes.MenuScenes.MainMenu.Component.Button_ToMenu;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;
import Scene.AllScenes.MenuScenes.SettingsScene.Component.Button_Audio;
import Scene.AllScenes.MenuScenes.SettingsScene.Component.Button_Control;
import Scene.AllScenes.MenuScenes.SettingsScene.Component.Button_Gameplay;
import Scene.AllScenes.MenuScenes.SettingsScene.Component.Button_Graphics;
import Scene.Scene;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 09/10/2014.
 * This class contains the scene of settings.
 */
public class SettingsScene extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(153, 217, 234);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(232, 153, 234);

    private static final int totalMenuItems = 5;

    private static Form sceneForm;

    public SettingsScene() {
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        AllSettings.saveUserData();

        if (thisSceneEverCreated) {
            if (sceneForm.getSelectedIndex() == totalMenuItems - 1) sceneForm.reset();
            else sceneForm.resetMouseAlignment();
            if (AllSettings.userSettings.cinematicEffectEnabled)
                RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
            else RenderGraphics.setAlphaOpaque(gradientBackground);
            return;
        }

        GradientColor.createGradientBackground_alongX(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);
        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
        else RenderGraphics.setAlphaOpaque(gradientBackground);

        sceneForm = new Form(new MainMenu(), Strings.MainMenu.Settings, totalMenuItems);
        createMenuComponents();

        thisSceneEverCreated = true;
    }

    private void createMenuComponents() {
        sceneForm.addMenuItem(new Button_Graphics(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_Audio(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_Gameplay(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_Control(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_ToMenu(sceneForm.getPositionIndex()));
    }

    public void update() {
        sceneForm.update();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

        sceneForm.render(unscaledScreenGraphics);

        g.scale(AllSettings.userSettings.displayScale, AllSettings.userSettings.displayScale);
        g.drawImage(unscaledScreenImage, 0, 0, null);
    }

    public void keyPressed(KeyEvent e) {
        sceneForm.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        sceneForm.keyReleased(e);
    }

    public void mousePressed(MouseEvent e) {
        sceneForm.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        sceneForm.mouseReleased(e);
    }

    public void mouseMoved(MouseEvent e) {
        sceneForm.mouseMoved(e);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        sceneForm.mouseWheelMoved(e);
    }

    public void mouseEntered(MouseEvent e) {
        sceneForm.mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        sceneForm.mouseExited(e);
    }

    public void mouseDragged(MouseEvent e) {
        sceneForm.mouseDragged(e);
    }

    public void focusGained(FocusEvent e) {
        sceneForm.focusGained(e);
    }

    public void focusLost(FocusEvent e) {
        sceneForm.focusLost(e);
    }

    public void reset() {
        sceneForm.resetMouseAlignment();
    }

}

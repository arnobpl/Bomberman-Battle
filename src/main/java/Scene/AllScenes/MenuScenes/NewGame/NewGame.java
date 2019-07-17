package Scene.AllScenes.MenuScenes.NewGame;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import MenuSystem.Form.Form;
import Scene.AllScenes.MenuScenes.MainMenu.Component.Button_ToMenu;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;
import Scene.AllScenes.MenuScenes.NewGame.Component.Button_CreateGame;
import Scene.AllScenes.MenuScenes.NewGame.Component.Button_JoinGame;
import Scene.AllScenes.MenuScenes.NewGame.Component.Button_OfflineGame;
import Scene.Scene;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 03/11/2014.
 * The scene is shown when "New Game" is selected
 */
public class NewGame extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorLeft = new java.awt.Color(153, 217, 234);
    private static final java.awt.Color gradientBackgroundColorRight = new java.awt.Color(232, 153, 234);

    private static final int totalMenuItems = 4;

    private static Form sceneForm;

    public NewGame() {
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        if (thisSceneEverCreated) {
            if (sceneForm.getSelectedIndex() == totalMenuItems - 1) sceneForm.reset();
            else sceneForm.resetMouseAlignment();
            if (AllSettings.userSettings.cinematicEffectEnabled)
                RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
            else RenderGraphics.setAlphaOpaque(gradientBackground);
            return;
        }

        GradientColor.createGradientBackground_alongX(gradientBackground, gradientBackgroundColorLeft, gradientBackgroundColorRight);
        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
        else RenderGraphics.setAlphaOpaque(gradientBackground);

        sceneForm = new Form(new MainMenu(), Strings.MainMenu.NewGame, totalMenuItems);
        createMenuComponents();

        thisSceneEverCreated = true;
    }

    private void createMenuComponents() {
        sceneForm.addMenuItem(new Button_CreateGame(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_JoinGame(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_OfflineGame(sceneForm.getPositionIndex()));
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

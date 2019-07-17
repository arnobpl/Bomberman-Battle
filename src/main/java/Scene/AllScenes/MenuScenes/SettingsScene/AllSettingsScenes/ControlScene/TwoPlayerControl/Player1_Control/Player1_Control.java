package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Player1_Control;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import FontRenderer.FontColor;
import FontRenderer.RenderString;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import MenuSystem.Form.Form;
import MenuSystem.Form.MenuItems.ControlChooser;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Component.Button_ToOfflinePlayerControl;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.Player1_Control.Component.*;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.ControlScene.TwoPlayerControl.TwoPlayerControl;
import Scene.Scene;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 17/02/2015.
 * Scene for 2nd player control
 */
public class Player1_Control extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    public static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(153, 217, 234);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(232, 153, 234);

    private static final int arrowMinDistance = ControlChooser.width;

    private static final int totalMenuItems = 6;

    private static Form sceneForm;

    private static final BufferedImage informationTextImage = RenderString.renderSmallString(Strings.ControlSettings.Information, FontColor.Orange);
    private static final int informationTextLeft = (AllSettings.unscaledWidth - informationTextImage.getWidth()) >> 1;
    private static final int informationTextTop = AllSettings.unscaledHeight - (AllSettings.unscaledHeight_half >> 3);


    public Player1_Control() {
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        if (thisSceneEverCreated) {
            sceneForm.reset();
            if (AllSettings.userSettings.cinematicEffectEnabled)
                RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
            else RenderGraphics.setAlphaOpaque(gradientBackground);
            sceneForm.clearMenuItems();
            createMenuComponents(); // this is called because ControlChooser can be in the invalid state when Enter and then Key_Escape is pressed
            return;
        }

        GradientColor.createGradientBackground_alongX(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);
        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
        else RenderGraphics.setAlphaOpaque(gradientBackground);

        sceneForm = new Form(new TwoPlayerControl(), Strings.ControlSettings.Player1, totalMenuItems, arrowMinDistance, -1);
        createMenuComponents();

        thisSceneEverCreated = true;
    }

    private void createMenuComponents() {
        sceneForm.addMenuItem(new Control_Up(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Control_Down(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Control_Left(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Control_Right(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Control_Fire(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_ToOfflinePlayerControl(sceneForm.getPositionIndex()));
    }

    public void update() {
        sceneForm.update();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

        unscaledScreenGraphics.drawImage(informationTextImage, informationTextLeft, informationTextTop, null);
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

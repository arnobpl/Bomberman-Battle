package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import MenuSystem.Form.Form;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.Component.Option_CinematicEffect;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.Component.Option_DisplayScale;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.Component.Option_HighQualityRendering;
import Scene.AllScenes.MenuScenes.SettingsScene.Component.Button_ToSettingsScene;
import Scene.AllScenes.MenuScenes.SettingsScene.SettingsScene;
import Scene.Scene;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 05/02/2015.
 * This class contains the scene of graphics settings.
 */
public class GraphicsScene extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    public static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    public static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    public static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(153, 217, 234);
    public static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(232, 153, 234);

    private static final int arrowMinDistance = OptionChooser.width;

    private static final int totalMenuItems = 4; //5;

    public static Form sceneForm;

    public static boolean restartNeeded = false;
//    private static final BufferedImage restartPromptTextImage = RenderString.renderSmallString(Strings.GraphicsSettings.RestartGameFPS, FontColor.Orange);
//    private static final int restartPromptTextLeft = (AllSettings.unscaledWidth - restartPromptTextImage.getWidth()) >> 1;
//    private static final int restartPromptTextTop = AllSettings.unscaledHeight - (AllSettings.unscaledHeight_half >> 3);


    public GraphicsScene() {
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        if (thisSceneEverCreated) {
            sceneForm.reset();
            if (AllSettings.userSettings.cinematicEffectEnabled)
                RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
            else RenderGraphics.setAlphaOpaque(gradientBackground);
            return;
        }

        GradientColor.createGradientBackground_alongY(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);
        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
        else RenderGraphics.setAlphaOpaque(gradientBackground);

        sceneForm = new Form(new SettingsScene(), Strings.Settings.Graphics, totalMenuItems, arrowMinDistance);
        createMenuComponents();

        thisSceneEverCreated = true;
    }

    private void createMenuComponents() {
        sceneForm.addMenuItem(new Option_DisplayScale(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Option_HighQualityRendering(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Option_CinematicEffect(sceneForm.getPositionIndex()));
//        sceneForm.addMenuItem(new Option_DesiredFPS(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_ToSettingsScene(sceneForm.getPositionIndex()));
    }

    public void update() {
        sceneForm.update();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

//        if (restartNeeded) {
//            unscaledScreenGraphics.drawImage(restartPromptTextImage, restartPromptTextLeft, restartPromptTextTop, null);
//        }
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

package Scene.AllScenes.MenuScenes.OfflineGame;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import GraphicsRenderer.Animation.Transition_Animation;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import MenuSystem.Form.Form;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;
import Scene.AllScenes.MenuScenes.NewGame.Component.Button_ToNewGame;
import Scene.AllScenes.MenuScenes.NewGame.NewGame;
import Scene.AllScenes.MenuScenes.OfflineGame.Component.Button_Play;
import Scene.AllScenes.MenuScenes.OfflineGame.Component.Option_ai_playerDifficulty;
import Scene.AllScenes.MenuScenes.OfflineGame.Component.Option_humanPlayer;
import Scene.AllScenes.MenuScenes.OfflineGame.Component.Option_totalPlayer;
import Scene.Scene;
import SoundSystem.BaseSound;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 17/04/2015.
 * The scene is shown when "Offline Game" is selected
 */
public class OfflineGame extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(153, 217, 234);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(232, 153, 234);

    private static final int arrowMinDistance = OptionChooser.width;

    private static final int totalMenuItems = 5;

    private static Form sceneForm;

    private static final int transitionTimeElapsed = 1000;
    private static final double transitionAlphaStep = 10 * AllSettings.userSettings.motionConst;

    public static Transition_Animation transitionAnimation;

    public OfflineGame() {
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        transitionAnimation = new Transition_Animation(transitionTimeElapsed, transitionAlphaStep);
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

        sceneForm = new Form(new NewGame(), Strings.NewGame.OfflineGame, totalMenuItems, arrowMinDistance);
        createMenuComponents();

        thisSceneEverCreated = true;
    }

    private void createMenuComponents() {
        sceneForm.addMenuItem(new Option_totalPlayer(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Option_humanPlayer(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Option_ai_playerDifficulty(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_Play(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_ToNewGame(sceneForm.getPositionIndex()));
    }

    public void update() {
        sceneForm.update();
        transitionAnimation.tick();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

        sceneForm.render(unscaledScreenGraphics);

        if (transitionAnimation.render(unscaledScreenGraphics)) {
            MainMenu.menuMusic.stop(BaseSound.fadeoutElapsed);
            BaseWindow.scene = new MainGame(false);
        }

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

package Scene.AllScenes.MenuScenes.OnlineGame.JoinGame;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import FontRenderer.FontColor;
import FontRenderer.RenderString;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import MenuSystem.Form.Form;
import MenuSystem.Form.MenuItems.OptionChooser;
import Network.BaseNetwork.NetworkTask;
import Scene.AllScenes.MenuScenes.NewGame.Component.Button_ToNewGame;
import Scene.AllScenes.MenuScenes.NewGame.NewGame;
import Scene.AllScenes.MenuScenes.OnlineGame.JoinGame.Component.Button_Play;
import Scene.AllScenes.MenuScenes.OnlineGame.JoinGame.Component.TextInput_GetIP;
import Scene.Scene;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 08/10/2014.
 * This class contains the scene of joining a game.
 */
public class JoinGame extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(153, 217, 234);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(232, 153, 234);

    private static final int arrowMinDistance = OptionChooser.width;

    private static final int totalMenuItems = 3;

    private static Form sceneForm;

    public static boolean invalidInput = false;
    private static final BufferedImage invalidInputImage = RenderString.renderBigString(Strings.JoinGame.InvalidInput, FontColor.Red);
    private static final int invalidInputImageLeft = AllSettings.unscaledWidth_half - (invalidInputImage.getWidth() >> 1);
    private static final int invalidInputImageTop = AllSettings.unscaledHeight - invalidInputImage.getHeight() - 1;

    public static TextInput_GetIP textInput_getIP;

    public JoinGame() {
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        if (thisSceneEverCreated) {
            sceneForm.reset();
            if (invalidInput) {
                textInput_getIP.setText(AllSettings.userSettings.UserIP);
                invalidInput = false;
            } else if (!NetworkTask.isValid_IP4_address(textInput_getIP.getText())) {
                textInput_getIP.setText(AllSettings.userSettings.UserIP);
            }
            if (AllSettings.userSettings.cinematicEffectEnabled)
                RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
            else RenderGraphics.setAlphaOpaque(gradientBackground);
            return;
        }

        GradientColor.createGradientBackground_alongY(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);

        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
        else RenderGraphics.setAlphaOpaque(gradientBackground);

        sceneForm = new Form(new NewGame(), Strings.NewGame.JoinGame, totalMenuItems, arrowMinDistance);
        createMenuComponents();

        thisSceneEverCreated = true;
    }

    private void createMenuComponents() {
        textInput_getIP = new TextInput_GetIP(sceneForm.getPositionIndex());
        sceneForm.addMenuItem(textInput_getIP);
        sceneForm.addMenuItem(new Button_Play(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_ToNewGame(sceneForm.getPositionIndex()));
    }

    public void update() {
        sceneForm.update();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

        sceneForm.render(unscaledScreenGraphics);

        if (invalidInput)
            unscaledScreenGraphics.drawImage(invalidInputImage, invalidInputImageLeft, invalidInputImageTop, null);

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

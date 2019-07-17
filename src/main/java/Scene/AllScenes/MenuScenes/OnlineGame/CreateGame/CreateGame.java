package Scene.AllScenes.MenuScenes.OnlineGame.CreateGame;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import FontRenderer.FontColor;
import FontRenderer.FontFromFile;
import FontRenderer.RenderString;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import MenuSystem.Form.Form;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.MenuScenes.NewGame.Component.Button_ToNewGame;
import Scene.AllScenes.MenuScenes.NewGame.NewGame;
import Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.Component.Button_Play;
import Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.Component.Option_ConnectionType;
import Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.Component.Option_humanPlayer;
import Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.Component.Option_totalPlayer;
import Scene.Scene;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Arnob on 08/10/2014.
 * This class contains the scene of creating a game.
 */

public class CreateGame extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(153, 217, 234);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(232, 153, 234);

    private static final int arrowMinDistance = OptionChooser.width;

    private static final int totalMenuItems = 5;

    private static Form sceneForm;

    public static final BufferedImage yourIP_Image = RenderString.renderSmallString(Strings.CreateGame.YourIP_Address, FontColor.Green);
    public static final int yourIP_ImageLeft = (AllSettings.unscaledWidth - yourIP_Image.getWidth()) >> 1;
    public static final int yourIP_ImageTop = AllSettings.unscaledHeight - ((FontFromFile.smallFontHeight + 1) << 1);

    public static BufferedImage hostIP_textImage;
    public static int hostIP_textImageLeft = AllSettings.unscaledWidth_half;
    public static final int hostIP_textImageTop = AllSettings.unscaledHeight - FontFromFile.smallFontHeight - 1;

    public static int networkConnectionType = 0;

    public static Option_totalPlayer option_totalPlayer;
    public static Option_humanPlayer option_humanPlayer;

    public CreateGame() {
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        if (thisSceneEverCreated) {
            sceneForm.reset();
            getHostAddressTextImage();
            if (AllSettings.userSettings.cinematicEffectEnabled)
                RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
            else RenderGraphics.setAlphaOpaque(gradientBackground);
            return;
        }

        GradientColor.createGradientBackground_alongY(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);
        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
        else RenderGraphics.setAlphaOpaque(gradientBackground);

        sceneForm = new Form(new NewGame(), Strings.NewGame.CreateGame, totalMenuItems, arrowMinDistance);
        createMenuComponents();

        getHostAddressTextImage();

        thisSceneEverCreated = true;
    }

    private void getHostAddressTextImage() {
        try {
            hostIP_textImage = RenderString.renderSmallString(InetAddress.getLocalHost().getHostAddress(), FontColor.Blue);
            hostIP_textImageLeft = (AllSettings.unscaledWidth - hostIP_textImage.getWidth()) >> 1;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            hostIP_textImage = RenderString.renderSmallString(Strings.CreateGame.IP_AddressRetrieveError, FontColor.Red);
            hostIP_textImageLeft = (AllSettings.unscaledWidth - hostIP_textImage.getWidth()) >> 1;
        }
    }

    private void createMenuComponents() {
        option_humanPlayer = new Option_humanPlayer(sceneForm.getPositionIndex());
        sceneForm.addMenuItem(option_humanPlayer);
        option_totalPlayer = new Option_totalPlayer(sceneForm.getPositionIndex());
        sceneForm.addMenuItem(option_totalPlayer);
        sceneForm.addMenuItem(new Option_ConnectionType(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_Play(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_ToNewGame(sceneForm.getPositionIndex()));
    }

    public void update() {
        sceneForm.update();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

        unscaledScreenGraphics.drawImage(yourIP_Image, yourIP_ImageLeft, yourIP_ImageTop, null);
        unscaledScreenGraphics.drawImage(hostIP_textImage, hostIP_textImageLeft, hostIP_textImageTop, null);
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

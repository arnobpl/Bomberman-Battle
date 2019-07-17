package Scene.AllScenes.MenuScenes.MainMenu;

import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import AppInfo.Strings;
import GameObject.Player.PlayerEnum.Color;
import GameObject.Player.PlayerEnum.Direction;
import GraphicsRenderer.Animation.PingPong_Animation;
import GraphicsRenderer.GfxFromFile;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import MenuSystem.Form.Form;
import Scene.AllScenes.MenuScenes.MainMenu.Component.*;
import Scene.Scene;
import SoundSystem.BaseSound;
import SoundSystem.LoopingSound;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 08/10/2014.
 * This class contains the main menu scene.
 */
public class MainMenu extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(153, 217, 234);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(232, 153, 234);

    private static final int totalMenuItems = 5;

    private static Form sceneForm;

    private static final int allBombermanTop = AllSettings.unscaledHeight - GfxFromFile.playerHeight - 2;
    private static final double allBombermanAnimationStep = -2 * AllSettings.userSettings.motionConst;
    private static final int eachBombermanLeftInterval = GfxFromFile.playerDirection_Width << 1;
    private static final int allBombermanAnimationUpperLimitExtendedPart = Color.totalColor * eachBombermanLeftInterval;
    private static final int allBombermanAnimationUpperLimit = AllSettings.unscaledWidth + allBombermanAnimationUpperLimitExtendedPart + GfxFromFile.playerDirection_Width;

    private static int allBombermanDirectionValue;
    private static final Direction[] allBombermanDirectionList = {Direction.Left, Direction.Right};
    private static PingPong_Animation allBombermanAnimation;

    private static final double eachBombermanAnimationStep = 0.4 * AllSettings.userSettings.motionConst;
    private static final int eachBombermanAnimationUpperLimit = 2;

    private static final PingPong_Animation[] eachBombermanAnimationList = new PingPong_Animation[Color.totalColor];

    public static final LoopingSound menuMusic = new LoopingSound(ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InMenu.MenuMusic), AllSettings.userSettings.backgroundMusicVolume);

    public MainMenu() {
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        menuMusic.playAfterSetVolume(AllSettings.userSettings.backgroundMusicVolume, BaseSound.fadeinElapsed);

        if (thisSceneEverCreated) {
            sceneForm.resetMouseAlignment();
            if (AllSettings.userSettings.cinematicEffectEnabled)
                RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
            else RenderGraphics.setAlphaOpaque(gradientBackground);
            return;
        }

        GradientColor.createGradientBackground_alongY(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);
        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
        else RenderGraphics.setAlphaOpaque(gradientBackground);

        sceneForm = new Form(null, Strings.WindowTitle, totalMenuItems);
        createMenuComponents();

        allBombermanDirectionValue = 0;
        allBombermanAnimation = new PingPong_Animation(allBombermanAnimationUpperLimit, allBombermanAnimationStep, allBombermanAnimationUpperLimit);

        double eachBombermanInitialAnimateValueStep = (eachBombermanAnimationUpperLimit + 1.0) / eachBombermanAnimationList.length;
        for (int i = 0; i < eachBombermanAnimationList.length; i++) {
            eachBombermanAnimationList[i] = new PingPong_Animation(eachBombermanAnimationUpperLimit, eachBombermanAnimationStep, i * eachBombermanInitialAnimateValueStep);
        }

        thisSceneEverCreated = true;
    }

    private void createMenuComponents() {
        sceneForm.addMenuItem(new Button_NewGame(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_HowToPlay(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_Settings(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_Credit(sceneForm.getPositionIndex()));
        sceneForm.addMenuItem(new Button_Exit(sceneForm.getPositionIndex()));
    }

    public void update() {
        sceneForm.update();

        allBombermanAnimation.animate();
        for (PingPong_Animation i : eachBombermanAnimationList) i.animate();

        if (allBombermanAnimation.isCycleCompleted()) {
            allBombermanDirectionValue = (allBombermanDirectionValue + 1) & 1;
        }
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

        for (int i = 0; i < eachBombermanAnimationList.length; i++) {
            unscaledScreenGraphics.drawImage(Initialized.gfx.player(Color.valuesCached()[i], allBombermanDirectionList[allBombermanDirectionValue], eachBombermanAnimationList[i].getAnimateValue()), allBombermanAnimation.getAnimateValue() - allBombermanAnimationUpperLimitExtendedPart + i * eachBombermanLeftInterval, allBombermanTop + ((i & 1) << 1), null);
        }

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

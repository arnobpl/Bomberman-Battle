package Scene.AllScenes.MenuScenes.OnlineGame.CreateGame;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import GraphicsRenderer.Animation.TitleString_Animation;
import GraphicsRenderer.Animation.Transition_Animation;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import Network.BaseNetwork.InitializeGameNet.InitializeCreateGameNet;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;
import Scene.Scene;
import SoundSystem.BaseSound;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 07/11/2014.
 * This scene is displayed when "Play" is selected in CreateGame scene.
 */

public class WaitingScene extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(147, 147, 234);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(232, 147, 234);

    private static final double titleStringMoveStep = -3 * AllSettings.userSettings.motionConst;
    private static final int titleStringTop = AllSettings.unscaledHeight_half;

    private static TitleString_Animation titleStringAnimation;

    private static final int transitionTimeElapsed = 1000;
    private static final double transitionAlphaStep = 10 * AllSettings.userSettings.motionConst;

    private static Transition_Animation transitionAnimation;


    public WaitingScene() {
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        transitionAnimation = new Transition_Animation(transitionTimeElapsed, transitionAlphaStep);
        if (!thisSceneEverCreated) {
            GradientColor.createGradientBackground_alongY(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);
            titleStringAnimation = new TitleString_Animation(Strings.NewGame.PleaseWait, titleStringMoveStep, titleStringTop);
            thisSceneEverCreated = true;
        }

        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
        else RenderGraphics.setAlphaOpaque(gradientBackground);
    }

    public void update() {
        titleStringAnimation.animate();

        if (InitializeCreateGameNet.isReady()) {   // true when all are ready, from network static function
            transitionAnimation.start();
        }

        transitionAnimation.tick();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

        titleStringAnimation.render(unscaledScreenGraphics);

        unscaledScreenGraphics.drawImage(CreateGame.yourIP_Image, CreateGame.yourIP_ImageLeft, CreateGame.yourIP_ImageTop, null);
        unscaledScreenGraphics.drawImage(CreateGame.hostIP_textImage, CreateGame.hostIP_textImageLeft, CreateGame.hostIP_textImageTop, null);

        if (transitionAnimation.render(unscaledScreenGraphics)) {
            MainMenu.menuMusic.stop(BaseSound.fadeoutElapsed);
            BaseWindow.scene = new MainGame(true);
        }

        g.scale(AllSettings.userSettings.displayScale, AllSettings.userSettings.displayScale);
        g.drawImage(unscaledScreenImage, 0, 0, null);
    }
}

package Scene.AllScenes.MenuScenes;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.Initialized;
import AppInfo.Strings;
import FontRenderer.FontColor;
import FontRenderer.RenderString;
import GraphicsRenderer.Animation.Transition_Animation;
import GraphicsRenderer.GfxFromFile;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import Network.BaseNetwork.InitializeGameNet.InitializeJoinGameNet;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;
import Scene.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Arnob on 31/01/2015.
 * This scene is shown when compatibility problem occurs.
 */
public class CompatibilityErrorScene extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(170, 170, 255);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(255, 170, 210);

    private static final BufferedImage titleStringImage = RenderString.renderBigString(Strings.Error.CompatibilityError, FontColor.Red);
    private static final int titleStringLeft = (AllSettings.unscaledWidth - titleStringImage.getWidth()) >> 1;
    private static final int titleStringTop = AllSettings.unscaledHeight_half;

    private static final int compatibilityErrorImageLeft = (AllSettings.unscaledWidth - GfxFromFile.compatibilityError_Width) >> 1;
    private static final int compatibilityErrorImageTop = ((AllSettings.unscaledHeight >> 1) - GfxFromFile.compatibilityError_Width) >> 1;

    private static final int sceneTimeElapsed = 4000;
    private static final int transitionTimeElapsed = 1000;
    private static final double transitionAlphaStep = 10 * AllSettings.userSettings.motionConst;

    private static Transition_Animation transitionAnimation;


    private static class CloseAllConnectionWhenNotCompatible implements Runnable {
        public CloseAllConnectionWhenNotCompatible() {
            new Thread(this).start();
        }

        public void run() {
            try {
                InitializeJoinGameNet.socket.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public CompatibilityErrorScene() {
        new CloseAllConnectionWhenNotCompatible();
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        transitionAnimation = new Transition_Animation(transitionTimeElapsed, transitionAlphaStep, sceneTimeElapsed).start();
        if (!thisSceneEverCreated) {
            GradientColor.createGradientBackground_alongY(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);
            thisSceneEverCreated = true;
        }

        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
        else RenderGraphics.setAlphaOpaque(gradientBackground);
    }

    public void update() {
        transitionAnimation.tick();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

        unscaledScreenGraphics.drawImage(titleStringImage, titleStringLeft, titleStringTop, null);

        unscaledScreenGraphics.drawImage(Initialized.gfx.compatibilityError(), compatibilityErrorImageLeft, compatibilityErrorImageTop, null);

        if (transitionAnimation.render(unscaledScreenGraphics)) {
            BaseWindow.scene = new MainMenu();
        }

        g.scale(AllSettings.userSettings.displayScale, AllSettings.userSettings.displayScale);
        g.drawImage(unscaledScreenImage, 0, 0, null);
    }
}

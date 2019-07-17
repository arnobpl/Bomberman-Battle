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
import Network.BaseNetwork.BaseNetwork;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;
import Scene.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 17/11/2014.
 * This scene is shown when some network problem occurs.
 */
public class ConnectionErrorScene extends Scene {
    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(170, 170, 255);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(255, 170, 210);

    private static final BufferedImage titleStringImage = RenderString.renderBigString(Strings.Error.ConnectionFailure, FontColor.Red);
    private static final int titleStringLeft = (AllSettings.unscaledWidth - titleStringImage.getWidth()) >> 1;
    private static final int titleStringTop = AllSettings.unscaledHeight_half;

    private static final int networkErrorImageLeft = (AllSettings.unscaledWidth - GfxFromFile.networkError_Width) >> 1;
    private static final int networkErrorImageTop = ((AllSettings.unscaledHeight >> 1) - GfxFromFile.networkError_Width) >> 1;

    private static final int sceneTimeElapsed = 5000;
    private static final int transitionTimeElapsed = 1000;
    private static final double transitionAlphaStep = 10 * AllSettings.userSettings.motionConst;

    private static Transition_Animation transitionAnimation;

    private static BufferedImage errorStringImage;
    private static int errorStringLeft;
    private static final int errorStringTop = AllSettings.unscaledHeight - (AllSettings.unscaledHeight_half >> 1);


    public ConnectionErrorScene(Exception exception) {
        BaseNetwork.closeAllConnection();

        String errorString;
        if (!(exception instanceof NullPointerException)) {
            errorString = exception.toString();
            int beginIndex = errorString.indexOf(':');
            int endIndex = errorString.lastIndexOf(':');
            if (beginIndex != -1) {
                if (beginIndex != endIndex) errorString = errorString.substring(beginIndex + 2, endIndex);
                else errorString = errorString.substring(beginIndex + 2);
            }
        } else {
            errorString = Strings.Error.ConnectionReset;
        }
        errorStringImage = RenderString.renderSmallString(errorString, FontColor.Brown);
        errorStringLeft = (AllSettings.unscaledWidth - errorStringImage.getWidth()) >> 1;

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
        unscaledScreenGraphics.drawImage(errorStringImage, errorStringLeft, errorStringTop, null);

        unscaledScreenGraphics.drawImage(Initialized.gfx.networkError(), networkErrorImageLeft, networkErrorImageTop, null);

        if (transitionAnimation.render(unscaledScreenGraphics)) {
            BaseWindow.scene = new MainMenu();
        }

        g.scale(AllSettings.userSettings.displayScale, AllSettings.userSettings.displayScale);
        g.drawImage(unscaledScreenImage, 0, 0, null);
    }
}

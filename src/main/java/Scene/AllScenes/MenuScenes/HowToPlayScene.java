package Scene.AllScenes.MenuScenes;

import AppInfo.Customization.AllSettings;
import AppInfo.Initialized;
import AppInfo.Strings;
import FontRenderer.FontColor;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import MenuSystem.TextInfoForm;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;
import Scene.Scene;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 09/10/2014.
 * This class contains the scene of playing rules.
 */
public class HowToPlayScene extends Scene {

    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(153, 217, 234);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(232, 153, 234);

//    private static final int textInfoGradientBackgroundEnlargeLength = 1;
//    static final BufferedImage textInfoGradientBackground = new BufferedImage(TextInfoForm.infoTextImageWidth + (textInfoGradientBackgroundEnlargeLength << 1), TextInfoForm.totalLinesInEachPage * TextInfoForm.eachLineInfoTextHeight + (textInfoGradientBackgroundEnlargeLength << 1), BufferedImage.TYPE_INT_ARGB);
//    private static final java.awt.Color textInfoGradientBackgroundColorUp = new java.awt.Color(128, 255, 128);
//    private static final java.awt.Color textInfoGradientBackgroundColorDown = new java.awt.Color(239, 228, 176);
//    private static final int textInfoGradientBackgroundTop = TextInfoForm.infoTextImageTop - textInfoGradientBackgroundEnlargeLength;
//    private static final int textInfoGradientBackgroundLeft = TextInfoForm.infoTextImageLeft - textInfoGradientBackgroundEnlargeLength;


    private static TextInfoForm sceneForm;

    private static final FontColor infoTextFontColor = FontColor.Orange;

    private static final String infoText = Initialized.HowToPlayInfoFromFile;


    public HowToPlayScene() {
        unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);    // to remove previous state if cinematicEffect is turned on

        if (thisSceneEverCreated) {
            sceneForm.resetScrolling();
            if (AllSettings.userSettings.cinematicEffectEnabled) {
                RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
                //RenderGraphics.setAlpha(textInfoGradientBackground, AllSettings.motionBlurAlpha);
            } else {
                RenderGraphics.setAlphaOpaque(gradientBackground);
                //RenderGraphics.setAlphaOpaque(textInfoGradientBackground);
            }
            return;
        }

        GradientColor.createGradientBackground_alongX(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);
        //GradientColor.createGradientBackground_alongY(textInfoGradientBackground, textInfoGradientBackgroundColorUp, textInfoGradientBackgroundColorDown);
        if (AllSettings.userSettings.cinematicEffectEnabled) {
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
            //RenderGraphics.setAlpha(textInfoGradientBackground, AllSettings.motionBlurAlpha);
        } else {
            RenderGraphics.setAlphaOpaque(gradientBackground);
            //RenderGraphics.setAlphaOpaque(textInfoGradientBackground);
        }

        sceneForm = new TextInfoForm(new MainMenu(), Strings.MainMenu.HowToPlay, infoText, infoTextFontColor, false);

        thisSceneEverCreated = true;
    }

    public void update() {
        sceneForm.update();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);
        //unscaledScreenGraphics.drawImage(textInfoGradientBackground, textInfoGradientBackgroundLeft, textInfoGradientBackgroundTop, null);

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

    public void focusGained(FocusEvent e) {
        sceneForm.focusGained(e);
    }

    public void focusLost(FocusEvent e) {
        sceneForm.focusLost(e);
    }
}

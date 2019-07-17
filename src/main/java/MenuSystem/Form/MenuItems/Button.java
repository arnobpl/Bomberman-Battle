package MenuSystem.Form.MenuItems;

import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import FontRenderer.FontColor;
import FontRenderer.RenderString;
import GraphicsRenderer.Animation.PingPong_Animation;
import SoundSystem.BaseSound;
import javafx.scene.media.Media;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 01/11/2014.
 * Class for standard button that can perform specific action
 */
public class Button implements MenuSystem.Form.MenuItems.MenuItem {
    public static final int width = AllSettings.unscaledWidth_half;    // by default, it is 152

    public static final int left = (AllSettings.unscaledWidth - width) >> 1;
    public final int top;

    private BufferedImage textImage;
    private int textImageLeft = left;
    private int textImageTop = textTop;

    private static final int leftRight_AnimationAmplitude = 2;
    private static final double leftRight_AnimationStep = 0.5 * AllSettings.userSettings.motionConst;

    private static final int animationUpperLimit = leftRight_AnimationAmplitude << 1;

    private PingPong_Animation buttonAnimation;
    private int leftOffset = 0;     // this is for animation through x-axis

    private boolean actionShouldBePerformed = false;

    private static final Media selectSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InMenu.MenuSelect);


    public Button(String text, int positionIndex, boolean ashColorNeeded)   // positionIndex range [1, MenuItem.maxIndex]
    {
        FontColor color;
        if (ashColorNeeded) color = FontColor.Ash;
        else {
            color = FontColor.valuesCached()[positionIndex % (FontColor.totalColor - 1) + 1]; // this can be all colors except ash (value 0)
        }
        textImage = RenderString.renderBigString(text, color);

        textImageLeft = left + ((width - textImage.getWidth()) >> 1);
        top = positionIndex * height;
        textImageTop = top + textTop;

        buttonAnimation = new PingPong_Animation(animationUpperLimit, leftRight_AnimationStep, Initialized.random.nextInt(animationUpperLimit + 1));
    }

    public final void update() {
        buttonAnimation.animate();
        leftOffset = leftRight_AnimationAmplitude - buttonAnimation.getAnimateValue();
    }

    public final void render(Graphics2D g) {
        g.drawImage(textImage, textImageLeft + leftOffset, textImageTop, null);

        if (actionShouldBePerformed) {
            performAction();    // This is called here because it may contain scene change.
            actionShouldBePerformed = false;
        }
    }

    public final void keyPressed(KeyEvent e) {
    }

    public final void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == AllSettings.Key_MenuEnter) {
            actionShouldBePerformed = true; // "performAction()" is not called here because it may contain scene change.

            BaseSound.playSound(selectSound, AllSettings.userSettings.soundEffectVolume);
        }
    }

    /**
     * This method is called when focus is entered.
     */
    public void focusEntered() {
    }

    /**
     * This method is called when focus is lost.
     */
    public void focusLost() {
    }

    /**
     * This method is called when <code>Enter</code> key is released.
     */
    public void performAction() {
    }

}

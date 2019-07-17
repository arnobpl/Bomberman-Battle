package MenuSystem.Form.MenuItems;

import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import AppInfo.Strings;
import FontRenderer.FontColor;
import FontRenderer.RenderString;
import GraphicsRenderer.Animation.PingPong_Animation;
import SoundSystem.BaseSound;
import javafx.scene.media.Media;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 07/02/2015.
 * Class for control chooser that can used to get key input
 */
public class ControlChooser implements MenuItem {
    public static final int width = (AllSettings.unscaledWidth >> 3) * 5;  // by default, it is 190

    public static final int left = (AllSettings.unscaledWidth - width) >> 1;
    public final int top;

    private BufferedImage textImage;
    private int textImageLeft = left;
    private int textImageTop = textTop;

    private static final int leftRight_AnimationAmplitude = 2;
    private static final double leftRight_AnimationStep = 0.5 * AllSettings.userSettings.motionConst;

    private static final int animationUpperLimit = leftRight_AnimationAmplitude << 1;

    private PingPong_Animation textAnimation;
    private int leftOffset = 0;     // this is for animation through x-axis

    private boolean listenForKeyCode = false;

    private Integer keyCode = null;

    private static final FontColor keyCodeImageColor = FontColor.Ash;

    private BufferedImage keyCodeImage = null;
    private int keyCodeImageLeft;
    private int keyCodeImageTop;

    private static final int keyCodeImageEmptyLeft = left + (width >> 2) * 3;  // when keyCodeImage is empty

    private static final int[] specialKeyCode = {
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN,
            KeyEvent.VK_LEFT,
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_SPACE,
            KeyEvent.VK_ENTER,
            KeyEvent.VK_CONTROL,
            KeyEvent.VK_ALT,
            KeyEvent.VK_SHIFT,
            KeyEvent.VK_TAB,
            KeyEvent.VK_DELETE,
            KeyEvent.VK_BACK_SPACE,
            KeyEvent.VK_OPEN_BRACKET,
            KeyEvent.VK_CLOSE_BRACKET
    };

    private static final BufferedImage[] specialKeyCodeImage = {
            RenderString.renderBigString(Strings.ControlChooser.UpArow, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.DownArrow, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.LeftArrow, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.RightArrow, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.Space, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.Enter, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.Ctrl, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.Alt, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.Shift, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.Tab, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.Delete, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.Backspace, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.BraketOpening, keyCodeImageColor),
            RenderString.renderBigString(Strings.ControlChooser.BraketClosing, keyCodeImageColor)
    };

    private static final Media selectSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InMenu.MenuSelect);
    private static final Media keyPressSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InMenu.OptionScroll);


    private static final BufferedImage unknownKeyCodeImage = RenderString.renderBigString(Strings.ControlChooser.Unknown, keyCodeImageColor);   // this one is for unknown keyCode


    /**
     * @param text          the name of the <code>MenuItem</code> that will be displayed
     * @param positionIndex range [1, MenuSystem.maxIndex]
     * @param keyCode       initial key to be assigned and displayed. If nothing is passed, default null key will be assigned and displayed.
     */
    public ControlChooser(String text, int positionIndex, Integer keyCode) {
        FontColor color = FontColor.valuesCached()[positionIndex % (FontColor.totalColor - 1) + 1];   // this can be all colors except ash (value 0)
        textImage = RenderString.renderBigString(text, color);

        textImageLeft = left + (((width >> 1) - textImage.getWidth()) >> 1);
        top = positionIndex * height;
        textImageTop = top + textTop;
        keyCodeImageTop = textImageTop;

        setKeyCode(keyCode);

        textAnimation = new PingPong_Animation(animationUpperLimit, leftRight_AnimationStep, Initialized.random.nextInt(animationUpperLimit + 1));
    }

    /**
     * @param text          the name of the <code>MenuItem</code> that will be displayed
     * @param positionIndex range [1, MenuSystem.maxIndex]
     */
    public ControlChooser(String text, int positionIndex) {
        this(text, positionIndex, null);
    }

    public final void update() {
        textAnimation.animate();
        leftOffset = leftRight_AnimationAmplitude - textAnimation.getAnimateValue();
    }

    public final void render(Graphics2D g) {
        g.drawImage(textImage, textImageLeft + leftOffset, textImageTop, null);
        g.drawImage(keyCodeImage, keyCodeImageLeft + leftOffset, keyCodeImageTop, null);
    }

    public final void keyPressed(KeyEvent e) {
    }

    public final void keyReleased(KeyEvent e) {
        if (listenForKeyCode) {
            if (!filterThisKeyEvent(e, keyCode)) {
                setKeyCode(e.getKeyCode());
                BaseSound.playSound(keyPressSound, AllSettings.userSettings.soundEffectVolume);
            } else {
                doThisAfterKeyCodeFiltered(e, keyCode);
            }
            listenForKeyCode = false;
        } else if (e.getKeyCode() == AllSettings.Key_MenuEnter) {
            listenForKeyCode = true;
            keyCodeImage = Initialized.gfx.nullImage();
            BaseSound.playSound(selectSound, AllSettings.userSettings.soundEffectVolume);
        }
    }

    public final Integer getKeyCode() {
        return keyCode;
    }

    /**
     * Changes <code>keyCode</code> at any time
     *
     * @param keyCode the key to be assigned and displayed
     */
    public final void setKeyCode(Integer keyCode) {
        this.keyCode = keyCode;
        listenForKeyCode = false;
        checkingKeyCode:
        {
            if (keyCode == null) {
                keyCodeImage = unknownKeyCodeImage;
            } else {
                int i = 0;
                for (; i < specialKeyCode.length; i++) {
                    if (specialKeyCode[i] == keyCode) {
                        keyCodeImage = specialKeyCodeImage[i];
                        break checkingKeyCode;
                    }
                }
                keyCodeImage = Initialized.font.getBigFont((char) keyCode.intValue(), keyCodeImageColor);
                if (keyCodeImage == null) keyCodeImage = unknownKeyCodeImage;
            }
        }
        keyCodeImageLeft = keyCodeImageEmptyLeft - (keyCodeImage.getWidth() >> 1);
    }

    /**
     * Override this method to set filter text condition.
     *
     * @param e       the <code>KeyEvent</code> that is pressed
     * @param keyCode the <code>keyCode</code> that is currently assigned
     * @return <code>true</code> to ignore <code>KeyEvent</code>, otherwise <code>false</code>
     */
    public boolean filterThisKeyEvent(KeyEvent e, Integer keyCode) {
        return false;
    }

    /**
     * This method is called each time when any key press is filtered.
     *
     * @param e       the <code>KeyEvent</code> that is filtered
     * @param keyCode the <code>keyCode</code> that is currently assigned
     */
    public void doThisAfterKeyCodeFiltered(KeyEvent e, Integer keyCode) {
    }

    /**
     * This method is called when focus is entered.
     */
    public void focusEntered() {
    }

    /**
     * This method is called when focus is lost.
     */
    public final void focusLost() {
        setKeyCode(keyCode);
        performAction(keyCode);
    }

    /**
     * This method is called when focus is lost.
     */
    public void performAction(Integer keyCode) {
    }

}

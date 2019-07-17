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
 * Created by Arnob on 07/11/2014.
 * Class for standard text input
 */
public class TextInput implements MenuItem {
    public static final int width = (AllSettings.unscaledWidth >> 3) * 5;  // by default, it is 190

    public static final int left = (AllSettings.unscaledWidth - width) >> 1;
    public final int top;

    private boolean filteringTextNeeded = false;

    private StringBuilder text;
    private int maxTextLength = 0;


    private BufferedImage textImage;
    FontColor color;
    private int textImageLeft = AllSettings.unscaledWidth_half;
    private int textImageTop = textTop;

    private static final int leftRight_AnimationAmplitude = 2;
    private static final double leftRight_AnimationStep = 0.5 * AllSettings.userSettings.motionConst;

    private static final int animationUpperLimit = leftRight_AnimationAmplitude << 1;

    private PingPong_Animation textAnimation;
    private int leftOffset = 0;     // this is for animation through x-axis

    private static final Media typingSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InMenu.MenuScroll);


    /**
     * @param text                initial text
     * @param positionIndex       range [1, MenuSystem.maxIndex]
     * @param filteringTextNeeded must be <code>true</code> to filter text, otherwise no filtering will occur.
     */
    public TextInput(String text, int positionIndex, boolean filteringTextNeeded) {
        this.text = new StringBuilder(text);
        color = FontColor.valuesCached()[positionIndex % (FontColor.totalColor - 1) + 1];   // this can be all colors except ash (value 0)
        textImage = RenderString.renderBigString(text, color);

        textImageLeft = left + ((width - textImage.getWidth()) >> 1);
        top = positionIndex * height;
        textImageTop = top + textTop;

        this.filteringTextNeeded = filteringTextNeeded;

        textAnimation = new PingPong_Animation(animationUpperLimit, leftRight_AnimationStep, Initialized.random.nextInt(animationUpperLimit + 1));
    }

    /**
     * Here 0 means unlimited.
     */
    public final void setMaxTextLength(int maxTextLength) {
        if (maxTextLength >= 0) this.maxTextLength = maxTextLength;
    }

    public final void update() {
        textAnimation.animate();
        leftOffset = leftRight_AnimationAmplitude - textAnimation.getAnimateValue();
    }

    public final void render(Graphics2D g) {
        g.drawImage(textImage, textImageLeft + leftOffset, textImageTop, null);
    }

    /**
     * Override this method to set filter text condition.
     * <br/>
     * <code>filteringTextNeeded</code> must be <code>true</code> to filter text, otherwise no filtering will occur.
     * Backspace or delete key can be filtered too.
     *
     * @return <code>true</code> to ignore <code>KeyEvent</code>, otherwise <code>false</code>.
     */
    public boolean filterThisText(KeyEvent e, String text) {
        return false;
    }

    private void updateText(KeyEvent e) {
        if (e.getKeyCode() == AllSettings.Key_Backspace || e.getKeyCode() == AllSettings.Key_Delete) {
            if (text.length() > 0) {
                text.setLength(text.length() - 1);
                BaseSound.playSound(typingSound, AllSettings.userSettings.soundEffectVolume);
            }
        } else {
            text.append(e.getKeyChar());
            BaseSound.playSound(typingSound, AllSettings.userSettings.soundEffectVolume);
        }
        textImage = RenderString.renderBigString(text.toString(), color);
        textImageLeft = left + ((width - textImage.getWidth()) >> 1);
    }

    public final void keyPressed(KeyEvent e) {
        if (e.getKeyCode() != AllSettings.Key_MenuUp && e.getKeyCode() != AllSettings.Key_MenuDown && e.getKeyCode() != AllSettings.Key_MenuLeft && e.getKeyCode() != AllSettings.Key_MenuRight && e.getKeyCode() != AllSettings.Key_MenuEnter && (maxTextLength == 0 || text.length() < maxTextLength || e.getKeyCode() == AllSettings.Key_Backspace || e.getKeyCode() == AllSettings.Key_Delete)) {
            if (!(filteringTextNeeded && filterThisText(e, text.toString()))) {
                updateText(e);
            } else {
                doThisAfterTextFiltered(e, text.toString());
            }
        }
    }

    public final void keyReleased(KeyEvent e) {
    }

    public final String getText() {
        return text.toString();
    }

    public final void setText(String text) {
        this.text = new StringBuilder(text);
        textImage = RenderString.renderBigString(text, color);
        textImageLeft = left + ((width - textImage.getWidth()) >> 1);
    }

    /**
     * This method is called when focus is entered.
     */
    public void focusEntered() {
    }

    public final void focusLost() {
        performAction(text.toString());
    }

    /**
     * This method is called each time when any <code>char</code> of text is filtered.
     */
    public void doThisAfterTextFiltered(KeyEvent e, String text) {
    }

    /**
     * This method is called when focus is lost.
     */
    public void performAction(String text) {
    }

}

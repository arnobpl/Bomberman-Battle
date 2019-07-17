package MenuSystem.Form.MenuItems;

import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import FontRenderer.FontColor;
import FontRenderer.RenderString;
import GraphicsRenderer.Animation.PingPong_Animation;
import GraphicsRenderer.GfxFromFile;
import SoundSystem.BaseSound;
import javafx.scene.media.Media;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 07/11/2014.
 * Class for option chooser that can used to get input within a list
 */
public class OptionChooser implements MenuSystem.Form.MenuItems.MenuItem {
    public static final int width = (AllSettings.unscaledWidth >> 2) * 3;   // by default, it is 228

    public static final int left = (AllSettings.unscaledWidth - width) >> 1;
    public final int top;

    private BufferedImage textImage;
    private int textImageLeft = left;
    private int textImageTop = textTop;

    private static final int animationAmplitude = 2;
    private static final double animationStep = 0.5 * AllSettings.userSettings.motionConst;

    private static final int animation_UpperLimit = animationAmplitude << 1;

    private PingPong_Animation animation;   // this is for animation through x-axis of the whole OptionChooser
    private int leftOffset = 0;

    private static final int leftRightOptionArrow_AnimationAmplitude = 4;
    private static final double leftRightOptionArrow_AnimationStep = 2 * AllSettings.userSettings.motionConst;

    private static final int animationOptionArrow_UpperLimit = leftRightOptionArrow_AnimationAmplitude << 1;

    private PingPong_Animation optionArrow_Animation;    // this is for animation through x-axis of the optionArrow
    private int optionArrow_leftOffset = 0;

    private static final int arrowTop = (height - GfxFromFile.arrowEditL_Width) >> 1;

    private int arrowImageTop = arrowTop;

    private boolean showArrowEditL = true;
    private boolean showArrowEditR = true;
    private static final int ArrowEditL_Left = left + (width >> 1);
    private static final int ArrowEditR_Left = left + width - GfxFromFile.arrowEditR_Width;

    private static final int optionTextLeft = left + (width >> 2) * 3;  // when option text is empty

    private static final FontColor optionItemColor = FontColor.Ash;

    private int selectedIndex = 0;
    private int totalOptions = 1;

    private BufferedImage[] optionTextImageList = new BufferedImage[totalOptions];
    private int[] optionTextLeftList = new int[totalOptions];
    private int indexToAdd = 0;

    private static final Media scrollSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InMenu.OptionScroll);


    public OptionChooser(String text, int positionIndex, int totalOptions, int selectedIndex) {
        FontColor color = FontColor.valuesCached()[positionIndex % (FontColor.totalColor - 1) + 1];   // this can be all colors except ash (value 0)
        textImage = RenderString.renderBigString(text, color);

        textImageLeft = left + (((width >> 1) - textImage.getWidth()) >> 1);
        top = positionIndex * height;
        textImageTop = top + textTop;
        arrowImageTop = top + arrowTop;

        if (totalOptions > 1) {
            this.totalOptions = totalOptions;
            optionTextImageList = new BufferedImage[totalOptions];
            optionTextLeftList = new int[totalOptions];
        }

        setSelectedIndex(selectedIndex);

        animation = new PingPong_Animation(animation_UpperLimit, animationStep, Initialized.random.nextInt(animation_UpperLimit + 1));
        optionArrow_Animation = new PingPong_Animation(animationOptionArrow_UpperLimit, leftRightOptionArrow_AnimationStep, Initialized.random.nextInt(animationOptionArrow_UpperLimit + 1));
    }

    public final void addOption(String optionItem) {
        if (indexToAdd < totalOptions) {
            optionTextImageList[indexToAdd] = RenderString.renderBigString(optionItem, optionItemColor);
            optionTextLeftList[indexToAdd] = optionTextLeft - (optionTextImageList[indexToAdd].getWidth() >> 1);
            indexToAdd++;
        }
    }

    public final void setSelectedIndex(int selectedIndex) {
        if (0 <= selectedIndex && selectedIndex < totalOptions) this.selectedIndex = selectedIndex;

        showArrowEditL = !(this.selectedIndex == 0);
        showArrowEditR = !(this.selectedIndex + 1 == totalOptions);
    }

    public final int getTotalOptions() {
        return totalOptions;
    }

    public final int getSelectedIndex() {
        return selectedIndex;
    }

    public final void update() {
        animation.animate();
        leftOffset = animationAmplitude - animation.getAnimateValue();

        optionArrow_Animation.animate();
        optionArrow_leftOffset = leftRightOptionArrow_AnimationAmplitude - optionArrow_Animation.getAnimateValue();
    }

    public final void render(Graphics2D g) {
        g.drawImage(textImage, textImageLeft + leftOffset, textImageTop, null);
        g.drawImage(optionTextImageList[selectedIndex], optionTextLeftList[selectedIndex] + leftOffset, textImageTop, null);

        if (showArrowEditL)
            g.drawImage(Initialized.gfx.arrowEditLeft(), ArrowEditL_Left + optionArrow_leftOffset, arrowImageTop, null);
        if (showArrowEditR)
            g.drawImage(Initialized.gfx.arrowEditRight(), ArrowEditR_Left + optionArrow_leftOffset, arrowImageTop, null);
    }

    public final void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == AllSettings.Key_MenuLeft) {
            if (selectedIndex > 0) {
                selectedIndex--;

                if (selectedIndex == 0) showArrowEditL = false;
                if (totalOptions > 1) showArrowEditR = true;

                valueChanged(selectedIndex);

                BaseSound.playSound(scrollSound, AllSettings.userSettings.soundEffectVolume);
            }
        } else if (e.getKeyCode() == AllSettings.Key_MenuRight) {
            if (selectedIndex + 1 < totalOptions) {
                selectedIndex++;

                if (selectedIndex + 1 == totalOptions) showArrowEditR = false;
                if (totalOptions > 1) showArrowEditL = true;

                valueChanged(selectedIndex);

                BaseSound.playSound(scrollSound, AllSettings.userSettings.soundEffectVolume);
            }
        }
    }

    public final void keyReleased(KeyEvent e) {
    }

    /**
     * This method is called when focus is entered.
     */
    public void focusEntered() {
    }

    public final void focusLost() {
        performAction(selectedIndex);
    }

    /**
     * This method is called when the selected option is changed.
     */
    public void valueChanged(int selectedIndex) {
    }

    /**
     * This method is called when focus is lost.
     */
    public void performAction(int selectedIndex) {
    }

}

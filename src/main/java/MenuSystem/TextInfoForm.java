package MenuSystem;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import FontRenderer.FontColor;
import FontRenderer.FontFromFile;
import FontRenderer.RenderString;
import GraphicsRenderer.Animation.Periodic_Animation;
import GraphicsRenderer.Animation.PingPong_Animation;
import GraphicsRenderer.Animation.TitleString_Animation;
import GraphicsRenderer.GfxFromFile;
import MenuSystem.Form.MenuItems.MenuItem;
import Scene.Scene;
import SoundSystem.BaseSound;
import javafx.scene.media.Media;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnob on 12/02/2015.
 * This is used to display info in small font from a long text with optional center alignment.
 * It supports scrolling (with optional auto-scrolling) if necessary.
 */
public class TextInfoForm {
    private static final double titleStringMoveStep = -2 * AllSettings.userSettings.motionConst;
    private static final int titleStringTop = 5;

    public static final int infoTextImageWidth = (int) (AllSettings.unscaledWidth * 0.9);
    public static final int infoTextImageHeight = (int) (AllSettings.unscaledHeight * 0.6);

    public static final int eachLineInfoTextHeight = 15;

    private static final int initialSubstringCharacter = infoTextImageWidth / FontFromFile.smallFontMaxWeight;  // this ensures that for all character combinations, "infoTextImage < infoTextImageWidth" is always true
    private static final int considerableSubstringCharacterWhenNoSpaceAfterInitialSubstringCharacter = (int) (initialSubstringCharacter * 1.5);

    public static final int totalLinesInEachPage = infoTextImageHeight / eachLineInfoTextHeight;

    private static final int scrollImageAnimationAmplitude = 2;
    private static final double scrollImageAnimationStep = 0.6 * AllSettings.userSettings.motionConst;

    private static final int gapBetweenScrollAndInfoText = 1;

    public static final int infoTextImageLeft = (AllSettings.unscaledWidth - infoTextImageWidth) >> 1;
    public static final int infoTextImageTop = MenuItem.height + scrollImageAnimationAmplitude + GfxFromFile.arrowU_Width + gapBetweenScrollAndInfoText;

    private static final int eachLineInfoTextTop = (eachLineInfoTextHeight - FontFromFile.smallFontHeight) >> 1;
    private static final int infoTextFirstImageTop = infoTextImageTop + eachLineInfoTextTop;

    private Scene parentScene;

    private boolean sceneShouldBeChanged = false;

    private TitleString_Animation titleStringAnimation;

    private FontColor fontColor;

    private List<BufferedImage> infoTextImageList;
    private List<Integer> infoTextImageLeft_offsetList;    // this is need only when 'centerAlignmentNeeded' is true

    private static final int scrollImageLeft = AllSettings.unscaledWidth_half - (GfxFromFile.arrowU_Width >> 1);
    private static final int scrollUpImageTop = infoTextImageTop - gapBetweenScrollAndInfoText - GfxFromFile.arrowU_Width;
    private static final int scrollDownImageTop = infoTextImageTop + totalLinesInEachPage * eachLineInfoTextHeight + gapBetweenScrollAndInfoText;

    private PingPong_Animation scrollImageAnimation;
    private int scrollImageTop_offset = 0;

    private int scrollIndex = 0;
    private int maxScrollIndex = 0;
    private int maxLinesForThisInfoText = 0;    // this is at most 'totalLinesInEachPage'
    private boolean showScrollUp = false;
    private boolean showScrollDown = false;

    private boolean autoScrollingEnabled = false;
    private int autoScrollingElapsed = 3000;    // this variable is not 'final' because user can change it
    private Periodic_Animation autoScrollCounter;   // though it is not for animation, it provides the task to count time
    private boolean autoResetScrollingNeeded = false;

    private static final Media goToParentSceneSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InMenu.MenuSelect);


    public TextInfoForm(Scene parentScene, String titleString, String infoText, FontColor fontColor, boolean centerAlignmentNeeded) {
        this.parentScene = parentScene;
        titleStringAnimation = new TitleString_Animation(titleString, titleStringMoveStep, titleStringTop);
        this.fontColor = fontColor;
        setInfoText(infoText, centerAlignmentNeeded);
        scrollImageAnimation = new PingPong_Animation(scrollImageAnimationAmplitude, scrollImageAnimationStep);
    }

    public TextInfoForm(Scene parentScene, String titleString, String infoText, FontColor fontColor) {
        this(parentScene, titleString, infoText, fontColor, false);
    }

    public synchronized void setInfoText(String infoText, boolean centerAlignmentNeeded) {
        //String infoTextRemained = infoText;
        //String infoTextRemained = infoText.trim().replaceAll("(\\s)+", "$1");
        String infoTextRemained = infoText.trim().replace('\t', ' ').replaceAll(" +", " "); // no consecutive spaces but can be "\n " substrings
        infoTextImageList = new ArrayList<>();
        infoTextImageLeft_offsetList = new ArrayList<>();
        while (infoTextRemained.length() > 0) {
            int newLineCharIndex = infoTextRemained.indexOf('\n');
            if (newLineCharIndex == -1) {   // no '\n' in the remaining string, so search for space
                infoTextRemained = addInfoTextWithoutNewLine(infoTextRemained, centerAlignmentNeeded);
                break;
            }
            BufferedImage textInfoImageToBeAdded = RenderString.renderSmallString(addInfoTextWithoutNewLine(infoTextRemained.substring(0, newLineCharIndex).trim(), centerAlignmentNeeded), fontColor);
            infoTextImageList.add(textInfoImageToBeAdded);
            if (centerAlignmentNeeded)
                infoTextImageLeft_offsetList.add((infoTextImageWidth - textInfoImageToBeAdded.getWidth()) >> 1);
            else
                infoTextImageLeft_offsetList.add(0);
            infoTextRemained = infoTextRemained.substring(newLineCharIndex + 1);
        }
        BufferedImage textInfoImageToBeAdded = RenderString.renderSmallString(infoTextRemained, fontColor);
        infoTextImageList.add(textInfoImageToBeAdded);
        if (centerAlignmentNeeded)
            infoTextImageLeft_offsetList.add((infoTextImageWidth - textInfoImageToBeAdded.getWidth()) >> 1);
        else
            infoTextImageLeft_offsetList.add(0);

        resetScrolling();
        if (infoTextImageList.size() > totalLinesInEachPage) {
            maxScrollIndex = infoTextImageList.size() - totalLinesInEachPage;
            maxLinesForThisInfoText = totalLinesInEachPage;
        } else {
            maxScrollIndex = 0;
            maxLinesForThisInfoText = infoTextImageList.size();
        }
    }

    private String addInfoTextWithoutNewLine(String infoTextRemained, boolean centerAlignmentNeeded) { // this function returns the remaining string of length being equal or less than 'initialSubstringCharacter'
        if (infoTextRemained.length() < initialSubstringCharacter) // checking if string is valid to render
            return infoTextRemained;
        do {    // string is not empty, so search for space from at and after 'initialSubstringCharacter' index
            int spaceCharIndex = infoTextRemained.indexOf(' ', initialSubstringCharacter);
            if (spaceCharIndex == -1) { // no space from at and after 'initialSubstringCharacter' index, so take the full string (if length is smaller than 'considerableSubstringCharacterWhenNoSpaceAfterInitialSubstringCharacter')
                if (infoTextRemained.length() < considerableSubstringCharacterWhenNoSpaceAfterInitialSubstringCharacter) {
                    BufferedImage testInfoTextLineImage = RenderString.renderSmallString(infoTextRemained, fontColor);
                    if (testInfoTextLineImage.getWidth() < infoTextImageWidth) {  // fortunately taking the full string fits in width
                        infoTextImageList.add(testInfoTextLineImage);
                        if (centerAlignmentNeeded)
                            infoTextImageLeft_offsetList.add((infoTextImageWidth - testInfoTextLineImage.getWidth()) >> 1);
                        else
                            infoTextImageLeft_offsetList.add(0);
                        return "";  // since the full string has been successfully taken
                    }
                }
                // full string cannot be taken, so search for a space before 'initialSubstringCharacter' index
                spaceCharIndex = infoTextRemained.lastIndexOf(' ', initialSubstringCharacter);
                if (spaceCharIndex == -1) { // this means the word is too long to fit in one line, so divide the word by the best possible fit
                    BufferedImage testInfoTextLineImage;
                    BufferedImage previousTestInfoTextLineImage;
                    int testInfoTextLineLength = initialSubstringCharacter;
                    testInfoTextLineImage = RenderString.renderSmallString(infoTextRemained.substring(0, testInfoTextLineLength++), fontColor);
                    do {
                        previousTestInfoTextLineImage = testInfoTextLineImage;
                        testInfoTextLineImage = RenderString.renderSmallString(infoTextRemained.substring(0, testInfoTextLineLength++), fontColor);
                    } while (testInfoTextLineImage.getWidth() < infoTextImageWidth);
                    if (testInfoTextLineImage.getWidth() > infoTextImageWidth) {
                        infoTextImageList.add(previousTestInfoTextLineImage);
                        if (centerAlignmentNeeded)
                            infoTextImageLeft_offsetList.add((infoTextImageWidth - previousTestInfoTextLineImage.getWidth()) >> 1);
                        else
                            infoTextImageLeft_offsetList.add(0);
                        infoTextRemained = infoTextRemained.substring(testInfoTextLineLength - 2);
                        continue;
                    }
                    // "testInfoTextLineImage.getWidth() == infoTextImageWidth" is true
                    infoTextImageList.add(testInfoTextLineImage);
                    if (centerAlignmentNeeded)
                        infoTextImageLeft_offsetList.add((infoTextImageWidth - testInfoTextLineImage.getWidth()) >> 1);
                    else
                        infoTextImageLeft_offsetList.add(0);
                    infoTextRemained = infoTextRemained.substring(testInfoTextLineLength - 1);
                    continue;
                }
                BufferedImage textInfoImageToBeAdded = RenderString.renderSmallString(infoTextRemained.substring(0, spaceCharIndex), fontColor);
                infoTextImageList.add(textInfoImageToBeAdded);
                if (centerAlignmentNeeded)
                    infoTextImageLeft_offsetList.add((infoTextImageWidth - textInfoImageToBeAdded.getWidth()) >> 1);
                else
                    infoTextImageLeft_offsetList.add(0);
                infoTextRemained = infoTextRemained.substring(spaceCharIndex + 1);  // "+1" for skipping the space in new line
                continue;
            }
            int testInfoTextLineLength = spaceCharIndex;
            BufferedImage testInfoTextLineImage = RenderString.renderSmallString(infoTextRemained.substring(0, spaceCharIndex), fontColor);
            int previousTestInfoTextLineLength = 0;
            BufferedImage previousTestInfoTextLineImage = null;
            while (testInfoTextLineImage.getWidth() < infoTextImageWidth) {
                previousTestInfoTextLineLength = testInfoTextLineLength;
                previousTestInfoTextLineImage = testInfoTextLineImage;
                spaceCharIndex = infoTextRemained.indexOf(' ', spaceCharIndex + 2); // "+2" for efficiency since there is no consecutive spaces
                if (spaceCharIndex != -1) {
                    testInfoTextLineLength = spaceCharIndex;
                    testInfoTextLineImage = RenderString.renderSmallString(infoTextRemained.substring(0, spaceCharIndex), fontColor);
                } else break;
            }
            if (testInfoTextLineImage.getWidth() > infoTextImageWidth) {
                if (previousTestInfoTextLineImage == null) {
                    // that means 'while' loop just exited without any operation;
                    // so no suitable space found from at and after 'initialSubstringCharacter' index,
                    // so search for a space before 'initialSubstringCharacter' index
                    spaceCharIndex = infoTextRemained.lastIndexOf(' ', initialSubstringCharacter);
                    if (spaceCharIndex == -1) { // this means the word is too long to fit in one line, so divide the word by the best possible fit
                        testInfoTextLineLength = initialSubstringCharacter;
                        testInfoTextLineImage = RenderString.renderSmallString(infoTextRemained.substring(0, testInfoTextLineLength++), fontColor);
                        do {
                            previousTestInfoTextLineImage = testInfoTextLineImage;
                            testInfoTextLineImage = RenderString.renderSmallString(infoTextRemained.substring(0, testInfoTextLineLength++), fontColor);
                        } while (testInfoTextLineImage.getWidth() < infoTextImageWidth);
                        if (testInfoTextLineImage.getWidth() > infoTextImageWidth) {
                            infoTextImageList.add(previousTestInfoTextLineImage);
                            if (centerAlignmentNeeded)
                                infoTextImageLeft_offsetList.add((infoTextImageWidth - previousTestInfoTextLineImage.getWidth()) >> 1);
                            else
                                infoTextImageLeft_offsetList.add(0);
                            infoTextRemained = infoTextRemained.substring(testInfoTextLineLength - 2);
                        } else {    // "testInfoTextLineImage.getWidth() == infoTextImageWidth" is true
                            infoTextImageList.add(testInfoTextLineImage);
                            if (centerAlignmentNeeded)
                                infoTextImageLeft_offsetList.add((infoTextImageWidth - testInfoTextLineImage.getWidth()) >> 1);
                            else
                                infoTextImageLeft_offsetList.add(0);
                            infoTextRemained = infoTextRemained.substring(testInfoTextLineLength - 1);
                        }
                        continue;
                    }
                    BufferedImage textInfoImageToBeAdded = RenderString.renderSmallString(infoTextRemained.substring(0, spaceCharIndex), fontColor);
                    infoTextImageList.add(textInfoImageToBeAdded);
                    if (centerAlignmentNeeded)
                        infoTextImageLeft_offsetList.add((infoTextImageWidth - textInfoImageToBeAdded.getWidth()) >> 1);
                    else
                        infoTextImageLeft_offsetList.add(0);
                    infoTextRemained = infoTextRemained.substring(spaceCharIndex + 1);  // "+1" for skipping the space in new line
                    continue;
                }
                infoTextImageList.add(previousTestInfoTextLineImage);
                if (centerAlignmentNeeded)
                    infoTextImageLeft_offsetList.add((infoTextImageWidth - previousTestInfoTextLineImage.getWidth()) >> 1);
                else
                    infoTextImageLeft_offsetList.add(0);
                infoTextRemained = infoTextRemained.substring(previousTestInfoTextLineLength + 1);  // "+1" for skipping the space in new line
                continue;
            }
            // "testInfoTextLineImage.getWidth() == infoTextImageWidth" is true
            infoTextImageList.add(testInfoTextLineImage);
            if (centerAlignmentNeeded)
                infoTextImageLeft_offsetList.add((infoTextImageWidth - testInfoTextLineImage.getWidth()) >> 1);
            else
                infoTextImageLeft_offsetList.add(0);
            infoTextRemained = infoTextRemained.substring(testInfoTextLineLength + 1);  // "+1" for skipping the space in new line
        } while (infoTextRemained.length() > initialSubstringCharacter);
        return infoTextRemained;
    }

    public synchronized void resetScrolling() {
        scrollIndex = 0;
        showScrollUp = false;
        showScrollDown = infoTextImageList.size() > totalLinesInEachPage;
    }

    public synchronized void scrollUp() {
        if (showScrollUp) {
            scrollIndex--;
            if (scrollIndex == 0) showScrollUp = false;
            if (scrollIndex < maxScrollIndex) showScrollDown = true;
        }
    }

    public synchronized void scrollDown() {
        if (showScrollDown) {
            scrollIndex++;
            if (scrollIndex == maxScrollIndex) showScrollDown = false;
            if (scrollIndex > 0) showScrollUp = true;
        }
    }

    public synchronized void setScrollIndex(int scrollIndex) {
        if (0 <= scrollIndex && scrollIndex <= maxScrollIndex) {
            this.scrollIndex = scrollIndex;
        }
    }

    public int getScrollIndex() {
        return scrollIndex;
    }

    public int getMaxScrollIndex() {
        return maxScrollIndex;
    }

    public synchronized void enableAutoScrolling(int autoScrollingElapsed) {
        if (autoScrollingElapsed > 0) this.autoScrollingElapsed = autoScrollingElapsed;

        autoScrollCounter = new Periodic_Animation(this.autoScrollingElapsed, AllSettings.userSettings.timeStep);
        autoScrollingEnabled = true;
    }

    public synchronized void enableAutoScrolling() {
        if (!autoScrollingEnabled) {
            autoScrollCounter = new Periodic_Animation(autoScrollingElapsed, AllSettings.userSettings.timeStep);
            autoScrollingEnabled = true;
        } else {
            autoScrollCounter.resetAnimation();
        }
    }

    public synchronized void disableAutoScrolling() {
        autoScrollingEnabled = false;
    }

    public void update() {
        titleStringAnimation.animate();

        scrollImageAnimation.animate();
        scrollImageTop_offset = scrollImageAnimation.getAnimateValue();

        if (autoScrollingEnabled) {
            autoScrollCounter.animate();
            if (autoScrollCounter.isCycleCompleted()) {
                if (autoResetScrollingNeeded) {
                    resetScrolling();
                    autoResetScrollingNeeded = false;
                } else {
                    scrollDown();
                }
                if (!showScrollDown) autoResetScrollingNeeded = true;
            }
        }
    }

    public void render(Graphics2D g) {
        titleStringAnimation.render(g);

        if (showScrollUp)
            g.drawImage(Initialized.gfx.arrowUp(), scrollImageLeft, scrollUpImageTop - scrollImageTop_offset, null);

        if (showScrollDown)
            g.drawImage(Initialized.gfx.arrowDown(), scrollImageLeft, scrollDownImageTop + scrollImageTop_offset, null);

        int j = 0;
        int limit = scrollIndex + maxLinesForThisInfoText;
        for (int i = scrollIndex; i < limit; i++) {
            g.drawImage(infoTextImageList.get(i), infoTextImageLeft + infoTextImageLeft_offsetList.get(i), infoTextFirstImageTop + (j++) * eachLineInfoTextHeight, null);
        }

        if (sceneShouldBeChanged) {
            BaseWindow.scene = parentScene; // scene is changed here to maintain "first update, then render" in the new scene
            parentScene.reset();
            sceneShouldBeChanged = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == AllSettings.Key_MenuDown) {
            scrollDown();
            if (autoScrollingEnabled) autoScrollCounter.resetAnimation();
        } else if (e.getKeyCode() == AllSettings.Key_MenuUp) {
            scrollUp();
            if (autoScrollingEnabled) autoScrollCounter.resetAnimation();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (parentScene != null && (e.getKeyCode() == AllSettings.Key_Escape || e.getKeyCode() == AllSettings.Key_MenuEnter)) {
            sceneShouldBeChanged = true;    // no  actual  change here because scene change must be done in 'render' method
            BaseSound.playSound(goToParentSceneSound, AllSettings.userSettings.soundEffectVolume);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            keyPressed(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, AllSettings.Key_MenuEnter, (char) AllSettings.Key_MenuEnter));
            keyReleased(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, AllSettings.Key_MenuEnter, (char) AllSettings.Key_MenuEnter));
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            keyPressed(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, AllSettings.Key_MenuUp, (char) AllSettings.Key_MenuUp));
            keyReleased(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, AllSettings.Key_MenuUp, (char) AllSettings.Key_MenuUp));
        } else {
            keyPressed(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, AllSettings.Key_MenuDown, (char) AllSettings.Key_MenuDown));
            keyReleased(new KeyEvent(BaseWindow.canvas, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, AllSettings.Key_MenuDown, (char) AllSettings.Key_MenuDown));
        }
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
    }

}

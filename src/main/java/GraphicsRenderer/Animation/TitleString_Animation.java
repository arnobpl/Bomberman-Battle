package GraphicsRenderer.Animation;

import AppInfo.Customization.AllSettings;
import AppInfo.Initialized;
import FontRenderer.FontColor;
import FontRenderer.RenderString;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 03/11/2014.
 * This class helps to generate animation that is used in title string image.
 */
public class TitleString_Animation {
    private final BufferedImage[] titleStringImage = new BufferedImage[FontColor.totalColor];

    private double titleStringMoveStep = 3;
    private int titleStringTop = 1;

    private double titleStringLeft;     // left is initially in the position so that the text is in the middle of the screen
    private int twoTitleStringRange;    // for which range of titleStringLeft, 2 partial strings is shown
    private int titleStringColorIndex;


    public TitleString_Animation(String titleString, double titleStringMoveStep, int titleStringTop) {
        for (int i = 0; i < titleStringImage.length; i++) {
            titleStringImage[i] = RenderString.renderBigString(titleString, FontColor.valuesCached()[i]);
        }

        int titleStringWidth = titleStringImage[0].getWidth();
        titleStringLeft = AllSettings.unscaledWidth_half - (titleStringWidth >> 1);
        twoTitleStringRange = AllSettings.unscaledWidth - titleStringWidth;
        titleStringColorIndex = Initialized.random.nextInt(FontColor.totalColor);

        this.titleStringMoveStep = titleStringMoveStep;
        this.titleStringTop = titleStringTop;
    }


    public void animate() {
        titleStringLeft += titleStringMoveStep;
        if (titleStringLeft >= AllSettings.unscaledWidth) {
            titleStringLeft -= AllSettings.unscaledWidth;
            titleStringColorIndex++;
            if (titleStringColorIndex >= FontColor.totalColor) {
                titleStringColorIndex = 0;
            }
        } else if (titleStringLeft < 0) {
            titleStringLeft += AllSettings.unscaledWidth;
            titleStringColorIndex--;
            if (titleStringColorIndex < 0) {
                titleStringColorIndex = FontColor.totalColor - 1;
            }
        }
    }

    public void render(Graphics2D g) {
        g.drawImage(titleStringImage[titleStringColorIndex], (int) Math.round(titleStringLeft), titleStringTop, null);
        if (titleStringLeft > twoTitleStringRange) {
            int otherTitleStringColorIndex = titleStringColorIndex + 1;
            if (otherTitleStringColorIndex >= FontColor.totalColor) {
                otherTitleStringColorIndex = 0;
            }
            g.drawImage(titleStringImage[otherTitleStringColorIndex], (int) Math.round(titleStringLeft) - AllSettings.unscaledWidth, titleStringTop, null);
        }
    }
}

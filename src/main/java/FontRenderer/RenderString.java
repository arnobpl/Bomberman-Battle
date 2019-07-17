package FontRenderer;

import AppInfo.Initialized;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 10/10/2014.
 * This class plays a vital part of rendering all string output in the game display.
 */
public class RenderString {

    public static BufferedImage renderBigString(String str, FontColor color) {
        if (str.length() > 0) {
            Graphics2D g;

            BufferedImage strImgTemp = new BufferedImage(FontFromFile.bigFontMaxWeight * str.length(), FontFromFile.bigFontHeight, BufferedImage.TYPE_INT_ARGB);
            g = strImgTemp.createGraphics();
            int actualWidth = 0;
            int left = 0;
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                g.drawImage(Initialized.font.getBigFont(ch, color), left, 0, null);
                left += FontFromFile.getBigFontWeight(ch);
                actualWidth += FontFromFile.getBigFontWeight(ch);
            }
            g.dispose();

            if (strImgTemp.getWidth() == actualWidth) return strImgTemp;

            if (actualWidth == 0) return Initialized.gfx.nullImage();

            BufferedImage strImgFinal = new BufferedImage(actualWidth, FontFromFile.bigFontHeight, BufferedImage.TYPE_INT_ARGB);
            g = strImgFinal.createGraphics();
            g.drawImage(strImgTemp, 0, 0, null);
            g.dispose();

            return strImgFinal;
        }
        return Initialized.gfx.nullImage();
    }

    public static BufferedImage renderSmallString(String str, FontColor color) {
        if (str.length() > 0) {
            Graphics2D g;

            BufferedImage strImgTemp = new BufferedImage(FontFromFile.smallFontMaxWeight * str.length(), FontFromFile.smallFontHeight, BufferedImage.TYPE_INT_ARGB);
            g = strImgTemp.createGraphics();
            int actualWidth = 0;
            int left = 0;
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                g.drawImage(Initialized.font.getSmallFont(ch, color), left, 0, null);
                left += FontFromFile.getSmallFontWeight(ch);
                actualWidth += FontFromFile.getSmallFontWeight(ch);
            }
            g.dispose();

            if (strImgTemp.getWidth() == actualWidth) return strImgTemp;

            if (actualWidth == 0) return Initialized.gfx.nullImage();

            BufferedImage strImgFinal = new BufferedImage(actualWidth, FontFromFile.smallFontHeight, BufferedImage.TYPE_INT_ARGB);
            g = strImgFinal.createGraphics();
            g.drawImage(strImgTemp, 0, 0, null);
            g.dispose();

            return strImgFinal;
        }
        return Initialized.gfx.nullImage();
    }

}

package FontRenderer;

import AppInfo.FilePath;
import AppInfo.Resource.ResourceLoader;
import AppInfo.Strings;
import GraphicsRenderer.RenderGraphics;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 10/10/2014.
 * This class parse letters from the font file. Firstly it stores the file in RAM.
 */
public class FontFromFile {
    public static final int bigFontHeight = 18;
    public static final int bigFontMaxWeight = 10;

    private static final int[] bigFontLetter_Left = {
            0,
            10,
            20,
            30,
            40,
            50,
            60,
            70,
            80,
            85,
            93,
            103,
            112,
            122,
            132,
            142,
            152,
            162,
            172,
            182,
            191,
            201,
            211,
            221,
            231,
            240,
            250
    };

    private static final int[] bigFontNumber_Left = {
            250,
            260,
            267,
            277,
            287,
            297,
            307,
            317,
            327,
            337,
            347
    };

    private static final int[] bigFontSymbol_Left = {
            347,
            353,
            361,
            371,
            381,
            391,
            401,
            406,
            416,
            426,
            436,
            445,
            450,
            459,
            464,
            474,
            479,
            484,
            494,
            501,
            511,
            521,
            531,
            541
    };

    public static final int smallFontHeight = 10;
    public static final int smallFontMaxWeight = 10;

    private static final int[] smallFontLetter_Left = {
            0,
            10,
            20,
            30,
            40,
            50,
            60,
            70,
            80,
            85,
            92,
            101,
            110,
            120,
            130,
            140,
            150,
            160,
            170,
            180,
            190,
            200,
            210,
            220,
            230,
            239,
            249
    };

    private static final int[] smallFontNumber_Left = {
            249,
            259,
            264,
            273,
            282,
            292,
            301,
            311,
            320,
            330,
            340
    };

    private static final int[] smallFontSymbol_Left = {
            340,
            345,
            354,
            364,
            374,
            384,
            393,
            398,
            405,
            412,
            422,
            431,
            436,
            444,
            449,
            459,
            464,
            469,
            477,
            484,
            492,
            502,
            512,
            522
    };

    private static final int bigSmallFontHeight = bigFontHeight + smallFontHeight;


    private BufferedImage fontFile;

    public FontFromFile() {
        try {
            fontFile = RenderGraphics.convertToARGB(ResourceLoader.loadImageFromJAR(FilePath.GfxFilePath.Font));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, Strings.Error.FontNotFound, Strings.Error.FatalError, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }


    public BufferedImage getBigFont(char ch, FontColor color) {
        if (97 <= ch && ch <= 122) {    // a to z
            ch -= 97;
            return fontFile.getSubimage(bigFontLetter_Left[ch], color.ordinal() * bigSmallFontHeight + smallFontHeight, bigFontLetter_Left[ch + 1] - bigFontLetter_Left[ch], bigFontHeight);
        } else if (65 <= ch && ch <= 90) {  // A to Z
            ch -= 65;
            return fontFile.getSubimage(bigFontLetter_Left[ch], color.ordinal() * bigSmallFontHeight + smallFontHeight, bigFontLetter_Left[ch + 1] - bigFontLetter_Left[ch], bigFontHeight);
        } else if (48 <= ch && ch <= 57) {  // 0 to 9
            ch -= 48;
            return fontFile.getSubimage(bigFontNumber_Left[ch], color.ordinal() * bigSmallFontHeight + smallFontHeight, bigFontNumber_Left[ch + 1] - bigFontNumber_Left[ch], bigFontHeight);
        } else if (33 <= ch && ch <= 47) {  // ! to /
            ch -= 33;
            return fontFile.getSubimage(bigFontSymbol_Left[ch], color.ordinal() * bigSmallFontHeight + smallFontHeight, bigFontSymbol_Left[ch + 1] - bigFontSymbol_Left[ch], bigFontHeight);
        } else if (58 <= ch && ch <= 64) {  // : to @
            ch -= 43;
            return fontFile.getSubimage(bigFontSymbol_Left[ch], color.ordinal() * bigSmallFontHeight + smallFontHeight, bigFontSymbol_Left[ch + 1] - bigFontSymbol_Left[ch], bigFontHeight);
        } else if (ch == 32) {  // space
            ch = 22;
            return fontFile.getSubimage(bigFontSymbol_Left[ch], color.ordinal() * bigSmallFontHeight + smallFontHeight, bigFontSymbol_Left[ch + 1] - bigFontSymbol_Left[ch], bigFontHeight);
        } else {    // unknown
            return null;
        }
    }

    public static int getBigFontWeight(char ch) {
        if (97 <= ch && ch <= 122) {    // a to z
            ch -= 97;
            return bigFontLetter_Left[ch + 1] - bigFontLetter_Left[ch];
        } else if (65 <= ch && ch <= 90) {  // A to Z
            ch -= 65;
            return bigFontLetter_Left[ch + 1] - bigFontLetter_Left[ch];
        } else if (48 <= ch && ch <= 57) {  // 0 to 9
            ch -= 48;
            return bigFontNumber_Left[ch + 1] - bigFontNumber_Left[ch];
        } else if (33 <= ch && ch <= 47) {  // ! to /
            ch -= 33;
            return bigFontSymbol_Left[ch + 1] - bigFontSymbol_Left[ch];
        } else if (58 <= ch && ch <= 64) {  // : to @
            ch -= 43;
            return bigFontSymbol_Left[ch + 1] - bigFontSymbol_Left[ch];
        } else if (ch == 32) {  // space
            ch = 22;
            return bigFontSymbol_Left[ch + 1] - bigFontSymbol_Left[ch];
        }
        return 0;   // unknown
    }

    public BufferedImage getSmallFont(char ch, FontColor color) {
        if (97 <= ch && ch <= 122) {    // a to z
            ch -= 97;
            return fontFile.getSubimage(smallFontLetter_Left[ch], color.ordinal() * bigSmallFontHeight, smallFontLetter_Left[ch + 1] - smallFontLetter_Left[ch], smallFontHeight);
        } else if (65 <= ch && ch <= 90) {  // A to Z
            ch -= 65;
            return fontFile.getSubimage(smallFontLetter_Left[ch], color.ordinal() * bigSmallFontHeight, smallFontLetter_Left[ch + 1] - smallFontLetter_Left[ch], smallFontHeight);
        } else if (48 <= ch && ch <= 57) {  // 0 to 9
            ch -= 48;
            return fontFile.getSubimage(smallFontNumber_Left[ch], color.ordinal() * bigSmallFontHeight, smallFontNumber_Left[ch + 1] - smallFontNumber_Left[ch], smallFontHeight);
        } else if (33 <= ch && ch <= 47) {  // ! to /
            ch -= 33;
            return fontFile.getSubimage(smallFontSymbol_Left[ch], color.ordinal() * bigSmallFontHeight, smallFontSymbol_Left[ch + 1] - smallFontSymbol_Left[ch], smallFontHeight);
        } else if (58 <= ch && ch <= 64) {  // : to @
            ch -= 43;
            return fontFile.getSubimage(smallFontSymbol_Left[ch], color.ordinal() * bigSmallFontHeight, smallFontSymbol_Left[ch + 1] - smallFontSymbol_Left[ch], smallFontHeight);
        } else if (ch == 32) {  // space
            ch = 22;
            return fontFile.getSubimage(smallFontSymbol_Left[ch], color.ordinal() * bigSmallFontHeight, smallFontSymbol_Left[ch + 1] - smallFontSymbol_Left[ch], smallFontHeight);
        } else {    // unknown
            return null;
        }
    }

    public static int getSmallFontWeight(char ch) {
        if (97 <= ch && ch <= 122) {    // a to z
            ch -= 97;
            return smallFontLetter_Left[ch + 1] - smallFontLetter_Left[ch];
        } else if (65 <= ch && ch <= 90) {  // A to Z
            ch -= 65;
            return smallFontLetter_Left[ch + 1] - smallFontLetter_Left[ch];
        } else if (48 <= ch && ch <= 57) {  // 0 to 9
            ch -= 48;
            return smallFontNumber_Left[ch + 1] - smallFontNumber_Left[ch];
        } else if (33 <= ch && ch <= 47) {  // ! to /
            ch -= 33;
            return smallFontSymbol_Left[ch + 1] - smallFontSymbol_Left[ch];
        } else if (58 <= ch && ch <= 64) {  // : to @
            ch -= 43;
            return smallFontSymbol_Left[ch + 1] - smallFontSymbol_Left[ch];
        } else if (ch == 32) {  // space
            ch = 22;
            return smallFontSymbol_Left[ch + 1] - smallFontSymbol_Left[ch];
        }
        return 0;   // unknown
    }

}


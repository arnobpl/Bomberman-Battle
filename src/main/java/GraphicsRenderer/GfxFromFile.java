package GraphicsRenderer;

import AppInfo.FilePath;
import AppInfo.Resource.ResourceLoader;
import AppInfo.Strings;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 26/09/2014.
 * This class is responsible for creating sub-images from sprite sheet.
 */
public class GfxFromFile {

    // The following values are top_left and dimension info of sub-images
    private static final int appIcon_Left = 921;
    private static final int appIcon_Top = 64;
    public static final int appIcon_Width = 64;

    public static final int gridWidth = 16;
    public static final int playerHeight = 24;

    private static final int nullImage_Left = 0;
    private static final int nullImage_Top = 60;
    public static final int nullImage_Width = gridWidth;

    private static final int playerStatus_Left = 0;
    public static final int playerStatus_Width = 15;

    private static final int playerDirection_Left = 45;
    public static final int playerDirection_Width = gridWidth;
    public static final int playerDirection_Height = playerHeight;

    private static final int playerIdle_Left = 237;
    public static final int playerIdle_Width = gridWidth;
    public static final int playerIdle_Height = playerHeight;

    private static final int playerDeath_Left = 253;
    public static final int playerDeath_Width = gridWidth;
    public static final int playerDeath_Height = playerHeight;

    private static final int bomb_Left = 317;
    public static final int bomb_Width = gridWidth;

    private static final int explosion_Left = 365;
    public static final int explosion_Width = gridWidth;

    private static final int winner_Left = 557;
    public static final int winner_Width = 26;
    public static final int winner_Height = 32;

    private static final int loser_Left = 869;
    public static final int loser_Width = 26;
    public static final int loser_Height = 32;

    private static final int boundary_Left = 921;
    public static final int boundary_Width = gridWidth;

    private static final int grass_Left = 1001;
    public static final int grass_Width = gridWidth;

    private static final int road_Left = 1033;
    public static final int road_Width = gridWidth;

    private static final int innerObj_Left = 1097;
    public static final int innerObj_Width = gridWidth;
    public static final int innerObj_Height = 22;

    private static final int outerHome_Left = 1161;
    public static final int outerHome_Width = 32;
    public static final int outerHome_Height = 48;

    private static final int outerGrid_Left = 1257;
    public static final int outerGrid_Width = gridWidth;

    private static final int stone_Left = 1273;
    public static final int stone_Width = gridWidth;

    private static final int readyGoHurry_Left = 1289;
    public static final int readyGoHurry_Width = 105;
    public static final int readyGoHurry_Height = 37;

    private static final int HUD_Left = 0;
    private static final int HUD_Top = 106;
    public static final int HUD_Width = 304;
    public static final int HUD_Height = 22;

    private static final int star_Left = 304;
    private static final int star_Top = 96;
    public static final int star_Width = 32;

    private static final int trophy_Left = 336;
    private static final int trophy_Top = 96;
    public static final int trophy_Width = 32;

    private static final int arrowL_Left = 368;
    private static final int arrowL_Top = 96;
    public static final int arrowL_Width = 32;

    private static final int arrowR_Left = 400;
    private static final int arrowR_Top = 96;
    public static final int arrowR_Width = 32;

    private static final int arrowEditL_Left = 432;
    private static final int arrowEditL_Top = 96;
    public static final int arrowEditL_Width = 32;

    private static final int arrowEditR_Left = 464;
    private static final int arrowEditR_Top = 96;
    public static final int arrowEditR_Width = 32;

    private static final int arrowU_Left = 496;
    private static final int arrowU_Top = 64;
    public static final int arrowU_Width = 32;

    private static final int arrowD_Left = 496;
    private static final int arrowD_Top = 96;
    public static final int arrowD_Width = 32;

    private static final int networkError_Left = 1001;
    private static final int networkError_Top = 64;
    public static final int networkError_Width = 64;

    private static final int compatibilityError_Left = 1065;
    private static final int compatibilityError_Top = 64;
    public static final int compatibilityError_Width = 64;


    private BufferedImage gfxFile;

    public GfxFromFile() {
        try {
            gfxFile = RenderGraphics.convertToARGB(ResourceLoader.loadImageFromJAR(FilePath.GfxFilePath.Gfx));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, Strings.Error.GfxNotFound, Strings.Error.FatalError, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public BufferedImage appIcon() {
        return gfxFile.getSubimage(appIcon_Left, appIcon_Top, appIcon_Width, appIcon_Width);
    }

    public BufferedImage nullImage() {
        return gfxFile.getSubimage(nullImage_Left, nullImage_Top, nullImage_Width, nullImage_Width);
    }

    public BufferedImage playerStatus(GameObject.Player.PlayerEnum.Color color, GameObject.Player.PlayerEnum.Status status) {
        return gfxFile.getSubimage(playerStatus_Left + status.ordinal() * playerStatus_Width, color.ordinal() * playerStatus_Width, playerStatus_Width, playerStatus_Width);
    }

    public BufferedImage player(GameObject.Player.PlayerEnum.Color color, GameObject.Player.PlayerEnum.Direction direction, int imageNumber) {
//        if (imageNumber < 0 || imageNumber > 2) {
//            return null;
//        }
        return gfxFile.getSubimage(playerDirection_Left + (direction.ordinal() * 3 + imageNumber) * playerDirection_Width, color.ordinal() * playerDirection_Height, playerDirection_Width, playerDirection_Height);
    }

    public BufferedImage playerIdle(GameObject.Player.PlayerEnum.Color color) {
        return gfxFile.getSubimage(playerIdle_Left, color.ordinal() * playerIdle_Height, playerIdle_Width, playerIdle_Height);
    }

    public BufferedImage playerDeath(GameObject.Player.PlayerEnum.Color color, int imageNumber) {
//        if (imageNumber < 0 || imageNumber > 3) {
//            return null;
//        }
        return gfxFile.getSubimage(playerDeath_Left + imageNumber * playerDeath_Width, color.ordinal() * playerDeath_Height, playerDeath_Width, playerDeath_Height);
    }

    public BufferedImage bomb(int imageNumber) {
//        if (imageNumber < 0 || imageNumber > 2) {
//            return null;
//        }
        return gfxFile.getSubimage(bomb_Left + imageNumber * bomb_Width, 0, bomb_Width, bomb_Width);
    }

    public BufferedImage explosionCenter(int imageNumber) {
//        if (imageNumber < 0 || imageNumber > 4) {
//            return null;
//        }
        return gfxFile.getSubimage(explosion_Left + 2 * explosion_Width, imageNumber * explosion_Width, explosion_Width, explosion_Width);
    }

    public BufferedImage explosion(GameObject.Bomb.Direction direction, int imageNumber, boolean corner) {
//        if (imageNumber < 0 || imageNumber > 4) {
//            return null;
//        }
        if (corner) {
            if (direction == GameObject.Bomb.Direction.Left) {
                return gfxFile.getSubimage(explosion_Left, imageNumber * explosion_Width, explosion_Width, explosion_Width);
            } else if (direction == GameObject.Bomb.Direction.Right) {
                return gfxFile.getSubimage(explosion_Left + 4 * explosion_Width, imageNumber * explosion_Width, explosion_Width, explosion_Width);
            } else if (direction == GameObject.Bomb.Direction.Up) {
                return gfxFile.getSubimage(explosion_Left + (5 + imageNumber) * explosion_Width, 0, explosion_Width, explosion_Width);
            } else {
                return gfxFile.getSubimage(explosion_Left + (5 + imageNumber) * explosion_Width, 3 * explosion_Width, explosion_Width, explosion_Width);
            }
        } else {
            if (direction == GameObject.Bomb.Direction.Left) {
                return gfxFile.getSubimage(explosion_Left + explosion_Width, imageNumber * explosion_Width, explosion_Width, explosion_Width);
            } else if (direction == GameObject.Bomb.Direction.Right) {
                return gfxFile.getSubimage(explosion_Left + 3 * explosion_Width, imageNumber * explosion_Width, explosion_Width, explosion_Width);
            } else if (direction == GameObject.Bomb.Direction.Up) {
                return gfxFile.getSubimage(explosion_Left + (5 + imageNumber) * explosion_Width, explosion_Width, explosion_Width, explosion_Width);
            } else {
                return gfxFile.getSubimage(explosion_Left + (5 + imageNumber) * explosion_Width, 2 * explosion_Width, explosion_Width, explosion_Width);
            }
        }
    }

    public BufferedImage explosionEnding(GameObject.Bomb.Direction direction, int imageNumber) {
//        if (imageNumber < 0 || imageNumber > 1) {
//            return null;
//        }
        if (direction == GameObject.Bomb.Direction.Left) {
            return gfxFile.getSubimage(explosion_Left + (11 - imageNumber) * explosion_Width, 0, explosion_Width, explosion_Width);
        } else if (direction == GameObject.Bomb.Direction.Right) {
            return gfxFile.getSubimage(explosion_Left + (10 + imageNumber) * explosion_Width, explosion_Width, explosion_Width, explosion_Width);
        } else if (direction == GameObject.Bomb.Direction.Up) {
            return gfxFile.getSubimage(explosion_Left + 10 * explosion_Width, (3 - imageNumber) * explosion_Width, explosion_Width, explosion_Width);
        } else {
            return gfxFile.getSubimage(explosion_Left + 11 * explosion_Width, (2 + imageNumber) * explosion_Width, explosion_Width, explosion_Width);
        }
    }

    public BufferedImage winnerUnstable(GameObject.Player.PlayerEnum.Color color, int imageIndex) {
        return gfxFile.getSubimage(winner_Left + imageIndex * winner_Width, color.ordinal() * winner_Height, winner_Width, winner_Height);
    }

    public BufferedImage winnerStable(GameObject.Player.PlayerEnum.Color color, int imageIndex) {
        imageIndex += 10;
        return gfxFile.getSubimage(winner_Left + imageIndex * winner_Width, color.ordinal() * winner_Height, winner_Width, winner_Height);
    }

    public BufferedImage loser(GameObject.Player.PlayerEnum.Color color, int imageIndex) {
        return gfxFile.getSubimage(loser_Left + imageIndex * loser_Width, color.ordinal() * loser_Height, loser_Width, loser_Height);
    }

    public BufferedImage leftBoundary() {
        return gfxFile.getSubimage(boundary_Left, 0, boundary_Width, boundary_Width);
    }

    public BufferedImage rightBoundary() {
        return gfxFile.getSubimage(boundary_Left + 3 * boundary_Width, 0, boundary_Width, boundary_Width);
    }

    public BufferedImage topBoundary(int imageNumber) {
        return gfxFile.getSubimage(boundary_Left + (imageNumber + 1) * boundary_Width, 0, boundary_Width, boundary_Width);
    }

    public BufferedImage bottomBoundary() {
        return gfxFile.getSubimage(boundary_Left + 4 * boundary_Width, 0, boundary_Width, boundary_Width);
    }

    public BufferedImage outerGrid() {
        return gfxFile.getSubimage(outerGrid_Left, 0, outerGrid_Width, outerGrid_Width);
    }

    public BufferedImage outerHome(int imageNumber) {
        return gfxFile.getSubimage(outerHome_Left + imageNumber * outerHome_Width, 0, outerHome_Width, outerHome_Height);
    }

    public BufferedImage grassBoundaryTopLeft() {
        return gfxFile.getSubimage(grass_Left, 0, grass_Width, grass_Width);
    }

    public BufferedImage grassBoundaryTop() {
        return gfxFile.getSubimage(grass_Left + grass_Width, 0, grass_Width, grass_Width);
    }

    public BufferedImage grassBoundaryRight() {
        return gfxFile.getSubimage(grass_Left + grass_Width, 3 * grass_Width, grass_Width, grass_Width);
    }

    public BufferedImage grassBoundaryLeft(int imageNumber) {
        return gfxFile.getSubimage(grass_Left, (imageNumber + 1) * grass_Width, grass_Width, grass_Width);
    }

    public BufferedImage grassShadow(int imageNumber) {
        if (imageNumber == 2) {
            return gfxFile.getSubimage(grass_Left, 3 * grass_Width, grass_Width, grass_Width);
        }
        return gfxFile.getSubimage(grass_Left + grass_Width, (imageNumber + 1) * grass_Width, grass_Width, grass_Width);
    }

    public BufferedImage roadX(int imageNumber) {
        return gfxFile.getSubimage(road_Left + imageNumber * road_Width, 0, road_Width, road_Width);
    }

    public BufferedImage roadY(int imageNumber) {
        return gfxFile.getSubimage(road_Left + 3 * road_Width, (imageNumber + 2) * road_Width, road_Width, road_Width);
    }

    public BufferedImage roadXY() {
        return gfxFile.getSubimage(road_Left + 3 * road_Width, 0, road_Width, road_Width);
    }

    public BufferedImage roadCrossX() {
        return gfxFile.getSubimage(road_Left + 3 * road_Width, road_Width, road_Width, road_Width);
    }

    public BufferedImage roadCrossY() {
        return gfxFile.getSubimage(road_Left + 2 * road_Width, 0, road_Width, road_Width);
    }

    public BufferedImage innerObject(int imageNumber) {
        return gfxFile.getSubimage(innerObj_Left + imageNumber * innerObj_Width, 0, innerObj_Width, innerObj_Height);
    }

    public BufferedImage stone(int imageNumber) {
        return gfxFile.getSubimage(stone_Left, imageNumber * stone_Width, stone_Width, stone_Width);
    }

    public BufferedImage readyGoHurry(int imageNumber) {
        return gfxFile.getSubimage(readyGoHurry_Left, imageNumber * readyGoHurry_Height, readyGoHurry_Width, readyGoHurry_Height);
    }

    public BufferedImage HUD() {
        return gfxFile.getSubimage(HUD_Left, HUD_Top, HUD_Width, HUD_Height);
    }

    public BufferedImage HUDstatus() {
        return gfxFile.getSubimage(HUD_Left + 40, HUD_Top, 29, HUD_Height);
    }

    public BufferedImage star() {
        return gfxFile.getSubimage(star_Left, star_Top, star_Width, star_Width);
    }

    public BufferedImage trophy() {
        return gfxFile.getSubimage(trophy_Left, trophy_Top, trophy_Width, trophy_Width);
    }

    public BufferedImage arrowLeft() {
        return gfxFile.getSubimage(arrowL_Left, arrowL_Top, arrowL_Width, arrowL_Width);
    }

    public BufferedImage arrowRight() {
        return gfxFile.getSubimage(arrowR_Left, arrowR_Top, arrowR_Width, arrowR_Width);
    }

    public BufferedImage arrowEditLeft() {
        return gfxFile.getSubimage(arrowEditL_Left, arrowEditL_Top, arrowEditL_Width, arrowEditL_Width);
    }

    public BufferedImage arrowEditRight() {
        return gfxFile.getSubimage(arrowEditR_Left, arrowEditR_Top, arrowEditR_Width, arrowEditR_Width);
    }

    public BufferedImage arrowUp() {
        return gfxFile.getSubimage(arrowU_Left, arrowU_Top, arrowU_Width, arrowU_Width);
    }

    public BufferedImage arrowDown() {
        return gfxFile.getSubimage(arrowD_Left, arrowD_Top, arrowD_Width, arrowD_Width);
    }

    public BufferedImage networkError() {
        return gfxFile.getSubimage(networkError_Left, networkError_Top, networkError_Width, networkError_Width);
    }

    public BufferedImage compatibilityError() {
        return gfxFile.getSubimage(compatibilityError_Left, compatibilityError_Top, compatibilityError_Width, compatibilityError_Width);
    }

}

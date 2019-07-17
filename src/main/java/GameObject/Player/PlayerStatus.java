package GameObject.Player;

import AppInfo.Initialized;
import FontRenderer.FontColor;
import FontRenderer.RenderString;
import GameObject.Player.PlayerEnum.Color;
import GameObject.Player.PlayerEnum.Status;
import Scene.AllScenes.GameScenes.MainGame.MainGame;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 10/10/2014.
 * This class helps to create a player status list used in HUD.
 */
public class PlayerStatus implements Comparable<PlayerStatus> {
    private static final int imageLocationY = 4;
    private static final int scoreImageLocationY = 7;
    // Note: there are more fixed values in the constructor.

    private Color color;

    private BufferedImage image;
    private int imageLocationX;

    private BufferedImage scoreImage;
    private int scoreImageLocationX;

    public PlayerStatus(Color color, Status status) {
        this.color = color;

        image = Initialized.gfx.playerStatus(color, status);
        scoreImage = RenderString.renderSmallString(Integer.toString(MainGame.gameResult.getRecord(color)), FontColor.Green);
        if (color == Color.White || color == Color.Black) {
            imageLocationX = color.ordinal() * 56 + 40;
            scoreImageLocationX = color.ordinal() * 56 + 58;
        } else if (color == Color.Blue || color == Color.Red) {
            imageLocationX = (color.ordinal() & 1) * 56 + 201;
            scoreImageLocationX = (color.ordinal() & 1) * 56 + 219;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setStatus(Status status) {
        image = Initialized.gfx.playerStatus(color, status);
    }

    public void render(Graphics2D g) {
        g.drawImage(image, imageLocationX, imageLocationY, null);
        g.drawImage(scoreImage, scoreImageLocationX, scoreImageLocationY, null);
    }

    public int compareTo(PlayerStatus o) {
        return Integer.compare(color.ordinal(), o.color.ordinal());
    }
}

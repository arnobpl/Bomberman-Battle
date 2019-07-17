package Scene.AllScenes.GameScenes;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import AppInfo.Strings;
import GameObject.Player.PlayerEnum.Color;
import GameObject.Player.PlayerEnum.Direction;
import GameObject.Result.Result;
import GraphicsRenderer.Animation.PingPong_Animation;
import GraphicsRenderer.Animation.TitleString_Animation;
import GraphicsRenderer.Animation.Transition_Animation;
import GraphicsRenderer.GfxFromFile;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import Network.BaseNetwork.BaseNetwork;
import Scene.AllScenes.GameScenes.MainGame.HurryUpState;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.Scene;
import SoundSystem.BaseSound;
import javafx.scene.media.Media;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 08/10/2014.
 * This class contains the scene of displaying result for each match.
 */
public class ResultScene extends Scene {
    private boolean isDraw = false;
    private boolean championFound = false;

    private static Color winnerPlayerColor;

    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(32, 224, 64);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(0, 32, 16);

    private static final int scoreBoardWidth = 240;
    private static final int scoreBoardHeight = 192;

    private static final java.awt.Color gridBlockColorUp = new java.awt.Color(34, 177, 76);
    private static final java.awt.Color gridBlockColorDown = new java.awt.Color(55, 217, 104);
    private static final java.awt.Color gridLineColor = new java.awt.Color(128, 128, 128);

    private static final int scoreBoardLeft = (AllSettings.unscaledWidth - scoreBoardWidth) >> 1;
    private static final int scoreBoardTop = ((AllSettings.unscaledHeight - scoreBoardHeight) >> 1) + 5;
    private int scoreBoardEachWidth = scoreBoardWidth / (MainGame.matchToWin + 1);  // minimum width is 48
    private int scoreBoardEachHeight = scoreBoardHeight / MainGame.totalPlayers;    // minimum height is 48

    private static final double titleStringMoveStep = -3 * AllSettings.userSettings.motionConst;
    private static final int titleStringTop = 1;

    private static TitleString_Animation titleStringAnimation;

    private int playerLeft = scoreBoardLeft + ((scoreBoardEachWidth - GfxFromFile.playerIdle_Width) >> 1);
    private int playerTop = scoreBoardTop + ((scoreBoardEachHeight - GfxFromFile.playerIdle_Height) >> 1);

    private static final double animateStep = 0.3 * AllSettings.userSettings.motionConst;
    private static final int winnerImageVibrateAmplitude = 2;

    private PingPong_Animation resultScenePlayerAnimation;
    private BufferedImage resultImage = new BufferedImage(scoreBoardWidth, scoreBoardHeight, BufferedImage.TYPE_INT_ARGB);
    private BufferedImage winnerImage = new BufferedImage(scoreBoardEachWidth, scoreBoardHeight, BufferedImage.TYPE_INT_ARGB);
    private static int winnerImageLeft;
    private static int winnerImageTop;
    private static final int winnerImageVibrateRandomRange = (winnerImageVibrateAmplitude << 1) + 1;

    private static int winnerImageLeft_vibrateOffset;
    private static int winnerImageTop_vibrateOffset;

    private static final int sceneTimeElapsed = 4000;
    private static final int transitionTimeElapsed = 1000;
    private static final double transitionAlphaStep = 10 * AllSettings.userSettings.motionConst;

    private static Transition_Animation transitionAnimation;

    private static final long soundPlaybackDelay = 1000;

    private static Media winnerMusic = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InGame.WinnerMusic);
    private static Media drawGameMusic = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InGame.DrawGameMusic);


    public ResultScene(Color color, boolean championFound) {    // called when winner or champion found
        winnerPlayerColor = color;
        this.championFound = championFound;
        if (championFound) BaseNetwork.closeAllConnection();
        createResultWinnerImage();
        doCommonConstructorTask();
        BaseSound.playSound(winnerMusic, AllSettings.userSettings.soundEffectVolume, soundPlaybackDelay);
    }

    public ResultScene() {  // called when no winner found
        isDraw = true;
        createResultDrawImage();
        doCommonConstructorTask();
        BaseSound.playSound(drawGameMusic, AllSettings.userSettings.soundEffectVolume, soundPlaybackDelay);
    }

    private void doCommonConstructorTask() {
        resultScenePlayerAnimation = new PingPong_Animation(2, animateStep, 1);
        transitionAnimation = new Transition_Animation(transitionTimeElapsed, transitionAlphaStep, sceneTimeElapsed).start();
        createBackground();

        MainGame.gameMusic.stop(BaseSound.fadeoutElapsed);
        HurryUpState.hurryUpMusic.stop(BaseSound.fadeoutElapsed);

        if (!thisSceneEverCreated) {
            titleStringAnimation = new TitleString_Animation(Strings.Game.ScoreBoard, titleStringMoveStep, titleStringTop);
            thisSceneEverCreated = true;
        }
    }

    private void createBackground() {
        GradientColor.createGradientBackground_alongY(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);

        Graphics2D g = gradientBackground.createGraphics();

        GradientColor color = new GradientColor(gridBlockColorUp, gridBlockColorDown, scoreBoardEachHeight);
        int scoreBoardX_end = scoreBoardLeft + scoreBoardWidth;
        for (int i = 0; i < MainGame.totalPlayers; i++) {
            int scoreBoardEachTop = scoreBoardTop + i * scoreBoardEachHeight;
            for (int j = 0; j < scoreBoardEachHeight; j++) {
                g.setColor(color.getGradientColor(j));
                g.drawLine(scoreBoardLeft, scoreBoardEachTop + j, scoreBoardX_end, scoreBoardEachTop + j);
            }
        }

        g.setColor(gridLineColor);
        g.drawRect(scoreBoardLeft, scoreBoardTop, scoreBoardWidth, scoreBoardHeight);

        int scoreBoardY_end = scoreBoardTop + scoreBoardHeight;
        for (int i = scoreBoardLeft + scoreBoardEachWidth; i < scoreBoardX_end; i += scoreBoardEachWidth) {
            g.drawLine(i, scoreBoardTop, i, scoreBoardY_end);
        }

        g.dispose();

        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
    }

    private void createResultWinnerImage() {
        Graphics2D g = resultImage.createGraphics();
        int leftOffset = ((scoreBoardEachWidth - GfxFromFile.star_Width) >> 1);
        int topOffset = ((scoreBoardEachHeight - GfxFromFile.star_Width) >> 1);
        for (int i = 0; i < MainGame.totalPlayers; i++) {
            int score = Result.records.get(i).getScore();
            if (i == winnerPlayerColor.ordinal()) score--;
            for (int j = 0; j < score; j++) {
                g.drawImage(Initialized.gfx.star(), (j + 1) * scoreBoardEachWidth + leftOffset, i * scoreBoardEachHeight + topOffset, null);
            }
        }
        g.dispose();

        g = winnerImage.createGraphics();
        winnerImageLeft = Result.records.get(winnerPlayerColor.ordinal()).getScore() * scoreBoardEachWidth + scoreBoardLeft;
        winnerImageTop = winnerPlayerColor.ordinal() * scoreBoardEachHeight + scoreBoardTop;
        if (championFound) {
            g.drawImage(Initialized.gfx.trophy(), leftOffset, topOffset, null);
        } else {
            g.drawImage(Initialized.gfx.star(), leftOffset, topOffset, null);
        }
        g.dispose();
    }

    private void createResultDrawImage() {
        Graphics2D g = resultImage.createGraphics();
        int leftOffset = ((scoreBoardEachWidth - GfxFromFile.star_Width) >> 1);
        int topOffset = ((scoreBoardEachHeight - GfxFromFile.star_Width) >> 1);
        for (int i = 0; i < MainGame.totalPlayers; i++) {
            int score = Result.records.get(i).getScore();
            for (int j = 0; j < score; j++) {
                g.drawImage(Initialized.gfx.star(), (j + 1) * scoreBoardEachWidth + leftOffset, i * scoreBoardEachHeight + topOffset, null);
            }
        }
        g.dispose();
    }

    public void update() {
        titleStringAnimation.animate();

        resultScenePlayerAnimation.animate();

        if (!isDraw) {
            winnerImageLeft_vibrateOffset = winnerImageVibrateAmplitude - Initialized.random.nextInt(winnerImageVibrateRandomRange);
            winnerImageTop_vibrateOffset = winnerImageVibrateAmplitude - Initialized.random.nextInt(winnerImageVibrateRandomRange);
        }

        transitionAnimation.tick();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

        titleStringAnimation.render(unscaledScreenGraphics);

        for (int i = 0; i < MainGame.totalPlayers; i++) {
            unscaledScreenGraphics.drawImage(Initialized.gfx.player(Color.valuesCached()[i], Direction.Down, resultScenePlayerAnimation.getAnimateValue()), playerLeft, i * scoreBoardEachHeight + playerTop, null);
        }

        unscaledScreenGraphics.drawImage(resultImage, scoreBoardLeft, scoreBoardTop, null);

        if (!isDraw) {
            unscaledScreenGraphics.drawImage(winnerImage, winnerImageLeft + winnerImageLeft_vibrateOffset, winnerImageTop + winnerImageTop_vibrateOffset, null);
        }

        if (transitionAnimation.render(unscaledScreenGraphics)) {
            if (championFound) BaseWindow.scene = new Champion(winnerPlayerColor);
            else {
                BaseWindow.scene = new MainGame((MainGame.selectedLocation + 1) & 1);
            }
        }

        g.scale(AllSettings.userSettings.displayScale, AllSettings.userSettings.displayScale);
        g.drawImage(unscaledScreenImage, 0, 0, null);
    }

}

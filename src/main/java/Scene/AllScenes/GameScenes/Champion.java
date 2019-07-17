package Scene.AllScenes.GameScenes;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import AppInfo.Strings;
import GameObject.Player.PlayerEnum.Color;
import GraphicsRenderer.Animation.Circular_Animation;
import GraphicsRenderer.Animation.PingPong_Animation;
import GraphicsRenderer.Animation.TitleString_Animation;
import GraphicsRenderer.Animation.Transition_Animation;
import GraphicsRenderer.GfxFromFile;
import GraphicsRenderer.GradientColor;
import GraphicsRenderer.RenderGraphics;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;
import Scene.Scene;
import SoundSystem.BaseSound;
import javafx.scene.media.Media;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 08/10/2014.
 * This class contains the scene of champion.
 */
public class Champion extends Scene {
    private static Color championPlayerColor;

    private static boolean thisSceneEverCreated = false;

    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage gradientBackground = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final java.awt.Color gradientBackgroundColorUp = new java.awt.Color(153, 217, 234);
    private static final java.awt.Color gradientBackgroundColorDown = new java.awt.Color(192, 128, 150);

    private static final double titleStringMoveStep = -3 * AllSettings.userSettings.motionConst;
    private static final int titleStringTop = 5;

    private static TitleString_Animation titleStringAnimation;

    private static final double winnerAnimateStep = 0.14 * AllSettings.userSettings.motionConst;
    private static final double winnerAnimateStepStable_Coefficient = 0.5;
    private static final double loserAnimateStep = 0.2 * AllSettings.userSettings.motionConst;

    private boolean stableAnimation = false;
    private static PingPong_Animation winnerAnimation;
    private static final int winnerLeft = (AllSettings.unscaledWidth - GfxFromFile.winner_Width) >> 1;
    private static final int winnerTop = (AllSettings.unscaledHeight - GfxFromFile.winner_Height) >> 1;
    private static PingPong_Animation loserAnimation;
    private static int loserPlayerNumber;
    private static final Color[] loserPlayerColor = new Color[3];
    private static final int[] loserPlayerImageLeft = new int[3];
    private static final int loserPlayerImageTop = AllSettings.unscaledHeight - GfxFromFile.loser_Height - 1;

    private static final int starNumber = 5;
    private static final double starAnimationStep = 0.01 * AllSettings.userSettings.motionConst;
    private static final int starAnimationDiameter = AllSettings.unscaledHeight_half;
    private static final int starAnimationDiameterExtend = starAnimationDiameter >> 2;
    private static final double starAnimationDiameterStep = 0.3;

    private static final Circular_Animation[] starAnimationList = new Circular_Animation[starNumber];
    private static PingPong_Animation starDiameterAnimation;

    private static final int timeElapsedAfterStable = 1500;
    private static final int transitionTimeElapsed = 3000;
    private static final double transitionAlphaStep = 4 * AllSettings.userSettings.motionConst;

    private static Transition_Animation transitionAnimation;

    private static final Media championMusic = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InGame.ChampionMusic);


    public Champion(Color championPlayerColor) {
        Champion.championPlayerColor = championPlayerColor;
        loserPlayerNumber = MainGame.totalPlayers - 1;
        createLoserPlayerColorList();
        createLoserImageLeft();
        winnerAnimation = new PingPong_Animation(9, winnerAnimateStep);
        loserAnimation = new PingPong_Animation(1, loserAnimateStep);
        createStarAnimation();
        transitionAnimation = new Transition_Animation(transitionTimeElapsed, transitionAlphaStep, timeElapsedAfterStable);
        BaseSound.playSound(championMusic, AllSettings.userSettings.soundEffectVolume);

        if (!thisSceneEverCreated) {
            GradientColor.createGradientBackground_alongY(gradientBackground, gradientBackgroundColorUp, gradientBackgroundColorDown);
            titleStringAnimation = new TitleString_Animation(Strings.Game.GameOver, titleStringMoveStep, titleStringTop);
            thisSceneEverCreated = true;
        }

        if (AllSettings.userSettings.cinematicEffectEnabled)
            RenderGraphics.setAlpha(gradientBackground, AllSettings.motionBlurAlpha);
        else RenderGraphics.setAlphaOpaque(gradientBackground);
    }

    private void createLoserPlayerColorList() {
        int index = 0;
        for (int i = 0; i < MainGame.totalPlayers; i++) {
            if (championPlayerColor != Color.valuesCached()[i]) {
                loserPlayerColor[index++] = Color.valuesCached()[i];
            }
        }
    }

    private void createLoserImageLeft() {
        int loserPlayerImageX_gap = (AllSettings.unscaledWidth - loserPlayerNumber * GfxFromFile.loser_Width) / MainGame.totalPlayers;
        int loserPlayerImageX_Interval = loserPlayerImageX_gap + GfxFromFile.loser_Width;
        for (int i = 0; i < loserPlayerNumber; i++) {
            loserPlayerImageLeft[i] = loserPlayerImageX_gap + i * loserPlayerImageX_Interval;
        }
    }

    private void createStarAnimation() {
        double starAnimateValueInterval = 1.0 / starNumber;
        for (int i = 0; i < starNumber; i++) {
            starAnimationList[i] = new Circular_Animation(starAnimationStep, starAnimationDiameter, i * starAnimateValueInterval);
            starAnimationList[i].setCenterX(AllSettings.unscaledWidth_half, GfxFromFile.star_Width);
            starAnimationList[i].setCenterY(AllSettings.unscaledHeight_half, GfxFromFile.star_Width);
        }
        starDiameterAnimation = new PingPong_Animation(starAnimationDiameterExtend, starAnimationDiameterStep);
    }

    public void update() {
        titleStringAnimation.animate();

        winnerAnimation.animate();
        if (!stableAnimation) { // check if winnerAnimation is in 'stable' state
            if (!winnerAnimation.isAnimateStepPositive()) {
                winnerAnimation.setAnimateStepByFactor(winnerAnimateStepStable_Coefficient);
                winnerAnimation.setUpperLimit(1);
                winnerAnimation.resetAnimation();
                stableAnimation = true;
            }
        } else {
            transitionAnimation.start();
        }
        loserAnimation.animate();

        for (Circular_Animation i : starAnimationList) {
            starDiameterAnimation.animate();
            i.animate_Clockwise(starAnimationDiameter + starDiameterAnimation.getAnimateValue_Double());
        }

        transitionAnimation.tick();
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
        unscaledScreenGraphics.drawImage(gradientBackground, 0, 0, null);

        titleStringAnimation.render(unscaledScreenGraphics);

        if (!stableAnimation) {
            unscaledScreenGraphics.drawImage(Initialized.gfx.winnerUnstable(championPlayerColor, winnerAnimation.getAnimateValue()), winnerLeft, winnerTop, null);
        } else {
            unscaledScreenGraphics.drawImage(Initialized.gfx.winnerStable(championPlayerColor, winnerAnimation.getAnimateValue()), winnerLeft, winnerTop, null);
        }

        for (int i = 0; i < loserPlayerNumber; i++) {
            unscaledScreenGraphics.drawImage(Initialized.gfx.loser(loserPlayerColor[i], loserAnimation.getAnimateValue()), loserPlayerImageLeft[i], loserPlayerImageTop, null);
        }

        for (Circular_Animation i : starAnimationList) {
            unscaledScreenGraphics.drawImage(Initialized.gfx.star(), i.getX(), i.getY(), null);
        }

        if (transitionAnimation.render(unscaledScreenGraphics)) {
            BaseWindow.scene = new MainMenu();
        }

        g.scale(AllSettings.userSettings.displayScale, AllSettings.userSettings.displayScale);
        g.drawImage(unscaledScreenImage, 0, 0, null);
    }

}

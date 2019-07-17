package GameObject.Bomb;

import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import GameObject.Location.GridLocation;
import GameObject.Location.Location;
import GameObject.Player.InputController.InputController;
import GameObject.Player.OnilnePlayer.Player;
import GraphicsRenderer.Animation.PingPong_Animation;
import GraphicsRenderer.GraphicsObject;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import SoundSystem.BaseSound;
import javafx.scene.media.Media;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Arnob on 03/10/2014.
 * This class contains all variables and methods related to bomb and explosion.
 */
public class Bomb implements GraphicsObject {

    private final Player bombOwner;
    private GridLocation gridLocation;

    private BufferedImage image;
    private double imageLocationX;
    private double imageLocationY;
    private PingPong_Animation bombAnimation;

    private static final double animateStep = 0.2 * AllSettings.userSettings.motionConst;
    private static final double animateStepCoefficientBeforeExplosion = 1.02;

    public static final int bombPower = 4;
    public static final int explosionTimeout = 3000;
    private static final int explodingTime = 400;
    private static final int explosionEndingPerGrid = 60;
    private static final double explosionEndingPerGridCoefficient = 0.7; // lower value for faster explosion ending of the farthest objects from the bomb centre
    private static final int explosionAnimateStepCoefficient = 8;

    private int bombStarted = 0;

    // This is used to prevent more than one call of 'getBombWithMaxTimeElapsed()' for this bomb.
    // New bomb after call cannot normally exceed previous 'bombWithMaxTimeElapsed.bombStarted' value.
    // But it may exceed if 2 non-chaining nearby bombs are connected with a bomb after call.
    private boolean bombTimeElapsedAdvancedCalculated = false;
    private Bomb bombWithMaxTimeElapsed;

    // This is used in other objects to check whether a new bomb has been created
    // or any bomb has been destroyed for optimization reason.
    private static int bombGlobalID = 0;

    private static final int explosionEndingTimeout = explosionTimeout + explodingTime;
    private boolean exploding = false;
    private boolean explosionEnding = false;
    public static final int[][] directionStep = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private final List<Explosion> explosionImageList = new ArrayList<>();

    public static final int[] explosionEndingPerGridList;
    public static final int[] explosionEndingPerGrid2xList;

    private static final Bomb[] bombListByGridLocation = new Bomb[Location.totalMovableGridLocation];

    private int indexForBombListSortedByGridLocation;

    private static final Media createBombSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InGame.CreateBombSound);
    private static final Media explosionSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InGame.ExplosionSound);

    private double soundBalance = 0.0;

    static {
        explosionEndingPerGridList = new int[bombPower];
        explosionEndingPerGrid2xList = new int[bombPower];
        for (int i = 0; i < bombPower; i++) {
            explosionEndingPerGridList[i] = (int) (i * explosionEndingPerGridCoefficient * AllSettings.userSettings.timeStep) + explosionEndingPerGrid;
            explosionEndingPerGrid2xList[i] = explosionEndingPerGridList[i] + explosionEndingPerGrid;
        }
    }


    public Bomb(Player bombOwner, GridLocation gridLocation) {
        this.bombOwner = bombOwner;
        this.gridLocation = new GridLocation(gridLocation.x, gridLocation.y);
        imageLocationX = GridLocation.getGridImageLocationX_withoutOffset(gridLocation.x);
        imageLocationY = GridLocation.getGridImageLocationY_withoutOffset(gridLocation.y);
        bombAnimation = new PingPong_Animation(2, animateStep, 2);
        image = Initialized.gfx.bomb(bombAnimation.getAnimateValue());

        indexForBombListSortedByGridLocation = Location.indexOfMovableLocationByGridLocation(gridLocation);
        bombListByGridLocation[indexForBombListSortedByGridLocation] = this;

        MainGame.graphicsObjectList.add(this);

        bombGlobalID++;

        soundBalance = BaseSound.getSoundBalance(gridLocation);
        BaseSound.playSound(createBombSound, AllSettings.userSettings.soundEffectVolume, soundBalance);
    }

    private void BombDestructor(Iterator iterator) {
        iterator.remove();
        bombOwner.decreaseBombCounter();
        bombListByGridLocation[indexForBombListSortedByGridLocation] = null;

        bombGlobalID++;
    }


    public static void initializeBombListSortedByGridLocation() {
        for (int i = 0; i < bombListByGridLocation.length; i++) bombListByGridLocation[i] = null;
    }

    /**
     * @return <code>Bomb</code> if any bomb is currently placed at the <code>GridLocation</code>, otherwise <code>null</code>
     */
    public static Bomb bombAtGridLocation(GridLocation gridLocation) {
        return bombListByGridLocation[Location.indexOfMovableLocationByGridLocation(gridLocation)];
    }


    public GridLocation getGridLocation() {
        return gridLocation;
    }

    public int getGridLocationX() {
        return gridLocation.x;
    }

    public int getGridLocationY() {
        return gridLocation.y;
    }

    public double getImageLocationX() {
        return imageLocationX;
    }

    public double getImageLocationY() {
        return imageLocationY;
    }


    public void update(Iterator iterator) {
        bombStarted += AllSettings.userSettings.timeStep;
        // checking if the bomb has not exploded
        if (bombStarted < explosionTimeout) {
            bombAnimation.animate();
            bombAnimation.setAnimateStepByFactor(animateStepCoefficientBeforeExplosion);
            image = Initialized.gfx.bomb(bombAnimation.getAnimateValue());
        } else {
            // checking if explosion is happening
            if (exploding) {
                // checking if bomb is in the explosion state
                if (!explosionEnding && bombStarted < explosionEndingTimeout) {
                    bombAnimation.animate();
                    for (Explosion i : explosionImageList) {
                        i.update(bombAnimation.getAnimateValue());
                    }
                    image = Initialized.gfx.explosionCenter(bombAnimation.getAnimateValue());
                }
                // bomb is finishing its explosion
                else {
                    explosionEnding = true;
                    explosionImageList.removeIf(Explosion::updateEnding);
                    if (explosionImageList.isEmpty()) {
                        BombDestructor(iterator);
                    }
                }
            }
            // explosion has just started
            else {
                exploding = true;
                bombAnimation.setAnimateStep(animateStep * explosionAnimateStepCoefficient);
                bombAnimation.setUpperLimit(3); // Though the sprite sheet contains 5 images, the last image creates bad animation
                bombAnimation.resetAnimation();
                image = Initialized.gfx.explosionCenter(bombAnimation.getAnimateValue());
                BaseSound.playSound(explosionSound, AllSettings.userSettings.soundEffectVolume, soundBalance);
                // checking if player in the same grid of the bomb
                for (InputController p : MainGame.inputControllersList) {
                    if (gridLocation.equals(p.getGridLocation())) {
                        p.kill();
                    }
                }
                // checking each direction
                for (int i = 0; i < directionStep.length; i++) {
                    GridLocation g = new GridLocation(gridLocation.x, gridLocation.y);
                    for (int j = 0; j < bombPower; j++) {
                        g.x += directionStep[i][0];
                        g.y += directionStep[i][1];
                        // checking boundary and inner obstacle
                        if ((g.x < 0 || g.x > Location.totalGridWidth) || (g.y < 0 || g.y > Location.totalGridHeight) || ((g.x & 1) == 1 && (g.y & 1) == 1)) {
                            break;
                        }
                        // checking other bomb for invoking early explosion
                        Bomb nearbyBomb = bombAtGridLocation(g);
                        if (nearbyBomb != null) nearbyBomb.invokeEarlyExplosion();
                        // checking player before explosion
                        for (InputController p : MainGame.inputControllersList) {
                            if (g.equals(p.getGridLocation())) {
                                p.kill();
                            }
                        }
                        if (j + 1 == bombPower) {
                            explosionImageList.add(new Explosion(Initialized.gfx.explosion(Direction.valuesCached()[i], 0, true), g, Direction.valuesCached()[i], true, j));
                        } else {
                            explosionImageList.add(new Explosion(Initialized.gfx.explosion(Direction.valuesCached()[i], 0, false), g, Direction.valuesCached()[i], false, j));
                        }
                    }
                }
            }
        }
    }

    public void render(Graphics2D g) {
        if (!explosionEnding) {
            g.drawImage(image, (int) Math.round(imageLocationX + Location.offsetX), (int) Math.round(imageLocationY + Location.offsetY), null);
        }
        if (exploding) {
            for (Explosion i : explosionImageList) {
                g.drawImage(i.image, (int) Math.round(i.imageLocationX + Location.offsetX), (int) Math.round(i.imageLocationY + Location.offsetY), null);
            }
        }
    }


    public void invokeEarlyExplosion() {
        if (!exploding) bombStarted = explosionTimeout;
    }


    public Player getBombOwner() {
        return bombOwner;
    }


    /**
     * This method is for checking whether a new bomb has been created
     * or any bomb has been destroyed for optimization reason.
     *
     * @return <code>bombGlobalID</code>
     */
    public static int getBombGlobalID() {
        return bombGlobalID;
    }


    /**
     * This method does not consider early explosion by consecutive bombs.
     * But this method returns <code>getBombTimeElapsedAdvanced()</code> value if previously calculated.
     */
    public int getBombTimeElapsed() {
        if (bombTimeElapsedAdvancedCalculated) return bombWithMaxTimeElapsed.bombStarted;
        return bombStarted;
    }

    /**
     * This method considers early explosion by consecutive bombs at one level.
     * This method gives a better output (but not perfect because of checking only at one level) than the output of <code>getBombTimeElapsed()</code> method.
     * If it was calculated previously, then it returns value instantly due to memoization technique.
     */
    public int getBombTimeElapsedAdvanced() {
        return getBombWithMaxTimeElapsed().getBombTimeElapsed();
    }

    /**
     * This method considers early explosion by consecutive bombs at one level.
     * This method gives a better output (but not perfect because of checking only at one level) than the output of <code>getBombTimeElapsed()</code> method.
     * If it was calculated previously, then it returns value instantly due to memoization technique.
     */
    public Bomb getBombWithMaxTimeElapsed() {
        if (bombTimeElapsedAdvancedCalculated) return bombWithMaxTimeElapsed;

        bombWithMaxTimeElapsed = this;
        for (int i = 0; i < directionStep.length; i++) {
            int x = gridLocation.x;
            int y = gridLocation.y;
            for (int j = 0; j < Bomb.bombPower; j++) {
                x += Bomb.directionStep[i][0];
                y += Bomb.directionStep[i][1];
                // checking boundary and inner obstacle
                if ((x < 0 || x > Location.totalGridWidth) || (y < 0 || y > Location.totalGridHeight) || ((x & 1) == 1 && (y & 1) == 1)) {
                    break;
                }
                // checking bomb at this position
                Bomb nearbyBomb = Bomb.bombAtGridLocation(new GridLocation(x, y));
                if (nearbyBomb != null && (nearbyBomb.getBombTimeElapsed() > bombWithMaxTimeElapsed.bombStarted)) {
                    bombWithMaxTimeElapsed = nearbyBomb;
                }
            }
        }
        bombTimeElapsedAdvancedCalculated = true;
        return bombWithMaxTimeElapsed;
    }

}

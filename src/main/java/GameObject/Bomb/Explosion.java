package GameObject.Bomb;

import AppInfo.Customization.AllSettings;
import AppInfo.Initialized;
import GameObject.Location.GridLocation;
import GameObject.Player.InputController.InputController;
import Scene.AllScenes.GameScenes.MainGame.MainGame;

import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 07/10/2014.
 * This class contains a structure of necessary objects and update method for rendering explosion.
 */
public class Explosion {
    public BufferedImage image;
    private GridLocation gridLocation;
    public double imageLocationX;
    public double imageLocationY;

    private Direction direction;
    private boolean isCorner;

    private boolean ending = false;
    private int endingStarted = 0;
    private int distanceFromCentre;

    public Explosion(BufferedImage image, GridLocation gridLocation, Direction direction, boolean isCorner, int distanceFromCentre) {
        this.image = image;
        this.gridLocation = new GridLocation(gridLocation);
        this.imageLocationX = GridLocation.getGridImageLocationX_withoutOffset(gridLocation.x);
        this.imageLocationY = GridLocation.getGridImageLocationY_withoutOffset(gridLocation.y);
        this.direction = direction;
        this.isCorner = isCorner;
        this.distanceFromCentre = distanceFromCentre;
    }

    public void update(int imageIndex) {
        image = Initialized.gfx.explosion(direction, imageIndex, isCorner);
        // checking player while explosion
        for (InputController p : MainGame.inputControllersList) {
            if (gridLocation.equals(p.getGridLocation())) {
                p.kill();
            }
        }
    }

    /**
     * @return <code>true</code> if this object should be removed, otherwise <code>false</code>
     */
    public boolean updateEnding() {
        endingStarted += AllSettings.userSettings.timeStep;
        if (ending) {
            if (endingStarted > Bomb.explosionEndingPerGrid2xList[distanceFromCentre]) {
                return true;
            } else if (endingStarted > Bomb.explosionEndingPerGridList[distanceFromCentre]) {
                image = Initialized.gfx.explosionEnding(direction, 1);
            }
        } else {
            ending = true;
            image = Initialized.gfx.explosionEnding(direction, 0);
        }
        // checking player while ending explosion
        for (InputController p : MainGame.inputControllersList) {
            if (gridLocation.equals(p.getGridLocation())) {
                p.kill();
            }
        }
        return false;
    }

}

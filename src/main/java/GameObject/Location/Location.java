package GameObject.Location;

import GraphicsRenderer.GfxFromFile;
import GraphicsRenderer.GraphicsObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * Created by Arnob on 03/10/2014.
 * This class contains all location info and methods.
 */
public class Location implements GraphicsObject {
    public static final int totalGridWidth = 12;   // zero-based, equal
    public static final int totalGridHeight = 10;  // zero-based, equal

    public static final int totalGridWidth_half = totalGridWidth >> 1;
    public static final int totalGridHeight_half = totalGridHeight >> 1;

    public static final int offsetX_absolute = 3 * GfxFromFile.gridWidth;               // x-axis distance between (0, 0) and game-board's top-left point
    public static final int offsetY_absolute = (int) (2.25 * GfxFromFile.gridWidth);    // y-axis distance between (0, 0) and game-board's top-left point

    public static int offsetX = offsetX_absolute;
    public static int offsetY = offsetY_absolute;

    public static final int gridWidth_half = GfxFromFile.gridWidth >> 1;

    public static final int totalWidth = totalGridWidth * GfxFromFile.gridWidth;
    public static final int totalHeight = totalGridHeight * GfxFromFile.gridWidth;

    public static final int totalMovableGridLocation = (totalGridWidth + 1) * (totalGridHeight + 1) - ((totalGridWidth + 1) >> 1) * ((totalGridHeight + 1) >> 1);
    public static final int totalMovableGridLocationInEachTwoRow = ((totalGridWidth + 1) << 1) - ((totalGridWidth + 1) >> 1);


    public static BufferedImage[] innerObjectImage = new BufferedImage[totalGridHeight >> 1];

    private BufferedImage image;
    private double imageLocationX;
    private double imageLocationY;
    private static final int imageLocationY_offset = GfxFromFile.gridWidth - GfxFromFile.innerObj_Height;
    private GridLocation gridLocation;


    public Location(int imageIndex) {
        image = innerObjectImage[imageIndex];
        imageLocationX = 0;
        imageLocationY = ((imageIndex << 1) + 1) * GfxFromFile.gridWidth;
        gridLocation = new GridLocation(0, (imageIndex << 1) + 1);
    }


    public GridLocation getGridLocation() {
        return gridLocation;
    }

    public double getImageLocationX() {
        return imageLocationX;
    }

    public double getImageLocationY() {
        return imageLocationY;
    }


    public void update(Iterator iterator) {
    }

    public void render(Graphics2D g) {
        g.drawImage(image, (int) Math.round(imageLocationX + offsetX), (int) Math.round(imageLocationY + offsetY + imageLocationY_offset), null);
    }


    public static void changeOffset(int offsetX, int offsetY) {
        Location.offsetX = offsetX_absolute + offsetX;
        Location.offsetY = offsetY_absolute + offsetY;
    }


    /**
     * @return <code>true</code> if <code>gridLocation</code> has no inner obstacle, otherwise <code>false</code>
     */
    public static boolean isMovableLocation(GridLocation gridLocation) {
        return (((gridLocation.x & 1) == 0) || ((gridLocation.y & 1) == 0));
    }

    /**
     * @param gridLocation it must be a movable location,
     *                     otherwise an invalid value will be returned or an exception will be thrown
     * @return index of the movable location in a list of movable locations
     */
    public static int indexOfMovableLocationByGridLocation(GridLocation gridLocation) {
        int index = (gridLocation.y >> 1) * totalMovableGridLocationInEachTwoRow;
        if ((gridLocation.y & 1) == 0) index += gridLocation.x;
        else index = index + (totalGridWidth + 1) + (gridLocation.x >> 1);
        return index;
    }

}

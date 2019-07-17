package GameObject.Location;

import GraphicsRenderer.GfxFromFile;

/**
 * Created by Arnob on 03/10/2014.
 * This class contains GridLocation for player, bomb and location.
 * This class also has some methods for obtaining the imageLocation in various ways.
 */
public class GridLocation {

    public int x;
    public int y;


    public GridLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GridLocation(GridLocation gridLocation) {
        x = gridLocation.x;
        y = gridLocation.y;
    }


    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof GridLocation) {
            GridLocation gridLocation = (GridLocation) obj;
            return x == gridLocation.x && y == gridLocation.y;
        }
        return false;
    }

    public String toString() {
        return x + " " + y;
    }


    public int getGridImageLocationX() {
        return x * GfxFromFile.gridWidth + Location.offsetX;
    }

    public int getGridImageLocationY() {
        return y * GfxFromFile.gridWidth + Location.offsetY;
    }


    public int getGridImageLocationX_withoutOffset() {
        return x * GfxFromFile.gridWidth;
    }

    public int getGridImageLocationY_withoutOffset() {
        return y * GfxFromFile.gridWidth;
    }


    public static int getGridImageLocationX(int i) {
        return i * GfxFromFile.gridWidth + Location.offsetX;
    }

    public static int getGridImageLocationY(int i) {
        return i * GfxFromFile.gridWidth + Location.offsetY;
    }


    public static int getGridImageLocationX_withoutOffset(int i) {
        return i * GfxFromFile.gridWidth;
    }

    public static int getGridImageLocationY_withoutOffset(int i) {
        return i * GfxFromFile.gridWidth;
    }

    /**
     * @return the heuristic path distance between <code>location1</code> and <code>location2</code>
     */
    public static int getDistance(GridLocation location1, GridLocation location2) {
        return Math.abs(location1.x - location2.x) + Math.abs(location1.y - location2.y);
    }

}

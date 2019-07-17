package GraphicsRenderer;

import GameObject.Location.GridLocation;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by Arnob on 26/09/2014.
 * This interface creates a common environment for different types of graphic objects.
 */
public interface GraphicsObject {
    RenderingOrder renderingOrder = new RenderingOrder();

    GridLocation getGridLocation();

    double getImageLocationX();

    double getImageLocationY();


    void update(Iterator iterator);

    void render(Graphics2D g);
}

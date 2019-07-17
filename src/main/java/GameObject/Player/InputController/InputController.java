package GameObject.Player.InputController;

import GameObject.Location.GridLocation;
import GameObject.Player.PlayerEnum.Color;
import GameObject.Player.PlayerEnum.Direction;

/**
 * Created by Arnob on 06/10/2014.
 * This interface creates a common and sequential environment for input controller.
 */
public interface InputController {
    InputControllerOrder inputControllerOrder = new InputControllerOrder();

    GridLocation getGridLocation();

    int getID();

    Color getColor();

    Direction getDirection();

    void processInput();

    void kill();

    boolean isKilled();
}

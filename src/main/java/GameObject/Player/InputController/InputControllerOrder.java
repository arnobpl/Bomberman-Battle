package GameObject.Player.InputController;

import java.util.Comparator;

/**
 * Created by Arnob on 22/07/2015.
 * This class is responsible for the order of <code>InputController</code> objects.
 */
public class InputControllerOrder implements Comparator<InputController> {
    public int compare(InputController p1, InputController p2) {
        return Integer.compare(p1.getID(), p2.getID());
    }
}

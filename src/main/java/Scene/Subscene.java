package Scene;

import java.awt.*;

/**
 * Created by Arnob on 13/12/2014.
 * This interface is implements where a subscene is needed (such as in MainGame).
 */
public interface Subscene {
    void update();

    void render(Graphics2D g);
}

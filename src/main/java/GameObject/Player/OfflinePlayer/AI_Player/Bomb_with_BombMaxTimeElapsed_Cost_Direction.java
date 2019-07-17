package GameObject.Player.OfflinePlayer.AI_Player;

import GameObject.Bomb.Bomb;
import GameObject.Bomb.Direction;

/**
 * Created by Arnob on 16/04/2015.
 * This class is helpful to analyse a nearby bomb by its distance from the current position.
 * <code>bombWithMaxTimeElapsed</code> is helpful to store <code>getBombTimeElapsedAdvanced()</code> value.
 * Nearer bomb has higher <code>cost</code> value. Also, multiple consecutive bombs increase this value.
 * Direction means the direction to where the bomb is placed. That is helpful to get optimal attacking location.
 */
public class Bomb_with_BombMaxTimeElapsed_Cost_Direction {
    public Bomb bomb;
    public Bomb bombWithMaxTimeElapsed;
    public int cost;
    public Direction direction;

    public Bomb_with_BombMaxTimeElapsed_Cost_Direction(Bomb bomb, Bomb bombWithMaxTimeElapsed, int cost, Direction direction) {
        this.bomb = bomb;
        this.bombWithMaxTimeElapsed = bombWithMaxTimeElapsed;
        this.cost = cost;
        this.direction = direction;
    }
}

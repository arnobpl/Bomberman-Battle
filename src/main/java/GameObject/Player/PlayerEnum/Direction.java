package GameObject.Player.PlayerEnum;

/**
 * Created by Arnob on 26/09/2014.
 * This enum helps to easily access player's direction.
 */
public enum Direction {
    Down,
    Left,
    Right,
    Up;

    private static final Direction[] values = values();

    /**
     * You should just read it. Do not change it.
     */
    public static Direction[] valuesCached() {
        return values;
    }
}

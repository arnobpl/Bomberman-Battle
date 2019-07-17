package GameObject.Bomb;

/**
 * Created by Arnob on 03/10/2014.
 * This enum helps to easily access bomb's direction.
 */
public enum Direction {
    Left,
    Right,
    Up,
    Down;

    private static final Direction[] values = values();

    /**
     * You should just read it. Do not change it.
     */
    public static Direction[] valuesCached() {
        return values;
    }
}

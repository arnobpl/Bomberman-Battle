package GameObject.Player.PlayerEnum;

/**
 * Created by Arnob on 26/09/2014.
 * This enum helps to easily access player's color.
 */
public enum Color {
    White,
    Black,
    Blue,
    Red;

    public static final int totalColor = 4;

    private static final Color[] values = values();

    /**
     * You should just read it. Do not change it.
     */
    public static Color[] valuesCached() {
        return values;
    }
}

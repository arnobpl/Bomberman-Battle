package FontRenderer;

/**
 * Created by Arnob on 10/10/2014.
 * This enum helps to access font's color.
 */
public enum FontColor {
    Ash,
    Brown,
    Blue,
    Red,
    Orange,
    Green;

    public static final int totalColor = 6;

    private static final FontColor[] values = values();

    /**
     * You should just read it. Do not change it.
     */
    public static FontColor[] valuesCached() {
        return values;
    }
}

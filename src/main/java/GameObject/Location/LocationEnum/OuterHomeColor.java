package GameObject.Location.LocationEnum;

/**
 * Created by Arnob on 05/10/2014.
 * This enum helps to easily access outer home's color.
 */
public enum OuterHomeColor {
    Red,
    Yellow,
    Blue;

    private static final OuterHomeColor[] values = values();

    /**
     * You should just read it. Do not change it.
     */
    public static OuterHomeColor[] valuesCached() {
        return values;
    }
}

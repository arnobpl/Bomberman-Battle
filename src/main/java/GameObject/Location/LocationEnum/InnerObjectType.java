package GameObject.Location.LocationEnum;

/**
 * Created by Arnob on 05/10/2014.
 * This enum helps to easily access inner home's type.
 */
public enum InnerObjectType {
    Tree,
    RedHome,
    YellowHome,
    BlueHome;

    private static final InnerObjectType[] values = values();

    /**
     * You should just read it. Do not change it.
     */
    public static InnerObjectType[] valuesCached() {
        return values;
    }
}

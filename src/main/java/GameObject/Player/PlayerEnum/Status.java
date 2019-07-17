package GameObject.Player.PlayerEnum;

/**
 * Created by Arnob on 02/10/2014.
 * This enum helps to easily access player's status.
 */
public enum Status {
    Myself,
    Opponent,
    Killed;

    private static final Status[] values = values();

    /**
     * You should just read it. Do not change it.
     */
    public static Status[] valuesCached() {
        return values;
    }
}

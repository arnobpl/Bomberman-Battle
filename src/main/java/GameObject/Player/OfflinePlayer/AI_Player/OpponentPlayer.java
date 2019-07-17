package GameObject.Player.OfflinePlayer.AI_Player;

import GameObject.Location.GridLocation;
import GameObject.Player.OnilnePlayer.Player;
import GameObject.Player.PlayerEnum.Color;

import java.util.Iterator;

/**
 * Created by Arnob on 15/04/2015.
 * This class is interally used by AI_Player for path distance and enmity ranking.
 */
public class OpponentPlayer implements Comparable<OpponentPlayer> {

    private final Player opponentPlayer;
    private final Color opponentPlayerColor;
    private final GridLocation opponentPlayerGridLocation;
    private final GridLocation selfPlayerGridLocation;

    private int enmity = 0;
    private int distance = Integer.MAX_VALUE;
    private int differenceOfEnmityAndDistance = -Integer.MAX_VALUE;

    /**
     * @param opponentPlayer the opponent player to attack or defense
     * @param selfPlayer     the AI player for which <code>OpponentPlayer</code> object is created
     */
    public OpponentPlayer(Player opponentPlayer, Player selfPlayer) {
        this.opponentPlayer = opponentPlayer;
        opponentPlayerColor = opponentPlayer.getColor();
        opponentPlayerGridLocation = opponentPlayer.getGridLocation();
        selfPlayerGridLocation = selfPlayer.getGridLocation();
    }

    /**
     * @return <code>opponentPlayer</code>'s <code>Color</code>
     */
    public Color getColor() {
        return opponentPlayerColor;
    }

    /**
     * @return <code>opponentPlayer</code>'s <code>GridLocation</code>
     */
    public GridLocation getGridLocation() {
        return opponentPlayerGridLocation;
    }

    /**
     * This method removes killed player.
     * If not killed, it updates the distance between <code>selfPlayer</code> and <code>opponentPlayer</code>.
     * It also updates <code>differenceOfEnmityAndDistance</code>.
     */
    public void update(Iterator iterator) {
        if (opponentPlayer.isKilled()) {
            iterator.remove();
            return;
        }
        distance = GridLocation.getDistance(opponentPlayerGridLocation, selfPlayerGridLocation);
        updateDifferenceOfEnmityAndDistance();
    }

    /**
     * <code>update()</code> must be called before calling this method.
     *
     * @return the heuristic path distance between <code>opponentPlayer</code> and <code>selfPlayer</code>
     */
    public int getDistance() {
        return distance;
    }

    /**
     * @return the heuristic path distance between <code>opponentPlayer</code> and <code>gridLocation</code>
     */
    public int getDistance(GridLocation gridLocation) {
        return GridLocation.getDistance(opponentPlayerGridLocation, gridLocation);
    }

    public void increaseEnmity(int enmityIncrementValue, int multiplier) {
        enmity += (enmityIncrementValue * multiplier);
    }

    public void increaseEnmity(int enmityIncrementValue) {
        enmity += enmityIncrementValue;
    }

    public int getEnmity() {
        return enmity;
    }

    public void updateDifferenceOfEnmityAndDistance() {
        differenceOfEnmityAndDistance = enmity - distance;
    }

    public int getDifferenceOfEnmityAndDistance() {
        return differenceOfEnmityAndDistance;
    }

    /**
     * Sort to place the most-wanted players in lower indexes
     */
    public int compareTo(OpponentPlayer o) {
        return o.differenceOfEnmityAndDistance - differenceOfEnmityAndDistance;
    }
}

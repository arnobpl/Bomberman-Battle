package GameObject.Player.OfflinePlayer.AI_Player;

import GameObject.Bomb.Bomb;
import GameObject.Location.GridLocation;

/**
 * Created by Arnob on 15/04/2015.
 * This class generates cost of <code>currentLocation</code> for moving to <code>destinationLocation</code>
 */
public class GridLocationWithCost implements Comparable<GridLocationWithCost> {

    public final GridLocation currentLocation;
    public int cost = Integer.MAX_VALUE;

    public int passingGridLocationNumber;
    public int costWithoutPassingGridTimeElapsed = Integer.MAX_VALUE;

    /**
     * This constructor creates a cost of infinite value or a distance value.
     */
    public GridLocationWithCost(AI_Player selfPlayer, GridLocation currentLocation, GridLocation destinationLocation, int passingGridLocationNumber) {
        this.currentLocation = currentLocation;
        this.passingGridLocationNumber = passingGridLocationNumber;
        if (!selfPlayer.isPathVisited(currentLocation) && Bomb.bombAtGridLocation(currentLocation) == null) {
            Bomb_with_BombMaxTimeElapsed_Cost_Direction nearbyBomb = selfPlayer.getBombNearbyAdvanced(currentLocation);
            if (nearbyBomb != null) {
                costWithoutPassingGridTimeElapsed = GridLocation.getDistance(currentLocation, destinationLocation) + nearbyBomb.bombWithMaxTimeElapsed.getBombTimeElapsed() + nearbyBomb.cost;
                cost = costWithoutPassingGridTimeElapsed + passingGridLocationNumber * AI_Player.eachGridLocationPassingTimeElapsed;
            } else {
                costWithoutPassingGridTimeElapsed = GridLocation.getDistance(currentLocation, destinationLocation);
                cost = costWithoutPassingGridTimeElapsed;
            }
        }
    }

    /**
     * Destination location is not specified here. This is applicable when destination location is currently unknown.
     * This constructor is used to detect a safe location
     */
    public GridLocationWithCost(AI_Player selfPlayer, GridLocation currentLocation, int passingGridLocationNumber) {
        this.currentLocation = currentLocation;
        this.passingGridLocationNumber = passingGridLocationNumber;
        if (!selfPlayer.isPathVisited(currentLocation) && Bomb.bombAtGridLocation(currentLocation) == null) {
            Bomb_with_BombMaxTimeElapsed_Cost_Direction nearbyBomb = selfPlayer.getBombNearbyAdvanced(currentLocation);
            if (nearbyBomb != null) {
                costWithoutPassingGridTimeElapsed = nearbyBomb.bombWithMaxTimeElapsed.getBombTimeElapsed() + nearbyBomb.cost;
                cost = costWithoutPassingGridTimeElapsed + passingGridLocationNumber * AI_Player.eachGridLocationPassingTimeElapsed;
            } else {
                costWithoutPassingGridTimeElapsed = 0;
                cost = costWithoutPassingGridTimeElapsed;
            }
        }
    }

    public int compareTo(GridLocationWithCost g) {
        return cost - g.cost;
    }
}

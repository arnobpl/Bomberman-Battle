package GameObject.Player.OfflinePlayer.AI_Player;

import GameObject.Location.GridLocation;

/**
 * Created by Arnob on 14/05/2015.
 * This class is intended to be used in Dijkstra's algorithm.
 */
public class GridLocationWithCostAndRoot {
    public GridLocationWithCostAndRoot rootLocation;
    public GridLocationWithCost gridLocationWithCost;
    public int absoluteRootDirectionOrdinal;
    public int passingGridLocationNumber = 0;
    public boolean isRoot = false;

    public GridLocationWithCostAndRoot(GridLocationWithCostAndRoot rootLocation, GridLocationWithCost gridLocationWithCost, int currentDirectionOrdinal) {
        this.rootLocation = rootLocation;
        this.gridLocationWithCost = gridLocationWithCost;
        if (rootLocation.isRoot) {
            absoluteRootDirectionOrdinal = currentDirectionOrdinal;
        } else {
            absoluteRootDirectionOrdinal = rootLocation.absoluteRootDirectionOrdinal;
        }
        this.passingGridLocationNumber = gridLocationWithCost.passingGridLocationNumber;
    }

    public GridLocationWithCostAndRoot(GridLocationWithCostAndRoot rootLocation, AI_Player selfPlayer, GridLocation currentLocation, GridLocation destinationLocation, int passingGridLocationNumber, int currentDirectionOrdinal) {
        this.rootLocation = rootLocation;
        gridLocationWithCost = new GridLocationWithCost(selfPlayer, currentLocation, destinationLocation, passingGridLocationNumber);
        if (rootLocation.isRoot) {
            absoluteRootDirectionOrdinal = currentDirectionOrdinal;
        } else {
            absoluteRootDirectionOrdinal = rootLocation.absoluteRootDirectionOrdinal;
        }
        this.passingGridLocationNumber = gridLocationWithCost.passingGridLocationNumber;
    }

    public GridLocationWithCostAndRoot(GridLocationWithCostAndRoot rootLocation, AI_Player selfPlayer, GridLocation currentLocation, int passingGridLocationNumber, int currentDirectionOrdinal) {
        this.rootLocation = rootLocation;
        gridLocationWithCost = new GridLocationWithCost(selfPlayer, currentLocation, passingGridLocationNumber);
        if (rootLocation.isRoot) {
            absoluteRootDirectionOrdinal = currentDirectionOrdinal;
        } else {
            absoluteRootDirectionOrdinal = rootLocation.absoluteRootDirectionOrdinal;
        }
        this.passingGridLocationNumber = gridLocationWithCost.passingGridLocationNumber;
    }

    /**
     * This constructor creates a root.
     */
    public GridLocationWithCostAndRoot() {
        isRoot = true;
    }
}

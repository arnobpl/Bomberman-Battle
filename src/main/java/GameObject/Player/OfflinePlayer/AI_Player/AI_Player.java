package GameObject.Player.OfflinePlayer.AI_Player;

import AppInfo.Customization.AllSettings;
import AppInfo.Initialized;
import GameObject.Bomb.Bomb;
import GameObject.Location.GridLocation;
import GameObject.Location.Location;
import GameObject.Player.OnilnePlayer.Player;
import GameObject.Player.PlayerEnum.Color;
import GameObject.Player.PlayerEnum.Direction;
import GraphicsRenderer.GfxFromFile;
import Scene.AllScenes.GameScenes.MainGame.MainGame;

import java.util.*;

/**
 * Created by Arnob on 25/02/2015.
 * This class is for performing artificial intelligent players.
 * This class only supports offline game mode.
 */
public class AI_Player extends Player {

    //TODO: Check: While the player and opponents are in a single consecutive safe location, they moves like a foolish!
    //TODO: Check: Creating bomb in unsafe location is good enough.
    //TODO: Check: Passing through bomb may not be accurate sometimes!
    //TODO: Check: Current AI is not intelligent enough to place bomb at right situations to trap opponents.
    //TODO: Check: Too much draw game!
    //TODO: AI is too far to be difficult! It is very easy to win! Improve AI skills.

    private static final int AI_updateIntervalMax = 600;    // max time interval of AI update (exclusive)
    private static final int AI_updateIntervalMin = 400;    // min time interval of AI update (inclusive)

    private static final int AI_updateIntervalLength = AI_updateIntervalMax - AI_updateIntervalMin;
    private int AI_updateInterval = AI_updateIntervalMin + Initialized.random.nextInt(AI_updateIntervalLength);

    private int AI_updateTime = Initialized.random.nextInt(AI_updateInterval + 1);
    // The above line is done so that all AI players do not update AI at the same time.
    // Because if updating AI for all AI players at a time, game would be slow.

    private static final int createBombProbabilityPrecision = 1000;     // precision of probability
    private static final int createBombProbabilityThresholdMax = 400;   // max probability value out of precision (exclusive)
    private static final int createBombProbabilityThresholdMin = 200;   // min probability value out of precision (inclusive)

    private static final int createBombProbabilityThresholdLength = createBombProbabilityThresholdMax - createBombProbabilityThresholdMin;
    protected int createBombProbabilityThreshold = createBombProbabilityThresholdMin + Initialized.random.nextInt(createBombProbabilityThresholdLength);    // probability value out of precision

    private static final int gotOneStarEnmity = 10;
    private static final int bombEnmity = 2;

    protected static final int maxAttackDistance = Bomb.bombPower;
    protected static final int maxNearDistance = 1;

    private static final double nearbyBombAsObstacleOpponentPathThresholdMultiplier = 0.8;  // taking less risk while creating a path to an opponent
    private static final double nearbyBombAsObstacleSafePathThresholdMultiplier = 0.95;     // taking more risk while creating an emergency safe path

    private static final int nearbyBombAsObstacleOpponentPathTimeElapsedThreshold = (int) (Bomb.explosionTimeout * nearbyBombAsObstacleOpponentPathThresholdMultiplier);
    private static final int nearbyBombAsObstacleSafePathTimeElapsedThreshold = (int) (Bomb.explosionTimeout * nearbyBombAsObstacleSafePathThresholdMultiplier);

    public static final int eachGridLocationPassingTimeElapsed = (int) Math.ceil(GfxFromFile.gridWidth / Player.moveStep * AllSettings.userSettings.timeStep);

    protected int passingGridLocationNumberTimeElapsedForPath;  // intended for the return value of 'createPathToNearSafeLocationByAssumedBomb' method

    private static final int advancedConsecutiveBombCostMultiplier = 20;

    private static final int gridLocationAlignmentPrecision = (int) (0.1 * GfxFromFile.gridWidth * AllSettings.userSettings.motionConst);

    private final List<OpponentPlayer> opponentPlayers = new ArrayList<>();
    private final Map<Player, OpponentPlayer> opponentPlayersHashMap = new HashMap<>(MainGame.totalPlayers);    // to access OpponentPlayer by Player

    private GridLocation mostWantedOpponentLocation;
    private GridLocation optimalAttackingLocation;

    protected final GridLocation currentLocation = getGridLocation();
    protected final Queue<GridLocation> path = new LinkedList<>();
    private boolean[][] gridLocationVisited;  // this includes boundary and static inner obstacles too

    // the following variables are used only for optimization
    private static int[] previousBombGlobalID_nearbyBombList = new int[Location.totalMovableGridLocation];
    private static int[] previousBombGlobalID_nearbyBombAdvancedList = new int[Location.totalMovableGridLocation];
    private static Bomb_with_BombMaxTimeElapsed_Cost_Direction[] nearbyBombListByGridLocation = new Bomb_with_BombMaxTimeElapsed_Cost_Direction[Location.totalMovableGridLocation];
    private static int[] previousBombGlobalID_adjacentLocationSafetyList = new int[Location.totalMovableGridLocation];
    private static boolean[] adjacentLocationSafetyListByGridLocation = new boolean[Location.totalMovableGridLocation];
    private static int[] previousBombGlobalID_locationTrappedList = new int[Location.totalMovableGridLocation];
    private static boolean[] locationTrappedListByGridLocation = new boolean[Location.totalMovableGridLocation];


    public AI_Player(Color color) {
        super(color);
        initializeGridLocationVisited();
    }

    private void initializeGridLocationVisited() {
        final int boundaryX = Location.totalGridWidth + 2;
        final int boundaryY = Location.totalGridHeight + 2;
        gridLocationVisited = new boolean[boundaryX + 1][boundaryY + 1];
        for (int i = 0; i < gridLocationVisited.length; i++) {
            gridLocationVisited[i][0] = true;
            gridLocationVisited[i][boundaryY] = true;
        }
        for (int i = 0; i < gridLocationVisited[0].length; i++) {
            gridLocationVisited[0][i] = true;
            gridLocationVisited[boundaryX][i] = true;
        }
        for (int i = 1; i < boundaryX; i++) {
            for (int j = 1; j < boundaryY; j++) {
                gridLocationVisited[i][j] = ((i & 1) == 0) && ((j & 1) == 0);
            }
        }
    }


    /**
     * This method should be called once in each game to initialize all the lists for optimization.
     */
    public static void initializeOptimizationLists() {
        int currentBombGlobalID = Bomb.getBombGlobalID();
        for (int i = 0; i < Location.totalMovableGridLocation; i++) {
            previousBombGlobalID_nearbyBombList[i] = currentBombGlobalID;
            previousBombGlobalID_nearbyBombAdvancedList[i] = currentBombGlobalID;
            nearbyBombListByGridLocation[i] = null;
            previousBombGlobalID_adjacentLocationSafetyList[i] = currentBombGlobalID;
            adjacentLocationSafetyListByGridLocation[i] = true;
            previousBombGlobalID_locationTrappedList[i] = currentBombGlobalID;
            locationTrappedListByGridLocation[i] = false;
        }
    }

    /**
     * This is not called in constructor because initially <code>inputControllersList</code> does not have enough elements.
     * This should be called only after sorting <code>inputControllersList</code>.
     */
    public void initializeOpponentPlayers() {
        int thisPlayerID = getID();
        for (int i = 0; i < thisPlayerID; i++) {
            Player playerToAddInOpponentPlayer = (Player) MainGame.inputControllersList.get(i);
            OpponentPlayer opponentPlayerToAdd = new OpponentPlayer(playerToAddInOpponentPlayer, this);
            opponentPlayerToAdd.increaseEnmity(gotOneStarEnmity, MainGame.gameResult.getRecord(opponentPlayerToAdd.getColor()));
            opponentPlayers.add(opponentPlayerToAdd);
            opponentPlayersHashMap.put(playerToAddInOpponentPlayer, opponentPlayerToAdd);
        }
        for (int i = thisPlayerID + 1; i < MainGame.totalPlayers; i++) {
            Player playerToAddInOpponentPlayer = (Player) MainGame.inputControllersList.get(i);
            OpponentPlayer opponentPlayerToAdd = new OpponentPlayer(playerToAddInOpponentPlayer, this);
            opponentPlayerToAdd.increaseEnmity(gotOneStarEnmity, MainGame.gameResult.getRecord(opponentPlayerToAdd.getColor()));
            opponentPlayers.add(opponentPlayerToAdd);
            opponentPlayersHashMap.put(playerToAddInOpponentPlayer, opponentPlayerToAdd);
        }
    }


    protected byte getInputData() {
        if (!isKilled()) {
            boolean isCurrentLocationSafe = false;
            boolean safetyChecked = false;  // this is needed to check if 'createBombWhenSafe()' actually creates a new bomb
            if (MainGame.inputControllersList.size() > 1) {
                if (AI_updateTime < AI_updateInterval) {
                    AI_updateTime += AllSettings.userSettings.timeStep;
                } else {
                    AI_updateTime = 0;  // reset AI_updateTime
                    isCurrentLocationSafe = createBombWhenSafe();           // check whether a bomb can be placed in the current position to attack any opponent player (defense)
                    safetyChecked = true;
                    createPathToMostWantedOpponent(isCurrentLocationSafe);  // create path to reach the most-wanted opponent
                }
            }

            if (!path.isEmpty()) {  // move the player if 'path' is not empty
                GridLocation gridLocationToMoveNext = path.peek();
                if (!gridLocationToMoveNext.equals(currentLocation)) {  // move until reach 'gridLocationToMoveNext'
                    Direction direction = directionToGridLocation(gridLocationToMoveNext);
                    int directionOrdinal = direction.ordinal();
                    int x = currentLocation.x + directionStep[directionOrdinal][0];
                    int y = currentLocation.y + directionStep[directionOrdinal][1];
                    GridLocation gridLocation = new GridLocation(x, y);
                    if (Bomb.bombAtGridLocation(gridLocation) != null) {
                        createPathToMostWantedOpponent(false);
                        AI_updateTime = 0;  // reset AI_updateTime
                    }
                    encodeMove(direction);  // this statement is not in 'else' block to auto-align while bomb obstacle close to the player
                } else if (!isAlignedToGridLocation(gridLocationToMoveNext)) {
                    alignToGridLocation(gridLocationToMoveNext);
                } else {
                    removeGridLocationToMoveNext(); // player has reached 'gridLocationToMoveNext', so remove it from 'path'
                    if (!path.isEmpty()) {  // to move smoothly
                        gridLocationToMoveNext = path.peek();
                        encodeMove(directionToGridLocation(gridLocationToMoveNext));
                    }
                }
            } else {
                int bombTimeElapsed = Integer.MIN_VALUE;
                if (!isCurrentLocationSafe) {
                    bombTimeElapsed = isLocationSafeAdvanced(currentLocation);
                    if (bombTimeElapsed == Integer.MIN_VALUE && safetyChecked) {
                        bombTimeElapsed = 0;    // a new bomb is created by this player through 'createBombWhenSafe()', so current location is not safe
                    }
                }
                if (bombTimeElapsed != Integer.MIN_VALUE) {     // check if current position is safe
                    // place the player to a near reachable safe location considering various factors
                    if (bombTimeElapsed != 0 && (isNearAnyOpponentPlayer(maxAttackDistance) && isAdjacentLocationSafe(currentLocation) && createPathToNearSafeLocationByAssumedBomb(currentLocation))) {
                        // check if bomb can be placed considering consecutive bomb explosion
                        if (bombTimeElapsed + passingGridLocationNumberTimeElapsedForPath < nearbyBombAsObstacleOpponentPathTimeElapsedThreshold) {
                            encodeFire();
                        } else {
                            createPathToNearSafeLocation(currentLocation);
                        }
                    } else {
                        createPathToNearSafeLocation(currentLocation);
                    }
                    AI_updateTime = 0;  // reset AI_updateTime
                } else if (isNearAnyOpponentPlayer(maxAttackDistance) && isAdjacentLocationSafe(currentLocation)) {
                    // place a bomb if the most-wanted opponent is in the same or near location
                    encodeFire();
                    createPathToMostWantedOpponent(false);
                    AI_updateTime = 0;  // reset AI_updateTime
                } else if (!isAlignedToGridLocation(currentLocation)) {
                    alignToGridLocation(currentLocation);
                }
            }
        }

        byte inputDataBuffer = inputData;
        encodeMoveClear();      // clear inputData so that encoding move is cleared if any.
        return inputDataBuffer;
    }


    /**
     * This method does not check whether a bomb is placed in <code>gridLocation</code>, but checks for bombs in only nearby locations.
     * This method does not consider additional cost for multiple bombs and <code>getBombTimeElapsedAdvanced()</code> of a bomb.
     *
     * @return <code>null</code> if no bomb nearby from <code>gridLocation</code>,
     * otherwise the <code>Bomb_with_BombMaxTimeElapsed_Cost_Direction</code> object
     */
    protected Bomb_with_BombMaxTimeElapsed_Cost_Direction getBombNearby(GridLocation gridLocation) {
        int currentBombGlobalID = Bomb.getBombGlobalID();
        int gridLocationIndex = Location.indexOfMovableLocationByGridLocation(gridLocation);
        if (previousBombGlobalID_nearbyBombList[gridLocationIndex] == currentBombGlobalID) {
            return nearbyBombListByGridLocation[Location.indexOfMovableLocationByGridLocation(gridLocation)];
        }

        previousBombGlobalID_nearbyBombList[gridLocationIndex] = currentBombGlobalID;

        nearbyBombListByGridLocation[gridLocationIndex] = null;
        for (int i = 0; i < directionStep.length; i++) {
            int x = gridLocation.x;
            int y = gridLocation.y;
            for (int j = 0; j < Bomb.bombPower; j++) {
                x += Bomb.directionStep[i][0];
                y += Bomb.directionStep[i][1];
                // checking boundary and inner obstacle
                if ((x < 0 || x > Location.totalGridWidth) || (y < 0 || y > Location.totalGridHeight) || ((x & 1) == 1 && (y & 1) == 1)) {
                    break;
                }
                // checking bomb at this position
                Bomb nearbyBomb = Bomb.bombAtGridLocation(new GridLocation(x, y));
                if (nearbyBomb != null) {
                    Player bombOwner = nearbyBomb.getBombOwner();
                    if (bombOwner != this) {
                        opponentPlayersHashMap.get(bombOwner).increaseEnmity(bombEnmity);
                    }
                    nearbyBombListByGridLocation[gridLocationIndex] = new Bomb_with_BombMaxTimeElapsed_Cost_Direction(nearbyBomb, nearbyBomb, Bomb.bombPower - j, GameObject.Bomb.Direction.valuesCached()[i]);
                    return nearbyBombListByGridLocation[gridLocationIndex];
                }
            }
        }
        return nearbyBombListByGridLocation[gridLocationIndex];
    }

    /**
     * This method does not check whether a bomb is placed in <code>gridLocation</code>, but checks for bombs in only nearby locations.
     * This method considers additional cost for multiple bombs and <code>getBombTimeElapsedAdvanced()</code> of a bomb. Cost is multiplied by a multiplier.
     *
     * @return <code>null</code> if no bomb nearby from <code>gridLocation</code>,
     * otherwise the <code>Bomb_with_BombMaxTimeElapsed_Cost_Direction</code> object
     */
    protected Bomb_with_BombMaxTimeElapsed_Cost_Direction getBombNearbyAdvanced(GridLocation gridLocation) {
        int currentBombGlobalID = Bomb.getBombGlobalID();
        int gridLocationIndex = Location.indexOfMovableLocationByGridLocation(gridLocation);
        if (previousBombGlobalID_nearbyBombAdvancedList[gridLocationIndex] == currentBombGlobalID) {
            return nearbyBombListByGridLocation[Location.indexOfMovableLocationByGridLocation(gridLocation)];
        }

        previousBombGlobalID_nearbyBombAdvancedList[gridLocationIndex] = currentBombGlobalID;
        previousBombGlobalID_nearbyBombList[gridLocationIndex] = currentBombGlobalID;   // since this method is advanced

        nearbyBombListByGridLocation[gridLocationIndex] = null;
        for (int i = 0; i < directionStep.length; i++) {
            int x = gridLocation.x;
            int y = gridLocation.y;
            for (int j = 0; j < Bomb.bombPower; j++) {
                x += Bomb.directionStep[i][0];
                y += Bomb.directionStep[i][1];
                // checking boundary and inner obstacle
                if ((x < 0 || x > Location.totalGridWidth) || (y < 0 || y > Location.totalGridHeight) || ((x & 1) == 1 && (y & 1) == 1)) {
                    break;
                }
                // checking bomb at this position
                Bomb nearbyBomb = Bomb.bombAtGridLocation(new GridLocation(x, y));
                if (nearbyBomb != null) {
                    Player bombOwner = nearbyBomb.getBombOwner();
                    if (bombOwner != this) {
                        opponentPlayersHashMap.get(bombOwner).increaseEnmity(bombEnmity);
                    }
                    if (nearbyBombListByGridLocation[gridLocationIndex] == null) {
                        nearbyBombListByGridLocation[gridLocationIndex] = new Bomb_with_BombMaxTimeElapsed_Cost_Direction(nearbyBomb, nearbyBomb.getBombWithMaxTimeElapsed(), Bomb.bombPower - j, GameObject.Bomb.Direction.valuesCached()[i]);
                        // 'getBombTimeElapsedAdvanced()' is needed for more precision for chaining bombs
                    } else {
                        Bomb bombWithMaxTimeElapsed = nearbyBomb.getBombWithMaxTimeElapsed();
                        if (bombWithMaxTimeElapsed.getBombTimeElapsed() > nearbyBombListByGridLocation[gridLocationIndex].bombWithMaxTimeElapsed.getBombTimeElapsed()) {
                            nearbyBombListByGridLocation[gridLocationIndex].bomb = nearbyBomb;
                            nearbyBombListByGridLocation[gridLocationIndex].bombWithMaxTimeElapsed = bombWithMaxTimeElapsed;
                            nearbyBombListByGridLocation[gridLocationIndex].direction = GameObject.Bomb.Direction.valuesCached()[i];
                        }
                        nearbyBombListByGridLocation[gridLocationIndex].cost += Bomb.bombPower - j;
                    }
                    break;  // since 'nearbyBomb.getBombWithMaxTimeElapsed()' is used, so no need to further loop execution
                }
            }
        }
        if (nearbyBombListByGridLocation[gridLocationIndex] != null) {
            nearbyBombListByGridLocation[gridLocationIndex].cost *= advancedConsecutiveBombCostMultiplier;
        }
        return nearbyBombListByGridLocation[gridLocationIndex];
    }

    /**
     * This method calculates bomb cost of <code>gridLocation</code> considering the location and its adjacent locations.
     */
    protected int getBombCost(GridLocation gridLocation) {
        int bombCost = 0;
        Bomb bomb = Bomb.bombAtGridLocation(gridLocation);
        Bomb_with_BombMaxTimeElapsed_Cost_Direction nearbyBomb = getBombNearbyAdvanced(gridLocation);
        if (bomb != null) {
            bombCost += (Bomb.bombPower + 1) * advancedConsecutiveBombCostMultiplier;
            if (nearbyBomb != null) {
                bombCost += nearbyBomb.bombWithMaxTimeElapsed.getBombTimeElapsed() + nearbyBomb.cost;
            } else {
                bombCost += bomb.getBombTimeElapsed();
            }
        } else if (nearbyBomb != null) {
            bombCost += nearbyBomb.bombWithMaxTimeElapsed.getBombTimeElapsed() + nearbyBomb.cost;
        }
        return bombCost;
    }

    /**
     * This method does not check whether a bomb is placed in <code>gridLocation</code>, but checks for bombs in only nearby locations.
     * This method does not consider additional cost for multiple bombs and <code>getBombTimeElapsedAdvanced()</code> of a bomb.
     *
     * @return <code>null</code> if no bomb nearby from <code>gridLocation</code>,
     * otherwise the <code>Player</code> object who is the bomb owner
     */
    protected Player getBombOwnerOfBombNearby(GridLocation gridLocation) {
        Bomb_with_BombMaxTimeElapsed_Cost_Direction bomb_with_BombMaxTimeElapsed_Cost_Direction = getBombNearby(gridLocation);
        if (bomb_with_BombMaxTimeElapsed_Cost_Direction != null) {
            Bomb nearbyBomb = bomb_with_BombMaxTimeElapsed_Cost_Direction.bomb;
            return nearbyBomb.getBombOwner();
        }
        return null;
    }

    /**
     * This method does not check whether a bomb is placed in <code>gridLocation</code>, but checks for bombs in only nearby locations.
     * This method considers additional cost for multiple bombs and <code>getBombTimeElapsedAdvanced()</code> of a bomb.
     *
     * @return <code>null</code> if no bomb nearby from <code>gridLocation</code>,
     * otherwise the <code>Player</code> object who is the bomb owner
     */
    protected Player getBombOwnerOfBombNearbyAdvanced(GridLocation gridLocation) {
        Bomb_with_BombMaxTimeElapsed_Cost_Direction bomb_with_BombMaxTimeElapsed_Cost_Direction = getBombNearbyAdvanced(gridLocation);
        if (bomb_with_BombMaxTimeElapsed_Cost_Direction != null) {
            Bomb nearbyBomb = bomb_with_BombMaxTimeElapsed_Cost_Direction.bomb;
            return nearbyBomb.getBombOwner();
        }
        return null;
    }


    /**
     * @return <code>true</code> if <code>gridLocation</code> is safe to stay in, otherwise <code>false</code>
     */
    protected boolean isLocationSafe(GridLocation gridLocation) {
        return Bomb.bombAtGridLocation(gridLocation) == null && getBombNearby(gridLocation) == null;
    }

    /**
     * @return <code>Integer.MIN_VALUE</code> if <code>gridLocation</code> is safe to stay in,
     * otherwise <code>getBombTimeElapsedAdvanced()</code> value
     */
    protected int isLocationSafeAdvanced(GridLocation gridLocation) {
        int bombTimeElapsed = Integer.MIN_VALUE;

        Bomb bomb = Bomb.bombAtGridLocation(gridLocation);
        if (bomb != null) bombTimeElapsed = bomb.getBombTimeElapsed();

        Bomb_with_BombMaxTimeElapsed_Cost_Direction bomb_with_BombMaxTimeElapsed_Cost_Direction = getBombNearbyAdvanced(gridLocation);
        if (bomb_with_BombMaxTimeElapsed_Cost_Direction != null) {
            bombTimeElapsed = bomb_with_BombMaxTimeElapsed_Cost_Direction.bombWithMaxTimeElapsed.getBombTimeElapsed();
        }

        return bombTimeElapsed;
    }

    /**
     * @return <code>true</code> if any location adjacent to <code>gridLocation</code> is safe to stay in, otherwise <code>false</code>
     */
    protected boolean isAdjacentLocationSafe(GridLocation gridLocation) {
        int currentBombGlobalID = Bomb.getBombGlobalID();
        int gridLocationIndex = Location.indexOfMovableLocationByGridLocation(gridLocation);
        if (previousBombGlobalID_adjacentLocationSafetyList[gridLocationIndex] == currentBombGlobalID) {
            return adjacentLocationSafetyListByGridLocation[gridLocationIndex];
        }

        previousBombGlobalID_adjacentLocationSafetyList[gridLocationIndex] = currentBombGlobalID;

        for (int i = 0; i < directionStep.length; i++) {
            int x = gridLocation.x;
            int y = gridLocation.y;
            x += Bomb.directionStep[i][0];
            y += Bomb.directionStep[i][1];
            // checking boundary and inner obstacle
            if ((x < 0 || x > Location.totalGridWidth) || (y < 0 || y > Location.totalGridHeight) || ((x & 1) == 1 && (y & 1) == 1)) {
                continue;
            }
            if (isLocationSafe(new GridLocation(x, y))) {
                adjacentLocationSafetyListByGridLocation[gridLocationIndex] = true;
                return adjacentLocationSafetyListByGridLocation[gridLocationIndex];
            }
        }
        adjacentLocationSafetyListByGridLocation[gridLocationIndex] = false;
        return adjacentLocationSafetyListByGridLocation[gridLocationIndex];
    }

    /**
     * This method first checks whether the current location and adjacent locations are safe.
     * If safe, it will create a bomb by probability threshold or if any opponent nearby.
     *
     * @return <code>true</code> if the current location and adjacent locations are safe after placing bomb (if any), otherwise <code>false</code>
     */
    protected boolean createBombWhenSafe() {
        if (isLocationSafe(currentLocation)) {
            if (isAdjacentLocationSafe(currentLocation)) {
                // decide to place a bomb by probability threshold or if any opponent nearby
                if (Initialized.random.nextInt(createBombProbabilityPrecision) < createBombProbabilityThreshold || isNearAnyOpponentPlayer(maxAttackDistance)) {
                    encodeFire();
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    /**
     * @return <code>GridLocation</code> in which attacking is optimal
     * such as attacking from the adjacent location instead of the exact location
     * by considering nearby bombs and others
     */
    protected GridLocation getOptimalAttackingLocation(GridLocation mostWantedOpponentLocation, Bomb_with_BombMaxTimeElapsed_Cost_Direction bomb_with_BombMaxTimeElapsed_Cost_Direction) {
        if (bomb_with_BombMaxTimeElapsed_Cost_Direction == null) {
            if (mostWantedOpponentLocation.equals(currentLocation)) {
                int x = mostWantedOpponentLocation.x;
                int y = mostWantedOpponentLocation.y;
                if (((x & 1) == 0) && ((y & 1) == 0)) {
                    int changeDirection = Initialized.random.nextInt(2);
                    if (changeDirection == 0) {
                        if (mostWantedOpponentLocation.x == 0) x++;
                        else if (mostWantedOpponentLocation.x == Location.totalGridWidth) x--;
                        else {
                            int x_step = Initialized.random.nextInt(2);
                            if (x_step == 0) x_step--;
                            x += x_step;
                        }
                    } else {
                        if (mostWantedOpponentLocation.y == 0) y++;
                        else if (mostWantedOpponentLocation.y == Location.totalGridHeight) y--;
                        else {
                            int y_step = Initialized.random.nextInt(2);
                            if (y_step == 0) y_step--;
                            y += y_step;
                        }
                    }
                } else if ((x & 1) == 0) {
                    int y_step = Initialized.random.nextInt(2);
                    if (y_step == 0) y_step--;
                    y += y_step;
                } else {
                    int x_step = Initialized.random.nextInt(2);
                    if (x_step == 0) x_step--;
                    x += x_step;
                }
                return new GridLocation(x, y);
            }
            int x_step = Integer.signum(mostWantedOpponentLocation.x - currentLocation.x);
            int y_step = Integer.signum(mostWantedOpponentLocation.y - currentLocation.y);
            int x = mostWantedOpponentLocation.x - x_step;
            int y = mostWantedOpponentLocation.y - y_step;
            if (((x & 1) == 1) && ((y & 1) == 1)) {     // checking inner obstacle
                x = mostWantedOpponentLocation.x;
                y = mostWantedOpponentLocation.y;
                if (x_step != 0 && y_step != 0) {
                    int changeDirection = Initialized.random.nextInt(2);
                    if (changeDirection == 0) x -= x_step;
                    else y -= y_step;
                } else if (x_step == 0) {
                    x_step = Initialized.random.nextInt(2);
                    if (x_step == 0) x_step--;
                    x += x_step;
                } else {
                    y_step = Initialized.random.nextInt(2);
                    if (y_step == 0) y_step--;
                    y += y_step;
                }
            }
            return new GridLocation(x, y);
        }
        int bombDirectionIndex = bomb_with_BombMaxTimeElapsed_Cost_Direction.direction.ordinal();
        int x = mostWantedOpponentLocation.x - Bomb.directionStep[bombDirectionIndex][0];
        int y = mostWantedOpponentLocation.y - Bomb.directionStep[bombDirectionIndex][1];
        if ((0 <= x && x <= Location.totalGridWidth) && (0 <= y && y <= Location.totalGridHeight)) {
            GridLocation gridLocation = new GridLocation(x, y);
            if (Bomb.bombAtGridLocation(gridLocation) == null) return gridLocation;
            if (Bomb.directionStep[bombDirectionIndex][0] == 0) {
                y = mostWantedOpponentLocation.y;
                if ((y & 1) == 0) {
                    if (mostWantedOpponentLocation.x == 0) x++;
                    else if (mostWantedOpponentLocation.x == Location.totalGridWidth) x--;
                    else {
                        int x_step = Integer.signum(mostWantedOpponentLocation.x - currentLocation.x);
                        x -= x_step;
                        gridLocation = new GridLocation(x, y);
                        if (Bomb.bombAtGridLocation(gridLocation) == null) return gridLocation;
                        x += (x_step << 1);
                    }
                    gridLocation = new GridLocation(x, y);
                    if (Bomb.bombAtGridLocation(gridLocation) == null) return gridLocation;
                }
            } else {
                x = mostWantedOpponentLocation.x;
                if ((x & 1) == 0) {
                    if (mostWantedOpponentLocation.y == 0) y++;
                    else if (mostWantedOpponentLocation.y == Location.totalGridHeight) y--;
                    else {
                        int y_step = Integer.signum(mostWantedOpponentLocation.y - currentLocation.y);
                        y -= y_step;
                        gridLocation = new GridLocation(x, y);
                        if (Bomb.bombAtGridLocation(gridLocation) == null) return gridLocation;
                        y += (y_step << 1);
                    }
                    gridLocation = new GridLocation(x, y);
                    if (Bomb.bombAtGridLocation(gridLocation) == null) return gridLocation;
                }
            }
        } else if (0 <= x && x <= Location.totalGridWidth) {
            y = mostWantedOpponentLocation.y;
            if (mostWantedOpponentLocation.x == 0) x++;
            else if (mostWantedOpponentLocation.x == Location.totalGridWidth) x--;
            else {
                int x_step = Integer.signum(mostWantedOpponentLocation.x - currentLocation.x);
                x -= x_step;
                GridLocation gridLocation = new GridLocation(x, y);
                if (Bomb.bombAtGridLocation(gridLocation) == null) return gridLocation;
                x += (x_step << 1);
            }
            GridLocation gridLocation = new GridLocation(x, y);
            if (Bomb.bombAtGridLocation(gridLocation) == null) return gridLocation;
        } else {
            x = mostWantedOpponentLocation.x;
            if (mostWantedOpponentLocation.y == 0) y++;
            else if (mostWantedOpponentLocation.y == Location.totalGridHeight) y--;
            else {
                int y_step = Integer.signum(mostWantedOpponentLocation.y - currentLocation.y);
                y -= y_step;
                GridLocation gridLocation = new GridLocation(x, y);
                if (Bomb.bombAtGridLocation(gridLocation) == null) return gridLocation;
                y += (y_step << 1);
            }
            GridLocation gridLocation = new GridLocation(x, y);
            if (Bomb.bombAtGridLocation(gridLocation) == null) return gridLocation;
        }
        return mostWantedOpponentLocation;
    }

    /**
     * @return <code>true</code> if any opponent player is near by <code>maxAttackDistance</code> value, otherwise <code>false</code>
     */
    protected boolean isNearAnyOpponentPlayer(int maxDistance) {
        if (currentLocation.equals(optimalAttackingLocation)) {
            return true;
        }

        boolean isCurrentLocationX_even = ((currentLocation.x & 1) == 0);
        boolean isCurrentLocationY_even = ((currentLocation.y & 1) == 0);
        for (OpponentPlayer opponentPlayer : opponentPlayers) {
            GridLocation opponentPlayerLocation = opponentPlayer.getGridLocation();
            if (isCurrentLocationX_even && currentLocation.x == opponentPlayerLocation.x && Math.abs(currentLocation.y - opponentPlayerLocation.y) <= maxDistance) {
                return true;
            }
            if (isCurrentLocationY_even && currentLocation.y == opponentPlayerLocation.y && Math.abs(currentLocation.x - opponentPlayerLocation.x) <= maxDistance) {
                return true;
            }
        }
        return false;
    }


    /**
     * Creates a path to the most-wanted opponent when successful or a path to a possible safe location (may not be the nearest if risky)
     * This method will create empty path if no suitable reachable opponent found, or no safe or less risky location found.
     * Note that the method first checks for <code>currentLocation</code>'s bomb cost, and if it is already risky, then it just tries to create a safe path.
     * It also checks if <code>currentLocation</code> is the single consecutive safe location, and if not, then it does nothing.
     *
     * @return <code>true</code> if a path to the most-wanted opponent is found, otherwise <code>false</code>
     */
    protected boolean createPathToMostWantedOpponent(boolean isCurrentLocationSafe) {
        //System.out.println(getColor().toString() + ":\t" + "Most-wanted path...");

        clearPath();

        if (!isCurrentLocationSafe) {
            if (getBombCost(currentLocation) > nearbyBombAsObstacleOpponentPathTimeElapsedThreshold) {
                createPathToNearSafeLocation(currentLocation);
                return false;   // it is not a suitable time to attack any opponent
            }
        } else if (isNearAnyOpponentPlayer(maxNearDistance) && !isAdjacentLocationSafe(currentLocation)) {
            return false;
        }

        // update distance before sorting by distance and enmity ranking
        for (Iterator<OpponentPlayer> i = opponentPlayers.iterator(); i.hasNext(); ) {
            i.next().update(i);
        }
        if (opponentPlayers.size() == 0) {
            if (!isCurrentLocationSafe) {
                // place the player to a near reachable safe location because the current location is not safe
                createPathToNearSafeLocation(currentLocation);
            }
            return false;  // when no opponent player exists
        }
        // sort opponentPlayers to get the most-wanted opponent
        Collections.sort(opponentPlayers);
        // get suitable most-wanted opponent
        boolean suitableMostWantedOpponentFound = false;
        for (OpponentPlayer i : opponentPlayers) {
            mostWantedOpponentLocation = i.getGridLocation();
            optimalAttackingLocation = getOptimalAttackingLocation(mostWantedOpponentLocation, getBombNearbyAdvanced(mostWantedOpponentLocation));
            if (!isLocationTrapped(optimalAttackingLocation)) {     // check if opponent is trapped
                suitableMostWantedOpponentFound = true;
                break;
            }
        }
        if (!suitableMostWantedOpponentFound) {
            //System.out.println(getColor().toString() + ":\t" + "No suitable opponent found!");

            if (!isCurrentLocationSafe) {
                // place the player to a near reachable safe location because the current location is not safe
                createPathToNearSafeLocation(currentLocation);
            }
            return false; // when no suitable most-wanted opponent found
        }

        //System.out.println(getColor().toString() + ":\t" + "Optimal location: " + optimalAttackingLocation.toString());

        // create path to reach the most-wanted opponent
        GridLocation destinationLocation = createPath(currentLocation, optimalAttackingLocation);
        if (!destinationLocation.equals(optimalAttackingLocation)) {
            //System.out.println(getColor().toString() + ":\t" + "Break wanted!");

            // cannot reach the most-wanted opponent, so check if the unsuccessful location or the current location is safe
            if (!isLocationSafe(destinationLocation)) {
                if (!isCurrentLocationSafe) {
                    // place the player to a near reachable safe location because the unsuccessful location and the current location are not safe
                    createPathToNearSafeLocation(currentLocation);
                }
            }
            return false;   // suitable most-wanted opponent found but not reachable
        }
        return true;    // reachable most-wanted opponent found
    }

    /**
     * Creates a path to <code>destinationLocation</code> by A* algorithm.
     * This method considers safety so that no explosion occurs in the path.
     *
     * @return <code>GridLocation</code> of <code>destinationLocation</code> when successful or the last unsuccessful location
     */
    protected GridLocation createPath(GridLocation sourceLocation, GridLocation destinationLocation) {
        clearPath();

        GridLocation pathLocation = new GridLocation(sourceLocation);
        markPathVisited(pathLocation);  // mark source location as visited to calculate path

        int passingGridLocationNumber = 1;

        int currentDirectionOrdinal = getDirection().ordinal();     // this is used to prefer not to change direction while creating a path
        while (!pathLocation.equals(destinationLocation)) {
            //System.out.println(getColor().toString() + ":\t" + pathLocation);

            List<GridLocationWithCost> gridLocationWithCosts = new ArrayList<>(directionList.length);
            for (int i = 0; i < directionList.length; i++) {
                gridLocationWithCosts.add(new GridLocationWithCost(this, new GridLocation(pathLocation.x + directionStep[i][0], pathLocation.y + directionStep[i][1]), mostWantedOpponentLocation, passingGridLocationNumber));
            }
            Collections.sort(gridLocationWithCosts);
            currentDirectionOrdinal = getIndexOfGridLocationWithCost(gridLocationWithCosts, currentDirectionOrdinal);
            GridLocationWithCost gridLocationWithCost = gridLocationWithCosts.get(currentDirectionOrdinal);
            if (gridLocationWithCost.cost >= nearbyBombAsObstacleOpponentPathTimeElapsedThreshold) {   // no way to reach the destination safely
                unmarkPathVisited(sourceLocation); // mark source location as unvisited after path calculation
                return pathLocation;
            }
            path.add(gridLocationWithCost.currentLocation);
            markPathVisited(gridLocationWithCost.currentLocation);
            pathLocation = gridLocationWithCost.currentLocation;
            passingGridLocationNumber++;
        }
        unmarkPathVisited(sourceLocation); // mark source location as unvisited after path calculation
        return destinationLocation;
    }


    /**
     * Creates a path to the reachable nearest safe location when successful (by Dijkstra's algorithm).
     * If unsuccessful by bomb or inner obstacles around, it creates empty path.
     * When unsuccessful but less or equal risky location found, this method creates the path (by Dijkstra's algorithm).
     * It always treats the source location as unsafe.
     * This should be only called when it is confirmed that the source location is unsafe.
     *
     * @return <code>true</code> if any reachable safe location is found, otherwise <code>false</code>
     */
    protected boolean createPathToNearSafeLocation(GridLocation sourceLocation) {
        clearPath();

        GridLocation pathLocation = new GridLocation(sourceLocation);
        markPathVisited(pathLocation);  // mark source location as visited to calculate path

        int previousGridLocationCost = getBombCost(pathLocation);

        List<GridLocationWithCostAndRoot> lessOrEqualRiskyLocations = new ArrayList<>();
        // This list does not contain locations within 'nearbyBombAsObstacleSafePathTimeElapsedThreshold'.
        // Because it would stop checking less or equal risky locations for the possible reduction of 'previousGridLocationCost' value.

        Queue<GridLocation> temporaryVisitedGridLocations = new LinkedList<>();
        // 'temporaryVisitedGridLocations' purpose: temporary mark and unmark 'visited' state without adding location to 'path'

        Queue<GridLocationWithCostAndRoot> gridLocationWithCostAndRoots = new LinkedList<>();
        GridLocationWithCostAndRoot gridLocationWithCostAndRoot = new GridLocationWithCostAndRoot();

        int currentDirectionOrdinal = getDirection().ordinal();     // this is used to prefer not to change direction while creating a path
        GridLocation gridLocation = new GridLocation(pathLocation.x + directionStep[currentDirectionOrdinal][0], pathLocation.y + directionStep[currentDirectionOrdinal][1]);
        GridLocationWithCost gridLocationWithCost = new GridLocationWithCost(this, gridLocation, gridLocationWithCostAndRoot.passingGridLocationNumber + 1);
        GridLocationWithCostAndRoot location = new GridLocationWithCostAndRoot(gridLocationWithCostAndRoot, gridLocationWithCost, currentDirectionOrdinal);
        if (location.gridLocationWithCost.cost < nearbyBombAsObstacleSafePathTimeElapsedThreshold) {
            gridLocationWithCostAndRoots.add(location);     // adding this to the queue so that changing direction will not occur when possible
            temporaryVisitedGridLocations.add(gridLocation);
            markPathVisited(gridLocation);
        } else if (location.gridLocationWithCost.costWithoutPassingGridTimeElapsed <= previousGridLocationCost) {
            previousGridLocationCost = location.gridLocationWithCost.costWithoutPassingGridTimeElapsed;
            lessOrEqualRiskyLocations.add(location);
            temporaryVisitedGridLocations.add(gridLocation);
            markPathVisited(gridLocation);
        }

        do {
            List<GridLocationWithCostAndRoot> gridLocationsByDirection = new ArrayList<>(directionList.length);
            for (int i = 0; i < directionList.length; i++) {
                gridLocation = new GridLocation(pathLocation.x + directionStep[i][0], pathLocation.y + directionStep[i][1]);
                gridLocationWithCost = new GridLocationWithCost(this, gridLocation, gridLocationWithCostAndRoot.passingGridLocationNumber + 1);

                location = new GridLocationWithCostAndRoot(gridLocationWithCostAndRoot, gridLocationWithCost, i);
                if (location.gridLocationWithCost.cost < nearbyBombAsObstacleSafePathTimeElapsedThreshold) {
                    gridLocationsByDirection.add(location);
                    temporaryVisitedGridLocations.add(gridLocation);
                    markPathVisited(gridLocation);
                } else if (location.gridLocationWithCost.costWithoutPassingGridTimeElapsed <= previousGridLocationCost) {
                    previousGridLocationCost = location.gridLocationWithCost.costWithoutPassingGridTimeElapsed;
                    lessOrEqualRiskyLocations.add(location);
                    temporaryVisitedGridLocations.add(gridLocation);
                    markPathVisited(gridLocation);
                }
            }

            int gridLocationsByDirectionLength = gridLocationsByDirection.size();
            for (int i = 0; i < gridLocationsByDirectionLength; i++) {
                int randomIndex = Initialized.random.nextInt(gridLocationsByDirection.size());
                gridLocationWithCostAndRoots.add(gridLocationsByDirection.get(randomIndex));
                gridLocationsByDirection.remove(randomIndex);
            }

            gridLocationWithCostAndRoot = gridLocationWithCostAndRoots.poll();

            if (gridLocationWithCostAndRoot == null) {  // check if queue is empty (unsuccessful)
                //System.out.println(getColor().toString() + ":\t" + "Nearest safe not found!");

                if (!lessOrEqualRiskyLocations.isEmpty()) {
                    GridLocationWithCostAndRoot lessOrEqualRiskyLocation = getGridLocationWithCostAndRoot(lessOrEqualRiskyLocations, currentDirectionOrdinal);

                    Stack<GridLocationWithCostAndRoot> pathLocationStack = new Stack<>();

                    do {
                        pathLocationStack.push(lessOrEqualRiskyLocation);
                        lessOrEqualRiskyLocation = lessOrEqualRiskyLocation.rootLocation;
                    } while (!lessOrEqualRiskyLocation.isRoot);

                    int pathLocationStackLength = pathLocationStack.size();
                    for (int i = 0; i < pathLocationStackLength; i++) {
                        location = pathLocationStack.pop();
                        path.add(location.gridLocationWithCost.currentLocation);

                        //System.out.println(getColor().toString() + ":\t" + "To less or equal risky: " + location.gridLocationWithCost.currentLocation.toString());
                    }
                }

                // reset visited
                int temporaryVisitedGridLocationsLength = temporaryVisitedGridLocations.size();
                for (int i = 0; i < temporaryVisitedGridLocationsLength; i++) {
                    unmarkPathVisited(temporaryVisitedGridLocations.poll());
                }
                unmarkPathVisited(sourceLocation);  // mark source location as unvisited after path calculation
                return false;
            }

            pathLocation = gridLocationWithCostAndRoot.gridLocationWithCost.currentLocation;

            if (isLocationSafe(pathLocation)) {     // check if safe location found
                //System.out.println(getColor().toString() + ":\t" + "Nearest safe found : " + pathLocation.toString());

                Stack<GridLocationWithCostAndRoot> pathLocationStack = new Stack<>();

                do {
                    pathLocationStack.push(gridLocationWithCostAndRoot);
                    gridLocationWithCostAndRoot = gridLocationWithCostAndRoot.rootLocation;
                } while (!gridLocationWithCostAndRoot.isRoot);

                int pathLocationStackLength = pathLocationStack.size();
                for (int i = 0; i < pathLocationStackLength; i++) {
                    location = pathLocationStack.pop();
                    path.add(location.gridLocationWithCost.currentLocation);

                    //System.out.println(getColor().toString() + ":\t" + "To Safe: " + location.gridLocationWithCost.currentLocation);
                }

                // reset visited
                int temporaryVisitedGridLocationsLength = temporaryVisitedGridLocations.size();
                for (int i = 0; i < temporaryVisitedGridLocationsLength; i++) {
                    unmarkPathVisited(temporaryVisitedGridLocations.poll());
                }
                unmarkPathVisited(sourceLocation);  // mark source location as unvisited after path calculation
                return true;
            }

        } while (true);
    }

    /**
     * This method only considers walls of unmovable things such as inner obstacles and bombs (by Dijkstra's algorithm).
     * It does not consider any risk caused by bomb explosions.
     * 'Visited' state must be unmarked before calling this method.
     *
     * @return <code>true</code> if <code>sourceLocation</code> is trapped, otherwise <code>false</code>
     */
    protected boolean isLocationTrapped(GridLocation sourceLocation) {
        int currentBombGlobalID = Bomb.getBombGlobalID();
        int gridLocationIndex = Location.indexOfMovableLocationByGridLocation(sourceLocation);
        if (previousBombGlobalID_locationTrappedList[gridLocationIndex] == currentBombGlobalID) {
            return locationTrappedListByGridLocation[gridLocationIndex];
        }

        previousBombGlobalID_locationTrappedList[gridLocationIndex] = currentBombGlobalID;

        GridLocation pathLocation = new GridLocation(sourceLocation);
        markPathVisited(pathLocation);  // mark source location as visited to calculate path

        Queue<GridLocation> temporaryVisitedGridLocations = new LinkedList<>();
        // 'temporaryVisitedGridLocations' purpose: temporary mark and unmark 'visited' state without adding location to 'path'

        Queue<GridLocationWithCostAndRoot> gridLocationWithCostAndRoots = new LinkedList<>();
        GridLocationWithCostAndRoot gridLocationWithCostAndRoot = new GridLocationWithCostAndRoot();

        do {
            List<GridLocationWithCostAndRoot> gridLocationsByDirection = new ArrayList<>(directionList.length);
            for (int i = 0; i < directionList.length; i++) {
                GridLocation gridLocation = new GridLocation(pathLocation.x + directionStep[i][0], pathLocation.y + directionStep[i][1]);
                GridLocationWithCost gridLocationWithCost = new GridLocationWithCost(this, gridLocation, 0);

                GridLocationWithCostAndRoot location = new GridLocationWithCostAndRoot(gridLocationWithCostAndRoot, gridLocationWithCost, i);
                if (location.gridLocationWithCost.cost != Integer.MAX_VALUE) {
                    gridLocationsByDirection.add(location);
                    temporaryVisitedGridLocations.add(gridLocation);
                    markPathVisited(gridLocation);
                }
            }

            int gridLocationsByDirectionLength = gridLocationsByDirection.size();
            for (int i = 0; i < gridLocationsByDirectionLength; i++) {
                int randomIndex = Initialized.random.nextInt(gridLocationsByDirection.size());
                gridLocationWithCostAndRoots.add(gridLocationsByDirection.get(randomIndex));
                gridLocationsByDirection.remove(randomIndex);
            }

            gridLocationWithCostAndRoot = gridLocationWithCostAndRoots.poll();

            if (gridLocationWithCostAndRoot == null) {  // check if queue is empty (unsuccessful)
                //System.out.println(getColor().toString() + ":\t" + sourceLocation.toString() + " : is trapped");

                // reset visited
                int temporaryVisitedGridLocationsLength = temporaryVisitedGridLocations.size();
                for (int i = 0; i < temporaryVisitedGridLocationsLength; i++) {
                    unmarkPathVisited(temporaryVisitedGridLocations.poll());
                }
                locationTrappedListByGridLocation[gridLocationIndex] = true;
                unmarkPathVisited(sourceLocation);  // mark source location as unvisited after path calculation
                return locationTrappedListByGridLocation[gridLocationIndex];
            }

            pathLocation = gridLocationWithCostAndRoot.gridLocationWithCost.currentLocation;

            if (isLocationSafe(pathLocation)) {     // check if safe location found
                //System.out.println(getColor().toString() + ":\t" + sourceLocation.toString() + " : is not trapped");

                // reset visited
                int temporaryVisitedGridLocationsLength = temporaryVisitedGridLocations.size();
                for (int i = 0; i < temporaryVisitedGridLocationsLength; i++) {
                    unmarkPathVisited(temporaryVisitedGridLocations.poll());
                }
                locationTrappedListByGridLocation[gridLocationIndex] = false;
                unmarkPathVisited(sourceLocation);  // mark source location as unvisited after path calculation
                return locationTrappedListByGridLocation[gridLocationIndex];
            }

        } while (true);
    }

    /**
     * This method assumes that <code>sourceLocation</code> exists a bomb. By this condition,
     * this method creates a path to the reachable nearest safe location when successful (by Dijkstra's algorithm).
     * When unsuccessful by bomb or inner obstacles around, it creates empty path even if less or equal risky location can be found.
     * <code>passingGridLocationNumberTimeElapsedForPath</code> is assigned by the last location of the assumed bomb.
     * It always treats the source location as unsafe.
     * This should be only called when it is confirmed that the source location is unsafe.
     * Note that this method takes less risk to create a safe path.
     *
     * @return <code>true</code> if any reachable safe location which satisfies the assumed bomb condition, is found, otherwise <code>false</code>
     */
    protected boolean createPathToNearSafeLocationByAssumedBomb(GridLocation sourceLocation) {
        clearPath();

        GridLocation pathLocation = new GridLocation(sourceLocation);
        markPathVisited(pathLocation);  // mark source location as visited to calculate path

        Queue<GridLocation> temporaryVisitedGridLocations = new LinkedList<>();
        // 'temporaryVisitedGridLocations' purpose: temporary mark and unmark 'visited' state without adding location to 'path'

        int passingGridLocationNumberLastLocationAssumedBomb = 0;

        Queue<GridLocationWithCostAndRoot> gridLocationWithCostAndRoots = new LinkedList<>();
        GridLocationWithCostAndRoot gridLocationWithCostAndRoot = new GridLocationWithCostAndRoot();

        int currentDirectionOrdinal = getDirection().ordinal();     // this is used to prefer not to change direction while creating a path
        GridLocation gridLocation = new GridLocation(pathLocation.x + directionStep[currentDirectionOrdinal][0], pathLocation.y + directionStep[currentDirectionOrdinal][1]);
        GridLocationWithCost gridLocationWithCost = new GridLocationWithCost(this, gridLocation, gridLocationWithCostAndRoot.passingGridLocationNumber + 1);
        GridLocationWithCostAndRoot location = new GridLocationWithCostAndRoot(gridLocationWithCostAndRoot, gridLocationWithCost, currentDirectionOrdinal);
        if (location.gridLocationWithCost.cost < nearbyBombAsObstacleOpponentPathTimeElapsedThreshold) {
            gridLocationWithCostAndRoots.add(location);     // adding this to the queue so that changing direction will not occur when possible
            temporaryVisitedGridLocations.add(gridLocation);
            markPathVisited(gridLocation);
        }

        do {
            List<GridLocationWithCostAndRoot> gridLocationsByDirection = new ArrayList<>(directionList.length);
            for (int i = 0; i < directionList.length; i++) {
                gridLocation = new GridLocation(pathLocation.x + directionStep[i][0], pathLocation.y + directionStep[i][1]);
                gridLocationWithCost = new GridLocationWithCost(this, gridLocation, gridLocationWithCostAndRoot.passingGridLocationNumber + 1);

                location = new GridLocationWithCostAndRoot(gridLocationWithCostAndRoot, gridLocationWithCost, i);
                if (location.gridLocationWithCost.cost < nearbyBombAsObstacleOpponentPathTimeElapsedThreshold) {
                    gridLocationsByDirection.add(location);
                    temporaryVisitedGridLocations.add(gridLocation);
                    markPathVisited(gridLocation);
                }
            }

            int gridLocationsByDirectionLength = gridLocationsByDirection.size();
            for (int i = 0; i < gridLocationsByDirectionLength; i++) {
                int randomIndex = Initialized.random.nextInt(gridLocationsByDirection.size());
                gridLocationWithCostAndRoots.add(gridLocationsByDirection.get(randomIndex));
                gridLocationsByDirection.remove(randomIndex);
            }

            gridLocationWithCostAndRoot = gridLocationWithCostAndRoots.poll();

            if (gridLocationWithCostAndRoot == null) {  // check if queue is empty (unsuccessful)
                //System.out.println(getColor().toString() + ":\t" + "Nearest safe not found!");

                // reset visited
                int temporaryVisitedGridLocationsLength = temporaryVisitedGridLocations.size();
                for (int i = 0; i < temporaryVisitedGridLocationsLength; i++) {
                    unmarkPathVisited(temporaryVisitedGridLocations.poll());
                }
                unmarkPathVisited(sourceLocation);  // mark source location as unvisited after path calculation
                return false;
            }

            pathLocation = gridLocationWithCostAndRoot.gridLocationWithCost.currentLocation;

            // check if it is in the assumed bomb range
            if (isInBombRange(sourceLocation, pathLocation)) {
                //System.out.println(getColor().toString() + ":\t" + "Nearest safe found in the assumed bomb: " + pathLocation.toString());

                passingGridLocationNumberLastLocationAssumedBomb = gridLocationWithCostAndRoot.passingGridLocationNumber;
                continue;
            }

            if (isLocationSafe(pathLocation)) {     // check if safe location found
                //System.out.println(getColor().toString() + ":\t" + "Nearest safe found : " + pathLocation.toString());

                passingGridLocationNumberTimeElapsedForPath = (passingGridLocationNumberLastLocationAssumedBomb + 1) * eachGridLocationPassingTimeElapsed;

                Stack<GridLocationWithCostAndRoot> pathLocationStack = new Stack<>();

                do {
                    pathLocationStack.push(gridLocationWithCostAndRoot);
                    gridLocationWithCostAndRoot = gridLocationWithCostAndRoot.rootLocation;
                } while (!gridLocationWithCostAndRoot.isRoot);

                int pathLocationStackLength = pathLocationStack.size();
                for (int i = 0; i < pathLocationStackLength; i++) {
                    location = pathLocationStack.pop();
                    path.add(location.gridLocationWithCost.currentLocation);

                    //System.out.println(getColor().toString() + ":\t" + "To Safe: " + location.gridLocationWithCost.currentLocation);
                }

                // reset visited
                int temporaryVisitedGridLocationsLength = temporaryVisitedGridLocations.size();
                for (int i = 0; i < temporaryVisitedGridLocationsLength; i++) {
                    unmarkPathVisited(temporaryVisitedGridLocations.poll());
                }
                unmarkPathVisited(sourceLocation);  // mark source location as unvisited after path calculation
                return true;
            }

        } while (true);
    }

    /**
     * @return <code>true</code> if any 2 bombs in the locations can invoke early explosion, otherwise <code>false</code>
     */
    protected boolean isInBombRange(GridLocation location1, GridLocation location2) {
        return (((location1.x & 1) == 0) && location1.x == location2.x && Math.abs(location1.y - location2.y) <= Bomb.bombPower) ||
               (((location1.y & 1) == 0) && location1.y == location2.y && Math.abs(location1.x - location2.x) <= Bomb.bombPower);
    }


    /**
     * @return the index of the object reference of current direction if minimum cost (which is the cost of first index),
     * or the index of the object reference of same cost as of the first index randomly
     */
    protected int getIndexOfGridLocationWithCost(List<GridLocationWithCost> gridLocationWithCosts, int currentDirectionOrdinal) {
        int costBound = gridLocationWithCosts.get(0).cost;
        int listSize = gridLocationWithCosts.size();
        int indexBound = 1;
        for (; indexBound < listSize; indexBound++) {
            if (gridLocationWithCosts.get(indexBound).cost != costBound) break;
        }
        if (currentDirectionOrdinal < indexBound) return currentDirectionOrdinal;
        return Initialized.random.nextInt(indexBound);
    }

    /**
     * @return the reference of the first index of the object references (which is of minimum cost),
     * or the reference of the object of same cost as of the first index randomly
     */
    protected GridLocationWithCostAndRoot getGridLocationWithCostAndRoot(List<GridLocationWithCostAndRoot> gridLocationWithCostAndRoots, int currentDirectionOrdinal) {
        int lastIndex = gridLocationWithCostAndRoots.size() - 1;
        int costBound = gridLocationWithCostAndRoots.get(lastIndex).gridLocationWithCost.costWithoutPassingGridTimeElapsed;
        List<GridLocationWithCostAndRoot> preferredGridLocationWithCostAndRoots = new ArrayList<>();
        int indexBound = lastIndex - 1;
        for (; indexBound >= 0; indexBound--) {
            GridLocationWithCostAndRoot checkingGridLocationWithCostAndRoot = gridLocationWithCostAndRoots.get(indexBound);
            if (checkingGridLocationWithCostAndRoot.gridLocationWithCost.costWithoutPassingGridTimeElapsed != costBound) {
                break;
            }
            if (checkingGridLocationWithCostAndRoot.absoluteRootDirectionOrdinal == currentDirectionOrdinal) {
                preferredGridLocationWithCostAndRoots.add(checkingGridLocationWithCostAndRoot);
            }
        }
        if (!preferredGridLocationWithCostAndRoots.isEmpty()) {
            return preferredGridLocationWithCostAndRoots.get(Initialized.random.nextInt(preferredGridLocationWithCostAndRoots.size()));
        }
        return gridLocationWithCostAndRoots.get(lastIndex - Initialized.random.nextInt(lastIndex - indexBound));
    }


    /**
     * Marks the next element in <code>gridLocationVisited</code> as unvisited and removes from <code>path</code>
     */
    protected void removeGridLocationToMoveNext() {
        GridLocation gridLocationToMoveNext = path.peek();
        unmarkPathVisited(gridLocationToMoveNext);
        path.remove();
    }

    /**
     * Resets <code>gridLocationVisited</code> and clears <code>path</code>
     */
    protected void clearPath() {
        while (!path.isEmpty()) {
            removeGridLocationToMoveNext();
        }
    }


    protected boolean isPathVisited(GridLocation gridLocation) {
        return gridLocationVisited[gridLocation.x + 1][gridLocation.y + 1];
    }

    protected void markPathVisited(GridLocation gridLocation) {
        gridLocationVisited[gridLocation.x + 1][gridLocation.y + 1] = true;
    }

    protected void unmarkPathVisited(GridLocation gridLocation) {
        gridLocationVisited[gridLocation.x + 1][gridLocation.y + 1] = false;
    }

    protected boolean isPathVisited(int x, int y) {
        return gridLocationVisited[x + 1][y + 1];
    }

    protected void markPathVisited(int x, int y) {
        gridLocationVisited[x + 1][y + 1] = true;
    }

    protected void unmarkPathVisited(int x, int y) {
        gridLocationVisited[x + 1][y + 1] = false;
    }


    /**
     * @param gridLocation immediate <code>GridLocation</code> to move
     * @return <code>Direction</code> to reach <code>gridLocation</code>
     */
    protected Direction directionToGridLocation(GridLocation gridLocation) {
        if (gridLocation.x < currentLocation.x) return Direction.Left;
        if (gridLocation.x > currentLocation.x) return Direction.Right;
        if (gridLocation.y < currentLocation.y) return Direction.Up;
        if (gridLocation.y > currentLocation.y) return Direction.Down;
        return null;
    }

    /**
     * @return <code>true</code> if <code>gridLocation</code> is nearly aligned to <code>gridLocation</code>, otherwise <code>false</code>
     */
    protected boolean isAlignedToGridLocation(GridLocation gridLocation) {
        return Math.abs(gridLocation.getGridImageLocationX_withoutOffset() - getImageLocationX()) <= gridLocationAlignmentPrecision && Math.abs(gridLocation.getGridImageLocationY_withoutOffset() - getImageLocationY()) <= gridLocationAlignmentPrecision;
    }

    /**
     * This method moves the player to align its position to <code>gridLocation</code>.
     */
    protected void alignToGridLocation(GridLocation gridLocation) {   // it can be shown that deviation occurs in only one axis at a time
        double x_deviation = gridLocation.getGridImageLocationX_withoutOffset() - getImageLocationX();
        if (x_deviation != 0) {
            if (x_deviation > 0) encodeMove(Direction.Right);
            else encodeMove(Direction.Left);
        } else {
            double y_deviation = gridLocation.getGridImageLocationY_withoutOffset() - getImageLocationY();
            if (y_deviation > 0) encodeMove(Direction.Down);
            else if (y_deviation < 0) encodeMove(Direction.Up);
        }
    }


    protected void vibrateWhenKilled() {   // this function only does vibration for this PC-controlled single human player
    }

    protected void resetVibrationAfterDeath() {    // this function only does vibration for this PC-controlled single human player
    }

    protected void playDyingSoundWithReverberation() {     // this is the special sound effect which only works for this PC-controlled player in online games
    }

}

package GameObject.Player.OfflinePlayer.AI_Player;

import AppInfo.Customization.AllSettings;
import AppInfo.Initialized;
import GameObject.Bomb.Bomb;
import GameObject.Location.GridLocation;
import GameObject.Player.PlayerEnum.Color;
import Scene.AllScenes.GameScenes.MainGame.MainGame;

/**
 * Created by Arnob on 18/04/2015.
 * This class is the newbie version of <code>AI_Player</code> class.
 * For this, aggressive behaviour is completely removed.
 * But compulsory (move to prevent death) and optional (to protect) defensive behaviours are existed.
 * This class only supports offline game mode.
 */
public class AI_PlayerNewbie extends AI_Player {

    private static final int AI_updateIntervalMax = 800;    // max time interval of AI update (exclusive)
    private static final int AI_updateIntervalMin = 700;    // min time interval of AI update (inclusive)

    private static final int AI_updateIntervalLength = AI_updateIntervalMax - AI_updateIntervalMin;
    private int AI_updateInterval = AI_updateIntervalMin + Initialized.random.nextInt(AI_updateIntervalLength);

    private int AI_updateTime = Initialized.random.nextInt(AI_updateInterval + 1);
    // The above line is done so that all AI players do not update AI at the same time.
    // Because if updating AI for all AI players at a time, game would be slow.

    private static final int createBombProbabilityThresholdMax = 200;   // max probability value out of precision (exclusive)
    private static final int createBombProbabilityThresholdMin = 100;   // min probability value out of precision (inclusive)

    private static final int createBombProbabilityThresholdLength = createBombProbabilityThresholdMax - createBombProbabilityThresholdMin;

    public AI_PlayerNewbie(Color color) {
        super(color);
        createBombProbabilityThreshold = createBombProbabilityThresholdMin + Initialized.random.nextInt(createBombProbabilityThresholdLength);    // probability value out of precision
    }

    protected byte getInputData() {
        if (!isKilled()) {
            boolean isCurrentLocationSafe = false;
            boolean safetyChecked = false;  // this is needed because when 'isCurrentLocationSafe' is false after 'createBombWhenSafe()', then no need to call 'isLocationSafe(currentLocation)'
            if (MainGame.inputControllersList.size() > 1) {
                if (AI_updateTime < AI_updateInterval) {
                    AI_updateTime += AllSettings.userSettings.timeStep;
                } else if (MainGame.thisPC_controlledPlayerList.size() == 0) {  // add suicidal behaviour when no human player
                    encodeFire();
                } else {
                    AI_updateTime = 0;  // reset AI_updateTime
                    isCurrentLocationSafe = createBombWhenSafe();   // check whether a bomb can be placed in the current position to attack any opponent player (defense)
                    safetyChecked = true;
                    clearPath();
                }
            }

            if (!path.isEmpty()) {  // move the player if 'path' is not empty
                GridLocation gridLocationToMoveNext = path.peek();
                if (!gridLocationToMoveNext.equals(currentLocation)) {  // move until reach 'gridLocationToMoveNext'
                    encodeMove(directionToGridLocation(gridLocationToMoveNext));
                } else if (!isAlignedToGridLocation(gridLocationToMoveNext)) {
                    alignToGridLocation(gridLocationToMoveNext);
                } else {
                    removeGridLocationToMoveNext(); // player has reached 'gridLocationToMoveNext', so remove it from 'path'
                    if (!path.isEmpty()) {  // to move smoothly
                        gridLocationToMoveNext = path.peek();
                        encodeMove(directionToGridLocation(gridLocationToMoveNext));
                    }
                }
            } else if (!isCurrentLocationSafe && (safetyChecked || !isLocationSafe(currentLocation))) {    // check if current position is safe
                // place the player to a near reachable safe location
                createPathToNearSafeLocation(currentLocation);  // when no safe location is found, this method is called in every frame (as a hope for safety!)
                AI_updateTime = 0;  // reset AI_updateTime
            } else if (Bomb.bombAtGridLocation(currentLocation) == null && (isNearAnyOpponentPlayer(maxAttackDistance) && isAdjacentLocationSafe(currentLocation))) {
                // place a bomb if the most-wanted opponent is in the same or near location
                encodeFire();
                createPathToNearSafeLocation(currentLocation);
                AI_updateTime = 0;  // reset AI_updateTime
            } else if (!isAlignedToGridLocation(currentLocation)) {
                alignToGridLocation(currentLocation);
            }
        }

        byte inputDataBuffer = inputData;
        encodeMoveClear();      // clear inputData so that encoding move is cleared if any.
        return inputDataBuffer;
    }

}

package GameObject.Player.OnilnePlayer;

import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import GameObject.Bomb.Bomb;
import GameObject.Location.GridLocation;
import GameObject.Location.Location;
import GameObject.Player.InputController.InputController;
import GameObject.Player.PlayerEnum.Color;
import GameObject.Player.PlayerEnum.Direction;
import GameObject.Player.PlayerEnum.Status;
import GraphicsRenderer.Animation.PingPong_Animation;
import GraphicsRenderer.GfxFromFile;
import GraphicsRenderer.GraphicsObject;
import Network.BaseNetwork.BaseNetwork;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import SoundSystem.BaseSound;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 * Created by Arnob on 02/10/2014.
 * This class contains all variables and methods related to a player.
 * This class also has an additional feature of sending data over network.
 */
public class Player implements GraphicsObject, InputController {

    private Color color;
    private GridLocation gridLocation;
    private Direction direction = Direction.Down;

    private static final int maxBombCounter = 4;
    private int bombCounter = 0;

    private BufferedImage image;
    private double imageLocationX;
    private double imageLocationY;
    private PingPong_Animation playerAnimation;

    private static final int imageLocationY_offset = GfxFromFile.gridWidth - GfxFromFile.playerHeight;

    protected static final double moveStep = 2 * AllSettings.userSettings.motionConst;
    private static final double animateStep = 0.4 * AllSettings.userSettings.motionConst;

    protected static final Direction[] directionList = Direction.valuesCached();
    protected static final int[][] directionStep = {{0, 1}, {-1, 0}, {1, 0}, {0, -1}};

    private static final int idleAnimateElapsed = 3000;
    private static final int idleDownDirectionElapsed = 3000;
    private static final int idleEyeBlinkedElapsed = 100;
    private static final int idleEyeBlinkGapElapsed = 200;
    private static final int idleEyeBlinkTime = 3;

    private static final double killedAnimateStepCoefficient = 0.2;
    private static final int deathTimeElapsed = 500;
    private static final int deathVanishTimeElapsed = 70;
    private static final int deathVanishGapTimeElapsed = 100;
    private static final int deathVanishingTime = 5;

    private static final double motionStep = 40 * AllSettings.userSettings.motionConst;
    private int lastAnimatedTime = 0;
    private boolean inIdleMode = false;
    private int idleDownDirectionStarted = 0;
    private static final int idleEyeBlinkInterval = idleEyeBlinkedElapsed + idleEyeBlinkGapElapsed;

    private static final int deathVanishInterval = deathVanishTimeElapsed + deathVanishGapTimeElapsed;
    private boolean killed = false;
    private boolean death = false;
    private int deathStarted = 0;
    private boolean deathVanishing = false;


    protected byte inputData = 0x00;
    /*
    inputData will be used both for data transmission and player's input calculation.
	This variable has no use in NetworkPlayer.
	inputData can be:       0        0        0        1        0        0        1        0
	that means:             N/A      N/A      N/A      Fire     Up       Right    Left     Down
	*/


    private static final int vibratingValueWhenKilled = (int) (2 * AllSettings.userSettings.motionConst);
    private static final long dyingSoundReverberationDelay = 200;
    private static final double dyingSoundReverberationVolumeFactor = 0.5;

    private static final MediaPlayer walkingSound = new MediaPlayer(ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InGame.WalkingSound));
    private static final Media dyingSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InGame.DyingSound);


    static {
        walkingSound.setOnEndOfMedia(walkingSound::stop);
    }


    public Player(Color color) {
        this.color = color;

        if (color == Color.White) {
            gridLocation = new GridLocation(0, 0);

            imageLocationX = 0;
            imageLocationY = 0;
        } else if (color == Color.Black) {
            gridLocation = new GridLocation(Location.totalGridWidth, 0);

            imageLocationX = Location.totalGridWidth * GfxFromFile.gridWidth;
            imageLocationY = 0;
        } else if (color == Color.Blue) {
            gridLocation = new GridLocation(0, Location.totalGridHeight);

            imageLocationX = 0;
            imageLocationY = Location.totalGridHeight * GfxFromFile.gridWidth;
        } else {
            gridLocation = new GridLocation(Location.totalGridWidth, Location.totalGridHeight);

            imageLocationX = Location.totalGridWidth * GfxFromFile.gridWidth;
            imageLocationY = Location.totalGridHeight * GfxFromFile.gridWidth;
        }

        playerAnimation = new PingPong_Animation(2, animateStep, 1);
        image = Initialized.gfx.player(color, direction, playerAnimation.getAnimateValue());
    }

    private void PlayerDestructor(Iterator iterator) {
        iterator.remove();
        MainGame.thisPC_controlledPlayerList.remove(this);
        MainGame.inputControllersList.remove(this);
        MainGame.ai_Players.remove(this);
        MainGame.playerStatusList.get(color.ordinal()).setStatus(Status.Killed);
    }


    public GridLocation getGridLocation() {
        return gridLocation;
    }

    public double getImageLocationX() {
        return imageLocationX;
    }

    public double getImageLocationY() {
        return imageLocationY;
    }


    public void update(Iterator iterator) {
        // death
        if (killed) {
            animateMove();
            vibrateWhenKilled();
            if (death) {
                deathStarted += motionStep;
                if (deathVanishing) {
                    if (deathStarted / deathVanishInterval > deathVanishingTime) {
                        resetVibrationAfterDeath();
                        PlayerDestructor(iterator);
                    } else {
                        int deathElapsed = deathStarted % deathVanishInterval;
                        // vanished
                        if (deathElapsed < deathVanishTimeElapsed) {
                            image = Initialized.gfx.nullImage();
                        }
                        // gap after a vanish
                        else {
                            image = Initialized.gfx.playerDeath(color, 3);
                        }
                    }
                } else if (deathStarted > deathTimeElapsed) {
                    deathVanishing = true;
                    image = Initialized.gfx.nullImage();
                    deathStarted = 0;
                }
            } else {
                if (playerAnimation.getAnimateValue() == 3) death = true;
                image = Initialized.gfx.playerDeath(color, playerAnimation.getAnimateValue());
            }
            return;
        }
        // checking state (moving or idle)
        if (lastAnimatedTime < idleAnimateElapsed) {
            lastAnimatedTime += motionStep;
            image = Initialized.gfx.player(color, direction, playerAnimation.getAnimateValue());
            return;
        }
        // idle mode (eye blinking)
        idleDownDirectionStarted += motionStep;
        if (inIdleMode) {
            int idleModeElapsed = idleDownDirectionStarted;
            // checking idle mode state (standing or eye blinking)
            if (idleModeElapsed > idleDownDirectionElapsed) {
                idleModeElapsed -= idleDownDirectionElapsed;
                // standing state finishing
                if ((double) idleModeElapsed / idleEyeBlinkInterval > idleEyeBlinkTime) {
                    image = Initialized.gfx.player(color, Direction.Down, 1);
                    idleDownDirectionStarted = 0;
                }
                // eye blinking state
                else {
                    int eyeBlinkElapsed = idleModeElapsed % idleEyeBlinkInterval;
                    // eye closed
                    if (eyeBlinkElapsed < idleEyeBlinkedElapsed) {
                        image = Initialized.gfx.playerIdle(color);
                    }
                    // eye opened
                    else {
                        image = Initialized.gfx.player(color, Direction.Down, 1);
                    }
                }
            }
            return;
        }
        // idle mode started
        image = Initialized.gfx.player(color, Direction.Down, 1);
        idleDownDirectionStarted = 0;
        inIdleMode = true;
    }

    public void render(Graphics2D g) {
        g.drawImage(image, (int) Math.round(imageLocationX + Location.offsetX), (int) Math.round(imageLocationY + Location.offsetY + imageLocationY_offset), null);
    }


    public Color getColor() {
        return color;
    }

    public int getID() {
        return color.ordinal();
    }

    public Direction getDirection() {
        return direction;
    }


    protected byte getInputData() {
        byte inputDataBuffer = inputData;
        BaseNetwork.networkConnection.sendToOthers(inputDataBuffer);

        return inputDataBuffer;
    }

    public void processInput() {
        byte inputDataBuffer = getInputData();

        decodeDirection(inputDataBuffer);
        decodeFire(inputDataBuffer);
    }


    private void decodeDirection(byte data) {
        byte mask = 0x01;       // binary 00000001
        int totalDirections = Direction.valuesCached().length;
        for (int i = 0; i < totalDirections; i++) {
            if ((mask & (data >> i)) == 1) {
                move(Direction.valuesCached()[i]);
                return;
            }
        }
        move(null);
    }

    private void decodeFire(byte data) {
        byte mask = 0x10;   // binary 00010000
        if ((data & mask) != 0) createBomb();
        inputData &= 0xef;  // binary 11101111
    }


    protected void encodeMove(Direction direction) {
        inputData |= (1 << direction.ordinal());
    }

    protected void encodeMoveClear(Direction direction) {
        inputData &= Integer.rotateLeft((~1), direction.ordinal());
    }

    protected void encodeMoveClear() {
        inputData &= 0xf0;      // binary 11110000
    }

    protected void encodeFire() {
        inputData |= 0x10;  // binary 00010000
    }


    public void keyPressed(KeyEvent e) {
        if (isKeyDown(e)) {
            encodeMove(Direction.Down);
        } else if (isKeyLeft(e)) {
            encodeMove(Direction.Left);
        } else if (isKeyRight(e)) {
            encodeMove(Direction.Right);
        } else if (isKeyUp(e)) {
            encodeMove(Direction.Up);
        } else if (isKeyFire(e)) {
            encodeFire();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (isKeyDown(e)) {
            encodeMoveClear(Direction.Down);
        } else if (isKeyLeft(e)) {
            encodeMoveClear(Direction.Left);
        } else if (isKeyRight(e)) {
            encodeMoveClear(Direction.Right);
        } else if (isKeyUp(e)) {
            encodeMoveClear(Direction.Up);
        }
    }

    public void focusLost(FocusEvent e) {
        inputData = 0;
    }


    protected boolean isKeyUp(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Key_Up;
    }

    protected boolean isKeyDown(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Key_Down;
    }

    protected boolean isKeyLeft(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Key_Left;
    }

    protected boolean isKeyRight(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Key_Right;
    }

    protected boolean isKeyFire(KeyEvent e) {
        return e.getKeyCode() == AllSettings.userSettings.Key_Fire;
    }


    private void animateMove() {
        lastAnimatedTime = 0;
        inIdleMode = false;

        playerAnimation.animate();
        if (walkingSound.getStatus() != MediaPlayer.Status.PLAYING)
            BaseSound.playSound(walkingSound, AllSettings.userSettings.soundEffectVolume, BaseSound.getSoundBalance(gridLocation));
    }

    private void move(Direction direction) {
        if (direction == null || killed) return;

        if (this.direction != direction) {
            playerAnimation.setAnimateValue(1);
            image = Initialized.gfx.player(color, direction, playerAnimation.getAnimateValue());
            this.direction = direction;
        }

        int[] moveDirectionStep = directionStep[direction.ordinal()];
        GridLocation possibleBombGridLocation = new GridLocation(
                gridLocation.x + moveDirectionStep[0],
                gridLocation.y + moveDirectionStep[1]);

        // checking boundary obstacle
        if ((moveDirectionStep[0] != 0 && 0 <= possibleBombGridLocation.x && possibleBombGridLocation.x <= Location.totalGridWidth) ||
            (moveDirectionStep[0] == 0 && 0 <= possibleBombGridLocation.y && possibleBombGridLocation.y <= Location.totalGridHeight)) {
            // checking bomb obstacle (known: no boundary obstacle)
            if (Location.isMovableLocation(possibleBombGridLocation)) {
                Bomb obstacleBomb = Bomb.bombAtGridLocation(possibleBombGridLocation);
                if (obstacleBomb != null) {
                    if (moveDirectionStep[0] != 0) {
                        double calcImageLocationX = imageLocationX + moveDirectionStep[0] * (moveStep + GfxFromFile.bomb_Width);
                        if ((moveDirectionStep[0] * calcImageLocationX) < (moveDirectionStep[0] * obstacleBomb.getImageLocationX())) {
                            imageLocationX += moveDirectionStep[0] * moveStep;
                            animateMove();
                        } else {
                            imageLocationX = obstacleBomb.getImageLocationX() - moveDirectionStep[0] * GfxFromFile.bomb_Width;
                        }
                    } else {
                        double calcImageLocationY = imageLocationY + moveDirectionStep[1] * (moveStep + GfxFromFile.bomb_Width);
                        if ((moveDirectionStep[1] * calcImageLocationY) < (moveDirectionStep[1] * obstacleBomb.getImageLocationX())) {
                            imageLocationY += moveDirectionStep[1] * moveStep;
                            animateMove();
                        } else {
                            imageLocationY = obstacleBomb.getImageLocationY() - moveDirectionStep[1] * GfxFromFile.bomb_Width;
                        }
                    }
                }
                // checking inner obstacle (known: no bomb obstacle)
                else {
                    if (moveDirectionStep[0] != 0) {
                        // checking alignment (known: no inner obstacle)
                        double alignmentDiff = Math.signum(gridLocation.y * GfxFromFile.gridWidth - imageLocationY);
                        if (alignmentDiff != 0.0) {
                            double possibleImageLocationY = imageLocationY + alignmentDiff * moveStep;
                            if (alignmentDiff * possibleImageLocationY < alignmentDiff * gridLocation.y * GfxFromFile.gridWidth) {
                                imageLocationY = possibleImageLocationY;
                            } else {
                                imageLocationY = gridLocation.y * GfxFromFile.gridWidth;
                            }
                        }
                        // can move freely (known: aligned)
                        else {
                            imageLocationX += moveDirectionStep[0] * moveStep;
                            if (moveDirectionStep[0] * (imageLocationX + Location.gridWidth_half) >
                                moveDirectionStep[0] * ((gridLocation.x + ((moveDirectionStep[0] + 1) >> 1)) * GfxFromFile.gridWidth)) {
                                gridLocation.x += moveDirectionStep[0];
                            }
                        }
                        animateMove();
                    } else {
                        // checking alignment (known: no inner obstacle)
                        double alignmentDiff = Math.signum(gridLocation.x * GfxFromFile.gridWidth - imageLocationX);
                        if (alignmentDiff != 0.0) {
                            double possibleImageLocationX = imageLocationX + alignmentDiff * moveStep;
                            if (alignmentDiff * possibleImageLocationX < alignmentDiff * gridLocation.x * GfxFromFile.gridWidth) {
                                imageLocationX = possibleImageLocationX;
                            } else {
                                imageLocationX = gridLocation.x * GfxFromFile.gridWidth;
                            }
                        }
                        // can move freely (known: aligned)
                        else {
                            imageLocationY += moveDirectionStep[1] * moveStep;
                            if (moveDirectionStep[1] * (imageLocationY + Location.gridWidth_half) >
                                moveDirectionStep[1] * ((gridLocation.y + ((moveDirectionStep[1] + 1) >> 1)) * GfxFromFile.gridWidth)) {
                                gridLocation.y += moveDirectionStep[1];
                            }
                        }
                        animateMove();
                    }
                }
            }
        }
        // checking offset (known: boundary obstacle found)
        else {
            if (moveDirectionStep[0] != 0) {
                double possibleImageLocationX = imageLocationX + moveDirectionStep[0] * moveStep;
                if (0 < possibleImageLocationX && possibleImageLocationX < Location.totalWidth) {
                    imageLocationX = possibleImageLocationX;
                    animateMove();
                } else {
                    imageLocationX = ((moveDirectionStep[0] + 1) >> 1) * Location.totalWidth;
                }
            } else {
                double possibleImageLocationY = imageLocationY + moveDirectionStep[1] * moveStep;
                if (0 < possibleImageLocationY && possibleImageLocationY < Location.totalHeight) {
                    imageLocationY = possibleImageLocationY;
                    animateMove();
                } else {
                    imageLocationY = ((moveDirectionStep[1] + 1) >> 1) * Location.totalHeight;
                }
            }
        }
    }


    private void createBomb() {
        if (!isKilled() && bombCounter < maxBombCounter) {
            if (Bomb.bombAtGridLocation(gridLocation) != null) return;
            new Bomb(this, gridLocation);
            bombCounter++;
        }
    }

    public void decreaseBombCounter() {
        bombCounter--;
    }


    public void kill() {
        if (!killed) {
            killed = true;
            playerAnimation.setAnimateStepByFactor(killedAnimateStepCoefficient);
            playerAnimation.setUpperLimit(3);
            playerAnimation.resetAnimation();
            BaseSound.playSound(dyingSound, AllSettings.userSettings.soundEffectVolume, BaseSound.getSoundBalance(gridLocation));
            playDyingSoundWithReverberation();
        }
    }

    public boolean isKilled() {
        return killed;
    }


    protected void vibrateWhenKilled() {   // this function only does vibration for this PC-controlled single human player
        MainGame.vibrateOffsetX = vibratingValueWhenKilled - Initialized.random.nextInt((vibratingValueWhenKilled << 1) + 1);
        MainGame.vibrateOffsetY = vibratingValueWhenKilled - Initialized.random.nextInt((vibratingValueWhenKilled << 1) + 1);
        Location.changeOffset(MainGame.vibrateOffsetX, MainGame.vibrateOffsetY);
    }

    protected void resetVibrationAfterDeath() {    // this function only does vibration for this PC-controlled single human player
        MainGame.vibrateOffsetX = 0;
        MainGame.vibrateOffsetY = 0;
        Location.changeOffset(MainGame.vibrateOffsetX, MainGame.vibrateOffsetY);
    }

    protected void playDyingSoundWithReverberation() { // this is the special sound effect which only works for this PC-controlled player in online games
        BaseSound.playSound(dyingSound, AllSettings.userSettings.soundEffectVolume * dyingSoundReverberationVolumeFactor, -BaseSound.getSoundBalance(gridLocation), dyingSoundReverberationDelay);
    }

}

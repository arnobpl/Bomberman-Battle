package Scene.AllScenes.GameScenes.MainGame;

import AppInfo.Customization.AllSettings;
import AppInfo.FilePath;
import AppInfo.Initialized;
import AppInfo.Resource.ResourceLoader;
import GameObject.Bomb.Bomb;
import GameObject.Location.GridLocation;
import GameObject.Location.Location;
import GameObject.Location.LocationEnum.InnerObjectType;
import GameObject.Location.LocationEnum.OuterHomeColor;
import GameObject.Player.InputController.InputController;
import GameObject.Player.OfflinePlayer.AI_Player.AI_Player;
import GameObject.Player.OfflinePlayer.AI_Player.AI_PlayerNewbie;
import GameObject.Player.OfflinePlayer.OfflinePlayer;
import GameObject.Player.OfflinePlayer.Player0;
import GameObject.Player.OfflinePlayer.Player1;
import GameObject.Player.OnilnePlayer.AI_NetworkPlayer;
import GameObject.Player.OnilnePlayer.NetworkPlayer;
import GameObject.Player.OnilnePlayer.Player;
import GameObject.Player.PlayerEnum.Color;
import GameObject.Player.PlayerEnum.Status;
import GameObject.Player.PlayerStatus;
import GameObject.Result.Result;
import GraphicsRenderer.Animation.Transition_Animation;
import GraphicsRenderer.GfxFromFile;
import GraphicsRenderer.GraphicsObject;
import GraphicsRenderer.RenderGraphics;
import Network.BaseNetwork.BaseNetwork;
import Scene.Scene;
import Scene.Subscene;
import SoundSystem.BaseSound;
import SoundSystem.LoopingSound;
import javafx.scene.media.Media;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Arnob on 01/10/2014.
 * This class is responsible for drawing graphics object in the main game state.
 */
public class MainGame extends Scene {

    public static int totalPlayers = AllSettings.userSettings.totalPlayers; // this variable is intended to written from outside classes
    public static int totalHumanPlayers = AllSettings.userSettings.totalHumanPlayers;   // this variable is intended to written from outside classes

    public static Color thisPlayerColor = Color.White;  // this variable is intended to written from outside classes

    public static int AI_playerDifficulty = AllSettings.userSettings.AI_playerDifficulty;   // 0 is for "Newbie", 1 is for "Normal"

    public static int selectedLocation;
    private static boolean isOnlineGame = false;


    private static final BufferedImage unscaledScreenImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D unscaledScreenGraphics = unscaledScreenImage.createGraphics();

    private static final BufferedImage mainLocationImage = new BufferedImage(AllSettings.unscaledWidth, AllSettings.unscaledHeight, BufferedImage.TYPE_INT_ARGB);
    private static final BufferedImage HUD_image = new BufferedImage(GfxFromFile.HUD_Width, GfxFromFile.HUD_Height, BufferedImage.TYPE_INT_ARGB);


    public static List<GraphicsObject> graphicsObjectList;
    public static List<Player> thisPC_controlledPlayerList;
    public static List<InputController> inputControllersList;
    public static List<AI_Player> ai_Players;
    public static List<PlayerStatus> playerStatusList;


    public static Subscene subscene;


    public static int matchToWin = AllSettings.userSettings.matchToWin;
    public static int matchDurationMinute = AllSettings.userSettings.matchDurationMinute;

    public static int timeoutWarningElapsed;

    public static int time;
    public static int matchDuration;
    private static String minute;
    private static String second;


    private static final Font HUD_time_Font = unscaledScreenGraphics.getFont().deriveFont(10.0f);
    private static final int HUD_time_Left = (AllSettings.unscaledWidth - 32) >> 1;
    private static final int HUD_time_Top = 15;

    public static Result gameResult;

    public static int vibrateOffsetX;
    public static int vibrateOffsetY;

    private static final int transitionTimeElapsed = 1000;
    private static final double transitionAlphaStep = 10 * AllSettings.userSettings.motionConst;

    public static Transition_Animation transitionAnimation;

    private static final int readyGoSoundDelay = 100;

    public static final LoopingSound gameMusic = new LoopingSound(ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InGame.GameMusic), AllSettings.userSettings.backgroundMusicVolume);
    private static final Media readyGoSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InGame.ReadyGoSound);
    public static final Media resultFoundSound = ResourceLoader.loadSoundFromJAR(FilePath.SfxFilePath.InGame.ResultFoundSound);
    public static boolean resultFoundSoundPlayed;


    /*
    There are 3 situations of a draw game:
        1. When all players are dying in the exact frame
        2. When all players are dying in the almost exact frames
        3. Game is timeout
    */
    public static boolean drawGame;
    public static Color winnerPlayerColor;


    /**
     * This is for creating a new match of the current running game.
     */
    public MainGame(int selectedLocation) {
        MainGame.selectedLocation = selectedLocation;

        initializeGame();
        if (isOnlineGame) createOnlineGame();
        else createOfflineGame();

        finalizeInitialization();
    }

    /**
     * This is for creating a new game.
     */
    public MainGame(boolean isOnlineGame) {
        MainGame.isOnlineGame = isOnlineGame;
        gameResult = new Result(totalPlayers);
        selectedLocation = 0;

        initializeGame();
        if (isOnlineGame) createOnlineGame();
        else createOfflineGame();

        finalizeInitialization();
    }

    private void initializeGame() {
        transitionAnimation = new Transition_Animation(transitionTimeElapsed, transitionAlphaStep).start();

        thisPC_controlledPlayerList = new ArrayList<>();
        inputControllersList = new ArrayList<>();
        ai_Players = new ArrayList<>();
        playerStatusList = new ArrayList<>();
        graphicsObjectList = new ArrayList<>();

        Bomb.initializeBombListSortedByGridLocation();

        createMainLocationImage(selectedLocation);
        createInnerObjectImage(selectedLocation);
        createHUDimage(totalPlayers);
        unscaledScreenGraphics.setColor(java.awt.Color.black);

        matchDuration = matchDurationMinute * 60 * 1000;
        time = 0;
        timeoutWarningElapsed = MainGame.matchDuration - AllSettings.timeoutWarningStartingTimeBeforeTimeout;
        vibrateOffsetX = 0;
        vibrateOffsetY = 0;
        Location.changeOffset(vibrateOffsetX, vibrateOffsetY);
        subscene = new ReadyGoState();

        gameMusic.playAfterSetVolume(AllSettings.userSettings.backgroundMusicVolume, BaseSound.fadeinElapsed);
        BaseSound.playSound(readyGoSound, AllSettings.userSettings.soundEffectVolume, 0.0, readyGoSoundDelay);
        resultFoundSoundPlayed = false;
    }

    private void createOnlineGame() {
        Player thisPC_controlledPlayer = new Player(thisPlayerColor);
        thisPC_controlledPlayerList.add(thisPC_controlledPlayer);
        inputControllersList.add(thisPC_controlledPlayer);
        playerStatusList.add(new PlayerStatus(thisPlayerColor, Status.Myself));
        graphicsObjectList.add(thisPC_controlledPlayer);
        if (BaseNetwork.thisPC_createdGame) {
            for (int i = 1; i < totalHumanPlayers; i++) {
                NetworkPlayer netPlayer = new NetworkPlayer(Color.valuesCached()[i]);
                inputControllersList.add(netPlayer);
                playerStatusList.add(new PlayerStatus(netPlayer.getColor(), Status.Opponent));
                graphicsObjectList.add(netPlayer);
            }
            for (int i = totalHumanPlayers; i < totalPlayers; i++) {
                AI_Player ai_Player = new AI_NetworkPlayer(Color.valuesCached()[i]);
                inputControllersList.add(ai_Player);
                ai_Players.add(ai_Player);
                playerStatusList.add(new PlayerStatus(ai_Player.getColor(), Status.Opponent));
                graphicsObjectList.add(ai_Player);
            }
        } else {
            final int thisPlayerColorOrdinal = thisPlayerColor.ordinal();
            for (int i = 0; i < thisPlayerColorOrdinal; i++) {
                NetworkPlayer netPlayer = new NetworkPlayer(Color.valuesCached()[i]);
                inputControllersList.add(netPlayer);
                playerStatusList.add(new PlayerStatus(netPlayer.getColor(), Status.Opponent));
                graphicsObjectList.add(netPlayer);
            }
            for (int i = thisPlayerColorOrdinal + 1; i < totalPlayers; i++) {
                NetworkPlayer netPlayer = new NetworkPlayer(Color.valuesCached()[i]);
                inputControllersList.add(netPlayer);
                playerStatusList.add(new PlayerStatus(netPlayer.getColor(), Status.Opponent));
                graphicsObjectList.add(netPlayer);
            }
        }
    }

    private void createOfflineGame() {
        if (totalHumanPlayers == 1) {
            thisPlayerColor = Color.White;
            OfflinePlayer offlinePlayer = new OfflinePlayer();
            thisPC_controlledPlayerList.add(offlinePlayer);
            inputControllersList.add(offlinePlayer);
            playerStatusList.add(new PlayerStatus(thisPlayerColor, Status.Myself));
            graphicsObjectList.add(offlinePlayer);
        } else if (totalHumanPlayers == 2) {
            thisPC_controlledPlayerList.add(new Player0());
            thisPC_controlledPlayerList.add(new Player1());
            for (Player i : thisPC_controlledPlayerList) {
                inputControllersList.add(i);
                playerStatusList.add(new PlayerStatus(i.getColor(), Status.Opponent));
                graphicsObjectList.add(i);
            }
        }

        if (AI_playerDifficulty == 1) {
            for (int i = totalHumanPlayers; i < totalPlayers; i++) {
                AI_Player ai_Player = new AI_Player(Color.valuesCached()[i]);
                inputControllersList.add(ai_Player);
                ai_Players.add(ai_Player);
                playerStatusList.add(new PlayerStatus(ai_Player.getColor(), Status.Opponent));
                graphicsObjectList.add(ai_Player);
            }
        } else {
            for (int i = totalHumanPlayers; i < totalPlayers; i++) {
                AI_Player ai_Player = new AI_PlayerNewbie(Color.valuesCached()[i]);
                inputControllersList.add(ai_Player);
                ai_Players.add(ai_Player);
                playerStatusList.add(new PlayerStatus(ai_Player.getColor(), Status.Opponent));
                graphicsObjectList.add(ai_Player);
            }
        }
    }

    private void finalizeInitialization() {
        inputControllersList.sort(InputController.inputControllerOrder);
        Collections.sort(playerStatusList);
        graphicsObjectList.sort(GraphicsObject.renderingOrder);

        if (!ai_Players.isEmpty()) {
            for (AI_Player i : ai_Players) {
                i.initializeOpponentPlayers();
            }
            AI_Player.initializeOptimizationLists();
        }
    }


    private static void createMainLocationImage(int selectedLocation) {
        // Creating a static image surface of boundary, grass, road, outer home, outer grid

        Graphics2D g = mainLocationImage.createGraphics();
        final OuterHomeColor[][][] outerHomeColorList = {{
                {OuterHomeColor.Blue, OuterHomeColor.Yellow, OuterHomeColor.Red, OuterHomeColor.Blue},
                {OuterHomeColor.Yellow, OuterHomeColor.Blue, OuterHomeColor.Yellow, OuterHomeColor.Red}
        }, {
                {OuterHomeColor.Red, OuterHomeColor.Blue, OuterHomeColor.Yellow, OuterHomeColor.Red},
                {OuterHomeColor.Blue, OuterHomeColor.Yellow, OuterHomeColor.Red, OuterHomeColor.Blue}
        }};
        for (int i = 0; i < 4; i++) {
            g.drawImage(Initialized.gfx.outerHome(outerHomeColorList[selectedLocation][0][i].ordinal()), Location.offsetX - GfxFromFile.outerHome_Width - GfxFromFile.boundary_Width, i * GfxFromFile.outerHome_Height + Location.offsetY - GfxFromFile.gridWidth, null);
            g.drawImage(Initialized.gfx.outerHome(outerHomeColorList[selectedLocation][1][i].ordinal()), AllSettings.unscaledWidth + Location.offsetX - GfxFromFile.boundary_Width - (GfxFromFile.outerHome_Width << 1), i * GfxFromFile.outerHome_Height + Location.offsetY - GfxFromFile.gridWidth, null);
        }
        for (int i = Location.totalGridHeight + 3; i > 0; i--) {
            g.drawImage(Initialized.gfx.leftBoundary(), Location.offsetX - GfxFromFile.boundary_Width, (i - 2) * GfxFromFile.gridWidth + Location.offsetY, null);
            g.drawImage(Initialized.gfx.rightBoundary(), AllSettings.unscaledWidth + Location.offsetX - GfxFromFile.outerGrid_Width - GfxFromFile.boundary_Width - (GfxFromFile.outerHome_Width << 1), (i - 2) * GfxFromFile.gridWidth + Location.offsetY, null);
        }
        for (int i = 0; i < 2; i++) {
            g.drawImage(Initialized.gfx.outerGrid(), i * GfxFromFile.outerGrid_Width + Location.offsetX - GfxFromFile.outerHome_Width - GfxFromFile.boundary_Width, 4 * GfxFromFile.outerHome_Height + GfxFromFile.gridWidth + Location.offsetY - GfxFromFile.gridWidth - GfxFromFile.boundary_Width, null);
            g.drawImage(Initialized.gfx.outerGrid(), AllSettings.unscaledWidth - (i + 1) * GfxFromFile.outerGrid_Width + Location.offsetX - GfxFromFile.outerHome_Width - GfxFromFile.boundary_Width, 4 * GfxFromFile.outerHome_Height + GfxFromFile.gridWidth + Location.offsetY - GfxFromFile.gridWidth - GfxFromFile.boundary_Width, null);
        }
        for (int i = Location.totalGridWidth; i > -1; i--) {
            g.drawImage(Initialized.gfx.topBoundary(Initialized.random.nextInt(2)), i * GfxFromFile.boundary_Width + GfxFromFile.outerGrid_Width + Location.offsetX - GfxFromFile.boundary_Width, Location.offsetY - GfxFromFile.boundary_Width, null);
            g.drawImage(Initialized.gfx.grassBoundaryTop(), i * GfxFromFile.boundary_Width + GfxFromFile.outerGrid_Width + Location.offsetX - GfxFromFile.boundary_Width, GfxFromFile.gridWidth + Location.offsetY - GfxFromFile.gridWidth, null);
            g.drawImage(Initialized.gfx.bottomBoundary(), i * GfxFromFile.boundary_Width + GfxFromFile.outerGrid_Width + Location.offsetX - GfxFromFile.boundary_Width, AllSettings.unscaledHeight + Location.offsetY - GfxFromFile.gridWidth - (GfxFromFile.boundary_Width << 1), null);
        }
        g.drawImage(Initialized.gfx.grassBoundaryTopLeft(), GfxFromFile.outerGrid_Width + Location.offsetX - GfxFromFile.boundary_Width, Location.offsetY, null);
        for (int i = 0; i < Location.totalGridHeight; i++) {
            g.drawImage(Initialized.gfx.grassBoundaryRight(), AllSettings.unscaledWidth + Location.offsetX - GfxFromFile.boundary_Width - ((GfxFromFile.gridWidth + GfxFromFile.outerHome_Width) << 1), (i + 2) * GfxFromFile.gridWidth + Location.offsetY - GfxFromFile.boundary_Width, null);
            g.drawImage(Initialized.gfx.grassBoundaryLeft(i & 1), GfxFromFile.outerGrid_Width + Location.offsetX - GfxFromFile.boundary_Width, (i + 2) * GfxFromFile.gridWidth + Location.offsetY - GfxFromFile.boundary_Width, null);
        }
        for (int i = Location.totalGridWidth - 2; i > 0; i -= 2) {
            for (int j = 0; j < Location.totalGridHeight; j++) {
                g.drawImage(Initialized.gfx.grassShadow(j & 1), i * GfxFromFile.gridWidth + GfxFromFile.outerGrid_Width + Location.offsetX - GfxFromFile.boundary_Width, (j + 2) * GfxFromFile.gridWidth + Location.offsetY - GfxFromFile.boundary_Width, null);
            }
        }
        for (int i = Location.totalGridWidth - 1; i > 0; i -= 2) {
            for (int j = 0; j < Location.totalGridHeight; j += 2) {
                g.drawImage(Initialized.gfx.grassShadow(2), i * GfxFromFile.gridWidth + GfxFromFile.outerGrid_Width + Location.offsetX - GfxFromFile.boundary_Width, (j + 3) * GfxFromFile.gridWidth + Location.offsetY - GfxFromFile.boundary_Width, null);
            }
        }
        // Creating roads
        final GridLocation[] roadX0_gridLocationList =
                {new GridLocation(0, 2), new GridLocation(0, 8)};
        final GridLocation[] roadX1_gridLocationList =
                {new GridLocation(1, 2), new GridLocation(1, 8), new GridLocation(3, 2), new GridLocation(3, 8), new GridLocation(4, 2)};
        final GridLocation[] roadY0_gridLocationList =
                {new GridLocation(4, 3), new GridLocation(4, 7)};
        final GridLocation[] roadY1_gridLocationList =
                {new GridLocation(4, 4), new GridLocation(4, 6)};
        final GridLocation[] roadCrossY_gridLocationList =
                {new GridLocation(2, 2), new GridLocation(2, 8)};
        final GridLocation[] roadCrossX_gridLocationList =
                {new GridLocation(4, 5)};
        final GridLocation[] roadXY_gridLocationList =
                {new GridLocation(4, 8)};
        for (GridLocation i : roadX0_gridLocationList) {
            g.drawImage(Initialized.gfx.roadX(0), i.getGridImageLocationX(), i.getGridImageLocationY(), null);
            g.drawImage(Initialized.gfx.roadX(1), GridLocation.getGridImageLocationX(Location.totalGridWidth - i.x), i.getGridImageLocationY(), null);
        }
        for (GridLocation i : roadX1_gridLocationList) {
            g.drawImage(Initialized.gfx.roadX(1), i.getGridImageLocationX(), i.getGridImageLocationY(), null);
            g.drawImage(Initialized.gfx.roadX(1), GridLocation.getGridImageLocationX(Location.totalGridWidth - i.x), i.getGridImageLocationY(), null);
        }
        for (GridLocation i : roadY0_gridLocationList) {
            g.drawImage(Initialized.gfx.roadY(0), i.getGridImageLocationX(), i.getGridImageLocationY(), null);
            g.drawImage(Initialized.gfx.roadY(0), GridLocation.getGridImageLocationX(Location.totalGridWidth - i.x), i.getGridImageLocationY(), null);
        }
        for (GridLocation i : roadY1_gridLocationList) {
            g.drawImage(Initialized.gfx.roadY(1), i.getGridImageLocationX(), i.getGridImageLocationY(), null);
            g.drawImage(Initialized.gfx.roadY(1), GridLocation.getGridImageLocationX(Location.totalGridWidth - i.x), i.getGridImageLocationY(), null);
        }
        for (GridLocation i : roadCrossY_gridLocationList) {
            g.drawImage(Initialized.gfx.roadCrossY(), i.getGridImageLocationX(), i.getGridImageLocationY(), null);
            g.drawImage(Initialized.gfx.roadCrossY(), GridLocation.getGridImageLocationX(Location.totalGridWidth - i.x), i.getGridImageLocationY(), null);
        }
        for (GridLocation i : roadCrossX_gridLocationList) {
            g.drawImage(Initialized.gfx.roadCrossX(), i.getGridImageLocationX(), i.getGridImageLocationY(), null);
            g.drawImage(Initialized.gfx.roadCrossX(), GridLocation.getGridImageLocationX(Location.totalGridWidth - i.x), i.getGridImageLocationY(), null);
        }
        for (GridLocation i : roadXY_gridLocationList) {
            g.drawImage(Initialized.gfx.roadXY(), i.getGridImageLocationX(), i.getGridImageLocationY(), null);
            g.drawImage(Initialized.gfx.roadXY(), GridLocation.getGridImageLocationX(Location.totalGridWidth - i.x), i.getGridImageLocationY(), null);
        }
        g.dispose();
        if (AllSettings.userSettings.cinematicEffectEnabled) {
            RenderGraphics.setAlpha(mainLocationImage, AllSettings.motionBlurAlpha);
        }
    }

    private static void createInnerObjectImage(int selectedLocation) {
        // Creating static image surfaces of inner objects

        Graphics2D g;
        final InnerObjectType[][][] innerObjectTypeList = {{
                {InnerObjectType.Tree, InnerObjectType.RedHome, InnerObjectType.BlueHome, InnerObjectType.RedHome, InnerObjectType.YellowHome, InnerObjectType.Tree},
                {InnerObjectType.RedHome, InnerObjectType.YellowHome, InnerObjectType.Tree, InnerObjectType.Tree, InnerObjectType.RedHome, InnerObjectType.BlueHome},
                {InnerObjectType.Tree, InnerObjectType.BlueHome, InnerObjectType.Tree, InnerObjectType.Tree, InnerObjectType.YellowHome, InnerObjectType.Tree},
                {InnerObjectType.BlueHome, InnerObjectType.RedHome, InnerObjectType.Tree, InnerObjectType.Tree, InnerObjectType.BlueHome, InnerObjectType.YellowHome},
                {InnerObjectType.Tree, InnerObjectType.YellowHome, InnerObjectType.BlueHome, InnerObjectType.RedHome, InnerObjectType.BlueHome, InnerObjectType.Tree}
        }, {
                {InnerObjectType.RedHome, InnerObjectType.Tree, InnerObjectType.BlueHome, InnerObjectType.RedHome, InnerObjectType.Tree, InnerObjectType.YellowHome},
                {InnerObjectType.Tree, InnerObjectType.RedHome, InnerObjectType.YellowHome, InnerObjectType.RedHome, InnerObjectType.BlueHome, InnerObjectType.Tree},
                {InnerObjectType.RedHome, InnerObjectType.YellowHome, InnerObjectType.Tree, InnerObjectType.Tree, InnerObjectType.BlueHome, InnerObjectType.YellowHome},
                {InnerObjectType.Tree, InnerObjectType.BlueHome, InnerObjectType.RedHome, InnerObjectType.BlueHome, InnerObjectType.YellowHome, InnerObjectType.Tree},
                {InnerObjectType.YellowHome, InnerObjectType.Tree, InnerObjectType.BlueHome, InnerObjectType.RedHome, InnerObjectType.Tree, InnerObjectType.BlueHome}
        }};
        int innerObjectRow = Location.totalGridHeight >> 1;
        int innerObjectCol = Location.totalGridWidth >> 1;
        for (int i = 0; i < innerObjectRow; i++) {
            Location.innerObjectImage[i] = new BufferedImage(Location.totalWidth + GfxFromFile.gridWidth, GfxFromFile.innerObj_Height, BufferedImage.TYPE_INT_ARGB);
            g = Location.innerObjectImage[i].createGraphics();
            for (int j = 0; j < innerObjectCol; j++) {
                g.drawImage(Initialized.gfx.innerObject(innerObjectTypeList[selectedLocation][i][j].ordinal()), ((j << 1) + 1) * GfxFromFile.gridWidth, 0, null);
            }
            g.dispose();
            graphicsObjectList.add(new Location(i));
        }
    }

    private static void createHUDimage(int totalPlayers) {
        Graphics2D g = HUD_image.createGraphics();
        g.drawImage(Initialized.gfx.HUD(), 0, 0, null);
        for (int i = 2; i < totalPlayers; i++) {
            g.drawImage(Initialized.gfx.HUDstatus(), (i & 1) * 56 + 201, 0, null);
        }
        g.dispose();
    }


    public void update() {
        // time string update
        int remainingSeconds = (MainGame.matchDuration - time) / 1000;
        minute = String.format("%02d", remainingSeconds / 60);
        second = String.format("%02d", remainingSeconds % 60);

        // subscene update
        subscene.update();

        // update
        for (Iterator<GraphicsObject> i = graphicsObjectList.iterator(); i.hasNext(); ) {
            i.next().update(i);
        }

        // sorting rendering order
        graphicsObjectList.sort(GraphicsObject.renderingOrder);
    }

    public void render(Graphics2D g) {
        //unscaledScreenGraphics.clearRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);

        unscaledScreenGraphics.drawImage(mainLocationImage, vibrateOffsetX, vibrateOffsetY, null);

        for (GraphicsObject i : graphicsObjectList) {
            i.render(unscaledScreenGraphics);
        }

        unscaledScreenGraphics.drawImage(HUD_image, 0, 0, null);
        unscaledScreenGraphics.setFont(HUD_time_Font);
        unscaledScreenGraphics.drawString(minute + " : " + second, HUD_time_Left, HUD_time_Top);

        for (PlayerStatus i : playerStatusList) {
            i.render(unscaledScreenGraphics);
        }

        // subscene render
        subscene.render(unscaledScreenGraphics);

        g.scale(AllSettings.userSettings.displayScale, AllSettings.userSettings.displayScale);
        g.drawImage(unscaledScreenImage, 0, 0, null);
    }


    public void keyPressed(KeyEvent e) {
        for (Player i : thisPC_controlledPlayerList) {
            i.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        for (Player i : thisPC_controlledPlayerList) {
            i.keyReleased(e);
        }
    }


    public void focusLost(FocusEvent e) {
        for (Player i : thisPC_controlledPlayerList) {
            i.focusLost(e);
        }
    }

}

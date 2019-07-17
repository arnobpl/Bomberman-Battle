package AppInfo;

/**
 * Created by Arnob on 28/09/2014.
 * This class contains all the strings displayed in the game.
 */
public class Strings {
    public static final String WindowTitle = AppInfo.AppName;

    public static class Error {
        public static final String FatalError = "Fatal Error!";

        public static final String GfxNotFound = "Graphics not found or invalid!";
        public static final String FontNotFound = "Font not found or invalid!";
        public static final String AudioNotFound = "Audio not found or invalid!";
        public static final String InfoNotFound = "Info not found or invalid!";

        public static final String ConnectionFailure = "Connection Failure!";
        public static final String ConnectionReset = "Connection Reset!";
        public static final String CompatibilityError = "Compatibility Error!";
    }

    public static class Game {
        public static final String HurryUp = "Hurry Up!";
        public static final String ScoreBoard = "Score Board";
        public static final String GameOver = "Game Over!";
    }

    public static class MainMenu {
        public static final String NewGame = "New Game";
        public static final String HowToPlay = "How to Play";
        public static final String Settings = "Settings";
        public static final String Credit = "Credit";
        public static final String Exit = "Exit";

        public static final String Back = "Back";   // not used here but convenient to place it here
    }

    public static class NewGame {
        public static final String CreateGame = "Create Game";
        public static final String JoinGame = "Join Game";
        public static final String OfflineGame = "Offline Game";

        public static final String Play = "Play";                   // not used here but convenient to place it here
        public static final String PleaseWait = "Please Wait...";   // not used here but convenient to place it here
    }

    public static class CreateGame {
        public static final String TotalPlayers = "Players";
        public static final String TotalHumanPlayers = "Human";
        public static final String ConnectionType = "Connection";
        public static final String P2P = "P2P";
        public static final String CS = "CS";
        public static final String YourIP_Address = "Your IP:";
        public static final String IP_AddressRetrieveError = "Error Occurred!";
    }

    public static class JoinGame {
        public static final String DefaultIP = "127.0.0.1";
        public static final String InvalidInput = "Invalid Input!";
    }

    public static class OfflineGame {
        public static final String AI_PlayerDifficulty = "Type";
        public static final String Newbie = "Newbie";
        public static final String Normal = "Normal";
    }

    public static class Settings {
        public static final String Graphics = "Graphics";
        public static final String Audio = "Audio";
        public static final String Gameplay = "Gameplay";
        public static final String Control = "Control";

        public static final String Enabled = "On";      // not used here but convenient to place it here
        public static final String Disabled = "Off";    // not used here but convenient to place it here
        public static final String High = "High";   // not used here but convenient to place it here
        public static final String Low = "Low";     // not used here but convenient to place it here
    }

    public static class GraphicsSettings {
        public static final String DisplayScale = "Scale";
        public static final String HighQualityRendering = "Rendering";
        public static final String CinematicEffect = "Cinematic";
        public static final String DesiredFPS = "Max FPS";
//        public static final String RestartGameFPS = "Restart game to apply FPS!";
    }

    public static class AudioSettings {
        public static final String MusicVolume = "Music Vol.";
        public static final String SoundVolume = "Sound Vol.";
    }

    public static class GameplaySettings {
        public static final String MatchDuration = "Match Time";
        public static final String MatchToWin = "Winning";
        public static final String Information = "It affects game creation only!";
    }

    public static class ControlSettings {
        public static final String Up = "Up";
        public static final String Down = "Down";
        public static final String Left = "Left";
        public static final String Right = "Right";
        public static final String Fire = "Fire";

        public static final String OneHumanPlayer = "Single Player";
        public static final String TwoHumanPlayer = "Two Players";

        public static final String Player0 = "1st Player";
        public static final String Player1 = "2nd Player";

        public static final String Information = "Enter and press key to edit!";
    }

    public static class ControlChooser {
        public static final String UpArow = "Up";
        public static final String DownArrow = "Down";
        public static final String LeftArrow = "Left";
        public static final String RightArrow = "Right";
        public static final String Space = "Space";
        public static final String Enter = "Enter";
        public static final String Ctrl = "Ctrl";
        public static final String Alt = "Alt";
        public static final String Shift = "Shift";
        public static final String Tab = "Tab";
        public static final String Delete = "Delete";
        public static final String Backspace = "BSpace";
        public static final String BraketOpening = "(";
        public static final String BraketClosing = ")";
        public static final String Unknown = "???";
    }

}

package AppInfo.Customization;

import AppInfo.FilePath;
import AppInfo.Strings;
import Network.BaseNetwork.BaseNetwork;
import Network.BaseNetwork.NetworkTask;

import java.awt.event.KeyEvent;
import java.io.*;

/**
 * Created by Arnob on 17/02/2015.
 * This class contains all user-modifiable settings.
 */
public class UserSettings {

    private static final int serialVersionUID = 231940001;  // this ID is used for version compatibility
    private static final int serialVersionUID_mask = 15;    // only checks 4 LSB bits


    public UserSettings() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(FilePath.UserFilePath.UserData)));
            read(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* Graphics */
    public double displayScale = 3;
    public int displayHeight = (int) (displayScale * AllSettings.unscaledHeight);
    public int displayWidth = (int) (displayScale * AllSettings.unscaledWidth);
    public double undoDisplayScale = 1.0 / displayScale;

    public boolean highQualityRendering = true;
    public boolean cinematicEffectEnabled = true;   // does motion blur by using alpha transparency in background images

    public int desiredFPS = 24;
    public double motionConst = 24.0 / desiredFPS;  // The game was originally built for 24 FPS. So ratio is respective to 24.
    public double timeStep = 1000.0 / desiredFPS;
    public long desiredLoopTime = (long) timeStep;

//    public void setDesiredFPS(int FPS) {
//        desiredFPS = FPS;
//        motionConst = 24.0 / desiredFPS;    // The game was originally built for 24 FPS. So ratio is respective to 24.
//        timeStep = 1000.0 / desiredFPS;
//        desiredLoopTime = (long) timeStep;
//    }


    /* Audio */
    public double backgroundMusicVolume = 0.5;
    public double soundEffectVolume = 0.5;


    /* Gameplay */
    public int matchToWin = 2;
    public int matchDurationMinute = 3;


    /* Control */
    // online player key
    public int Key_Up = KeyEvent.VK_UP;
    public int Key_Down = KeyEvent.VK_DOWN;
    public int Key_Left = KeyEvent.VK_LEFT;
    public int Key_Right = KeyEvent.VK_RIGHT;
    public int Key_Fire = KeyEvent.VK_A;

    // offline player key
    public int Player0_Key_Up = KeyEvent.VK_W;
    public int Player0_Key_Down = KeyEvent.VK_S;
    public int Player0_Key_Left = KeyEvent.VK_A;
    public int Player0_Key_Right = KeyEvent.VK_D;
    public int Player0_Key_Fire = KeyEvent.VK_Q;

    public int Player1_Key_Up = KeyEvent.VK_UP;
    public int Player1_Key_Down = KeyEvent.VK_DOWN;
    public int Player1_Key_Left = KeyEvent.VK_LEFT;
    public int Player1_Key_Right = KeyEvent.VK_RIGHT;
    public int Player1_Key_Fire = KeyEvent.VK_SLASH;


    /* Others */
    public String UserIP = Strings.JoinGame.DefaultIP;
    public int totalPlayers = 2;
    public int totalHumanPlayers = 1;
    public int AI_playerDifficulty = 0;     // 0 is for "Newbie", 1 is for "Normal"


    public void write(ObjectOutputStream out) throws IOException {
        out.writeInt(serialVersionUID);


        out.writeDouble(displayScale);

        out.writeBoolean(highQualityRendering);
        out.writeBoolean(cinematicEffectEnabled);

//        out.writeInt(desiredFPS);


        out.writeDouble(backgroundMusicVolume);
        out.writeDouble(soundEffectVolume);


        out.writeInt(matchToWin);
        out.writeInt(matchDurationMinute);


        out.writeInt(Key_Up);
        out.writeInt(Key_Down);
        out.writeInt(Key_Left);
        out.writeInt(Key_Right);
        out.writeInt(Key_Fire);

        out.writeInt(Player0_Key_Up);
        out.writeInt(Player0_Key_Down);
        out.writeInt(Player0_Key_Left);
        out.writeInt(Player0_Key_Right);
        out.writeInt(Player0_Key_Fire);

        out.writeInt(Player1_Key_Up);
        out.writeInt(Player1_Key_Down);
        out.writeInt(Player1_Key_Left);
        out.writeInt(Player1_Key_Right);
        out.writeInt(Player1_Key_Fire);


        int UserIP_dot0 = UserIP.indexOf('.');
        int UserIP_dot1 = UserIP.indexOf('.', UserIP_dot0 + 1);
        int UserIP_dot2 = UserIP.lastIndexOf('.');
        try {
            out.writeInt(Integer.parseInt(UserIP.substring(0, UserIP_dot0)));
            out.writeInt(Integer.parseInt(UserIP.substring(UserIP_dot0 + 1, UserIP_dot1)));
            out.writeInt(Integer.parseInt(UserIP.substring(UserIP_dot1 + 1, UserIP_dot2)));
            out.writeInt(Integer.parseInt(UserIP.substring(UserIP_dot2 + 1)));
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            e.printStackTrace();
        }

        out.writeInt(totalPlayers);
        out.writeInt(totalHumanPlayers);
        out.writeInt(AI_playerDifficulty);
    }

    private void read(ObjectInputStream in) throws IOException {
        if ((serialVersionUID & serialVersionUID_mask) != (in.readInt() & serialVersionUID_mask)) return;


        double testDoubleValue = in.readDouble();
        if (2.0 <= testDoubleValue && testDoubleValue <= 4.0)
            displayScale = testDoubleValue;
        undoDisplayScale = 1.0 / displayScale;
        displayWidth = (int) (displayScale * AllSettings.unscaledWidth);
        displayHeight = (int) (displayScale * AllSettings.unscaledHeight);

        highQualityRendering = in.readBoolean();
        cinematicEffectEnabled = in.readBoolean();

//        int testIntValue = in.readInt();
//        if (15 <= testIntValue && testIntValue <= 120) {
//            setDesiredFPS(testIntValue);
//        }


        testDoubleValue = in.readDouble();
        if (0.0 <= testDoubleValue && testDoubleValue <= 1.0)
            backgroundMusicVolume = testDoubleValue;
        testDoubleValue = in.readDouble();
        if (0.0 <= testDoubleValue && testDoubleValue <= 1.0)
            soundEffectVolume = testDoubleValue;


        int testIntValue = in.readInt();
        if (1 <= testIntValue && testIntValue <= 5)
            matchToWin = testIntValue;
        testIntValue = in.readInt();
        if (1 <= testIntValue && testIntValue <= 7)
            matchDurationMinute = testIntValue;


        Key_Up = in.readInt();
        Key_Down = in.readInt();
        Key_Left = in.readInt();
        Key_Right = in.readInt();
        Key_Fire = in.readInt();

        Player0_Key_Up = in.readInt();
        Player0_Key_Down = in.readInt();
        Player0_Key_Left = in.readInt();
        Player0_Key_Right = in.readInt();
        Player0_Key_Fire = in.readInt();

        Player1_Key_Up = in.readInt();
        Player1_Key_Down = in.readInt();
        Player1_Key_Left = in.readInt();
        Player1_Key_Right = in.readInt();
        Player1_Key_Fire = in.readInt();


        int UserIP_byte0 = in.readInt();
        int UserIP_byte1 = in.readInt();
        int UserIP_byte2 = in.readInt();
        int UserIP_byte3 = in.readInt();
        String ip = UserIP_byte0 + "." + UserIP_byte1 + "." + UserIP_byte2 + "." + UserIP_byte3;
        if (NetworkTask.isValid_IP4_address(ip)) UserIP = ip;

        testIntValue = in.readInt();
        if (2 <= testIntValue && testIntValue <= BaseNetwork.maxPlayers) {
            totalPlayers = testIntValue;
        }
        testIntValue = in.readInt();
        if (0 <= testIntValue && testIntValue <= BaseNetwork.maxPlayers) {
            totalHumanPlayers = testIntValue;
        }
        testIntValue = in.readInt();
        if (0 <= testIntValue && testIntValue <= 1) {
            AI_playerDifficulty = testIntValue;
        }
    }

}

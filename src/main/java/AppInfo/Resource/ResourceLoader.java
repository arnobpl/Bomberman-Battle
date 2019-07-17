package AppInfo.Resource;

import AppInfo.Strings;
import javafx.scene.media.Media;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Arnob on 21/10/2014.
 * This class helps to easily access embedded resources in the JAR file.
 * There are also 2 more methods for read/write purposes in the outside JAR file.
 */
public class ResourceLoader {
    private static final int bufferSize = 1 << 13;

    public static BufferedImage loadImageFromJAR(String filePath) throws IOException {
        return ImageIO.read(ResourceLoader.class.getResource(filePath));
    }

    public static Media loadSoundFromJAR(String filePath) {
        try {
            return new Media(ResourceLoader.class.getResource(filePath).toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, Strings.Error.AudioNotFound, Strings.Error.FatalError, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return null;
    }

    public static String loadTextFromJAR(String filePath) {
        BufferedReader in = new BufferedReader(new InputStreamReader(ResourceLoader.class.getResourceAsStream(filePath)));

        char[] buffer = new char[bufferSize];
        StringBuilder sb = new StringBuilder();
        try {
            while (in.read(buffer) != -1) {
                sb.append(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, Strings.Error.InfoNotFound, Strings.Error.FatalError, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static Object loadDataFromOutside(String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath)));

        Object object = in.readObject();

        in.close();

        return object;
    }

    public static void saveDataToOutside(String filePath, Object object) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath)));

        out.writeObject(object);

        out.close();
    }
}

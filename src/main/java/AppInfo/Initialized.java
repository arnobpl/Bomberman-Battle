package AppInfo;

import AppInfo.Customization.AllSettings;
import AppInfo.Resource.ResourceLoader;
import FontRenderer.FontFromFile;
import GraphicsRenderer.GfxFromFile;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by Arnob on 30/09/2014.
 * This class contains resources (such as gfx, sfx, settings, etc) that must be loaded in startup.
 */
public class Initialized {
    public static final JFrame baseFrame = new JFrame(Strings.WindowTitle);
    public static final JPanel panel = (JPanel) baseFrame.getContentPane();

    public static final GfxFromFile gfx = new GfxFromFile();
    public static final FontFromFile font = new FontFromFile();

    public static final String CreditInfoFromFile = ResourceLoader.loadTextFromJAR(FilePath.InfoFilePath.CreditInfo);
    public static final String HowToPlayInfoFromFile = ResourceLoader.loadTextFromJAR(FilePath.InfoFilePath.HowToPlayInfo);

    public static final Random random = new Random();

    static {
        baseFrame.setIconImage(Initialized.gfx.appIcon());
        panel.setBackground(Color.black);
        panel.setPreferredSize(new Dimension(AllSettings.userSettings.displayWidth, AllSettings.userSettings.displayHeight));
        panel.setLayout(null);

        // initializing JavaFX for media playback (especially mp3 files)
        final JFXPanel jfxPanel = new JFXPanel();
        panel.add(jfxPanel);
        Platform.runLater(() -> {
            javafx.scene.Scene scene = new javafx.scene.Scene(new Group());
            jfxPanel.setScene(scene);
        });
    }
}

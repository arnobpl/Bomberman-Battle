package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;

/**
 * Created by Arnob on 05/02/2015.
 * User input to get high quality rendering ability
 */
public class Option_HighQualityRendering extends OptionChooser {
    public Option_HighQualityRendering(int positionIndex) {
        super(Strings.GraphicsSettings.HighQualityRendering, positionIndex, 2, AllSettings.userSettings.highQualityRendering ? 0 : 1);

        addOption(Strings.Settings.High);
        addOption(Strings.Settings.Low);
    }

    public void valueChanged(int selectedIndex) {
        AllSettings.userSettings.highQualityRendering = (selectedIndex == 0);
    }
}

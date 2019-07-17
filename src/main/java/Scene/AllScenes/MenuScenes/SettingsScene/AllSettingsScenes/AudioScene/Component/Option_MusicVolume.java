package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.AudioScene.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;

/**
 * Created by Arnob on 06/02/2015.
 * User input to get music volume
 */
public class Option_MusicVolume extends OptionChooser {
    public Option_MusicVolume(int positionIndex) {
        super(Strings.AudioSettings.MusicVolume, positionIndex, 21, (int) (AllSettings.userSettings.backgroundMusicVolume / 0.05));

        int totalOptions = getTotalOptions();
        for (int i = 0; i < totalOptions; i++)
            addOption(Integer.toString(i * 5));
    }

    public void valueChanged(int selectedIndex) {
        AllSettings.userSettings.backgroundMusicVolume = selectedIndex * 0.05;
        MainMenu.menuMusic.setVolume(AllSettings.userSettings.backgroundMusicVolume);
    }
}

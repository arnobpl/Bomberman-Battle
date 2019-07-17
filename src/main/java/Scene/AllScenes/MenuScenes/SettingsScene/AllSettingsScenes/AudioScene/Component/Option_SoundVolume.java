package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.AudioScene.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import MenuSystem.Form.MenuItems.OptionChooser;

/**
 * Created by Arnob on 06/02/2015.
 * User input to get sound volume
 */
public class Option_SoundVolume extends OptionChooser {
    public Option_SoundVolume(int positionIndex) {
        super(Strings.AudioSettings.SoundVolume, positionIndex, 21, (int) (AllSettings.userSettings.soundEffectVolume / 0.05));

        int totalOptions = getTotalOptions();
        for (int i = 0; i < totalOptions; i++)
            addOption(Integer.toString(i * 5));
    }

    public void valueChanged(int selectedIndex) {
        AllSettings.userSettings.soundEffectVolume = selectedIndex * 0.05;
    }
}

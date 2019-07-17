package Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.Component;

import AppInfo.Customization.AllSettings;
import AppInfo.Strings;
import GraphicsRenderer.RenderGraphics;
import MenuSystem.Form.MenuItems.OptionChooser;
import Scene.AllScenes.MenuScenes.SettingsScene.AllSettingsScenes.GraphicsScene.GraphicsScene;

/**
 * Created by Arnob on 05/02/2015.
 * User input to get cinematic effect ability
 */
public class Option_CinematicEffect extends OptionChooser {

    public Option_CinematicEffect(int positionIndex) {
        super(Strings.GraphicsSettings.CinematicEffect, positionIndex, 2, AllSettings.userSettings.cinematicEffectEnabled ? 0 : 1);

        addOption(Strings.Settings.Enabled);
        addOption(Strings.Settings.Disabled);
    }

    public void valueChanged(int selectedIndex) {
        AllSettings.userSettings.cinematicEffectEnabled = (selectedIndex == 0);
        if (AllSettings.userSettings.cinematicEffectEnabled) {
            RenderGraphics.setAlpha(GraphicsScene.gradientBackground, AllSettings.motionBlurAlpha);
        } else {
            RenderGraphics.setAlphaOpaque(GraphicsScene.gradientBackground);
        }
    }
}

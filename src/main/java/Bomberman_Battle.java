import AppInfo.BaseWindow;
import AppInfo.Initialized;
import Scene.AllScenes.MenuScenes.MainMenu.MainMenu;

class Bomberman_Battle {
    public static void main(String[] args) {
        new Initialized();
        BaseWindow.run(new MainMenu());
    }
}

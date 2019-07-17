package AppInfo;

/**
 * Created by Arnob on 27/09/2014.
 * This class contains all the file paths.
 */
public class FilePath {

    public static class GfxFilePath {
        public static final String GfxFolder = "/Gfx/";
        public static final String Gfx = GfxFolder + "Gfx.png";
        public static final String Font = GfxFolder + "Font.png";
    }

    public static class SfxFilePath {
        public static final String SfxFolder = "/Sfx/";

        public static class InMenu {
            public static final String MenuMusic = SfxFolder + "MenuMusic.mp3";

            public static final String MenuScroll = SfxFolder + "MenuScroll.mp3";
            public static final String MenuSelect = SfxFolder + "MenuSelect.mp3";
            public static final String OptionScroll = SfxFolder + "OptionScroll.mp3";
        }

        public static class InGame {
            public static final String GameMusic = SfxFolder + "GameMusic.mp3";

            public static final String ReadyGoSound = SfxFolder + "ReadyGo.mp3";

            public static final String WalkingSound = SfxFolder + "Walking.mp3";

            public static final String CreateBombSound = SfxFolder + "CreateBomb.mp3";
            public static final String ExplosionSound = SfxFolder + "Explosion.mp3";

            public static final String DyingSound = SfxFolder + "Dying.mp3";

            public static final String HurryUpWarning = SfxFolder + "HurryUpWarning.mp3";
            public static final String HurryUpMusic = SfxFolder + "HurryUpMusic.mp3";

            public static final String ResultFoundSound = SfxFolder + "ResultFound.mp3";
            public static final String WinnerMusic = SfxFolder + "Winner.mp3";
            public static final String DrawGameMusic = SfxFolder + "DrawGame.mp3";

            public static final String ChampionMusic = SfxFolder + "Champion.mp3";
        }
    }

    public static class InfoFilePath {
        public static final String InfoFolder = "/Info/";

        public static final String CreditInfo = InfoFolder + "Credit.txt";
        public static final String HowToPlayInfo = InfoFolder + "HowToPlay.txt";
    }

    public static class UserFilePath {
        public static final String UserFolder = "";

        public static final String UserData = UserFolder + "User.bin";
    }
}

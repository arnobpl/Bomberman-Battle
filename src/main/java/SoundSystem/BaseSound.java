package SoundSystem;

import AppInfo.Customization.AllSettings;
import GameObject.Location.GridLocation;
import GameObject.Location.Location;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Created by Arnob on 24/01/2015.
 * This contains all necessary methods for the game's sound system.
 */
public class BaseSound {

    public static final double fadeinElapsed = 1000;    // in millisecond, default
    public static final double fadeoutElapsed = 2000;   // in millisecond, default

    public static final long loopWaitingInterval = 10;  // in millisecond

    private static final long volumeChangingInterval = AllSettings.userSettings.desiredLoopTime;    // in millisecond

    private static final double fadeinFinishingVolume = 0.5;    // default
    private static final double soundVolume = 0.5;              // default

    /**
     * This method starts sound gradually by <code>fadeinElapsed</code> value. It does not create a new instance of the <code>MediaPlayer</code> object internally.
     */
    public static void playSoundByFadein(final MediaPlayer sound, final double fadeinElapsed, final double finishingVolume) {
        sound.setVolume(0.0);
        sound.play();
        sound.setOnPlaying(() -> {
            double time = 0.0;
            while (true) {
                time += volumeChangingInterval;
                double newVolume = finishingVolume * (time / fadeinElapsed);
                if (newVolume >= finishingVolume) {
                    sound.setVolume(finishingVolume);
                    break;
                }
                sound.setVolume(newVolume);
                try {
                    Thread.sleep(volumeChangingInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method starts sound gradually by <code>fadeinElapsed</code> value. It does not create a new instance of the <code>MediaPlayer</code> object internally.
     */
    public static void playSoundByFadein(MediaPlayer sound, double fadeinElapsed) {
        playSoundByFadein(sound, fadeinElapsed, fadeinFinishingVolume);
    }

    /**
     * This method starts sound gradually by the default elapsed time value. It does not create a new instance of the <code>MediaPlayer</code> object internally.
     */
    public static void playSoundByFadein(MediaPlayer sound) {
        playSoundByFadein(sound, fadeinElapsed, fadeinFinishingVolume);
    }

    /**
     * This method starts sound gradually by <code>fadeinElapsed</code> value. It does not create a new instance of the <code>MediaPlayer</code> object internally.
     */
    public static void playSoundByFadein(final MediaPlayer sound, final double fadeinElapsed, final double finishingVolume, final long delayInMillisecond) {
        new Thread(() -> {
            try {
                Thread.sleep(delayInMillisecond);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            playSoundByFadein(sound, fadeinElapsed, finishingVolume);
        }).start();
    }

    /**
     * This method stops sound gradually by <code>fadeoutElapsed</code> value. It does not create a new instance of the <code>MediaPlayer</code> object internally.
     */
    public static void stopSoundByFadeout(final MediaPlayer sound, final double fadeoutElapsed) {
        sound.setCycleCount((int) (sound.getCycleDuration().toMillis() / fadeoutElapsed + 0.5));
        new Thread(() -> {
            double time = 0.0;
            double initialVolume = sound.getVolume();
            while (true) {
                time += volumeChangingInterval;
                double newVolume = initialVolume * (1.0 - time / fadeoutElapsed);
                if (newVolume <= 0.0) break;
                sound.setVolume(newVolume);
                try {
                    Thread.sleep(volumeChangingInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sound.stop();
        }).start();
    }

    /**
     * This method stops sound gradually by the default elapsed time value. It does not create a new instance of the <code>MediaPlayer</code> object internally.
     */
    public static void stopSoundByFadeout(MediaPlayer sound) {
        stopSoundByFadeout(sound, fadeoutElapsed);
    }

    /**
     * This method plays sound once.
     */
    public static void playSound(Media sound, double volume, double balance) {
        final MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setBalance(balance);
        mediaPlayer.setVolume(volume);

        mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
        mediaPlayer.play();
    }

    /**
     * This method plays sound once.
     */
    public static void playSound(Media sound, double volume) {
        playSound(sound, volume, 0.0);
    }

    /**
     * This method plays sound once.
     */
    public static void playSound(Media sound) {
        playSound(sound, soundVolume, 0.0);
    }

    /**
     * This method plays sound once.
     */
    public static void playSound(final Media sound, final double volume, final double balance, final long delayInMillisecond) {
        new Thread(() -> {
            try {
                Thread.sleep(delayInMillisecond);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            playSound(sound, volume, balance);
        }).start();
    }

    /**
     * This method plays sound once. It does not create a new instance of the <code>MediaPlayer</code> object internally.
     */
    public static void playSound(MediaPlayer sound, double volume, double balance) {
        sound.setBalance(balance);
        sound.setVolume(volume);
        sound.play();
    }

    /**
     * @param gridLocation <code>GridLocation</code> of the object
     * @return sound balance in range of [-1.0, +1.0] according to the <code>GridLocation</code> of the object
     */
    public static double getSoundBalance(GridLocation gridLocation) {
        return (gridLocation.x - Location.totalGridWidth_half) / (double) Location.totalGridWidth_half;
    }
}

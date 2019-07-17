package SoundSystem;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Arnob on 25/01/2015.
 * This class has been created for eliminating delay of looping sound of the standard JavaFX library.
 * Note that playing by fadein is not correct if fadeinElapsed is greater than the media duration.
 * But stopping by fadeout has no problem detected except the looping delay.
 */
public class LoopingSound {

    private static final long loopingDelay = 50;    // in millisecond

    private final Queue<MediaPlayer> mediaPlayersToBeDisposed = new LinkedList<>();

    private Media media;
    private double volume = 1.0;
    private MediaPlayer mediaPlayer;

    private boolean isReady = false;
    private boolean isPlaying = false;

    private long newPlayElapsedMillisecond = loopingDelay + 1;

    private Thread playByLoopThread;

    public LoopingSound(Media sound, double volume) {
        this.media = sound;
        this.volume = volume;

        final MediaPlayer mediaPlayerToBeDisposed = new MediaPlayer(media);
        mediaPlayerToBeDisposed.setOnReady(() -> {
            long possibleNewPlayElapsed = (long) media.getDuration().toMillis() - loopingDelay;
            if (possibleNewPlayElapsed > 0) newPlayElapsedMillisecond = possibleNewPlayElapsed;
            isReady = true;
            mediaPlayerToBeDisposed.dispose();
        });
    }

    /**
     * This method disposes the first <code>MediaPlayer</code> object from <code>mediaPlayersToBeDisposed</code> queue.
     */
    private void disposeMediaPlayer() {
        mediaPlayersToBeDisposed.poll().dispose();
    }

    private void waitForReady() {
        // waiting for ready if not ready already...
        while (!isReady) {
            try {
                Thread.sleep(BaseSound.loopWaitingInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void playByLoop() {
        while (!Thread.interrupted()) {
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(volume);

            mediaPlayersToBeDisposed.add(mediaPlayer);
            mediaPlayer.setOnEndOfMedia(this::disposeMediaPlayer);
            mediaPlayer.play();

            try {
                Thread.sleep(newPlayElapsedMillisecond);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void play() {
        if (!isPlaying) {
            isPlaying = true;

            playByLoopThread = new Thread(() -> {
                waitForReady();

                playByLoop();
            });
            playByLoopThread.start();
        }
    }

    public void play(final double fadeinElapsed) {
        if (!isPlaying) {
            isPlaying = true;

            playByLoopThread = new Thread(() -> {
                mediaPlayer = new MediaPlayer(media);

                mediaPlayersToBeDisposed.add(mediaPlayer);
                mediaPlayer.setOnEndOfMedia(this::disposeMediaPlayer);
                BaseSound.playSoundByFadein(mediaPlayer, fadeinElapsed, volume);

                waitForReady();

                try {
                    Thread.sleep(newPlayElapsedMillisecond);
                } catch (InterruptedException e) {
                    return;
                }

                playByLoop();
            });
            playByLoopThread.start();
        }
    }

    public void playAfterSetVolume(double volume) {
        setVolume(volume);
        play();
    }

    public void playAfterSetVolume(double volume, double fadeinElapsed) {
        setVolume(volume);
        play(fadeinElapsed);
    }

    public void stop() {
        if (isPlaying) {
            isPlaying = false;

            playByLoopThread.interrupt();

            mediaPlayersToBeDisposed.add(mediaPlayer);
            disposeMediaPlayer();
            mediaPlayer.stop();
        }
    }

    public void stop(final double fadeoutElapsed) {
        if (isPlaying) {
            isPlaying = false;

            playByLoopThread.interrupt();

            mediaPlayersToBeDisposed.add(mediaPlayer);
            mediaPlayer.setOnStopped(this::disposeMediaPlayer);
            BaseSound.stopSoundByFadeout(mediaPlayer, fadeoutElapsed);
        }
    }

    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) mediaPlayer.setVolume(volume);
    }
}

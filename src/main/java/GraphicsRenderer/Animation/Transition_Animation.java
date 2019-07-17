package GraphicsRenderer.Animation;

import AppInfo.Customization.AllSettings;
import GraphicsRenderer.RenderGraphics;

import java.awt.*;

/**
 * Created by Arnob on 03/11/2014.
 * This class helps to generate transition animation between 2 scenes.
 */
public class Transition_Animation {
    private int transitionTimeElapsed = 1000;
    private double transitionAlphaStep = 10 * AllSettings.userSettings.motionConst;
    private int sceneTimeElapsed = 0;

    private int time = 0;
    private int transitionAlpha = 0;
    private boolean started = false;
    private boolean inTransition = false;

    /**
     * Elapsed time parameter is in millisecond.
     */
    public Transition_Animation(int transitionTimeElapsed, double transitionAlphaStep) {
        if (transitionTimeElapsed > 0) this.transitionTimeElapsed = transitionTimeElapsed;
        if (transitionAlphaStep > 0) this.transitionAlphaStep = transitionAlphaStep;
        inTransition = true;
    }

    /**
     * Elapsed time parameters are in millisecond.
     */
    public Transition_Animation(int transitionTimeElapsed, double transitionAlphaStep, int sceneTimeElapsed) {
        if (transitionTimeElapsed > 0) this.transitionTimeElapsed = transitionTimeElapsed;
        if (transitionAlphaStep > 0) this.transitionAlphaStep = transitionAlphaStep;
        if (sceneTimeElapsed >= AllSettings.userSettings.timeStep) this.sceneTimeElapsed = sceneTimeElapsed;
        else inTransition = true;
    }

    /**
     * This must be called to count time, otherwise no transition animation will occur.
     * This can be called with the constructors or in any other time. Multiple calls of this method have no effect.
     */
    public Transition_Animation start() {
        started = true;
        return this;
    }

    /**
     * This must be called in the <code>update()</code> block to count time.
     */
    public void tick() {
        if (started) {
            time += AllSettings.userSettings.timeStep;
            if (inTransition) {
                transitionAlpha = RenderGraphics.clampValueInRGB(transitionAlpha + transitionAlphaStep);
            } else if (time > sceneTimeElapsed) {
                inTransition = true;
                time = 0;
            }
        }
    }

    /**
     * This must be called in the <code>if</code> block of <code>render(Graphics2D g)</code> block.
     *
     * @return <code>true</code> if new scene should be assigned, otherwise <code>false</code>.
     */
    public boolean render(Graphics2D g) {
        if (started && inTransition) {
            g.setColor(new Color(0, 0, 0, transitionAlpha));
            g.fillRect(0, 0, AllSettings.unscaledWidth, AllSettings.unscaledHeight);
            return (time > transitionTimeElapsed);
        }
        return false;
    }

}

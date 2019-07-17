package GraphicsRenderer.Animation;

/**
 * Created by Arnob on 18/11/2014.
 * This class helps to generate periodic sequence (0, 1, 2, 3, 0, 1, ...) which can be used in animation.
 * This class is thread-safe by synchronized with its instance.
 */
public class Periodic_Animation {
    private double animateValue = 0;    // range [0.0, upperLimit]
    private double animateStep = 1;     // range (-upperLimit, +upperLimit) or, 0 if upperLimit = 0
    private int upperLimit = 1;

    private boolean cycleCompleted = false;

    public Periodic_Animation(int upperLimit, double animateStep) {
        if (upperLimit >= 0) this.upperLimit = upperLimit;
        if (Math.abs(animateStep) < this.upperLimit) this.animateStep = animateStep;
    }

    public Periodic_Animation(int upperLimit, double animateStep, double animateValue) {
        if (upperLimit >= 0) this.upperLimit = upperLimit;
        if (Math.abs(animateStep) < this.upperLimit) this.animateStep = animateStep;
        if (0 <= animateValue && animateValue <= this.upperLimit) this.animateValue = animateValue;
    }


    public synchronized void animate() {
        animateValue += animateStep;
        if (animateValue > upperLimit) {
            animateValue = 0;
            cycleCompleted = true;
        } else if (animateValue < 0) {
            animateValue = upperLimit;
            cycleCompleted = true;
        }
    }


    public int getAnimateValue() {
        return (int) Math.round(animateValue);
    }

    public double getAnimateValue_Double() {
        return animateValue;
    }

    /**
     * @return <code>true</code> if cycle completed. After returning <code>true</code>, it will clear the state.
     */
    public synchronized boolean isCycleCompleted() {
        if (cycleCompleted) {
            cycleCompleted = false;
            return true;
        }
        return false;
    }


    public synchronized void setAnimateValue(double animateValue) {
        if (0 <= animateValue && animateValue <= upperLimit) this.animateValue = animateValue;
    }

    public synchronized void setAnimateValueMirror() {
        animateValue = upperLimit - animateValue;
    }

    public synchronized void setAnimateStep(double animateStep) {
        if (Math.abs(animateStep) < upperLimit) this.animateStep = animateStep;
    }

    public synchronized void setAnimateStepByFactor(double multiplyFactor) {
        double newAnimateStep = animateStep * multiplyFactor;
        if (Math.abs(newAnimateStep) < upperLimit) animateStep = newAnimateStep;
    }

    public synchronized void toggleDirection() {
        animateStep *= -1;
    }

    public synchronized void setUpperLimit(int upperLimit) {
        if (upperLimit >= 0) {
            if (upperLimit < animateValue) {
                animateValue = upperLimit;
            }
            if (upperLimit <= Math.abs(animateStep)) {
                if (upperLimit != 0) {
                    if (animateStep > 0)
                        animateStep = upperLimit - 0.1;     // Math.abs(animateStep) must be less than upperLimit
                    else
                        animateStep = 0.1 - upperLimit;     // Math.abs(animateStep) must be less than upperLimit
                } else {
                    animateStep = 0;
                }
            }
            this.upperLimit = upperLimit;
        }
    }

    public synchronized void resetAnimation() {
        if (animateStep < 0) animateValue = upperLimit;
        else animateValue = 0;
    }


}

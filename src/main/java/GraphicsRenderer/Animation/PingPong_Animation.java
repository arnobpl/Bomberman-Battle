package GraphicsRenderer.Animation;

/**
 * Created by Arnob on 30/10/2014.
 * This class helps to generate ping-pong sequence (0, 1, 2, 1, 0, 1, ...) which can be used in animation.
 */
public class PingPong_Animation {
    private double animateValue = 0;    // range [0.0, upperLimit]
    private double animateStep = 1;     // range (-upperLimit, +upperLimit) or, 0 if upperLimit = 0
    private int upperLimit = 1;

    private boolean cycleCompleted = false;

    public PingPong_Animation(int upperLimit, double animateStep) {
        if (upperLimit >= 0) this.upperLimit = upperLimit;
        if (Math.abs(animateStep) < this.upperLimit) this.animateStep = animateStep;
    }

    public PingPong_Animation(int upperLimit, double animateStep, double animateValue) {
        if (upperLimit >= 0) this.upperLimit = upperLimit;
        if (Math.abs(animateStep) < this.upperLimit) this.animateStep = animateStep;
        if (0 <= animateValue && animateValue <= this.upperLimit) this.animateValue = animateValue;
    }


    public void animate() {
        animateValue += animateStep;
        if (animateValue > upperLimit) {
            animateValue = upperLimit;
            animateStep *= -1;
            cycleCompleted = true;
        } else if (animateValue < 0) {
            animateValue = 0;
            animateStep *= -1;
            cycleCompleted = true;
        }
    }


    public int getAnimateValue() {
        return (int) Math.round(animateValue);
    }

    public double getAnimateValue_Double() {
        return animateValue;
    }


    public boolean isAnimateStepPositive() {
        return (animateStep >= 0);
    }

    /**
     * @return <code>true</code> if cycle completed. After returning <code>true</code>, it will clear the state.
     */
    public boolean isCycleCompleted() {
        if (cycleCompleted) {
            cycleCompleted = false;
            return true;
        }
        return false;
    }

    public void setAnimateValue(double animateValue) {
        if (0 <= animateValue && animateValue <= upperLimit) this.animateValue = animateValue;
    }

    public void setAnimateValueMirror() {
        animateValue = upperLimit - animateValue;
    }

    public void setAnimateStep(double animateStep) {
        if (Math.abs(animateStep) < upperLimit) this.animateStep = animateStep;
    }

    public void setAnimateStepByFactor(double multiplyFactor) {
        double newAnimateStep = animateStep * multiplyFactor;
        if (Math.abs(newAnimateStep) < upperLimit) animateStep = newAnimateStep;
    }

    public void toggleDirection() {
        animateStep *= -1;
    }

    public void setUpperLimit(int upperLimit) {
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

    public void resetAnimation() {
        animateValue = 0;
        if (animateStep < 0) animateStep *= -1;
    }

}

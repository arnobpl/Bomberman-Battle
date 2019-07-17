package GraphicsRenderer.Animation;

/**
 * Created by Arnob on 31/10/2014.
 * This class helps to generate circular X & Y sequences which can be used in animation.
 */
public class Circular_Animation {
    private double positionX = 0;       // range [-radius, +radius]
    private double positionY = 0;       // range [-radius, +radius]

    private double animateValue = 0;    // range [0.0, 1.0]
    private double animateStep = 0.1;   // range [0.0, 1.0]

    private double radius = 10;

    private double offsetX = radius;
    private double offsetY = radius;

    private static final double TwicePi = 2 * Math.PI;


    public Circular_Animation(double animateStep, double diameter) {
        if (0 <= animateStep && animateStep <= 1) this.animateStep = animateStep;
        if (diameter >= 0) radius = diameter / 2;
        refreshPosition();
    }

    public Circular_Animation(double animateStep, double diameter, double animateValue) {
        if (0 <= animateStep && animateStep <= 1) this.animateStep = animateStep;
        if (diameter >= 0) radius = diameter / 2;
        if (0 <= animateValue && animateValue <= 1) this.animateValue = animateValue;
        refreshPosition();
    }


    public void setCenterX(int centerX, int imageWidth) {
        if (imageWidth >= 0) offsetX = centerX - imageWidth / 2.0;
    }

    public void setCenterY(int centerY, int imageHeight) {
        if (imageHeight >= 0) offsetY = centerY - imageHeight / 2.0;
    }


    public void animate_Clockwise() {
        animateValue += animateStep;
        if (animateValue > 1) animateValue--;
        refreshPosition();
    }

    public void animate_AntiClockwise() {
        animateValue -= animateStep;
        if (animateValue < 0) animateValue++;
        refreshPosition();
    }

    public void animate_Clockwise(double diameter) {
        animateValue += animateStep;
        if (animateValue > 1) animateValue--;
        if (diameter >= 0) radius = diameter / 2;
        refreshPosition();
    }

    public void animate_AntiClockwise(double diameter) {
        animateValue -= animateStep;
        if (animateValue < 0) animateValue++;
        if (diameter >= 0) radius = diameter / 2;
        refreshPosition();
    }

    private void refreshPosition() {
        positionX = radius * Math.cos(toRadian(animateValue));
        positionY = radius * Math.sin(toRadian(animateValue));
    }

    private double toRadian(double num_in_zeroOneRange) {
        return num_in_zeroOneRange * TwicePi;
    }


    public int getX() {
        return (int) Math.round(positionX + offsetX);
    }

    public int getY() {
        return (int) Math.round(positionY + offsetY);
    }


    public void setAnimateStep(double animateStep) {
        if (0 <= animateStep && animateStep <= 1) this.animateStep = animateStep;
    }

    public void setAnimateStepByFactor(double multiplyFactor) {
        double newAnimateStep = animateStep * multiplyFactor;
        if (0 <= newAnimateStep && newAnimateStep <= 1) this.animateStep = newAnimateStep;
    }

    public void setDiameter(double diameter) {
        if (diameter >= 0) radius = diameter / 2;
        refreshPosition();
    }

    public void resetAnimation() {
        animateValue = 0;
        refreshPosition();
    }

}

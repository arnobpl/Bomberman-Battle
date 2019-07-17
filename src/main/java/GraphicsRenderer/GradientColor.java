package GraphicsRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 26/10/2014.
 * This class helps to generate gradient color.
 */
public class GradientColor {

    public static final Color transparentColor = new Color(0, 0, 0, 0);

    private Color color;
    private double changingLength;

    private double redDiff;
    private double greenDiff;
    private double blueDiff;
    private double alphaDiff;

    public GradientColor(Color color1, Color color2, double changingLength) {
        this.color = color1;
        this.changingLength = changingLength;

        redDiff = (color2.getRed() - color1.getRed()) / changingLength;
        greenDiff = (color2.getGreen() - color1.getGreen()) / changingLength;
        blueDiff = (color2.getBlue() - color1.getBlue()) / changingLength;
        alphaDiff = (color2.getAlpha() - color1.getAlpha()) / changingLength;
    }

    public Color getGradientColor(double distance) {
        if (distance < 0) distance = 0;
        else if (distance > changingLength) distance = changingLength;

        int red = (int) Math.round(color.getRed() + redDiff * distance);
        int green = (int) Math.round(color.getGreen() + greenDiff * distance);
        int blue = (int) Math.round(color.getBlue() + blueDiff * distance);
        int alpha = (int) Math.round(color.getAlpha() + alphaDiff * distance);

        return new Color(red, green, blue, alpha);
    }

    public static Color getMiddleColor(Color color1, Color color2) {
        int red = (int) Math.round(color1.getRed() + (color2.getRed() - color1.getRed()) / 2.0);
        int green = (int) Math.round(color1.getGreen() + (color2.getGreen() - color1.getGreen()) / 2.0);
        int blue = (int) Math.round(color1.getBlue() + (color2.getBlue() - color1.getBlue()) / 2.0);
        return new Color(red, green, blue);
    }

    /**
     * Changing gradient color is along with X-axis.
     */
    public static void createGradientBackground_alongX(BufferedImage gradientBackground, Color color1, Color color2, int fromLineX, int toLineX, int fromLineY, int toLineY) {
        Graphics2D g = gradientBackground.createGraphics();

        if (fromLineX > toLineX) {
            int tempX = fromLineX;
            fromLineX = toLineX;
            toLineX = tempX;

            Color tempColor = color1;
            color1 = color2;
            color2 = tempColor;
        }

        GradientColor color = new GradientColor(color1, color2, toLineX - fromLineX);
        int j = 0;
        for (int i = fromLineX; i < toLineX; i++) {
            g.setColor(color.getGradientColor(j++));
            g.drawLine(i, fromLineY, i, toLineY);
        }

        g.dispose();
    }

    /**
     * Changing gradient color is along with X-axis.
     */
    public static void createGradientBackground_alongX(BufferedImage gradientBackground, Color color1, Color color2, int fromLineX, int toLineX) {
        createGradientBackground_alongX(gradientBackground, color1, color2, fromLineX, toLineX, 0, gradientBackground.getHeight());
    }

    /**
     * Changing gradient color is along with X-axis.
     */
    public static void createGradientBackground_alongX(BufferedImage gradientBackground, Color color1, Color color2) {
        createGradientBackground_alongX(gradientBackground, color1, color2, 0, gradientBackground.getWidth(), 0, gradientBackground.getHeight());
    }

    /**
     * Changing gradient color is along with Y-axis.
     */
    public static void createGradientBackground_alongY(BufferedImage gradientBackground, Color color1, Color color2, int fromLineY, int toLineY, int fromLineX, int toLineX) {
        Graphics2D g = gradientBackground.createGraphics();

        if (fromLineY > toLineY) {
            int tempY = fromLineY;
            fromLineY = toLineY;
            toLineY = tempY;

            Color tempColor = color1;
            color1 = color2;
            color2 = tempColor;
        }

        GradientColor color = new GradientColor(color1, color2, toLineY - fromLineY);
        int j = 0;
        for (int i = fromLineY; i < toLineY; i++) {
            g.setColor(color.getGradientColor(j++));
            g.drawLine(fromLineX, i, toLineX, i);
        }

        g.dispose();
    }

    /**
     * Changing gradient color is along with Y-axis.
     */
    public static void createGradientBackground_alongY(BufferedImage gradientBackground, Color color1, Color color2, int fromLineY, int toLineY) {
        createGradientBackground_alongY(gradientBackground, color1, color2, fromLineY, toLineY, 0, gradientBackground.getWidth());
    }

    /**
     * Changing gradient color is along with Y-axis.
     */
    public static void createGradientBackground_alongY(BufferedImage gradientBackground, Color color1, Color color2) {
        createGradientBackground_alongY(gradientBackground, color1, color2, 0, gradientBackground.getHeight(), 0, gradientBackground.getWidth());
    }

}

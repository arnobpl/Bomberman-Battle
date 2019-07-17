package GraphicsRenderer;

import AppInfo.Customization.AllSettings;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Arnob on 27/09/2014.
 * This class contains all basic methods (such as scaling) for rendering graphics.
 */
public class RenderGraphics {
    public static BufferedImage convertToARGB(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    private static BufferedImage addMargin(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth() + 2, image.getHeight() + 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 1, 1, null);
        g.dispose();
        return newImage;
    }

    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        Image tempImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(tempImage, 0, 0, null);
        g.dispose();

        return resizedImage;
    }

    /**
     * This method is needed to draw a separate scaled image accurately.
     */
    public static boolean drawImage(Graphics2D g, BufferedImage img, int x, int y) {
        return g.drawImage(addMargin(img), x - 1, y - 1, null);
    }

    public static void drawStringCenter(Graphics2D g, String text, int x, int y) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        x = x - (metrics.stringWidth(text) / 2);
        y = y - (metrics.getHeight() / 2) + metrics.getAscent();
        g.drawString(text, x, y);
    }

    public static void setRenderQuality(Graphics2D g) {
        if (AllSettings.userSettings.highQualityRendering) {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        } else {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        }
    }

    public static int clampValueInRGB(double value) {
        return Math.max(0, Math.min(255, (int) value));
    }

    /**
     * This method returns the given <code>Color</code> with the given <code>alpha</code>.
     * The original <code>Color</code> will be unchanged.
     */
    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), clampValueInRGB(alpha));
    }

    /**
     * This method returns a new <code>Color</code> with the given <code>alpha</code>.
     * The original <code>Color</code> is remained unchanged.
     */
    public static Color colorWithAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), clampValueInRGB(alpha));
    }

    /**
     * This method changes the given <code>BufferedImage</code> with the given <code>alpha</code>.
     * The original <code>BufferedImage</code> will be changed.
     */
    public static void setAlpha(BufferedImage image, int alpha) {
        alpha = clampValueInRGB(alpha);

        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, (0x00ffffff | (alpha << 24)) & image.getRGB(x, y));
            }
        }
    }

    /**
     * This method returns a new <code>BufferedImage</code> with the given <code>alpha</code>.
     * The original <code>BufferedImage</code> is remained unchanged.
     */
    public static BufferedImage imageWithAlpha(BufferedImage image, int alpha) {
        alpha = clampValueInRGB(alpha);

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage imageWithAlpha = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                imageWithAlpha.setRGB(x, y, (0x00ffffff | (alpha << 24)) & image.getRGB(x, y));
            }
        }

        return imageWithAlpha;
    }

    /**
     * This method changes the given <code>BufferedImage</code> to be completely opaque.
     * The original <code>BufferedImage</code> will be changed.
     */
    public static void setAlphaOpaque(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, 0xff000000 | image.getRGB(x, y));
            }
        }
    }
}

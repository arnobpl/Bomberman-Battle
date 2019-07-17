package AppInfo;

import AppInfo.Customization.AllSettings;
import GraphicsRenderer.RenderGraphics;
import Scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Arnob on 27/09/2014.
 * This class contains the main thread, main loop, base window and vital methods inside loop.
 */
public class BaseWindow {
    public static Scene scene;

    public static Canvas canvas;
    private static BufferStrategy bufferStrategy;

    private static void initializeCanvas() {
        canvas = new Canvas();
        canvas.setBounds(0, 0, AllSettings.userSettings.displayWidth, AllSettings.userSettings.displayHeight);
        canvas.setIgnoreRepaint(true);
        Initialized.panel.add(canvas);


        canvas.addKeyListener(new KeyControl());
        canvas.addMouseListener(new MouseControl());
        canvas.addMouseMotionListener(new MouseControl());
        canvas.addMouseWheelListener(new MouseControl());
        canvas.addFocusListener(new FocusControl());


        Initialized.baseFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Initialized.baseFrame.setResizable(false);
        Initialized.baseFrame.pack();
        Initialized.baseFrame.setLocationRelativeTo(null);
        Initialized.baseFrame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.setFocusTraversalKeysEnabled(false);     // to listen Tab key
        canvas.requestFocusInWindow();
        canvas.requestFocus();
    }


    private static class KeyControl extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            scene.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            scene.keyReleased(e);
        }
    }

    private static class MouseControl extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            scene.mousePressed(e);
        }

        public void mouseReleased(MouseEvent e) {
            scene.mouseReleased(e);
        }

        public void mouseMoved(MouseEvent e) {
            scene.mouseMoved(e);
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            scene.mouseWheelMoved(e);
        }

        public void mouseEntered(MouseEvent e) {
            scene.mouseEntered(e);
        }

        public void mouseExited(MouseEvent e) {
            scene.mouseExited(e);
        }

        public void mouseDragged(MouseEvent e) {
            scene.mouseDragged(e);
        }
    }

    private static class FocusControl extends FocusAdapter {
        public void focusGained(FocusEvent e) {
            scene.focusGained(e);
        }

        public void focusLost(FocusEvent e) {
            scene.focusLost(e);
        }
    }


    public static boolean running = true;

    public static void run(Scene firstScene) {
        scene = firstScene;
        initializeCanvas();
        //boolean lowFPS_warningNeverShown = true;
        while (running) {
            long beginLoopTime = System.currentTimeMillis();
            update();   // this should not contain any scene change because it causes "first render, then update" in the new scene
            render();   // any scene change should be here too because it maintains "first update, then render" in the new scene
            long waitTime = beginLoopTime + AllSettings.userSettings.desiredLoopTime - System.currentTimeMillis();
            if (waitTime > 0) {
                try {
                    //System.out.println(waitTime * 100 / AllSettings.userSettings.desiredLoopTime);
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.exit(0);
    }

    private static void update() {
        scene.update();
    }

    private static void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        RenderGraphics.setRenderQuality(g);
        scene.render(g);
        g.dispose();
        bufferStrategy.show();
    }


    public static void setDisplayScale(double displayScale) {
        AllSettings.userSettings.displayScale = displayScale;
        AllSettings.userSettings.undoDisplayScale = 1.0 / displayScale;
        AllSettings.userSettings.displayWidth = (int) (displayScale * AllSettings.unscaledWidth);
        AllSettings.userSettings.displayHeight = (int) (displayScale * AllSettings.unscaledHeight);

        Initialized.panel.setPreferredSize(new Dimension(AllSettings.userSettings.displayWidth, AllSettings.userSettings.displayHeight));
        canvas.setBounds(0, 0, AllSettings.userSettings.displayWidth, AllSettings.userSettings.displayHeight);
        Initialized.baseFrame.pack();
        Initialized.baseFrame.setLocationRelativeTo(null);
    }

}

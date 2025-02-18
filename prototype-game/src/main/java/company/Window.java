package company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window {
    private static JFrame  frame;
    private static int     width, height;
    private static Game game;




    /**
     * Creates a new window with the given parameters
     * @param game The game to be played in the window
     * @param width The width of the window
     * @param height The height of the window
     * @param title The title of the window
     */
    public static void create(Game game, int width, int height, String title) {
        Window.width = width;
        Window.height = height;
        Dimension size = new Dimension(width, height);
        Window.game = game;
        Window.game.setPreferredSize(size);
        Window.game.setMaximumSize(size);
        Window.game.setMinimumSize(size);
        frame = new JFrame(title);
        frame.add(Window.game);
        frame.setSize(size);
        frame.setFocusable(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.stop();
                super.windowClosing(e);
            }
        });
        game.start();
    }

    /**
     * @return The width of the window
     */
    public static int getWidth() {
        return width;
    }

    /**
     * @return The height of the window
     */
    public static int getHeight() {
        return height;
    }

    /**
     * @return The center x coordinate
     */
    public static int getCenterX() {
        return width / 2;
    }

    /**
     * @return The center y coordinate
     */
    public static int getCenterY() {
        return height / 2;
    }
}

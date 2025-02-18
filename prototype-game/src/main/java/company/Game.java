package company;



import company.actor.Player;
import company.animation.SpriteManager;
import company.controller.GameController;
import company.controller.InputController;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import static company.Main.padding_y;


public class Game extends Canvas implements Runnable {
    private boolean running;
    private Thread thread;
    public static int ticks = 0;
    public static int frames = 0;
    public static int lastTick = 0;
    public static int lastFrame = 0;
    private long time;

    @Override
    public void run() {
        requestFocus();
        try {
            SpriteManager.loadSprites();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameController gameController = new GameController();
        gameController.addPlayer(new Player(Main.width / 4, Main.height - (128*2) - padding_y));

        long lastTime = System.nanoTime();
        final double numTicks = 60.0;
        double nanoSeconds = 1000000000.0 / numTicks;
        double delta = 0;
        time = System.currentTimeMillis();
        this.addKeyListener(new InputController(gameController));
        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / nanoSeconds;
            lastTime = currentTime;
            if (delta >= 1) {
                gameController.tickAll();
                ticks++;
                delta--;

            }
            draw(gameController);
            frames++;
            if (System.currentTimeMillis() - time > 1000) {
                time += 1000;
                lastTick = ticks;
                lastFrame = frames;
                ticks = 0;
                frames = 0;
            }
        }
    }




    private void draw(GameController gameController) {
        BufferStrategy bs = super.getBufferStrategy();
        if (bs == null) {
            super.createBufferStrategy(3);
        }
        bs = super.getBufferStrategy();
        Graphics drawGraphics = bs.getDrawGraphics();
        drawGraphics.fillRect(0,0,getWidth(),getHeight());

            drawGraphics.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            drawGraphics.setColor(Color.BLACK);  // Here
            drawGraphics.drawString(lastTick + " Ticks, FPS: " + lastFrame, 10, 30);

        gameController.renderAll(drawGraphics);
        bs.show();
    }


    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }
}


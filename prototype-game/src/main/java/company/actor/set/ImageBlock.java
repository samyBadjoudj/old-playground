package company.actor.set;


import company.actor.Actor;
import company.actor.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageBlock extends Actor {

    protected final BufferedImage bufferedImage;
    public boolean hit;
    protected boolean walkOn;
    protected int hitElapsedFrame = 120;

    public ImageBlock(int width, int height, int positionX, int positionY, BufferedImage bufferedImage) {
        super(positionX, positionY,width,height);
        this.width = width;
        this.height = height;
        this.bufferedImage = bufferedImage;
    }


    @Override
    public void tick(Player player) {
            updateVelocity(player);
            updatePosition();
    }





    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(bufferedImage, positionX, positionY, null);
    }


    public void hit() {
        this.hit= true;
    }
    public void walkOn() {
        this.walkOn= true;
    }
    public void reset() {
        this.walkOn= false;
    }

}

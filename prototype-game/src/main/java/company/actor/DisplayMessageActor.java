package company.actor;

import java.awt.*;

public class DisplayMessageActor extends Actor {
    private  int lifeTime;
    private final String message;

    public DisplayMessageActor(int positionX, int positionY, int width, int height, int lifeTime, String message) {
        super(positionX, positionY, width, height);
        this.lifeTime = lifeTime;
        this.message = message;
    }

    @Override
    public void tick(Player player) {
        lifeTime--;

    }

    @Override
    public void render(Graphics graphics) {

    }
}

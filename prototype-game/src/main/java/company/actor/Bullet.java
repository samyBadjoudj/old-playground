package company.actor;



import company.actor.set.EnemyImageBlock;
import company.animation.SpriteManager;
import company.controller.GameController;
import company.graphic.Direction;

import java.awt.*;

public class Bullet extends Actor {

    private final Direction currentDirection;
    private static final int DEFAULT_BULLET_SPEED = 10;

    public Bullet(Direction currentDirection, int positionX, int positionY, int width, int height, int velocityX, int velocityY) {
        super(positionX, positionY, width, height);
        this.currentDirection = currentDirection;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    @Override
    public void tick(Player player) {
        updateVelocity(player);
        updatePosition();
        checkCollision();
    }

    private void checkCollision() {
        EnemyImageBlock e = GameController.getCollideBlock(this);
        if(e != null){
            e.hit();
            shouldDisappear = true;
        }
    }

    @Override
    protected void updateVelocity(Player player) {
        if(!player.isAgainstBlock() && currentDirection != player.playerState.getCurrentDirection()){
           if(currentDirection == Direction.LEFT){
               this.velocityX = -DEFAULT_BULLET_SPEED - player.velocityX;
           }else {
               this.velocityX = DEFAULT_BULLET_SPEED - player.velocityX;
           }
       }
        this.velocityY = -player.playerState.getPreviousVelocityY();
        this.gravity = -player.playerState.getPreviousGravity();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(SpriteManager.getBullet(),positionX,positionY,null);
    }
}

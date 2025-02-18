package company.actor;



import company.animation.PlayerSpriteAnimator;
import company.animation.SpriteManager;
import company.controller.GameController;
import company.graphic.Direction;

import java.awt.*;

import static company.graphic.Direction.IDLE;


public class Player extends Actor {


    public static final int DEFAULT_GRAVITY = 3;
    public static final int DEFAULT_JUMP = 20;
    public static final int DEFAULT_JUMPING_LIMIT = 120;
    public static final int NULL_VELOCITY_X = 0;
    public static final int DEFAULT_SPEED_WALKING = 5;
    public static final int DEFAULT_WIDTH_PLAYER = 96;
    public static final int DEFAULT_HEIGHT_PLAYER = 128;
    public static final int NULL_GRAVITY = 0;
    public static final int NULL_VELOCITY_Y = 0;
    public static final int DEFAULT_TIME_HURT = 120;

    private final PlayerSpriteAnimator playerSpriteSheetAnimator;


    public Direction currentDirection = IDLE;
    public boolean walkToRight = false;
    public boolean walkToLeft= false;
    public boolean isJumping = false;
    public boolean isAgainstBlockRight = false;
    public boolean isAgainstBlockLeft = false;
    public boolean isOnBlock = false;
    public boolean hurt = false;
    public int timeRemaingHurt = DEFAULT_TIME_HURT;
    public int currentJumpLevel = 0;
    public int previousVelocityX;
    public int previousVelocityY;
    public int previousGravity;


    public Player(int positionX, int positionY) {
        super(positionX, positionY, DEFAULT_WIDTH_PLAYER,DEFAULT_HEIGHT_PLAYER);
        this.playerSpriteSheetAnimator = new PlayerSpriteAnimator(SpriteManager.playerSprites,this);

    }
    public Player(int positionX, int positionY, int width,int height) {
        super(positionX, positionY, width,height);
        this.playerSpriteSheetAnimator = new PlayerSpriteAnimator(SpriteManager.playerSprites,this);

    }
    @Override
    public void render(Graphics graphics) {
        playerSpriteSheetAnimator.animate(graphics);
    }

    @Override
    public void tick(Player player) {
        falling();
        updateVelocity();
        updatePosition();
        saveVelocityAndGravityApplied();
        checkCollision();
        playerSpriteSheetAnimator.tick();

    }
    @Override
    protected void updatePosition() {
        if(canGoToLeftWithoutWorldMoving()){
            positionX+= velocityX;
        }else if(canGoToRightWithoutWorldMoving()){
            positionX+= velocityX;
        }
        positionY+= velocityY + gravity;
    }

    public boolean canGoToRightWithoutWorldMoving() {
        return  !this.isOutOfSceneRightSide()
                && ( (GameController.isRightBoundaryVisible()  && this.isOnHalfRightScreen() )
                ||  (GameController.isLeftBoundaryVisible()  && this.isOnHalfLeftScreen() ))
                && this.velocityX > 0;
    }

    public boolean canGoToLeftWithoutWorldMoving() {
        return !this.isOutOfSceneLeftSide()
                && ( (GameController.isLeftBoundaryVisible()  && this.isOnHalfLeftScreen() )
                ||  (GameController.isRightBoundaryVisible()  && this.isOnHalfRightScreen() ))
                && this.velocityX < 0;
    }


    protected void updateJumping() {
        if (isJumping && currentJumpLevel < DEFAULT_JUMPING_LIMIT) {
            currentJumpLevel += -velocityY;
        } else {
            velocityY = NULL_VELOCITY_Y;
            isJumping = false;
            currentJumpLevel = 0;
        }
    }

    protected void updateVelocity() {
        if (walkToLeft) {
            this.velocityX = -DEFAULT_SPEED_WALKING;
        } else if (walkToRight) {
            this.velocityX = DEFAULT_SPEED_WALKING;
        } else {
            this.velocityX = NULL_VELOCITY_X;
        }
        updateJumping();
    }


    protected void falling() {
        if(isOnBlock){
            gravity = NULL_GRAVITY;
        }else {
            gravity = DEFAULT_GRAVITY;
        }
    }



    public void startJump() {
        if(isJumping || gravity != NULL_GRAVITY){
            return;
        }
        velocityY = -DEFAULT_JUMP;
       isJumping = true;
    }

    public void checkCollision(){
        isAgainstBlockRight =false;
        isAgainstBlockLeft =false;
        isOnBlock = false;
        CollisionDetection collisionDetection = GameController.getCollision(this);
        if(collisionDetection.hasHitEnemy()){
            hurt= true;
        }
        if(hurt && timeRemaingHurt > 0){
            timeRemaingHurt--;
        }else if(hurt){
            hurt = false;
            timeRemaingHurt = DEFAULT_TIME_HURT;
        }
        if(collisionDetection.hasBlockLeftCollision() && velocityX > NULL_VELOCITY_X){
            blockUser(true,false);
        }
        if(collisionDetection.hasBlockRightCollision()&& velocityX < NULL_VELOCITY_X){
            blockUser(false,true);
        }
        if (collisionDetection.hasBlockTopCollision()) {
            collisionDetection.getTopBorder().walkOn();
            isOnBlock = true;
            resetPhysics();
        } else if (collisionDetection.hasBlockBottomCollision()) {
            currentJumpLevel = DEFAULT_JUMPING_LIMIT;
            collisionDetection.getBottomBorder().hit();
        }

    }

    public void fireBullet(){
        GameController.addBullets(this);
    }

    protected void blockUser(boolean left,boolean right) {
        isAgainstBlockLeft = left;
        isAgainstBlockRight = right;
        velocityX = NULL_VELOCITY_X;
    }

    public boolean isAgainstBlock(){
        return isAgainstBlockLeft || isAgainstBlockRight;
    }

    private void resetPhysics() {
        gravity = NULL_GRAVITY;
        velocityY = NULL_VELOCITY_Y;
        isJumping = false;
    }

    protected void saveVelocityAndGravityApplied() {
        this.previousGravity = this.gravity;
        this.previousVelocityX = this.velocityX;
        this.previousVelocityY = this.velocityY;
    }

    public void setDirection(Direction currentDirection){
        this.currentDirection = currentDirection;
    }


    @Override
    public String toString() {
        return "Player{" +
                "height=" + height +
                ", width=" + width +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                '}';
    }
}

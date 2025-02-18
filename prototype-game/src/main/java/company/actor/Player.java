package company.actor;



import company.animation.PlayerSpriteAnimator;
import company.animation.SpriteManager;
import company.controller.GameController;
import company.graphic.Direction;

import java.awt.*;


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
    public final PlayerState playerState = new PlayerState();

    private final PlayerSpriteAnimator playerSpriteSheetAnimator;



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
        if (playerState.isJumping() && playerState.getCurrentJumpLevel() < DEFAULT_JUMPING_LIMIT) {
            playerState.setCurrentJumpLevel(playerState.getCurrentJumpLevel() + -velocityY);
        } else {
            velocityY = NULL_VELOCITY_Y;
            playerState.setJumping(false);
            playerState.setCurrentJumpLevel(0);
        }
    }

    protected void updateVelocity() {
        if (playerState.isWalkToLeft()) {
            this.velocityX = -DEFAULT_SPEED_WALKING;
        } else if (playerState.isWalkToRight()) {
            this.velocityX = DEFAULT_SPEED_WALKING;
        } else {
            this.velocityX = NULL_VELOCITY_X;
        }
        updateJumping();
    }


    protected void falling() {
        if(playerState.isOnBlock()){
            gravity = NULL_GRAVITY;
        }else {
            gravity = DEFAULT_GRAVITY;
        }
    }



    public void startJump() {
        if(playerState.isJumping() || gravity != NULL_GRAVITY){
            return;
        }
        velocityY = -DEFAULT_JUMP;
        playerState.setJumping(true);
    }

    public void checkCollision(){
        playerState.setAgainstBlockRight(false);
        playerState.setAgainstBlockLeft(false);
        playerState.setOnBlock(false);
        CollisionDetection collisionDetection = GameController.getCollision(this);
        if(collisionDetection.hasHitEnemy()){
            playerState.setHurt(true);
        }
        if(playerState.isHurt() && playerState.getTimeRemaingHurt() > 0){
            playerState.setTimeRemaingHurt(playerState.getTimeRemaingHurt() - 1);
        }else if(playerState.isHurt()){
            playerState.setHurt(false);
            playerState.setTimeRemaingHurt(DEFAULT_TIME_HURT);
        }
        if(collisionDetection.hasBlockLeftCollision() && velocityX > NULL_VELOCITY_X){
            blockUser(true,false);
        }
        if(collisionDetection.hasBlockRightCollision()&& velocityX < NULL_VELOCITY_X){
            blockUser(false,true);
        }
        if (collisionDetection.hasBlockTopCollision()) {
            collisionDetection.getTopBorder().walkOn();
            playerState.setOnBlock(true);
            resetPhysics();
        } else if (collisionDetection.hasBlockBottomCollision()) {
            playerState.setCurrentJumpLevel(DEFAULT_JUMPING_LIMIT);
            collisionDetection.getBottomBorder().hit();
        }

    }

    public void fireBullet(){
        GameController.addBullets(this);
    }

    protected void blockUser(boolean left,boolean right) {
        playerState.setAgainstBlockLeft(left);
        playerState.setAgainstBlockRight(right);
        velocityX = NULL_VELOCITY_X;
    }

    public boolean isAgainstBlock(){
        return playerState.isAgainstBlockLeft() || playerState.isAgainstBlockRight();
    }

    private void resetPhysics() {
        gravity = NULL_GRAVITY;
        velocityY = NULL_VELOCITY_Y;
        playerState.setJumping(false);
    }

    protected void saveVelocityAndGravityApplied() {
        this.playerState.setPreviousGravity(this.gravity);
        this.playerState.setPreviousVelocityX(this.velocityX);
        this.playerState.setPreviousVelocityY(this.velocityY);
    }

    public void setDirection(Direction currentDirection){
        this.playerState.setCurrentDirection(currentDirection);
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

package company.actor;

import company.graphic.Direction;

import static company.graphic.Direction.IDLE;

public class PlayerState {
    private Direction currentDirection = IDLE;
    private boolean walkToRight = false;
    private boolean walkToLeft = false;
    private boolean isJumping = false;
    private boolean isAgainstBlockRight = false;
    private boolean isAgainstBlockLeft = false;
    private boolean isOnBlock = false;
    private boolean hurt = false;
    private int timeRemaingHurt = Player.DEFAULT_TIME_HURT;;
    private int currentJumpLevel = 0;
    private int previousVelocityX;
    private int previousVelocityY;
    private int previousGravity;

    public PlayerState() {
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public boolean isWalkToRight() {
        return walkToRight;
    }

    public void setWalkToRight(boolean walkToRight) {
        this.walkToRight = walkToRight;
    }

    public boolean isWalkToLeft() {
        return walkToLeft;
    }

    public void setWalkToLeft(boolean walkToLeft) {
        this.walkToLeft = walkToLeft;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public boolean isAgainstBlockRight() {
        return isAgainstBlockRight;
    }

    public void setAgainstBlockRight(boolean againstBlockRight) {
        isAgainstBlockRight = againstBlockRight;
    }

    public boolean isAgainstBlockLeft() {
        return isAgainstBlockLeft;
    }

    public void setAgainstBlockLeft(boolean againstBlockLeft) {
        isAgainstBlockLeft = againstBlockLeft;
    }

    public boolean isOnBlock() {
        return isOnBlock;
    }

    public void setOnBlock(boolean onBlock) {
        isOnBlock = onBlock;
    }

    public boolean isHurt() {
        return hurt;
    }

    public void setHurt(boolean hurt) {
        this.hurt = hurt;
    }

    public int getTimeRemaingHurt() {
        return timeRemaingHurt;
    }

    public void setTimeRemaingHurt(int timeRemaingHurt) {
        this.timeRemaingHurt = timeRemaingHurt;
    }

    public int getCurrentJumpLevel() {
        return currentJumpLevel;
    }

    public void setCurrentJumpLevel(int currentJumpLevel) {
        this.currentJumpLevel = currentJumpLevel;
    }

    public int getPreviousVelocityX() {
        return previousVelocityX;
    }

    public void setPreviousVelocityX(int previousVelocityX) {
        this.previousVelocityX = previousVelocityX;
    }

    public int getPreviousVelocityY() {
        return previousVelocityY;
    }

    public void setPreviousVelocityY(int previousVelocityY) {
        this.previousVelocityY = previousVelocityY;
    }

    public int getPreviousGravity() {
        return previousGravity;
    }

    public void setPreviousGravity(int previousGravity) {
        this.previousGravity = previousGravity;
    }
}
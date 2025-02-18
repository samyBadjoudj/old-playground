package company.actor;


import company.Main;
import company.controller.GameController;

import java.awt.*;

public abstract class Actor {

    public int positionX;
    public int positionY;
    public int hitBoxThickness;
    public int id;
    public int gravity;
    public boolean shouldDisappear;

    public int velocityX;
    public int velocityY;
    public int height = 0;
    public int width = 0;
    protected boolean isOnBlock;

    public Actor(int positionX, int positionY, int width, int height) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.hitBoxThickness = 5;
    }

    public  abstract void tick(Player player);
    public  abstract void render(Graphics graphics);
    public   boolean isHorizontallyDynamic() {return false;};

    protected void updateVelocity(Player player) {
        if (isRelativeMovement(player)) {
            if (!player.isAgainstBlock() && player.velocityX != 0) {
                if (isHorizontallyDynamic()) {
                    this.velocityX += -player.playerState.getPreviousVelocityX();
                } else {
                    this.velocityX = -player.playerState.getPreviousVelocityX();
                }
            } else if (!isHorizontallyDynamic()) {
                this.velocityX = 0;
            }
        } else if (!isHorizontallyDynamic()) {
            this.velocityX = 0;
        }

            if(this.isHorizontallyDynamic()){
                this.gravity += -player.playerState.getPreviousGravity();
            }else{
                this.gravity = -player.playerState.getPreviousGravity();
            }
            this.velocityY = -(player.playerState.getPreviousVelocityY());


    }

    private boolean isRelativeMovement(Player player) {
        return (!GameController.isLeftBoundaryVisible() || (player.velocityX > 0 && player.isOnHalfRightScreen()))
                && (!GameController.isRightBoundaryVisible() || (player.velocityX < 0 && player.isOnHalfLeftScreen()));
    }


    public Rectangle getBottomBounds(){
        return new Rectangle(positionX, positionY + height, width, hitBoxThickness);
    }
    public Rectangle getTopBounds(){
        return new Rectangle(positionX, positionY-hitBoxThickness , width, hitBoxThickness);
    }
    public Rectangle getLeftBounds(){
        return new Rectangle(positionX-hitBoxThickness, positionY , hitBoxThickness, height);
    }
    public Rectangle getRightBounds(){
        return new Rectangle(positionX+width-hitBoxThickness, positionY , hitBoxThickness, height);
    }

    public Rectangle getBounds(){
        return new Rectangle(positionX, positionY , width, height);
    }

    protected void updatePosition() {
        positionX+= velocityX;
        positionY+= velocityY + gravity;
    }

    public boolean isOutOfSceneLeftSide(){
        return this.positionX <= 0;
    }
    public boolean isOutOfSceneRightSide(){
        return this.positionX +width >= Main.width;
    }
    public boolean isOnHalfRightScreen(){
        return this.positionX > Main.width/2;
    }
    public boolean isOnHalfLeftScreen(){
        return this.positionX-width <= Main.width/2;
    }

    @Override
    public String toString() {
        return "AbstractWidget{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", gravity=" + gravity +
                ", velocityX=" + velocityX +
                ", velocityY=" + velocityY +
                '}';
    }

    public boolean isOutOfScreen(){
        return false;
        //return (positionX+width < 0 || positionX > Main.width) || positionY > Main.height;
    }
}

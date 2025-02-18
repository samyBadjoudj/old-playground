package company.actor.set;

import company.Main;
import company.actor.Player;
import company.animation.HorizontalBlockSpriteAnimator;
import company.graphic.Direction;
import company.graphic.SpriteSheetXml;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class EnemyImageBlock extends Player {

    private static  final int LENGTH = 120;
    private  static final int DURATION_SEC = 1 ;
    private static final int STEP = 1;//LENGTH/(DURATION_SEC* Main.FRAME_PER_SEC);
    private final HorizontalBlockSpriteAnimator spriteAnimator;
    public boolean hit;
    private int path = LENGTH;
    public Direction direction = Direction.RIGHT;
    private boolean isAgainstBlock;
    private boolean hurt;
    private int timeRemaingHurt;
    private int currentJumpLevel = 0;


    public EnemyImageBlock(int positionX, int positionY) throws IOException {
        super( positionX, positionY,96,128);
        this.spriteAnimator = new HorizontalBlockSpriteAnimator(new SpriteSheetXml(new File("character_zombie_sheet.xml"), ImageIO.read(new File("character_zombie_sheet.png"))),this);
    }

    @Override
    public void tick(Player player) {
        falling();
        followPath();
        updateVelocity(player);
        //updateJumping();
        updatePosition();
        //saveVelocityAndGravityApplied();
        checkCollision();
        spriteAnimator.tick();


    }

    private void followPath() {
        if(path > 0){
            path--;
        }
        if(path <=0 || isAgainstBlock()){
            path =LENGTH;
            direction = direction == Direction.LEFT || playerState.isAgainstBlockRight() ? Direction.RIGHT : Direction.LEFT;
        }

        if(direction == Direction.RIGHT){
            velocityX = STEP;
        }else{
            velocityX = -STEP;
        }
    }

    protected void updatePosition() {
        positionX+= velocityX;
        positionY+= velocityY + gravity;
    }



    @Override
    public void render(Graphics graphics) {
        if(hit){
            graphics.setColor(Color.WHITE);
            graphics.drawString("Enemy hurt " + Math.abs(this.hashCode()), Main.width-500,Main.height/4);
        }
        spriteAnimator.animate(graphics);
    }




    @Override
    public   boolean isHorizontallyDynamic() {return true;};

    public void hit() {
        hit= true;
        shouldDisappear = true;
        
    }


}

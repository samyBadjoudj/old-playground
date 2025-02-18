package company.animation;

import company.actor.Player;
import company.actor.set.EnemyImageBlock;
import company.graphic.SpriteSheetXml;

import java.awt.*;
import java.awt.image.BufferedImage;

import static company.graphic.Direction.LEFT;
import static company.graphic.Direction.RIGHT;

public class HorizontalBlockSpriteAnimator implements SpriteAnimator {
    private final SpriteSheetXml spriteSheet;
    private EnemyImageBlock player;
    private final int DEFAULT_TIMER = 15;
    private int timerInFrame = DEFAULT_TIMER;
    private int timerInFrameDialog = DEFAULT_TIMER;
    private int lastLeftImageIndex = 0;
    private int lastRightImageIndex = 0;
    private final String[] playerRightSprite = {"walk0","walk1","walk2","walk3","walk4","walk6","walk7"};
    private final String[] playerLeftSprite = {"walk0vm","walk1vm","walk2vm","walk3vm","walk4vm","walk6vm","walk7vm"};
    public int width;
    public int height;
    public HorizontalBlockSpriteAnimator(SpriteSheetXml spriteSheet, EnemyImageBlock player) {
        this.spriteSheet = spriteSheet;
        this.player = player;
    }

    public void animate(Graphics graphics){

        switch (player.direction) {
            case LEFT:
                draw(player, graphics, playerLeftSprite[lastLeftImageIndex]);
                break;
            case RIGHT:
                draw(player, graphics,playerRightSprite[lastRightImageIndex]);
                break;
            case TOP:
                break;
            case BOTTOM:
                break;
            case IDLE:
                draw(player, graphics,playerRightSprite[lastRightImageIndex]);
                break;
        }
    }

    private void draw(Player player, Graphics graphics, String spriteIndex) {
        BufferedImage img1 = spriteSheet.sprites.get(spriteIndex);
        this.width = img1.getWidth();
        this.height = img1.getHeight();
        graphics.drawImage(img1, player.positionX,
                player.positionY, player.positionX + width, player.positionY + height,
                0, 0, img1.getWidth(), img1.getHeight(), (img, infoflags, x, y, width, height) -> false);
    }

    public void tick(){
        if(player.hit){
            player.hit = false;
        }
        if(timerInFrame <= 0 && (player.direction == RIGHT || player.direction == LEFT) && player.velocityX != 0){
            timerInFrame = DEFAULT_TIMER;
            if(lastLeftImageIndex%2 == 0){
                lastLeftImageIndex = 0;
            }
            lastLeftImageIndex++;

            if(lastRightImageIndex%2 == 0){
                lastRightImageIndex = 0;
            }
            lastRightImageIndex++;
        }
        timerInFrame--;
    }
}

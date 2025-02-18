package company.controller;

import com.sun.istack.internal.NotNull;
import company.Main;
import company.SoundManager;
import company.actor.Actor;
import company.actor.Bullet;
import company.actor.CollisionDetection;
import company.actor.Player;
import company.actor.set.EnemyImageBlock;
import company.actor.set.ImageBlock;
import company.actor.set.ImageSolidBlock;
import company.animation.AlphabetSpriteAnimator;
import company.graphic.Direction;
import company.level.Level;
import company.level.LevelManager;
import company.level.LevelName;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GameController {

    private static final Level currentLevel = LevelManager.getLevel(LevelName.ONE);
    private static Player player;
    private static GameState gameState = GameState.RUNNING;


    public GameController()  {
    }


    public static boolean isLeftBoundaryVisible(){
        return currentLevel.getLeftBoundary().positionX >= 0 ;
    }

    public static boolean isRightBoundaryVisible() {
        return currentLevel.getRightBoundary().positionX <= Main.width;
    }

    public static EnemyImageBlock getCollideBlock(Bullet bullet) {
        for (EnemyImageBlock block: currentLevel.getDynamicHorizontals()) {
            if (block.getBounds().intersects(bullet.getBounds())) {
                return block;
            }
        }
        return null;
    }

    public static void action() {
        if(gameState == GameState.DIALOG){
            currentLevel.getActiveDialog().values().forEach(AlphabetSpriteAnimator::markForDeletion);
        }else {
            GameController.triggerDialog();
        }
    }

    public void renderAll(Graphics graphics){
        player.render(graphics);
        synchronized (currentLevel.getAll()){
            for (Actor actor : currentLevel.getAll()) {
                if(!actor.isOutOfScreen()){
                    actor.render(graphics);
                }
            }
        }
        if(gameState == GameState.DIALOG){
            synchronized (currentLevel.getActiveDialog()){
                for (AlphabetSpriteAnimator actor : currentLevel.getActiveDialog().values()) {
                        actor.animate(graphics);
                }
            }
        }
    }
    public void tickAll(){
        switch (gameState) {
            case DIALOG:
                synchronized (currentLevel.getActiveDialog()){
                    cleanAndTIckDialog();
                    if(currentLevel.getActiveDialog().isEmpty()){
                        gameState = GameState.RUNNING;
                    }
                }
                break;
            case MENU:
                break;
            case RUNNING:
                player.tick(player);
                clean(currentLevel.getBullets(),true);
                clean(currentLevel.getDynamicHorizontals(),false);
                synchronized (currentLevel.getAll()) {
                    for (Actor actor : currentLevel.getAll()) {
                        actor.tick(player);
                    }
                }
                break;
        }

    }

    private void cleanAndTIckDialog() {
        final Set<String> keys = currentLevel.getActiveDialog().keySet();

        for (String key : keys) {
            final AlphabetSpriteAnimator alphabetSpriteAnimator = currentLevel.getActiveDialog().get(key);
            if(alphabetSpriteAnimator.markedForDeletion){
                currentLevel.getActiveDialog().remove(alphabetSpriteAnimator.id);
            }else{
                alphabetSpriteAnimator.tick();
            }
        }
    }

    private void clean(@NotNull List<? extends Actor> actor, boolean checkOutOfBounds) {
        //TODO use set for lower complexity
        synchronized (actor){
            Iterator<? extends  Actor> iterator = actor.iterator();
            while (iterator.hasNext()) {
                Actor next = iterator.next();
                if((checkOutOfBounds && (next.isOutOfSceneLeftSide() || next.isOutOfSceneRightSide())) || next.shouldDisappear){
                    currentLevel.getAll().remove(next);
                    iterator.remove();
                    if(next.shouldDisappear){
                        SoundManager.playSound("impactPunch_heavy_004.wav");
                    }
                }
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public static void addBullets(Player widget) {
        if(gameState != GameState.RUNNING) return;
        int bulletVelocityX= 5,bulletVelocityY = 0;
        int bulletPositionX = widget.positionX + widget.width;
        int bulletPositionY = widget.positionY + widget.height / 2;
        if(widget.playerState.getCurrentDirection() == Direction.LEFT){
            bulletVelocityX = -5;
            bulletVelocityY = 0;
            bulletPositionX = widget.positionX ;
            bulletPositionY = widget.positionY + widget.height / 2;
        }
        synchronized (currentLevel.getBullets()){
            currentLevel.addBullet(new Bullet(widget.playerState.getCurrentDirection(),
                    bulletPositionX, bulletPositionY,50,50,bulletVelocityX,bulletVelocityY));
        }

    }




    public static CollisionDetection getCollision(Actor actor){
        ImageBlock left = getBlockLeft(actor);
        ImageBlock right = getBlockRight(actor);
        ImageBlock bottom = getBlockBottom(actor);
        ImageBlock top = getBlockTop(actor);
        EnemyImageBlock enemy = getEnemyCrossing(actor);
        return new CollisionDetection(bottom, top, right, left,enemy);

    }

    private static EnemyImageBlock getEnemyCrossing(Actor actor) {
            for (EnemyImageBlock block: currentLevel.getDynamicHorizontals()) {
                if (block.getBounds().intersects(actor.getBounds())) {
                    return block;
                }

            }
            return null;
        }

    public void addPlayer(Player player){
        GameController.player = player;
    }


    private static ImageBlock getBlockLeft(Actor player) {
        for (ImageBlock block: currentLevel.getImageBlocks()) {
            if (block.getLeftBounds().intersects(player.getRightBounds())) {
                return block;
            }

        }
        return null;
    }

    private static ImageBlock getBlockRight(Actor player) {
        for (ImageBlock block: currentLevel.getImageBlocks()) {
            if (block.getRightBounds().intersects(player.getLeftBounds())) {
                return block;
            }

        }
        return null;
    }

    private static ImageSolidBlock getBlockTop(Actor player){
        for (ImageSolidBlock block: currentLevel.getImageBlocks()) {
            if (block.getTopBounds().intersects(player.getBottomBounds())) {
                return block;
            }

        }
        return null;
    }
    private static ImageSolidBlock getBlockBottom(Actor player){
        for (ImageSolidBlock block: currentLevel.getImageBlocks()) {
            if (block.getBottomBounds().intersects(player.getTopBounds())) {
                return block;
            }
        }
        return null;
    }

    public static void triggerDialog(){
        gameState = GameState.DIALOG;
        currentLevel.activateDialog("MAIN_BOX");
    }
    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            Main.class.getResourceAsStream("/path/to/sounds/" + url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

}

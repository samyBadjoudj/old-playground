package company.controller;

import company.actor.Player;
import company.graphic.Direction;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputController  extends KeyAdapter {

    public boolean leftKeyDown = false;
    public boolean rightKeyDown = false;
    private final GameController controller;

    public InputController(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);

        Player player = controller.getPlayer();

        if(e.getKeyCode() == KeyEvent.VK_W){
            player.startJump();
        }
        if(e.getKeyCode() == KeyEvent.VK_A){
            player.playerState.setWalkToLeft(true);
            leftKeyDown = true;
            player.setDirection(Direction.LEFT);
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            player.fireBullet();
        }
        if(e.getKeyCode() == KeyEvent.VK_E){
            GameController.action();
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            player.playerState.setWalkToRight(true);
            rightKeyDown = true;
            player.setDirection(Direction.RIGHT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        Player player = controller.getPlayer();
        if(e.getKeyCode() == KeyEvent.VK_A){
            leftKeyDown = false;
            player.playerState.setWalkToLeft(false);
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            rightKeyDown = false;
            player.playerState.setWalkToRight(false);

        }
        if(!rightKeyDown && leftKeyDown){
            player.playerState.setWalkToLeft(true);
        }
        if(rightKeyDown && !leftKeyDown){
            player.playerState.setWalkToRight(true);

        }
    }
}

package company.actor;

import company.actor.set.EnemyImageBlock;
import company.actor.set.ImageBlock;

public class CollisionDetection {

    private ImageBlock bottomBorder;
    private ImageBlock topBorder;
    private ImageBlock rightBorder;
    private ImageBlock leftBorder;
    private EnemyImageBlock enemy;

    public CollisionDetection(ImageBlock bottomBorder, ImageBlock topBorder, ImageBlock rightBorder, ImageBlock leftBorder, EnemyImageBlock enemy) {
        this.bottomBorder = bottomBorder;
        this.topBorder = topBorder;
        this.rightBorder = rightBorder;
        this.leftBorder = leftBorder;
        this.enemy = enemy;
    }

    public ImageBlock getBottomBorder() {
        return bottomBorder;
    }

    public ImageBlock getTopBorder() {
        return topBorder;
    }


    public boolean hasBlockRightCollision(){
        return rightBorder != null;
    }
    public boolean hasBlockLeftCollision(){
        return leftBorder != null;
    }
    public boolean hasBlockTopCollision(){
        return topBorder != null;
    }
    public boolean hasBlockBottomCollision(){
        return bottomBorder != null;
    }

    public boolean hasNoXCollision(){
        return leftBorder == null && rightBorder == null;
    }

    public boolean hasHitEnemy() {
        return enemy!=null;
    }
}

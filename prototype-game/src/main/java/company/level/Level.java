package company.level;

import company.actor.Actor;
import company.actor.Bullet;
import company.actor.set.EnemyImageBlock;
import company.actor.set.ImageBlock;
import company.actor.set.ImageSolidBlock;
import company.actor.set.ImageWeakBlock;
import company.animation.AlphabetSpriteAnimator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level {

    private LevelName name;
    private List<Bullet> bullets = new ArrayList<>();
    private List<ImageWeakBlock> imageWeakBlocks;
    private Map<String,AlphabetSpriteAnimator> dialog;
    private Map<String,AlphabetSpriteAnimator> activeDialog = new HashMap<>();
    private List<EnemyImageBlock> dynamicHorizontals = new ArrayList<>();
    private List<Actor> all = new ArrayList<>();
    private List<ImageSolidBlock> imageBlocks;
    private ImageBlock leftBoundary;
    private ImageBlock rightBoundary;


    public Level(LevelName name, List<ImageWeakBlock> imageWeakBlocks, Map<String, AlphabetSpriteAnimator> dialog, List<ImageSolidBlock> imageBlocks, List<EnemyImageBlock> dynamicHorizontal, ImageBlock leftBoundary, ImageBlock rightBoundary) {
        this.name = name;
        this.imageWeakBlocks = imageWeakBlocks;
        this.dialog = dialog;
        this.imageBlocks = imageBlocks;
        this.dynamicHorizontals = dynamicHorizontal;
        this.all.addAll(imageBlocks);
        this.all.addAll(imageWeakBlocks);
        this.all.addAll(dynamicHorizontal);
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
    }

    public LevelName getName() {
        return name;
    }

    public List<ImageWeakBlock> getImageWeakBlocks() {
        return imageWeakBlocks;
    }

    public List<ImageSolidBlock> getImageBlocks() {
        return imageBlocks;
    }

    public List<? extends Actor> getAll() {
        return all;
    }

    public void addBullet(Bullet bullet) {


        this.bullets.add(bullet);

        synchronized (all){
            this.all.add(bullet);
        }
    }

    public List<EnemyImageBlock> getDynamicHorizontals() {
        return dynamicHorizontals;
    }

    public List<Bullet> getBullets() {
        return this.bullets;
    }

    public ImageBlock getLeftBoundary() {
        return leftBoundary;
    }

    public ImageBlock getRightBoundary() {
        return rightBoundary;
    }

    public Map<String, AlphabetSpriteAnimator> getDialog() {
        return dialog;
    }

    public void activateDialog(String name) {
         activeDialog.put(name,new AlphabetSpriteAnimator(dialog.get(name).id,dialog.get(name).toDraw)) ;
    }

    public Map<String, AlphabetSpriteAnimator> getActiveDialog() {
        return activeDialog;
    }
}

package company.animation;



import company.Main;

import java.awt.*;
import java.util.Objects;

public class AlphabetSpriteAnimator implements SpriteAnimator {

    public String id ;
    public String toDraw ;
    public boolean markedForDeletion;
    private int DEFAULT_SPEED=1;
    private int DEFAULT_LINE_LENTGH=50;
    private int currentChar=0;
    private StringBuilder onScreen = new StringBuilder();


    public AlphabetSpriteAnimator(String id, String toDraw) {
        this.toDraw = toDraw;
        this.id = id;
    }

    @Override
    public String toString() {
        return "AlphabetSpriteAnimator{" +
                "toDraw=" + toDraw +
                ", currentChar=" + currentChar +
                '}';
    }

    @Override
    public void animate(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(Main.width-500,Main.height/4,200,200);
        graphics.setColor(Color.WHITE);
        graphics.drawString(onScreen.toString(),Main.width-500,Main.height/4);

    }

    @Override
    public void tick() {

        if(toDraw.length()-1 > currentChar){
            currentChar += DEFAULT_SPEED;
            onScreen.append(toDraw.charAt(currentChar));
        }
    }

    public void markForDeleteion(){
        if(toDraw.length()-1<= currentChar){
            this.markedForDeletion = true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlphabetSpriteAnimator that = (AlphabetSpriteAnimator) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

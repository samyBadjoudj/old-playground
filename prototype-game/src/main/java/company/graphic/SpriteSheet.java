package company.graphic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    public BufferedImage sprite;
    public List<BufferedImage> sprites = new ArrayList<>();
    int width;
    int height;
    int rows;
    int columns;
    public SpriteSheet(int width, int height, int rows, int columns, BufferedImage ss) throws IOException {
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
        this.sprite = ss;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                sprites.add(ss.getSubimage(j * width, i * height, width, height));
            }
        }
    }

}

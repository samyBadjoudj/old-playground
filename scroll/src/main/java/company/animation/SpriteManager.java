package company.animation;

import company.graphic.SpriteSheet;
import company.graphic.SpriteSheetXml;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteManager {

    public static SpriteSheetXml playerSprites;
    public static  SpriteSheet world ;
    public static SpriteSheet bulletSprites;

    public static void loadSprites() throws IOException {
        world = new SpriteSheet(32, 32, 16, 16, ImageIO.read(new File("goodly-2x.png")));
        bulletSprites = new SpriteSheet(32, 32, 16, 16, ImageIO.read(new File("goodly-2x.png")));
        playerSprites = new SpriteSheetXml(new File("character_maleAdventurer_sheet.xml"),ImageIO.read(new File("character_maleAdventurer_sheet.png")));;
    }

    public static BufferedImage getBullet(){
        return world.sprites.get(3);
    }
}

package company.util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageManipulation {

    public static BufferedImage flipHorizontally(BufferedImage image){
        BufferedImage mirrored = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D)mirrored.getGraphics();
        AffineTransform transform = new AffineTransform();
        transform.setToScale(-1, 1);
        transform.translate(-image.getWidth(),0);
        graphics.setTransform(transform);
        graphics.drawImage(image, 0, 0, null);
        return mirrored;
    }
}

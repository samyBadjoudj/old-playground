package com.sam.imagesearch.service.tools;

import com.sam.imagesearch.utils.ImageSearchUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Samy Badjoudj
 */
public class ImageToolsOperationsImpl implements ImageToolsOperations {


    private static final Logger LOGGER = Logger.getLogger(ImageToolsOperationsImpl.class);
    private final String PATH_ORIGINAL;
    private final String PATH_THUMB;
    private final ImmutablePair<Integer, Integer> IMAGE_THUMB_SIZE;
    private final ImmutablePair<Integer, Integer> IMAGE_EXTRACT_SIZE;
    private static final String FORMAT = "PNG";
    private static final String EXTENSION = ".png";


    public ImageToolsOperationsImpl(Integer thumbHeight, Integer thumbWidth, String pathThumb,
                                    String pathOriginal, Integer extractHeight, Integer extractWidth) throws IOException {

        this.PATH_THUMB = pathThumb;
        this.PATH_ORIGINAL = pathOriginal;
        this.IMAGE_THUMB_SIZE = new ImmutablePair<>(thumbWidth,thumbHeight);
        this.IMAGE_EXTRACT_SIZE = new ImmutablePair<>(extractWidth,extractHeight);

        FileUtils.forceMkdir(new File(PATH_ORIGINAL));
        FileUtils.forceMkdir(new File(PATH_THUMB));

    }

    //FACTOR THE IMAGE RESCALE
    @Override
    public BufferedImage resizeToExtractSignal(BufferedImage bufferedImage) {

        BufferedImage newImage = new BufferedImage(IMAGE_EXTRACT_SIZE.getLeft(), IMAGE_EXTRACT_SIZE.getRight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = newImage.createGraphics();
        graphics.drawImage(bufferedImage,0,0,IMAGE_EXTRACT_SIZE.getLeft(), IMAGE_EXTRACT_SIZE.getRight(), null);
        graphics.dispose();

        return newImage;
    }

    @Override
    public BufferedImage resizeThumb(BufferedImage bufferedImage) {


        //Keep proportions
        ImmutablePair<Integer,Integer> desirableScale = ImageSearchUtils.getScaled(new ImmutablePair<>(bufferedImage.getWidth(),bufferedImage.getHeight()),IMAGE_THUMB_SIZE);
        BufferedImage newImage = new BufferedImage(desirableScale.getLeft(), desirableScale.getRight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = newImage.createGraphics();
        graphics.drawImage(bufferedImage,0,0,desirableScale.getLeft(), desirableScale.getRight(), null);
        graphics.dispose();

        return  newImage;
    }

    @Override
    public void saveImagesFile(ImmutablePair<String, BufferedImage> original, ImmutablePair<String, BufferedImage> thumb){
        File thumbImageFile = new File(PATH_THUMB + thumb.getLeft()+ EXTENSION);
        File originalImageFile = new File(PATH_ORIGINAL + original.getLeft()+ EXTENSION);

        try {
            ImageIO.write(thumb.getRight(), FORMAT, thumbImageFile);
            ImageIO.write(original.getRight(), FORMAT, originalImageFile);

        } catch (IOException e) {
            LOGGER.warn("Failure saving images files on disk {}", e);
        }

    }

    @Override
    public void delete(Long imageId)  {
        try {
            FileUtils.forceDelete(new File(PATH_ORIGINAL+imageId+ EXTENSION));
            FileUtils.forceDelete(new File(PATH_THUMB+imageId+ EXTENSION));
        } catch (IOException e) {
            LOGGER.warn("Failure deleting images files from disk {}", e);
        }
    }


}

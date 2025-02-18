package com.sam.imagesearch.service.tools;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Samy Badjoudj
 */
public interface ImageToolsOperations {
    public BufferedImage resizeToExtractSignal(BufferedImage bufferedImage);
    public BufferedImage resizeThumb(BufferedImage bufferedImage);
    public void saveImagesFile(ImmutablePair<String, BufferedImage> original, ImmutablePair<String, BufferedImage> thumb) throws IOException;
    public void delete(Long imageId) ;
}

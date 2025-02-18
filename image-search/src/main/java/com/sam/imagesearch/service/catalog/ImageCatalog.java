package com.sam.imagesearch.service.catalog;

import com.sam.imagesearch.domain.SimiliratyDto;
import com.sam.imagesearch.entity.Image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Created by Samy Badjoudj
 */
public interface ImageCatalog {

    public double[] getSignal(BufferedImage image);
    public void computeSimilarities(Image image);
    public Long addImage(BufferedImage bufferedImage) throws IOException;
    public List<SimiliratyDto> searchSimilarImages(Image image);
    public void     removeImage(Long imageId);

    public List<SimiliratyDto> searchSimilarImagesById(Long imageId);
}

package com.sam.imagesearch.dao;

import com.sam.imagesearch.entity.Image;

import java.util.List;

/**
 * Created by Samy Badjoudj
 */
public interface ImageDao {
    public Image getImageBySignal(String signal);
    public Long saveImage(Image image);
    public void deleteImage(Long imageId);
    public List<Image> getAllImagesExceptOne(Image image);

    public  Image getImageById(long imageId);
}

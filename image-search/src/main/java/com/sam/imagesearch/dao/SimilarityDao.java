package com.sam.imagesearch.dao;

import com.sam.imagesearch.entity.Similarity;
import com.sam.imagesearch.entity.Image;

import java.util.Collection;
import java.util.List;

/**
 * Created by Samy Badjoudj
 */
public interface SimilarityDao {
    public void saveAllSimilarities(Collection<Similarity> similarities);
    public void deleteAllSimilarities(Long imageId);
    public List<Similarity> getImageNearestDistance(Image image);
}

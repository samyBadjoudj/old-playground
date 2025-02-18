package com.sam.imagesearch.domain;

import com.sam.imagesearch.entity.Similarity;

/**
 * User: Samy Badjoudj
 * Date: 08/03/14
 * Company: Antropix SARL
 */
public class SimiliratyDto {

    private final long imageId1;
    private final long imageId2;
    private final double  similarity;



    public SimiliratyDto(long imageId1, long imageId2, double similarity) {
        this.imageId1 = imageId1;
        this.imageId2 = imageId2;
        this.similarity = similarity;
    }

    public SimiliratyDto(Similarity similarity) {
        this.imageId1= similarity.getImage1().getId();
        this.imageId2= similarity.getImage2().getId();
        this.similarity= similarity.getSimilarity();
    }

    public long getImageId1() {
        return imageId1;
    }

    public long getImageId2() {
        return imageId2;
    }

    public double getSimilarity() {
        return similarity;
    }
}

package com.sam.imagesearch.entity;

import javax.persistence.*;

/**
 * Created by Samy Badjoudj
 */

@Entity
@Table(name = "SIMILARITIES")
public class Similarity {

    @Id
    @Column(name="SIMILARITY_ID")
    @GeneratedValue
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "IMAGE_1", referencedColumnName= "IMAGE_ID")
    private Image image1;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "IMAGE_2", referencedColumnName= "IMAGE_ID")
    private Image image2;

    @Column(name="SIMILARITY_VALUE")
    private Double similarityValue;


    public Similarity(Image image1, Image image2, Double similarityValue) {
        this.image1 = image1;
        this.image2 = image2;
        this.similarityValue = similarityValue;
    }

    public Similarity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Image getImage1() {
        return image1;
    }

    public void setImage1(Image image1) {
        this.image1 = image1;
    }

    public Image getImage2() {
        return image2;
    }

    public void setImage2(Image image2) {
        this.image2 = image2;
    }

    public Double getSimilarity() {
        return similarityValue;
    }

    public void setSimilarityValue(Double similarityValue) {
        this.similarityValue = similarityValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Similarity similarity = (Similarity) o;

        return similarityValue.equals(similarity.similarityValue) && id.equals(similarity.id) && image1.equals(similarity.image1)
                && image2.equals(similarity.image2);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + image1.hashCode();
        result = 31 * result + image2.hashCode();
        result = 31 * result + similarityValue.hashCode();
        return result;
    }
}

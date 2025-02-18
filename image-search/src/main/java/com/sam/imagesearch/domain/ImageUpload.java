package com.sam.imagesearch.domain;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Samy Badjoudj
 */
public class ImageUpload {

    private String name;
    private Double value;
    private CommonsMultipartFile imageData;

    public ImageUpload() {
    }
    public ImageUpload(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public CommonsMultipartFile getImageData() {
        return imageData;
    }

    public void setImageData(CommonsMultipartFile imageData) {
        this.imageData = imageData;
    }
}

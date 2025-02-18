package com.sam.imagesearch.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by Samy Badjoudj
 */
@Entity
@Table(name = "IMAGES")
public class Image {

    @Id
    @Column(name="IMAGE_ID")
    @GeneratedValue
    private Long id;

    @Column(name="IMAGE_SIGNAL")
    @Type(type="text")
    private String signal;

    public Image(String signal, String name) {
        this.signal = signal;
    }

    public Image() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignal() {
        return signal==null ? "" :signal ;
    }

    public void setSignal(String vector) {
        this.signal = vector;
    }


    private boolean isSignalEquivalent(String other){
        return signal.equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return id.equals(image.id) &&  signal.equals(image.signal);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + signal.hashCode();
        return result;
    }
}

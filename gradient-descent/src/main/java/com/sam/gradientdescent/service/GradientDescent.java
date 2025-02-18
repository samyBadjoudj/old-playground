package com.sam.gradientdescent.service;

import java.util.*;

public class GradientDescent<T> {

    private final List<T[]> path;
    private final T localMinimum;

    public GradientDescent(List<T[]> path, T localMinimum) {
        this.path = path;
        this.localMinimum = localMinimum;
    }

    public List<T[]> getPath() {
        return path;
    }

    public T getLocalMinimum() {
        return localMinimum;
    }
}

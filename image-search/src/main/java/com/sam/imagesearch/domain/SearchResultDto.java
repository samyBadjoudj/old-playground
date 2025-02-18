package com.sam.imagesearch.domain;

import java.util.List;

/**
 * User: Samy Badjoudj
 * Date: 08/03/14
 * Company: Antropix SARL
 */
public class SearchResultDto {



    private final Long currentImageId;
    private final String abosoluteUrlThumbs;
    private final String abosoluteUrlOriginals;
    private final List<SimiliratyDto> similarities;

    public SearchResultDto(Long currentImageId, String abosoluteUrlThumbs, String abosoluteUrlOriginals, List<SimiliratyDto> similarities) {
        this.currentImageId = currentImageId;
        this.abosoluteUrlThumbs = abosoluteUrlThumbs;
        this.abosoluteUrlOriginals = abosoluteUrlOriginals;
        this.similarities = similarities;
    }

    public String getAbosoluteUrlThumbs() {
        return abosoluteUrlThumbs;
    }
    public String getAbosoluteUrlOriginals() {
        return abosoluteUrlOriginals;
    }
    public List<SimiliratyDto> getSimilarities() {
        return similarities;
    }
    public Long getCurrentImageId() {
        return currentImageId;
    }
}

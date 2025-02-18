package com.sam.imagesearch.controllers;

import com.sam.imagesearch.domain.SearchResultDto;
import com.sam.imagesearch.service.catalog.ImageCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/image-similarities/")
public class ImageSimilarityJSONController {

    @Autowired
    private ImageCatalog imageCatalog;
    @Value("${imo.url.thumb}")
    private String ABOSOLUTE_URL_THUMBS;
    @Value("${imo.url.original}")
    private String ABOSOLUTE_URL_ORIGINALS;

    @RequestMapping(value = "{imageId}", method = RequestMethod.GET)
    public @ResponseBody SearchResultDto printStatistic( @PathVariable Long imageId) {
       return new SearchResultDto(imageId, ABOSOLUTE_URL_THUMBS, ABOSOLUTE_URL_ORIGINALS, imageCatalog.searchSimilarImagesById(imageId));
    }
}
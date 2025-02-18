package com.sam.imagesearch.controllers;

import com.sam.imagesearch.service.catalog.ImageCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

@Controller
@RequestMapping("/image-catalog-operations")
public class ImageCatalogOperationController {

    @Value("${imo.url.json.similarities}")
    private  String SIMILARITIES_JSON_URL;

    @Autowired
    public ImageCatalog imageCatalog;


    private static final Logger LOGGER = Logger.getLogger(ImageCatalogOperationController.class);

    @RequestMapping(value = "/addImage", method = RequestMethod.POST)
    public @ResponseBody String addImage(MultipartHttpServletRequest request, HttpServletResponse response) {
        Iterator<String> itr = request.getFileNames();

        LOGGER.info("Receiving image file");
        MultipartFile file = request.getFile(itr.next());
        Long imageIdToRequest = -1L;
        try {
            BufferedImage newImage = ImageIO.read(file.getInputStream());
            imageIdToRequest = imageCatalog.addImage(newImage);
        } catch (IOException e) {

            LOGGER.error("Unable to add the image in the system {}", e);

        }
        LOGGER.info("Image correctly added with id : " + imageIdToRequest);

        return SIMILARITIES_JSON_URL +String.valueOf(imageIdToRequest);
    }


    @RequestMapping(value = "/removeImage/{imageId}", method = RequestMethod.GET)
    public @ResponseBody String removeImage(@PathVariable Long imageId) {
        imageCatalog.removeImage(imageId);
        return "image removed:"+imageId;
    }


    public void setImageCatalog(ImageCatalog imageCatalog) {
        this.imageCatalog = imageCatalog;
    }
}
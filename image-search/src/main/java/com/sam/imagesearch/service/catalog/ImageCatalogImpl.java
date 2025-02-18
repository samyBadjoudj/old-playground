package com.sam.imagesearch.service.catalog;

import com.sam.imagesearch.dao.SimilarityDao;
import com.sam.imagesearch.dao.ImageDao;
import com.sam.imagesearch.domain.SimiliratyDto;
import com.sam.imagesearch.entity.Similarity;
import com.sam.imagesearch.entity.Image;
import com.sam.imagesearch.service.computation.SignalComparator;
import com.sam.imagesearch.service.imageprocessing.HistogramExtractor;
import com.sam.imagesearch.service.imageprocessing.SobelFilter;
import com.sam.imagesearch.service.tools.ImageToolsOperations;
import com.sam.imagesearch.utils.ImageSearchUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samy Badjoudj
 */
public class ImageCatalogImpl implements ImageCatalog {


    private final SobelFilter sobelFilter;
    private final HistogramExtractor histogramExtractor;
    private final ImageDao imageDao;
    private final SignalComparator signalComparator;
    private final SimilarityDao similarityDao;
    private final ImageToolsOperations imageToolsOperations;

    public ImageCatalogImpl(SobelFilter sobelFilter, HistogramExtractor histogramExtractor, ImageDao imageDao, SignalComparator signalComparator, SimilarityDao similarityDao, ImageToolsOperations imageToolsOperations) {
        this.sobelFilter = sobelFilter;
        this.histogramExtractor = histogramExtractor;
        this.imageDao = imageDao;
        this.signalComparator = signalComparator;
        this.similarityDao = similarityDao;
        this.imageToolsOperations = imageToolsOperations;
    }


    @Override
    public double[] getSignal(BufferedImage image) {

        final HashMap<Integer, double[]> rgbHistogram = histogramExtractor.getRGBHistogram(image);
        final ImmutablePair<int[][], int[][]> gradient = sobelFilter.getGradient(image);

        final double[] direction = sobelFilter.getDirection(gradient.getLeft(), gradient.getRight(),
                image.getHeight(), image.getWidth());

        double[] rgbVectorValues = ImageSearchUtils.concatDimensions(rgbHistogram.get(HistogramExtractor.RED),
                rgbHistogram.get(HistogramExtractor.GREEN),
                rgbHistogram.get(HistogramExtractor.BLUE));



        return ImageSearchUtils.concatDimensions(rgbVectorValues, direction);
    }

    @Override
    public void computeSimilarities(Image image) {


    }

    @Override
    public Long addImage(BufferedImage bufferedImage) throws IOException {

        final BufferedImage resizeToExtractSignal = imageToolsOperations.resizeToExtractSignal(bufferedImage);

        double[] signalVector = getSignal(resizeToExtractSignal);

        //StringUtils is fucked up
        String signalVectorToSave = ArrayUtils.toString(signalVector);
        signalVectorToSave = signalVectorToSave.substring(1, signalVectorToSave.length() - 1);

        final Image imageAlreadyInDb = imageDao.getImageBySignal(signalVectorToSave);
        final String signalFromDb = imageAlreadyInDb.getSignal();

        if (signalVectorToSave.equals(signalFromDb)) {
            return imageAlreadyInDb.getId();
        }

        Image imageToSave = new Image();
        imageToSave.setSignal(signalVectorToSave);
        Long id = imageDao.saveImage(imageToSave);

        final ImmutablePair originalTosave = new ImmutablePair(String.valueOf(id), bufferedImage);
        final ImmutablePair thumbTosave = new ImmutablePair(String.valueOf(id), imageToolsOperations.resizeThumb(bufferedImage));


        imageToolsOperations.saveImagesFile(originalTosave, thumbTosave);

        computeDistanceWithAllImages(imageToSave, signalVector);

        return id;
    }

    @Override
    public List<SimiliratyDto> searchSimilarImages(Image image) {
        return convertSimilarityEntityToDto(similarityDao.getImageNearestDistance(image));
    }

    @Override
    public void removeImage(Long imageId) {
        similarityDao.deleteAllSimilarities(imageId);
        imageDao.deleteImage(imageId);
        imageToolsOperations.delete(imageId);
    }

    @Override
    public List<SimiliratyDto> searchSimilarImagesById(Long imageId) {
        return searchSimilarImages(imageDao.getImageById(imageId));
    }


    private List<SimiliratyDto> convertSimilarityEntityToDto(List<Similarity> similarities) {

        List<SimiliratyDto> similiratyDtos = new ArrayList<>();
        for (Similarity similarity : similarities) {

            similiratyDtos.add(new SimiliratyDto(similarity));
        }

        return similiratyDtos;
    }

    private void computeDistanceWithAllImages(Image addedImage, double[] addedImageVector) {
        List<Image> allImages = imageDao.getAllImagesExceptOne(addedImage);
        List<Similarity> similarities = new ArrayList<>();
        for (Image image : allImages) {
            similarities.add(new Similarity(image, addedImage, signalComparator.similarity(addedImageVector, ImageSearchUtils.getVector(image.getSignal()))));
        }

        similarityDao.saveAllSimilarities(similarities);
    }
}

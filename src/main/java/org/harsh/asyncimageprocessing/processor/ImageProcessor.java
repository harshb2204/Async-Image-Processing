package org.harsh.asyncimageprocessing.processor;

import org.harsh.asyncimageprocessing.filter.ImageFilter; // Correct import
import org.harsh.asyncimageprocessing.image.DrawMultipleImagesOnCanvas;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageProcessor {

    private ExecutorService executorService;

    private DrawMultipleImagesOnCanvas drawfn;

    public ImageFilter imageFilter;

    ImageProcessor() {
        executorService = Executors.newFixedThreadPool(100);
        drawfn = DrawMultipleImagesOnCanvas.getInstance();
    }

    public void processImage(BufferedImage image, int num, ImageFilter imageFilter) {
        int numHorizontalImages = image.getWidth() / num;
        int numVerticalImages = image.getHeight() / num;

        for (int i = 0; i < numVerticalImages; i++) {
            for (int j = 0; j < numHorizontalImages; j++) {
                BufferedImage subImage = image.getSubimage(j * num, i * num, num, num);
                executorService.submit(new Callable<BufferedImage>() {
                    @Override
                    public BufferedImage call() throws Exception {
                        return imageFilter.filter(subImage); // Now accessible
                    }
                });
            }
        }
    }
}
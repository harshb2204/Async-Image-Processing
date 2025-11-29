package org.harsh.asyncimageprocessing.processor;

import org.harsh.asyncimageprocessing.filter.ImageFilter; // Correct import
import org.harsh.asyncimageprocessing.image.DrawMultipleImagesOnCanvas;
import org.harsh.asyncimageprocessing.image.ImageData;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ImageProcessor {

    /*
    Why are we using a multithreading approach over here?
    Multithreading is used to deal with cpu heavy tasks
    Async is used when it is a IObound task
    Project is async image processing but we are using threading
    Its pseudo async but it works fine as we have a fixed thread pool
    Manipulation of pixels -> cpu heavy
    When dividing the image into subimages (its IO)
    As java 21 is being used virtual threads take care of IO tasks



     */

    /*
    ImageProcessor divides a large image into many small tiles and 
    applies a filter on each tile in parallel using a fixed pool of worker threads.
    Each tile is processed independently 
    → results returned as Future<ImageData>
     → these are later consumed and drawn on a canvas.
    
    */
   

    private ExecutorService executorService;

    private DrawMultipleImagesOnCanvas drawfn;

    public ImageFilter imageFilter;

    public ImageProcessor() {
        executorService = Executors.newFixedThreadPool(100); //create 100 worker threads waiting for tasks.

    }

    public void processImage(BufferedImage image, int num, ImageFilter imageFilter, DrawMultipleImagesOnCanvas drawfn) {
        int numHorizontalImages = image.getWidth() / num;
        int numVerticalImages = image.getHeight() / num;

        List<Future<ImageData>> futures = new ArrayList<>();

        /*
        A Future can be:

        Not done → result not ready yet.

        Done → result available.

        Failed → throws inside future.get(). 
        */
        /*
         Callable because:

        You need each thread to return processed image data.

        You retrieve results using Future.

        Filtering may throw exceptions.

        The image-processing pipeline depends on task → result flow.
        
        */

        for (int i = 0; i < numVerticalImages; i++) {
            for (int j = 0; j < numHorizontalImages; j++) {
                BufferedImage subImage = image.getSubimage(j * num, i * num, num, num);

                int finali = i;
                int finalj = j;
                //A Callable in Java is basically a task that you can run in a separate thread 
                // AND get a return value from.
                Future<ImageData> future = executorService.submit(new Callable<ImageData>() {
                    @Override
                    public ImageData call() throws Exception {
                        BufferedImage result = imageFilter.filter(subImage);
                        return new ImageData(result,finali*num, finalj*num, num, num );
                    }
                });

                futures.add(future);
            }
        }

        for (Future<ImageData> future : futures) {
            try {
                drawfn.addImageToQueue(future.get());

            } catch (Exception e) {
                System.out.println("Not able to push image to the queue");
            }
        }



    }
}
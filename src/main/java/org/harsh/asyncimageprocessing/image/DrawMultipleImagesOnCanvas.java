package org.harsh.asyncimageprocessing.image;

public class DrawMultipleImagesOnCanvas {


    // We have to make this singleton to keep a single canvas
    private static DrawMultipleImagesOnCanvas instance;

    public static DrawMultipleImagesOnCanvas getInstance(){
        if(instance == null){
            return new DrawMultipleImagesOnCanvas();
        }
        return instance;
    }

}

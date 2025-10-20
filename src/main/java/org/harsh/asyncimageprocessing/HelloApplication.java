package org.harsh.asyncimageprocessing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.harsh.asyncimageprocessing.filter.GreyScaleFilter;
import org.harsh.asyncimageprocessing.filter.ImageFilter;
import org.harsh.asyncimageprocessing.image.DrawMultipleImagesOnCanvas;
import org.harsh.asyncimageprocessing.io.FileImageIO;
import org.harsh.asyncimageprocessing.io.ImageIOInterface;
import org.harsh.asyncimageprocessing.processor.ImageProcessor;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ImageIOInterface imageIO = new FileImageIO();


        BufferedImage image = imageIO.readImage("D:\\AsyncImageProcessing\\src\\main\\java\\org\\harsh\\asyncimageprocessing\\io\\image.jpg");

        DrawMultipleImagesOnCanvas drawMultipleImagesOnCanvas = DrawMultipleImagesOnCanvas.getInstance();
        drawMultipleImagesOnCanvas.initialize(stage);
        ImageProcessor processor = new ImageProcessor();

        ImageFilter imageFilter = new GreyScaleFilter();
        processor.processImage(image, 10, imageFilter, drawMultipleImagesOnCanvas) ;
    }


    public static void main(String[] args) {
        launch();
    }
}
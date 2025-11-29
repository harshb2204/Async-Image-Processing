package org.harsh.asyncimageprocessing.image;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DrawMultipleImagesOnCanvas {


    // We have to make this singleton to keep a single canvas
    private static DrawMultipleImagesOnCanvas instance;

    private Queue<ImageData> queue = new LinkedBlockingQueue<>();

    private Canvas canvas;

    private GraphicsContext graphicsContext;

    public boolean isDrawing = false;

    private Stage primaryStage;



    public static DrawMultipleImagesOnCanvas getInstance(){
        if(instance == null){
            instance = new DrawMultipleImagesOnCanvas();
        }
        return instance;
    }

    public void addImageToQueue(ImageData imageData){
        queue.offer(imageData);
    }

    public void initialize(Stage primaryStage, int imageWidth, int imageHeight){

        this.primaryStage = primaryStage;

        this.canvas = new Canvas(imageWidth,    imageHeight);
        this.graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, imageWidth, imageHeight);
        
        // Create a Scene with the canvas and show it
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, imageWidth, imageHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Async Image Processing");
        primaryStage.show();
        
        new AnimationTimer(){
            @Override
            public void handle(long now){
                // poll image and draw on canvas and use stage to show the image

                if(!isDrawing && !queue.isEmpty()){
                    isDrawing = true;
                    new Thread(() -> {
                        drawNextImage();
                    }).start(); // Fixed: Actually start the thread
                }
            }
        }.start();
    }

    private void drawNextImage(){

        ImageData imageData = queue.poll();
        Platform.runLater(() -> {
            if(imageData != null){
                graphicsContext.drawImage(SwingFXUtils.toFXImage(imageData.getImage(), null),
                        imageData.getI(), imageData.getJ(), imageData.getX(), imageData.getY());

                System.out.println(String.format("Drawing image at i: %d, j: %d", imageData.getI(), imageData.getJ()));
            }
            isDrawing = false; // Reset the flag so next image can be drawn
        });
    }

}

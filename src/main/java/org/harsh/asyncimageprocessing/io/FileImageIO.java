package org.harsh.asyncimageprocessing.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileImageIO implements ImageIOInterface{

    @Override
    public <T> BufferedImage readImage(T src)  {
        //read image from the path specified

        try {
            String path = (String) src;
            File imageFile = new File(path);
            return ImageIO.read(imageFile);
        } catch (Exception e) {
            System.out.println("Not able to read the image");
            return null;
        }

    }

    @Override
    public void sendImage(BufferedImage image) {
        // to store the image
    }
}

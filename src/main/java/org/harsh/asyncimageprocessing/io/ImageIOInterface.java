package org.harsh.asyncimageprocessing.io;

import java.awt.image.BufferedImage;

public interface ImageIOInterface {

    <T> BufferedImage readImage(T src);

    void sendImage(BufferedImage image);
}

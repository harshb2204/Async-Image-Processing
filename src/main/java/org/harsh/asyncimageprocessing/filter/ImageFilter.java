package org.harsh.asyncimageprocessing.filter;

import java.awt.image.BufferedImage;

public interface ImageFilter {

    BufferedImage filter(BufferedImage image);
}

package org.harsh.asyncimageprocessing.filter;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class GreyScaleFilter implements ImageFilter {
    @Override
    public BufferedImage filter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Create a new image with the same dimensions and type
        BufferedImage greyImage = new BufferedImage(width, height, image.getType());

        // Iterate through each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                // Extract color components
                Color color = new Color(rgb);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                // Compute the luminance using standard formula
                int grey = (int)(0.299 * red + 0.587 * green + 0.114 * blue);

                // Create a new grayscale color
                Color greyColor = new Color(grey, grey, grey);

                // Set the pixel
                greyImage.setRGB(x, y, greyColor.getRGB());
            }
        }

        return greyImage;
    }
}

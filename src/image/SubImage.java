package image;

import java.awt.*;

/**
 * Represents a square sub-image extracted from a larger image.
 * This class allows computing the grayscale brightness of the sub-image,
 * which is used in mapping it to an ASCII character.
 */
public class SubImage {
    public static final double MAX_RGB = 255.0;
    private final Color[][] pixels;


    /**
     * Constructs a SubImage from a 2D array of Color pixels.
     *
     * @param pixels a square 2D array representing the pixels of this sub-image
     */
    public SubImage(Color[][] pixels) {
        this.pixels = pixels;
    }

//    public Color[][] getPixels() {
//        return pixels;
//    }
//
//    public int getWidth() {
//        return pixels[0].length;
//    }
//
//    public int getHeight() {
//        return pixels.length;
//    }

    /**
     * Calculates the normalized grayscale brightness of this sub-image.
     * <p>
     * The grayscale value of each pixel is calculated using the formula:
     * {@code grey = 0.2126*R + 0.7152*G + 0.0722*B},
     * then the result is averaged over all pixels and normalized to the range [0, 1].
     *
     * @return the average grayscale brightness of the sub-image
     */
    public double calculateBrightness() {
        double result = 0;
        int numPixels = pixels.length * pixels[0].length;
        for(Color[] pixel : pixels) {
            for(Color color : pixel) {
                double grey = color.getRed() * 0.2126 +
                        color.getGreen() * 0.7152 +
                        color.getBlue() * 0.0722;
                result += grey;
            }
        }
        result /= numPixels;
        return result / MAX_RGB;
    }
}

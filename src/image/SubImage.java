package image;

import java.awt.*;

/**
 * Represents a square sub-image extracted from a larger image.
 * This class allows computing the grayscale brightness of the sub-image,
 * which is used in mapping it to an ASCII character.
 * @author Ishay Shaul
 * @author Maoz Bar Shimon
 * @see ImageProcessor
 */
public class SubImage {
    /** Represents the largest number a RGB unit can have.*/
    public static final double MAX_RGB = 255.0;

    /** multiplier for the red component of the greyscale to get the brightness.*/
    private static final double RED_MULTIPLIER = 0.2126;

    /** multiplier for the green component of the greyscale to get the brightness.*/
    private static final double GREEN_MULTIPLIER = 0.7152;

    /** multiplier for the blue component of the greyscale to get the brightness.*/
    private static final double BLUE_MULTIPLIER = 0.0722;

    /** 2d array of color objects representing the sub image.*/
    private final Color[][] pixels;


    /**
     * Constructs a SubImage from a 2D array of Color pixels.
     * @param pixels a square 2D array representing the pixels of this sub-image
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    public SubImage(Color[][] pixels) {
        this.pixels = pixels;
    }


    /**
     * Calculates the normalized grayscale brightness of this sub-image.
     * <p>
     * The grayscale value of each pixel is calculated using the formula:
     * {@code grey = 0.2126*R + 0.7152*G + 0.0722*B},
     * then the result is averaged over all pixels and normalized to the range [0, 1].
     * @return the average grayscale brightness of the sub-image
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    public double calculateBrightness() {
        double result = 0;
        int numPixels = pixels.length * pixels[0].length;
        for(Color[] pixel : pixels) {
            for(Color color : pixel) {
                double grey = color.getRed() * RED_MULTIPLIER +
                        color.getGreen() * GREEN_MULTIPLIER +
                        color.getBlue() * BLUE_MULTIPLIER;
                result += grey;
            }
        }
        result /= numPixels;
        return result / MAX_RGB;
    }
}

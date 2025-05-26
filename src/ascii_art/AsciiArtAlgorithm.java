package ascii_art;
import image.Image;
import image.ImageProcessor;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;

import java.util.Set;

/**
 * Class whose purpose is to return a 2d array representing the ascii art
 * @author  Ishay Shaul
 * @author Maoz Bar Shimon
 * @see Shell
 */
public class AsciiArtAlgorithm {
    /** The image that is being drawn.*/
    private final Image image;

    /** The resolution of the image.*/
    private final int resolutionsPerRow;

    /** Allows manipulation on the image such as padding and getting sub images.*/
    private final ImageProcessor imageProcessor;

    /** Allows adding and removing characters.*/
    private final SubImgCharMatcher charMatcher;

    /** The formula used for finding nearest brightness.*/
    private final RoundingMode roundingMode;

    /**
     * Constructs an asciiArtAlgorithm, in order to receive the representative 2d array
     * @param image             Image converted to ascii art
     * @param resolutionsPerRow How many pixels will be per row
     * @param imageProcessor    Allows us to prepare the image for the algorithm
     * @param charMatcher       Measures brightness, as well as adding and removing chars
     * @param roundingMode      The rounding mode used to select chars based on brightness values.
     */
    AsciiArtAlgorithm(Image image, int resolutionsPerRow,
                      ImageProcessor imageProcessor, SubImgCharMatcher charMatcher, RoundingMode roundingMode) {
        this.image = image;
        this.resolutionsPerRow = resolutionsPerRow;
        this.imageProcessor = imageProcessor;
        this.charMatcher = charMatcher;
        this.roundingMode = roundingMode;
    }

    /**
     * Returns a 2d array representing the ascii art version of the image.
     * First the method will pad the image, then divide it to sub images,
     * after which, the brightness of each sub image is calculated. The char
     * representing the brightness will be the character closest to said brightness
     * @return The ascii Art
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    public char[][] run(){
        Image paddedImage = imageProcessor.padToPowerOfTwo(image);
        SubImage[][] subImages = imageProcessor.returnSubImages(paddedImage, resolutionsPerRow);

        int rows = subImages.length;
        int cols = subImages[0].length;
        char[][] output = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double brightness = subImages[i][j].calculateBrightness();
                char c = charMatcher.getCharByImageBrightness(brightness, roundingMode);
                output[i][j] = c;
            }
        }
        return output;
    }

}


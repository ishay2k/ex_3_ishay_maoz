package ascii_art;
import image.Image;
import image.ImageProcessor;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;

import java.util.Set;

public class AsciiArtAlgorithm {
    private final Image image;
    private final int resolutionsPerRow;
    private final ImageProcessor imageProcessor;
    private final SubImgCharMatcher charMatcher;
    private final RoundingMode roundingMode;


    AsciiArtAlgorithm(Image image, int resolutionsPerRow,
                      ImageProcessor imageProcessor, SubImgCharMatcher charMatcher, RoundingMode roundingMode) {
        this.image = image;
        this.resolutionsPerRow = resolutionsPerRow;
        this.imageProcessor = imageProcessor;
        this.charMatcher = charMatcher;
        this.roundingMode = roundingMode;
    }

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
                System.out.println(c);
                output[i][j] = c;
            }
        }
        return output;
    }

}


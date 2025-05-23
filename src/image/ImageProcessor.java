package image;

import java.awt.*;

/**
 * A class that provides static image processing operations such as
 * padding an image to the nearest power-of-two dimensions and other future
 * manipulations like dividing an image into sub-images.
 * <p>
 * This class is stateless and designed to work only with static methods.
 * It operates on the {@link Image} class and serves as a helper for image-related tasks
 * needed in the ASCII art conversion pipeline.
 *
 */
public class ImageProcessor {

    /** The largest number a component of RGB greyscale can have.*/
    private static final int MAX_RGB = 255;

    /** Used in nextPowerOfTwo and calculate padSize.*/
    private static final int TWO_MULT = 2;

    /**
     * Pads an image with white pixels so that its width and/or height
     * become the next power of two (only if they are not already).
     * Padding is added symmetrically on both sides of each axis.
     *
     * @param image the original image to be padded
     * @return a new Image instance with padded dimensions,
     *         or the original image if no padding was needed
     */
    public Image padToPowerOfTwo(Image image){
        int oldWidth = image.getWidth();
        int oldHeight = image.getHeight();
        int newWidth, newHeight;
        int rowsToPad, columnsToPad;

        newWidth = getNewSize(oldWidth);
        newHeight = getNewSize(oldHeight);

        if (oldWidth == newWidth && oldHeight == newHeight)
            return image;

        Color[][] newPixels = new Color[newHeight][newWidth];

        createWhiteBackground(newHeight, newWidth, newPixels);

        rowsToPad = calculatePadSize(newHeight, oldHeight);
        columnsToPad = calculatePadSize(newWidth, oldWidth);

        for (int i = 0; i < oldHeight; i++) {
            for (int j = 0; j < oldWidth; j++) {
                newPixels[i + rowsToPad][j + columnsToPad] = image.getPixel(i, j);
            }
        }
        return new Image(newPixels, newWidth, newHeight);
    }

    /**
     * Calculates the number of pixels to pad on each side (symmetrically)
     * in order to expand a given dimension to the desired new size.
     * <p>
     * If the current size is already equal to the new size, no padding is needed
     * and the returned value will be zero.
     *
     * @param newSize the desired dimension (e.g., padded width or height)
     * @param oldSize the original dimension of the image
     * @return the number of pixels to pad on each side (left/right or top/bottom)
     */
    private static int calculatePadSize(int newSize, int oldSize) {
        int sizeToPad;
        if(newSize != oldSize){
            sizeToPad = (newSize - oldSize) / TWO_MULT;
        } else {
            sizeToPad = 0;
        }
        return sizeToPad;
    }

    /**
     * Fills the given 2D pixel array with white color (RGB: 255, 255, 255)
     * to serve as the background for a newly padded image.
     * <p>
     * This method assumes that the array {@code newPixels} has already been
     * allocated with dimensions {@code newHeight} × {@code newWidth}.
     *
     * @param newHeight the height of the image (number of rows)
     * @param newWidth  the width of the image (number of columns)
     * @param newPixels the pixel array to be filled with white color
     */
    private static void createWhiteBackground(int newHeight, int newWidth, Color[][] newPixels) {
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                newPixels[i][j] = new Color(MAX_RGB, MAX_RGB, MAX_RGB);
            }
        }
    }

    /**
     * Returns the adjusted dimension size such that it becomes a power of two.
     * <p>
     * If the original size is already a power of two, the same value is returned.
     * Otherwise, the next higher power of two is returned.
     *
     * @param oldSize the original dimension (width or height)
     * @return the new dimension that is a power of two and greater than or equal to {@code oldSize}
     */
    private static int getNewSize(int oldSize) {
        int newSize;
        if(isPowerOfTwo(oldSize)){
            newSize = oldSize;
        } else {
            newSize = nextPowerOfTwo(oldSize);
        }
        return newSize;
    }

    /**
     * Checks whether a given positive integer is a power of two.
     * <p>
     * This method uses a bitwise trick: for any number that is a power of two,
     * its binary representation contains a single '1' bit (e.g., 1, 2, 4, 8, 16...).
     * The expression {@code (n & (n - 1)) == 0} evaluates to true only in that case.
     *
     * @param n the number to check
     * @return {@code true} if {@code n} is a power of two; {@code false} otherwise
     */
    private static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * Calculates the smallest power of two that is greater than or equal to the given number.
     * <p>
     * For example, if {@code n} is 5, the method returns 8. If {@code n} is already a power of two,
     * the same value is returned.
     *
     * @param n a positive integer
     * @return the smallest power of two greater than or equal to {@code n}
     */
    private static int nextPowerOfTwo(int n) {
        int power = 1;
        while (power < n) {
            power *= TWO_MULT;
        }
        return power;
    }


    /**
     * Will turn the original image into a 2d array of sub-images
     * @param image              the original image that we will be dividing
     * @param resolutionsPerRow  the number of sub-images per row
     * @return                   a 2d array of all the sub-images
     * @author                   Ishay Shaul
     * @author                   Maoz Bar Shimon
     */
    public SubImage[][] returnSubImages(Image image, int resolutionsPerRow){
        int sizeOfMatrix = image.getWidth() / resolutionsPerRow;
        int subImagesToTop = image.getHeight() / sizeOfMatrix;

        SubImage[][] resolutionImage = new SubImage[subImagesToTop][resolutionsPerRow];

        for(int row = 0; row < subImagesToTop; row++){
            for(int column = 0; column < resolutionsPerRow; column++){
                int startX = row * sizeOfMatrix;
                int startY = column * sizeOfMatrix; // change X and Y
                SubImage curr = fillSubImage(startX, startY, sizeOfMatrix, image);
                resolutionImage[row][column] = curr;
            }
        }
        return resolutionImage;
    }

    /**
     * Creates a sub-image of the original image. The method will receive a row and a column which will
     * represent the top left corner of the sub-image, and will create a square sub-image using size
     * @param startRow  the row on the original image
     * @param startCol  the pixel on the original image
     * @param size      the size of the square that will represent the sub-image
     * @param image     the original image
     * @return          a sub-image
     * @author          Ishay Shaul
     * @author          Maoz Bar Shimon
     */
    public SubImage fillSubImage(int startRow, int startCol, int size, Image image){
        Color[][] subImage = new Color[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                subImage[i][j] = image.getPixel(i + startRow, j + startCol);
            }
        }
        return new SubImage(subImage);
    }
}






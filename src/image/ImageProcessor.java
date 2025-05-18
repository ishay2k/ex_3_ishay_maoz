package image;

import java.awt.*;

public class ImageProcessor {


    public static Image padToPowerOfTwo(Image image){
        int oldWidth = image.getWidth();
        int oldHeight = image.getHeight();

        int newWidth = nextPowerOfTwo(oldWidth);
        int newHeight = nextPowerOfTwo(oldHeight);


        int rowsToPad;
        int columnsToPad;

        if (oldWidth == newWidth && oldHeight == newHeight)
            return image;

        Color[][] newPixels = new Color[newHeight][newWidth];

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                newPixels[i][j] = new Color(255, 255, 255);
            }
        }

        if(newHeight != oldHeight){
            rowsToPad = (newHeight - oldHeight) / 2;
        } else {
            rowsToPad = 0;
        }

        if(newWidth != oldWidth){
            columnsToPad = (newWidth - oldWidth) / 2;
        } else {
            columnsToPad = 0;
        }

        for (int i = 0; i < oldHeight; i++) {
            for (int j = 0; j < oldWidth; j++) {
                newPixels[i + rowsToPad][j + columnsToPad] = image.getPixel(i, j);
            }
        }

        return new Image(newPixels, newWidth, newHeight);

    }


    private static int nextPowerOfTwo(int n) {
        int power = 1;
        while (power < n) {
            power *= 2;
        }
        return power;
    }

}






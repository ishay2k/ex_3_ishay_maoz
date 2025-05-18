package ascii_art;
import image.Image;
import image.ImageProcessor;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;

import java.util.Set;

public class AsciiArtAlgorithm {
    private final Image image;
    private final int resolutionsPerRow;
//    private final Set<Character> asciiCharSet;
    private final ImageProcessor imageProcessor;
    private final SubImgCharMatcher charMatcher;

    AsciiArtAlgorithm(Image image, int resolutionsPerRow,
                      ImageProcessor imageProcessor, SubImgCharMatcher charMatcher) {
        this.image = image;
        this.resolutionsPerRow = resolutionsPerRow;
//        this.asciiCharSet = asciiCharSet;
        this.imageProcessor = imageProcessor;
        this.charMatcher = charMatcher;
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
                char c = charMatcher.getCharByImageBrightness(brightness);
                System.out.println(c);
                output[i][j] = c;
            }
        }
        return output;
    }

    public static void main(String[] args) {
        try {
            // טען את התמונה (מהקובץ)
            Image image = new Image("C:\\Users\\MAOZ\\Documents\\OOP\\ex_3_ishay_maoz\\src\\examples\\board.jpeg");

            // הגדר רזולוציה
            int resPerRow = 2;

            char[] charArr = {'m', 'o'};
            SubImgCharMatcher matcher = new SubImgCharMatcher(charArr);

            // צור את המעבדz
            ImageProcessor processor = new ImageProcessor(); // או השתמש ב-Static אם מתאים

            // צור את האלגוריתם
            AsciiArtAlgorithm algo = new AsciiArtAlgorithm(image, resPerRow, processor, matcher);

            // הרץ את האלגוריתם
            char[][] result = algo.run();

            // הדפס את הפלט לשם דיבוג
            for (char[] row : result) {
                for (char c : row) {
                    System.out.print(c + " ");
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("❌ Error during debug run: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


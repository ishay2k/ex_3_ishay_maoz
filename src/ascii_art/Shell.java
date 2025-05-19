package ascii_art;

import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;

public class Shell {
    private static final String EXIT = "exit";
    private static final String VIEW = "chars";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String CHANGE_RES = "res";
    private static final String SELECT_OUTPUT = "output";
    private static final String ROUND = "round";
    private static final String RUN_ALGO = "asciiArt";

    private final String imagePath;
    private Image image;
    private ImageProcessor imageProcessor;
    private AsciiArtAlgorithm asciiArtAlgorithm;
    private char[] charArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    //    private char[] charArray = {'m', 'o'};
    private int resolution;
    private SubImgCharMatcher charMatcher;

    public Shell(String imagePath) throws IOException {
        this.imagePath = imagePath;
        this.image = new Image(imagePath);
        this.imageProcessor = new ImageProcessor();
        this.charMatcher = new SubImgCharMatcher(charArray);
        this.resolution = 2;
        this.asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution, imageProcessor, charMatcher);

    }

    public void run(String imageName) {
        char[][] result = asciiArtAlgorithm.run();

        while (true) {
            System.out.print(">>> ");
            String input = KeyboardInput.readLine();
            String[] tokens = input.trim().split("\\s+"); // seperate to words
            if (tokens.length == 0) continue;

            String command = tokens[0];

            if (command.equals("exit")) {
                break;
            }
        }
    }

    public static void main (String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Shell <image path>");
            return;
        }

        Shell shell = new Shell(args[0]);
        shell.run(shell.imagePath);

    }
//        try {
//            // טען את התמונה (מהקובץ)
//            Image image = new Image("C:\\Users\\ishay\\JAVA\\ex3\\src\\examples\\board.jpeg");
//
//            // הגדר רזולוציה
//            int resPerRow = 2;
//
//            char[] charArr = {'m', 'o'};
//            SubImgCharMatcher matcher = new SubImgCharMatcher(charArr);
//
//            // צור את המעבדz
//            ImageProcessor processor = new ImageProcessor(); // או השתמש ב-Static אם מתאים
//
//            // צור את האלגוריתם
//            AsciiArtAlgorithm algo = new AsciiArtAlgorithm(image, resPerRow, processor, matcher);
//
//            // הרץ את האלגוריתם
//            char[][] result = algo.run();
//
//            // הדפס את הפלט לשם דיבוג
//            for (char[] row : result) {
//                for (char c : row) {
//                    System.out.print(c + " ");
//                }
//                System.out.println();
//            }
//
//        } catch (Exception e) {
//            System.err.println("❌ Error during debug run: " + e.getMessage());
//            e.printStackTrace();
//        }
}



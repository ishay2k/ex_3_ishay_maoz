package ascii_art;

import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

public class Shell {
    private static final String EXIT = "exit";
    private static final String VIEW = "chars";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String CHANGE_RES = "res";
    private static final String SELECT_OUTPUT = "output";
    private static final String ROUND = "round";
    private static final String RUN_ALGO = "asciiArt";

    public Shell(){}

    public void run(String imageName){
        String command = KeyboardInput.readLine();
        while(!command.equals(EXIT)){
            System.out.println("<<<");
        }
    }

    public static void main(String[] args){
        try {
            // טען את התמונה (מהקובץ)
            Image image = new Image("C:\\Users\\ishay\\JAVA\\ex3\\src\\examples\\board.jpeg");

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

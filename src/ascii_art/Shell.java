package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.TreeMap;
import ascii_art.RoundingMode;


import static java.lang.Math.max;

public class Shell {
    private static final String EXIT = "exit";
    private static final String VIEW = "chars";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String CHANGE_RES = "res";
    private static final String SELECT_OUTPUT = "output";
    private static final String ROUND = "round";
    private static final String RUN_ALGO = "asciiArt";
    private static final String ALL = "all";
    private static final int LOW_INDEX = 32;
    private static final int HIGH_INDEX = 126;
    private static final String SPACE_ARG = "space";
    private static final String SPACE = " ";
    private static final String REMOVE_INCORRECT = "Did not remove due to incorrect format.";
    private static final String ADD_INCORRECT = "Did not add due to incorrect format.";
    private static final String RANGE_SPLITTER = "-";
    private static final String CONSOLE = "console";
    private static final String HTML = "html";
    private static final String HTML_FILENAME = "html.out";
    private static final String HTML_FONT = "Courier New";
    private static final String OUTPUT_ERROR = "Did not change output method due to incorrect format.";

    private final String imagePath;
    private Image image;
    private ImageProcessor imageProcessor;
    private AsciiArtAlgorithm asciiArtAlgorithm;
    private char[] charArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    //    private char[] charArray = {'m', 'o'};
    private int resolution;
    private SubImgCharMatcher charMatcher;
    private AsciiOutput currentOutput;
    private RoundingMode roundingMode = RoundingMode.ABS;


    public Shell(String imagePath) throws IOException {
        this.imagePath = imagePath;
        this.image = new Image(imagePath);
        this.imageProcessor = new ImageProcessor();
        this.charMatcher = new SubImgCharMatcher(charArray);
        this.resolution = 2;
        this.asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution,
                imageProcessor, charMatcher, roundingMode);
        currentOutput = new ConsoleAsciiOutput();
    }

    public void run(String imageName) {
        String subCommand;
        while (true) {
            System.out.print(">>> ");
            String input = KeyboardInput.readLine();
            String[] tokens = input.trim().split("\\s+"); // seperate to words
            if (tokens.length == 0) continue;
            String command = tokens[0];
            if (tokens.length == 1) {
                subCommand = "";
            } else {
                subCommand = tokens[1];
            }

            if (command.equals(EXIT)){
                break;
            }
            if(command.equals(CHANGE_RES)){
                if(!setResolution(subCommand)){
                    continue;
                }
            }
            if(command.equals(ADD)){
                addChars(tokens);
                continue;
            }
            if(command.equals(REMOVE)){
                removeChars(tokens);
                continue;
            }
            if(command.equals(VIEW)){
                displayChars();
                continue;
            }
            if(command.equals(SELECT_OUTPUT)){
                displayForUser(tokens);
                continue;
            }
            if(command.equals(ROUND)){
                roundCommand(subCommand);
                continue;
            }
        }
        char[][] result = asciiArtAlgorithm.run();
    }

    private boolean setResolution(String subCommand) {
        int newRes;
        switch (subCommand) {
            case "":
                break;
            case "up":
                newRes = resolution * 2;
                if(!checkBoundries(newRes)) return false;
                resolution *= 2;
                break;
            case "down":
                newRes = resolution / 2;
                if(!checkBoundries(newRes)) return false;
                resolution /= 2;
                break;
            default:
                System.out.println("Did not change resolution due to incorrect format.\n");
                return false;
        }
        System.out.println("Resolution set to %s.\n" + resolution);
        return true;
    }


    private boolean checkBoundries(int newRes) {
        int imgHeight = image.getHeight();
        int imgWidth = image.getWidth();
        int minCharsInRow = max(1, imgWidth / imgHeight);
        if (newRes > imgWidth || minCharsInRow > newRes) {
            System.out.println("Did not change resolution due to exceeding boundaries.");
            return false;
        }
        return true;
    }

    /**
     * Method that is employed when the command "add" was given
     * @param commands An array of strings that make up the original user command
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    private void addChars(String[] commands){
        if(commands[1].length() == 1){
            char c = commands[1].charAt(0);
            charMatcher.addChar(c);
            return;
        }
        // all
        if(commands[0].equals(ALL)){
            for(int i = LOW_INDEX; i <= HIGH_INDEX; i++){
                char c = (char) i;
                charMatcher.addChar(c);
            }
            return;
        }
        // range
        if(commands[1].length() == 3){
            String[] parts= commands[1].split(RANGE_SPLITTER);
            if(parts.length == 2){
                int first = (int) parts[0].charAt(0);
                int second = (int) parts[1].charAt(0);
                setAdds(first, second);
            }
            else{
                System.out.println(ADD_INCORRECT);
            }
        }
        // space
        if(commands[0].equals(SPACE_ARG)){
            char c = SPACE.charAt(0);
            charMatcher.addChar(c);
        }
        // else
        else{
            System.out.println(ADD_INCORRECT);
        }
    }

    /**
     * adds all chars in the range of n to m (or vice versa) to the char array
     * @param n The first char(int will be cast to char)
     * @param m The second char. n can be greater than m
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    private void setAdds(int n, int m){
        if(n <= m){
            for(int i = n; i <= m; i++){
                char c = (char) i;
                charMatcher.addChar(c);
            }
        }
        else{
            for(int i = m; i <= n; i++){
                char c = (char) i;
                charMatcher.addChar(c);
            }
        }
    }

    /**
     * method that takes care of the "remove" command from the user
     * @param commands Array of string which make up the command given by the user.
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    public void removeChars(String[] commands){
        if(commands[1].length() == 1){
            char c = commands[1].charAt(0);
            charMatcher.removeChar(c);
            return;
        }
        // all
        if(commands[0].equals(ALL)){
            for(int i = LOW_INDEX; i <= HIGH_INDEX; i++){
                char c = (char) i;
                charMatcher.removeChar(c);
            }
            return;
        }
        // range
        if(commands[1].length() == 3){
            String[] parts= commands[1].split(RANGE_SPLITTER);
            if(parts.length == 2){
                int first = (int) parts[0].charAt(0);
                int second = (int) parts[1].charAt(0);
                setRemove(first, second);
            }
            else{
                System.out.println(REMOVE_INCORRECT);
            }
        }
        // space
        if(commands[0].equals(SPACE_ARG)){
            char c = SPACE.charAt(0);
            charMatcher.removeChar(c);
        }
        // else
        else{
            System.out.println(REMOVE_INCORRECT);
        }
    }

    /**
     * Given two ints, this method will remove all chars that are in between the
     * two from the brightness map. n isn't necessarily smaller than m.
     * The integers are interpreted as Unicode values.
     * @param n The first int that is given.
     * @param m The second int that is given
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    public void setRemove(int n, int m){
        if(n <= m){
            for(int i = n; i <= m; i++){
                char c = (char) i;
                charMatcher.removeChar(c);
            }
        }
        else{
            for(int i = m; i <= n; i++){
                char c = (char) i;
                charMatcher.removeChar(c);
            }
        }
    }

    /**
     * Displays all characters currently in use for ASCII rendering.
     * <p>
     * The characters are retrieved from the brightness map provided by the {@code charMatcher}
     * and printed to the console, separated by spaces.
     * If no characters are in use, nothing is printed.
     *
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    public void displayChars(){
        TreeMap<Character, Double> charTreeMap = charMatcher.getCharBrightnessMap();
        if(charTreeMap.isEmpty()){
            return;
        }
        for(Character c: charTreeMap.keySet()){
            System.out.println(c + SPACE);
        }
        System.out.println();
    }

    /**
     * Sets the current output mode based on the user command.
     * <p>
     * Supported output types are:
     * <ul>
     *   <li><b>console</b> - Outputs to the terminal using {@link ConsoleAsciiOutput}</li>
     *   <li><b>html</b> - Outputs to an HTML file using {@link HtmlAsciiOutput}</li>
     * </ul>
     * If an unsupported output type is given, or if the command is incomplete,
     * an error message is printed.
     *
     * @param commands an array of strings representing the user command;
     *                 the second element specifies the desired output type
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    public void displayForUser(String[] commands){
        if(commands.length == 1){
            System.out.println(OUTPUT_ERROR);
            return;
        }
        if(commands[1].equals(CONSOLE)){
            currentOutput = new ConsoleAsciiOutput();
            return;
        }
        if(commands[1].equals(HTML)){
            currentOutput = new HtmlAsciiOutput(HTML_FILENAME, HTML_FONT);
            return;
        }
        else{
            System.out.println(OUTPUT_ERROR);
        }
    }

    private void roundCommand(String subCommand){
        switch (subCommand){
            case "up":
                roundingMode = RoundingMode.UP;
                break;
            case "down":
                roundingMode = RoundingMode.DOWN;
                break;
            case "abs":
                roundingMode = RoundingMode.ABS;
                break;
            default:
                System.out.println("Did not change rounding method due to incorrect format.");
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



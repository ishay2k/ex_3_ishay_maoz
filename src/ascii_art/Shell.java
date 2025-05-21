package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.TreeMap;


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
    public static final String ROUND_ERR_MESSAGE = "Did not change rounding method due to incorrect format.";
    public static final String BOUNDRIES_ERR_MESSAGE = "Did not change resolution due to exceeding boundaries.";
    public static final String RES_ERR_FORMAT = "Did not change resolution due to incorrect format.\n";
    public static final String RESOLUTION_SET_MESSAGE = "Resolution set to %s.\n";
    private static final String EMPTY = "";
    private static final int MIN_CHARS = 2;
    private static final String MIN_CHARS_ERROR = "Did not execute. Charset is too small.";


    private final String imagePath;
    private Image image;
    private ImageProcessor imageProcessor;
    private AsciiArtAlgorithm asciiArtAlgorithm;
    private char[] charArray = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
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
        char[][] result = asciiArtAlgorithm.run();
        while (true) {
            System.out.print(">>> ");
            String input = KeyboardInput.readLine();
            String[] tokens = input.trim().split("\\s+"); // seperate to words
            if (tokens.length == 0) continue;
            String command = tokens[0];
            if (tokens.length == 1) {
                subCommand = EMPTY;
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
                addChars(subCommand);
                continue;
            }
            if(command.equals(REMOVE)){
                removeChars(subCommand);
                continue;
            }
            if(command.equals(VIEW)){
                displayChars();
                continue;
            }
            if(command.equals(SELECT_OUTPUT)){
                displayForUser(subCommand);
                continue;
            }
            if(command.equals(ROUND)){
                roundCommand(subCommand);
                continue;
            }
            if(command.equals(RUN_ALGO)){
                runAlgorithm();
                continue;
            }
        }
    }

    /**
     * Attempts to change the current resolution based on the provided sub-command.
     *
     * Supported sub-commands:
     * - "up"   → doubles the current resolution (×2)
     * - "down" → halves the current resolution (/2)
     * - ""     → does nothing (silent no-op)
     *
     * The method uses {@code checkBoundaries(newRes)} to validate whether the new resolution
     * is within the allowed image boundaries. If the new resolution is invalid, an error
     * message is printed and the method returns false.
     *
     * If the sub-command is invalid (not recognized), the constant {@code RES_ERR_FORMAT}
     * is printed and the method returns false.
     *
     * Upon success, the resolution is updated and a message is printed using
     * {@code RESOLUTION_SET_MESSAGE + resolution}.
     *
     * @param subCommand a command string indicating how to adjust the resolution
     * @return true if the resolution was updated or left unchanged; false if an error occurred
     */
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
                System.out.println(RES_ERR_FORMAT);
                return false;
        }
        System.out.println(RESOLUTION_SET_MESSAGE + resolution);
        return true;
    }



    /**
     * Checks whether a proposed resolution value is within the legal display boundaries
     * based on the image's dimensions.
     *
     * The boundaries are defined as:
     * - The resolution must not exceed the image width.
     * - The resolution must be at least the number of characters per row, calculated as:
     *   max(1, imageWidth / imageHeight).
     *
     * If the proposed resolution violates these constraints, an error message is printed
     * (via {@code BOUNDRIES_ERR_MESSAGE}) and the method returns false.
     *
     * @param newRes the proposed resolution (number of characters per row)
     * @return true if the resolution is within the boundaries; false otherwise
     */
    private boolean checkBoundries(int newRes) {
        int imgHeight = image.getHeight();
        int imgWidth = image.getWidth();
        int minCharsInRow = max(1, imgWidth / imgHeight);
        if (newRes > imgWidth || minCharsInRow > newRes) {
            System.out.println(BOUNDRIES_ERR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Method that is employed when the command "add" was given
     * @param command A strings that make up the sub user command
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    private void addChars(String command){
        if(command.equals(EMPTY)){
            System.out.println(ADD_INCORRECT);
            return;
        }
        if(command.length() == 1){
            char c = command.charAt(0);
            charMatcher.addChar(c);
            return;
        }
        // all
        if(command.equals(ALL)){
            for(int i = LOW_INDEX; i <= HIGH_INDEX; i++){
                char c = (char) i;
                charMatcher.addChar(c);
            }
            return;
        }
        // range
        if(command.length() == 3){
            String[] parts= command.split(RANGE_SPLITTER);
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
        if(command.equals(SPACE_ARG)){
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
     * @param command Array of string which make up the command given by the user.
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    private void removeChars(String command){
        if(command.equals(EMPTY)){
            System.out.println(REMOVE_INCORRECT);
            return;
        }
        if(command.length() == 1){
            char c = command.charAt(0);
            charMatcher.removeChar(c);
            return;
        }
        // all
        if(command.equals(ALL)){
            for(int i = LOW_INDEX; i <= HIGH_INDEX; i++){
                char c = (char) i;
                charMatcher.removeChar(c);
            }
            return;
        }
        // range
        if(command.length() == 3){
            String[] parts= command.split(RANGE_SPLITTER);
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
        if(command.equals(SPACE_ARG)){
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
    private void setRemove(int n, int m){
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
    private void displayChars(){
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
     * @param command an array of strings representing the user command;
     *                 the second element specifies the desired output type
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    private void displayForUser(String command){
        if(command.equals(EMPTY)){
            System.out.println(OUTPUT_ERROR);
            return;
        }
        if(command.equals(CONSOLE)){
            currentOutput = new ConsoleAsciiOutput();
            return;
        }
        if(command.equals(HTML)){
            currentOutput = new HtmlAsciiOutput(HTML_FILENAME, HTML_FONT);
            return;
        }
        else{
            System.out.println(OUTPUT_ERROR);
        }
    }

    /**
     * Sets the rounding mode based on the provided sub-command string.
     *
     * Supported sub-commands (case-sensitive):
     * - "up"   → sets roundingMode to RoundingMode.UP
     * - "down" → sets roundingMode to RoundingMode.DOWN
     * - "abs"  → sets roundingMode to RoundingMode.ABS
     *
     * If the input does not match any known sub-command, the rounding mode is left unchanged,
     * and a message is printed to the console.
     *
     * @param subCommand the rounding command as a string (e.g., "up", "down", "abs")
     */
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
                System.out.println(ROUND_ERR_MESSAGE);
        }
    }

    private void runAlgorithm(){
        TreeMap<Character, Double> charTreeMap = charMatcher.getCharBrightnessMap();
        if(charTreeMap.size() <= MIN_CHARS){
            System.out.println(MIN_CHARS_ERROR);
            return;
        }
        System.out.println("number of chars: " + charTreeMap.size());
        char[][] board = asciiArtAlgorithm.run();
        currentOutput.out(board);
    }

    public static void main (String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Shell <image path>");
            return;
        }

        Shell shell = new Shell(args[0]);
        shell.run(shell.imagePath);

    }
}



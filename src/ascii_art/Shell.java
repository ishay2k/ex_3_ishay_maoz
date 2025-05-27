package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import exceptions.OutOfBoundsException;
import exceptions.TooFewCharactersException;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import exceptions.InvalidFormatException;



import static java.lang.Math.max;

/**
 * The shell class will serve as the main. Here the program (centering around run) will
 * employ the user's command on the image.
 * @author Ishay Shaul
 * @author Maoz Bar Shimon
 */
public class Shell {
    /** command to end the game.*/
    private static final String EXIT = "exit";

    /** command to see which characters are used for the image.*/
    private static final String VIEW = "chars";

    /** command to add characters .*/
    private static final String ADD = "add";

    /** command to remove characters.*/
    private static final String REMOVE = "remove";

    /** command to increase or decrease the resolution.*/
    private static final String CHANGE_RES = "res";

    /** command to select what type of output the program will generate.*/
    private static final String SELECT_OUTPUT = "output";

    /** Command the changes how the program rounds.*/
    private static final String ROUND = "round";

    /** Command that will run the asciiArtAlgorithm and produce the final image.*/
    private static final String RUN_ALGO = "asciiArt";

    /** sub command of add and remove. will perform the first command on all
     * characters.*/
    private static final String ALL = "all";

    /** Index of the lowest character in the ascii table.*/
    private static final int LOW_INDEX = 32;

    /** Index of the highest character in the ascii table.*/
    private static final int HIGH_INDEX = 126;

    /** One of the sub commands of add and remove. will add or remove a space.*/
    private static final String SPACE_ARG = "space";

    /** the empty space will be added or removed from the characters list .*/
    private static final String SPACE = " ";

    /** Error that is printed when an incorrect sub command is given to remove.*/
    private static final String REMOVE_INCORRECT = "Did not remove due to incorrect format.";

    /** Error that is printed when an incorrect sub command is given to add.*/
    private static final String ADD_INCORRECT = "Did not add due to incorrect format.";

    /** The splitter that is used when the user wants to add/remove a range of chars .*/
    private static final String RANGE_SPLITTER = "-";

    /** at each iteration, this arrow will "point" to the user, so they know to enter
     * command.*/
    private static final String ARROW_TO_USER = ">>> ";

    /** after the user command, this arrow will show that it is the program's turn.*/
    private static final String FROM_USER = "<<< ";

    /** Sub command of output. Meaning that the final image be printed to the console.*/
    private static final String CONSOLE = "console";

    /** Sub command of output. Meaning that the final image be printed to a html file.*/
    private static final String HTML = "html";

    /** The html file that the final image will be sent to.*/
    private static final String HTML_FILENAME = "out.html";

    /** the font of the html file.*/
    private static final String HTML_FONT = "Courier New";

    /** Error that is printed when an incorrect sub command is given to output.*/
    private static final String OUTPUT_ERROR = "Did not change output method due to incorrect format.";

    /** Error that is printed when an incorrect sub command is given to round.*/
    public static final String ROUND_ERR_MESSAGE = "Did not change rounding method due to incorrect format.";

    /** Error that is printed when the resolution is out of bounds.*/
    public static final String BOUNDRIES_ERR_MESSAGE = "Did not change resolution due to exceeding boundaries.";

    /** Error that is printed when the command for "res" is incorrect.*/
    public static final String RES_ERR_FORMAT = "Did not change resolution due to incorrect format.";

    /** Information message that is printed for the user to know what the resolution is
     * after the change.*/
    public static final String RESOLUTION_SET_MESSAGE = "Resolution set to ";

    /** empty string that will be in use if the user did not give a sub command.*/
    private static final String EMPTY = "";

    /** sub command to increase resolution.*/
    private static final String UP = "up";

    /** sub command to decrease resolution.*/
    private static final String DOWN  = "down";

    private static final int RES_MULTIPLIER = 2;

    /** minimum number of characters that can be in use.*/
    private static final int MIN_CHARS = 2;

    /** for the remove or add range, there must be no more than three chars.*/
    private static final int ADD_REM_RANGE = 3;

    /** after removing "-", there can be only two chars for the range.*/
    private static final int SIZE_RANGE = 2;

    /** Error message that is sent if the user commanded asciiArt and the number of chars
     * is no more than two.*/
    private static final String MIN_CHARS_ERROR = "Did not execute. Charset is too small.";

    /** the program will split the command to words for further evaluation.*/
    private static final String SPLITTER = "\\s+";

    /** Zero in ascii value.*/
    private static final int ZERO_REPRESENT = 48;

    /** One in ascii value.*/
    private static final int ONE_REPRESENT = 49;

    /** Two in ascii value.*/
    private static final int TWO_REPRESENT = 50;

    /** Three in ascii value.*/
    private static final int THREE_REPRESENT = 51;

    /** Four in ascii value.*/
    private static final int FOUR_REPRESENT = 52;

    /** Five in ascii value.*/
    private static final int FIVE_REPRESENT = 53;

    /** Six in ascii value.*/
    private static final int SIX_REPRESENT = 54;

    /** Seven in ascii value.*/
    private static final int SEVEN_REPRESENT = 55;

    /** Eight in ascii value.*/
    private static final int EIGHT_REPRESENT = 56;

    /** Nine in ascii value.*/
    private static final int NINE_REPRESENT = 57;

    /** command for absolute rounding.*/
    private static final String ABS = "abs";

    private static final String MAIN_ERROR = "Usage: java Shell <image path>";

    /** The path to the image we are using.*/
    private final String imagePath;

    /** the object of type Image that we are portraying.*/
    private Image image;

    /** The object ImageProcessor that turned the file path to the Image.*/
    private ImageProcessor imageProcessor;

    /** The algorithm that will turn our brightness levels to an image.*/
    private AsciiArtAlgorithm asciiArtAlgorithm;

    /** default characters in ascii value (the numbers represent 0-9).*/
    private char[] charArray = {ZERO_REPRESENT, ONE_REPRESENT, TWO_REPRESENT,
    THREE_REPRESENT, FOUR_REPRESENT, FIVE_REPRESENT, SIX_REPRESENT, SEVEN_REPRESENT,
    EIGHT_REPRESENT, NINE_REPRESENT};
    //    private char[] charArray = {'m', 'o'};

    /** The current resolution of the image.*/
    private int resolution;

    /** An object that lets us add and remove chars, while also calculating their
     * brightness levels.*/
    private SubImgCharMatcher charMatcher;

    /** current output of the program. Can be console or html*/
    private AsciiOutput currentOutput;

    /** The rounding mode of the program.*/
    private RoundingMode roundingMode = RoundingMode.ABS;


    /**
     * Constructs a new Shell object responsible for taking care of the ascii art
     * generation.
     * @param imagePath    A path to the image that will be portrayed
     * @throws IOException If the image cannot be read
     */
    public Shell(String imagePath) throws IOException {
        // check image here for exception
        this.imagePath = imagePath;
        this.image = new Image(imagePath);
        this.imageProcessor = new ImageProcessor();
        this.charMatcher = new SubImgCharMatcher(charArray);
        this.resolution = MIN_CHARS;
        currentOutput = new ConsoleAsciiOutput();
    }

    /**
     * Method will continue running until the user exits. While it runs, the program
     * will accept commands from the user and employ them
     * @param imageName the image file path that is used
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    public void run(String imageName) {
        String subCommand;
        while (true) {
            System.out.print(ARROW_TO_USER);
            String input = KeyboardInput.readLine();
            String[] tokens = input.trim().split(SPLITTER); // seperate to words
            if (tokens.length == 0) continue;
            String command = tokens[0];
            if (tokens.length == 1) {
                subCommand = EMPTY;
            } else {
                subCommand = tokens[1];
            }
            try {
                if (command.equals(EXIT)) {
                    break;
                }
                if (command.equals(CHANGE_RES)) {
                    System.out.println(FROM_USER);
                    if (!setResolution(subCommand)) {
                        continue;
                    }
                }
                if (command.equals(ADD)) {
                    System.out.println(FROM_USER);
                    addChars(subCommand);
                    continue;
                }
                if (command.equals(REMOVE)) {
                    System.out.println(FROM_USER);
                    removeChars(subCommand);
                    continue;
                }
                if (command.equals(VIEW)) {
                    System.out.println(FROM_USER);
                    displayChars();
                    continue;
                }
                if (command.equals(SELECT_OUTPUT)) {
                    System.out.println(FROM_USER);
                    displayForUser(subCommand);
                    continue;
                }
                if (command.equals(ROUND)) {
                    System.out.println(FROM_USER);
                    roundCommand(subCommand);
                    continue;
                }
                if (command.equals(RUN_ALGO)) {
                    System.out.println(FROM_USER);
                    runAlgorithm();
                    continue;
                }
            } catch (InvalidFormatException | OutOfBoundsException | TooFewCharactersException e){
                System.out.println(FROM_USER + e.getMessage());
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
    private boolean setResolution(String subCommand) throws InvalidFormatException, OutOfBoundsException{
        int newRes;
        switch (subCommand) {
            case EMPTY:
                break;
            case UP:
                newRes = resolution * RES_MULTIPLIER;
                if(!checkBoundries(newRes)) return false;
                resolution *= RES_MULTIPLIER;
                break;
            case DOWN:
                newRes = resolution / RES_MULTIPLIER;
                if(!checkBoundries(newRes)) return false;
                resolution /= RES_MULTIPLIER;
                break;
            default:
                throw new InvalidFormatException(RES_ERR_FORMAT);
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
    private boolean checkBoundries(int newRes) throws OutOfBoundsException {
        int imgHeight = image.getHeight();
        int imgWidth = image.getWidth();
        int minCharsInRow = max(1, imgWidth / imgHeight);
        if (newRes > imgWidth || minCharsInRow > newRes) {
            throw new OutOfBoundsException(BOUNDRIES_ERR_MESSAGE);
        }
        return true;
    }

    /**
     * Method that is employed when the command "add" was given
     * @param command A strings that make up the sub user command
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    private void addChars(String command) throws InvalidFormatException {
        if(command.equals(EMPTY)) {
            throw new InvalidFormatException(ADD_INCORRECT);
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
        if(command.length() == ADD_REM_RANGE){
            String[] parts= command.split(RANGE_SPLITTER);
            if(parts.length == SIZE_RANGE){
                int first = (int) parts[0].charAt(0);
                int second = (int) parts[1].charAt(0);
                setAdds(first, second);
                return;
            }
            else{
                throw new InvalidFormatException(ADD_INCORRECT);
            }
        }
        // space
        if(command.equals(SPACE_ARG)){
            char c = SPACE.charAt(0);
            charMatcher.addChar(c);
        }
        // else
        else{
            throw new InvalidFormatException(ADD_INCORRECT);
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
    private void removeChars(String command) throws InvalidFormatException {
        if(command.equals(EMPTY)){
            throw new InvalidFormatException(REMOVE_INCORRECT);
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
        if(command.length() == ADD_REM_RANGE){
            String[] parts= command.split(RANGE_SPLITTER);
            if(parts.length == SIZE_RANGE){
                int first = (int) parts[0].charAt(0);
                int second = (int) parts[1].charAt(0);
                setRemove(first, second);
                return;
            }
            else{
                throw new InvalidFormatException(REMOVE_INCORRECT);
            }
        }
        // space
        if(command.equals(SPACE_ARG)){
            char c = SPACE.charAt(0);
            charMatcher.removeChar(c);
        }
        // else
        else{
            throw new InvalidFormatException(REMOVE_INCORRECT);
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
        HashSet<Character> charSet = charMatcher.getCharSet();
        if(charSet.isEmpty()){
            return;
        }
        for(Character c: charSet){
            System.out.println(c + SPACE);
        }
        System.out.println();
//        TreeMap<Double, Character> charTreeMap = charMatcher.getCharBrightnessMap();
//       HashMap<Character, Double> charTreeMap = charMatcher.getCharBrightnessMap();
//        if(charTreeMap.isEmpty()){
//            return;
//        }
//        for(Character c: charTreeMap.keySet()){
//            System.out.println(c + SPACE);
//        }
//        System.out.println();
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
    private void displayForUser(String command) throws InvalidFormatException {
        if(command.equals(EMPTY)){
            throw new InvalidFormatException(OUTPUT_ERROR);
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
            throw new InvalidFormatException(OUTPUT_ERROR);
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
    private void roundCommand(String subCommand) throws InvalidFormatException {
        switch (subCommand){
            case UP:
                roundingMode = RoundingMode.UP;
                break;
            case DOWN:
                roundingMode = RoundingMode.DOWN;
                break;
            case ABS:
                roundingMode = RoundingMode.ABS;
                break;
            default:
                throw new InvalidFormatException(ROUND_ERR_MESSAGE);
        }
    }

    /**
     * On command this method will run the asciiArtAlgorithm to get the board
     * ready for the output
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    private void runAlgorithm(){
        this.asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution,
                imageProcessor, charMatcher, roundingMode);
        TreeMap<Character, Double> charTreeMap = charMatcher.getCharBrightnessMap();
//        HashMap<Character, Double> charTreeMap = charMatcher.getCharBrightnessMap();
        if(charTreeMap.size() <= MIN_CHARS){
            throw new TooFewCharactersException(MIN_CHARS_ERROR);
        }
        char[][] board = asciiArtAlgorithm.run();
        currentOutput.out(board);
    }

    public static void main (String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println(MAIN_ERROR);
            return;
        }

        try {
            Shell shell = new Shell(args[0]);
            shell.run(shell.imagePath);
        } catch (
                OutOfBoundsException |
                 TooFewCharactersException |
                 IOException e) {
            System.out.println(e.getMessage());

        }
    }
}



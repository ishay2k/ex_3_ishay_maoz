package image_char_matching;

import ascii_art.RoundingMode;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.Map;

/**
 * This class manages a set of characters and maps each one to a normalized brightness
 * value based on its visual representation in a 16x16 pixel grid
 * Supports adding and removing characters from the set
 * @author Ishay Shaul
 * @author Maoz Bar Shimon
 * @see ascii_art.AsciiArtAlgorithm
 */
public class SubImgCharMatcher {
    /** to conclude the brightness of the char, we divide it to this number of sub images.*/
    private static final int PIXEL_SIZE = 16;

    /** A set of all the characters in play.*/
    private final HashSet<Character> charSet;

    /** Mapping each character to its brightness level.*/
    private final TreeMap<Character, Double> charBrightnessMap;

    /** Maximum brightness between current characters .*/
    private double maxBrightness;

    /** minimum brightness between current characters.*/
    private double minBrightness;

    private final TreeMap<Character, Double> originalBrightnessMap = new TreeMap<>();


    /**
     * Constructs a class whose priority is being in charge of the characters and their
     * brightness
     * @param charset An array of chars that the class starts with
     */
    public SubImgCharMatcher(char[] charset){
        this.charSet = new HashSet<Character>();
        this.charBrightnessMap = new TreeMap<>();
        for(char c : charset){
            addChar(c);
        }
        setMaximumAndMinimumBrightness();
        normalizeBrightness();
    }

    /**
     * Removing a certain character from the map and hashset
     * @param c The character being removed
     */
    public void removeChar(char c){
        if (charSet.contains(c)) {
            originalBrightnessMap.remove(c);
            charBrightnessMap.remove(c);
            charSet.remove(c);
            setMaximumAndMinimumBrightness();
            normalizeBrightness();
        }
    }

    /**
     * Adds a character to the map and set
     * @param c The character that is being added
     */
    public void addChar(char c){
        if (!charSet.contains(c)) {
            charSet.add(c);
            double cBrightness = calculateBrightness(c);
            originalBrightnessMap.put(c, cBrightness);
            charBrightnessMap.put(c, cBrightness);
            setMaximumAndMinimumBrightness();
            normalizeBrightness();
        }
    }


    /**
     * We receive a brightness and search for the character with the brightness closest
     * to the input brightness, using the roundingMode formula.
     * @param brightness   The brightness which is sought after
     * @param roundingMode The formula deciding which is closest
     * @return             The character with the closest brightness
     */
    public char getCharByImageBrightness(double brightness, RoundingMode roundingMode){
        char selectedChar = 0;
        double bestDiff = Double.MAX_VALUE;
        boolean found = false;

        for(Map.Entry<Character, Double> entry : charBrightnessMap.entrySet()){
            double charBrightness = entry.getValue();
            char c = entry.getKey();

            switch (roundingMode) {
                case ABS:
                    double absDiff = Math.abs(charBrightness - brightness);
                    if (absDiff < bestDiff) {
                        bestDiff = absDiff;
                        selectedChar = c;
                    }
                    break;

                case UP:
                    if (charBrightness >= brightness) {
                        if (!found || charBrightness < bestDiff) {
                            bestDiff = charBrightness;
                            selectedChar = c;
                            found = true;
                        }
                    }
                    break;

                case DOWN:
                    if (charBrightness <= brightness) {
                        if (!found || charBrightness > bestDiff) {
                            bestDiff = charBrightness;
                            selectedChar = c;
                            found = true;
                        }
                    }
                    break;
            }
        }
        if (!found && roundingMode != RoundingMode.ABS) {
            return getCharByImageBrightness(brightness, RoundingMode.ABS);
        }
        return selectedChar;
    }

    /**
     * calculates brightness for a specific character. we turn the character into a 16x16
     * matrix, where some are false, and some true. brightness = sum true / (16*16)
     * @param c the character whose brightness is being calculated
     * @return  the brightness of character c
     *
     */
    private double calculateBrightness(Character c){
        boolean[][] miniMatrix = CharConverter.convertToBoolArray(c);
        int numTrue = 0;
        for(int i = 0; i < miniMatrix.length; i++) {
            for (int j = 0; j < miniMatrix[i].length; j++) {
                if (miniMatrix[i][j]) {
                    numTrue++;
                }
            }
        }
        return (double) numTrue / (PIXEL_SIZE * PIXEL_SIZE);
    }

    /**
     * Concludes the maximum and minimum brightness and sets them.
     * This method will come before the normalization
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    private void setMaximumAndMinimumBrightness(){
        double max = 0;
        double min = 1;
        for(Double brightness: originalBrightnessMap.values()){ //maoz

                if(brightness > max){
                max = brightness;
            }
            if(brightness < min){
                min = brightness;
            }
        }
        this.maxBrightness = max;
        this.minBrightness = min;
    }

    /**
     * Normalizes the brightness for each character.
     *
     * Formula used:
     * normalize = (current brightness - min brightness) / (max brightness - min brightness)
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    private void normalizeBrightness(){
        charBrightnessMap.clear();
        double denominator = maxBrightness - minBrightness;

        for(Map.Entry<Character, Double> entry : originalBrightnessMap.entrySet()){
            double normalized = (entry.getValue() - minBrightness) / denominator;
            charBrightnessMap.put(entry.getKey(), normalized);
        }
    }

    /**
     * getter for the map
     * @return the map of characters and their brightness level
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    public TreeMap<Character, Double> getCharBrightnessMap(){
        return charBrightnessMap;
    }

    /**
     * getter for the hash table containing the characters
     * @return the hash set
     */
    public HashSet<Character> getCharSet(){
        return charSet;
    }
}



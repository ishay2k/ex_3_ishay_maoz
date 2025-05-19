package image_char_matching;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.Map;
import java.util.TreeSet;


public class SubImgCharMatcher {
    private static final int PIXEL_SIZE = 16;


    private HashSet<Character> charSet;
    private TreeMap<Character, Double> charBrightnessMap;
    private double maxBrightness;
    private double minBrightness;


    public SubImgCharMatcher(char[] charset){
        this.charSet = new HashSet<Character>();
        this.charBrightnessMap = new TreeMap<>();
        for(char c : charset){
            addChar(c);
        }
        setMaximumAndMinimumBrightness();
        normalizeBrightness();
    }

    public void removeChar(char c){

        charBrightnessMap.remove(c);
        setMaximumAndMinimumBrightness();
        normalizeBrightness();
    }

    public void addChar(char c){
        this.charSet.add(c);
        double cBrightness = calculateBrightness(c);
        charBrightnessMap.put(c, cBrightness);
//        setMaximumAndMinimumBrightness();
//        normalizeBrightness();
    }


    /**
     * This method receives a certain brightness and returns the character with the closest brightness
     * @param brightness The parameter which will be used to measure which character is closest
     * @return           The character with the closest brightness to the input
     */
    public char getCharByImageBrightness(double brightness){
        double diff = Math.abs(brightness - charBrightnessMap.firstEntry().getValue());
        char curr = charBrightnessMap.firstKey();
        for(Map.Entry<Character, Double> entry : charBrightnessMap.entrySet()){
            if(diff > Math.abs(entry.getValue() - brightness)){
                diff = Math.abs(entry.getValue() - brightness);
                curr = entry.getKey();
            }
        }
        return curr;
    }

    /**
     * calculates brightness for a specific character. we turn the character into a 16x16
     * matrix, where some are false, and some negative. brightness = sum true / (16*16)
     * @param c the character for which we are calculating the brightness
     * @return  the brightness of character c
     */
    public double calculateBrightness(Character c){
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
     * sets the maximum and minimum brightness.
     * This method will come before the normalization
     * @author Ishay Shaul
     * @author Maoz Bar Shimon
     */
    public void setMaximumAndMinimumBrightness(){
        double max = 0;
        double min = 1;
        for(Double brightness: charBrightnessMap.values()){
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
     * normalizes every character's brightness.
     * normalize = (current brightness - min brightness) / (max brightness - min brightness)
     */
    public void normalizeBrightness(){
        double denominator = maxBrightness - minBrightness;
        for(Map.Entry<Character, Double> entry : charBrightnessMap.entrySet()){
            double charBrightness = entry.getValue();
            double normalized = (charBrightness - minBrightness) / denominator;
            entry.setValue(normalized);
        }
    }

    public TreeMap<Character, Double> getCharBrightnessMap(){
        return charBrightnessMap;
    }
}



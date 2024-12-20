package image_char_matching;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class CharMatcherHelper {
    private static final int SIXTEEN = 16;
    private static final int MAX_START_BRIGHTNESS = 0;
    private static final int MIN_START_BRIGHTNESS = 1;
    private static final int ROUND_UP = 1;
    private static final int ROUND_DOWN = 2;
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String ABS = "abs";
    private static final char MAX_ASCII = '~';
    private TreeMap<Character,Double> originalBrightnesses;
    private  TreeMap<Double,HashSet<Character>> brightnessAfterNormalisation;
    private int flagRound;
    private double maxBrightness;
    private double minBrightness;

    /**
     * constructor of helper
     * @param charset given set of chars
     */
    public CharMatcherHelper(char[] charset) {
        this.maxBrightness = MAX_START_BRIGHTNESS;
        this.minBrightness = MIN_START_BRIGHTNESS;
        this.originalBrightnesses = new TreeMap<>();
        buildOriginalBrightnessesTreeMap(charset);
        this.brightnessAfterNormalisation = new TreeMap<>();
        buildLinearisedTree();
    }

    /**
     *
     * @param brightness value of brightness
     * @return returns closest char by the current strategy
     *
     */
    public char getCharByImageBrightness(double brightness, int flagRound){
        Double lowerKey = this.brightnessAfterNormalisation.floorKey
                (brightness);
        Double higherKey = brightnessAfterNormalisation.ceilingKey
                (brightness);
        // need to check in team or forum if exception or check worst case
        if (lowerKey == null)
            return getLowestASCIIChar(this.brightnessAfterNormalisation.get
                    (higherKey));
        if (higherKey == null)
            return getLowestASCIIChar(this.brightnessAfterNormalisation.get
                    (lowerKey));
        switch (flagRound) {
            case ROUND_UP:
                return roundUp(higherKey);
            case ROUND_DOWN:
                return roundDown(lowerKey);
            default:
                return absoluteRound(lowerKey,higherKey,brightness);
        }
    }

    /**
     * adding char to tree map
     * @param c char we are adding
     */
    public void addChar(char c){
        if(this.originalBrightnesses.containsKey(c)){
            return;
        }
        double cBrightness = calculateBrightness(c);
        this.originalBrightnesses.put(c,cBrightness);
        if(updateMinMax(cBrightness)){
            this.brightnessAfterNormalisation.clear();
            buildLinearisedTree();
        }
        else{
            double newBrightness = linearNormalisedBrightness(cBrightness);
            this.brightnessAfterNormalisation.putIfAbsent
                    (newBrightness, new HashSet<>());
            this.brightnessAfterNormalisation.get(newBrightness).add(c);
        }
    }

    /**
     * remove char from tree map
     * @param c char we are removing
     */
    public void removeChar(char c){
        double brightness = calculateBrightness(c);
        if(!(this.originalBrightnesses.containsKey(c))){
            return;
        }
        this.originalBrightnesses.remove(c);
        if(this.originalBrightnesses.isEmpty()){
            freeTreeMap();
            return;
        }
        double newBrightness = linearNormalisedBrightness(brightness);
        this.brightnessAfterNormalisation.get(newBrightness).remove(c);
        if(this.brightnessAfterNormalisation.get(newBrightness).isEmpty()){
            this.brightnessAfterNormalisation.remove(newBrightness);
            if(brightness == this.maxBrightness){
                updateMax();
                this.brightnessAfterNormalisation.clear();
                buildLinearisedTree();
            }
            else if(brightness == this.minBrightness){
                updateMin();
                this.brightnessAfterNormalisation.clear();
                buildLinearisedTree();
            }
        }
    }

    /**
     * This function helps us to implement chars function in Shell
     * @return sorted chars array of originalBrightnesses
     */
    public char[] getCharSet(){
        char[] chars = new char[this.originalBrightnesses.size()];
        int index = 0;
        for(Character key : this.originalBrightnesses.keySet()) {
            chars[index] = key;
        }
        return chars;
    }

    // ------------------ private -----------------------------------

    // strategy of rounding up the num
    private char roundUp(Double higherKey){
        return getLowestASCIIChar(this.brightnessAfterNormalisation.get
                    (higherKey));
    }

    // strategy of rounding down the num
    private char roundDown(Double lowerKey){
        return getLowestASCIIChar(this.brightnessAfterNormalisation.get
                (lowerKey));
    }

    // strategy of rounding by absolute value
    private char absoluteRound(Double lowerKey, Double higherKey, double brightness){
        if (Math.abs(lowerKey - brightness) <= Math.abs(higherKey - brightness)){
            return getLowestASCIIChar(this.brightnessAfterNormalisation.get
                    (lowerKey));
        } else {
            return getLowestASCIIChar(this.brightnessAfterNormalisation.get
                    (higherKey));
        }
    }

    // returns char with the lowest ascii value in the set
    private char getLowestASCIIChar(Set<Character> charSet) {
        if (charSet == null || charSet.isEmpty()) {
            throw new IllegalArgumentException
                    ("Character set is empty. Cannot retrieve a character.");
        }
        char result = MAX_ASCII;
        for (Character c : charSet) {
            if (c < result) {
                result = c;
            }
        }
        return result;
    }

    // building original brightnesses Treemap
    private void buildOriginalBrightnessesTreeMap(char[] charset){
        for (char c : charset) {
            double currentBrightness = calculateBrightness(c);
            this.originalBrightnesses.put(c,currentBrightness);
            updateMinMax(currentBrightness);
        }
    }

    // calculates brightness value of certain char c
    private double calculateBrightness(char c) {
        boolean[][] arr = CharConverter.convertToBoolArray(c);
        int trueCounter = 0;
        for(int x = 0;x<SIXTEEN;x++ ){
            for(int y = 0;y<SIXTEEN;y++ ){
                if(arr[x][y]){
                    trueCounter++;
                }
            }
        }

        return (double) trueCounter /(SIXTEEN*SIXTEEN);
    }

    // check if a certain brightness bigger than max
    // or smaller than min. If it is then update it.
    private boolean updateMinMax(double brightness){
        boolean changed = false;
        if (brightness > this.maxBrightness) {
            this.maxBrightness = brightness;
            changed = true;
        }
        if (brightness < this.minBrightness) {
            this.minBrightness = brightness;
            changed = true;
        }
        return changed;
    }

    // build a hash tree map with linearised values of brightnesses
    private void buildLinearisedTree() {
        for (HashMap.Entry<Character, Double> entry :
                this.originalBrightnesses.entrySet()) {
            Double currentBrightness = entry.getValue();
            Double newBrightness = linearNormalisedBrightness
                    (currentBrightness);
            this.brightnessAfterNormalisation.putIfAbsent(newBrightness,
                    new HashSet<>());
            this.brightnessAfterNormalisation.get(newBrightness).add
                    (entry.getKey());
        }
    }

    // this function linearised the given brightness
    private double linearNormalisedBrightness(double brightness){
        double newBrightness = (brightness - this.minBrightness)
                / (this.maxBrightness - this.minBrightness);
        return newBrightness;
    }

    // update the value of max brightness
    private void updateMax(){
        this.maxBrightness = MAX_START_BRIGHTNESS;
        for (HashMap.Entry<Character, Double> entry : originalBrightnesses.entrySet()){
            if (entry.getValue() > this.maxBrightness) {
                this.maxBrightness = entry.getValue();
            }
        }
    }

    //update the value of min brightness
    private void updateMin(){
        this.minBrightness = MIN_START_BRIGHTNESS;
        for (HashMap.Entry<Character, Double> entry : originalBrightnesses.entrySet()){
            if (entry.getValue() < this.minBrightness) {
                this.minBrightness = entry.getValue();
            }
        }
    }

    //free the TreeMap
    private void freeTreeMap(){
        this.brightnessAfterNormalisation.clear();
        this.minBrightness = MIN_START_BRIGHTNESS;
        this.maxBrightness = MAX_START_BRIGHTNESS;
    }
}

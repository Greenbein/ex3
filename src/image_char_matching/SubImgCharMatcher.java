package image_char_matching;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.HashSet;

public class SubImgCharMatcher {
    private static final int SIXTEEN = 16;
    private static final int MAX_START_BRIGHTNESS = 0;
    private static final int MIN_START_BRIGHTNESS = 1;
    private  TreeMap<Double,HashSet<Character>> brightnessAfterNormalisation;
    private  TreeMap<Character,Double> originalBrightnesses;
    private double maxBrightness;
    private double minBrightness;

    /**
     * constructor
     * @param charset set of chars we would use
     */

    public SubImgCharMatcher(char[] charset){
        this.maxBrightness = MAX_START_BRIGHTNESS;
        this.minBrightness = MIN_START_BRIGHTNESS;
        this.originalBrightnesses = new TreeMap<>();
        buildOriginalBrightnessesMap(charset);
        this.brightnessAfterNormalisation = new TreeMap<>();
        buildLinearisedTree();
    }


    /**
     *
     * @param brightness value of brightness
     * @return char with the closest brightness value and with the smallest
     * ascii value
     */
    public char getCharByImageBrightness(double brightness){
        Double lowerKey = this.brightnessAfterNormalisation.floorKey
                (brightness);
        Double higherKey = brightnessAfterNormalisation.ceilingKey
                (brightness);
        if (lowerKey == null)
            return getLowestASCIIChar(this.brightnessAfterNormalisation.get
                    (higherKey));
        if (higherKey == null)
            return getLowestASCIIChar(this.brightnessAfterNormalisation.get
                    (lowerKey));
        if (Math.abs(lowerKey - brightness) <= Math.abs(higherKey - brightness)){
            return getLowestASCIIChar(this.brightnessAfterNormalisation.get
                    (lowerKey));
        } else {
            return getLowestASCIIChar(this.brightnessAfterNormalisation.get
                    (higherKey));
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

    // Here we expand the class's API
    // should not be here not part of the api
    public char[] getCharSet(){
        char[] chars = new char[this.originalBrightnesses.size()];
        int index = 0;
        for(Character key : this.originalBrightnesses.keySet()) {
            chars[index] = key;
        }
        return chars;

    }




    //---------------------------private helper functions-------------------------------

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

    // returns char with the lowest ascii value in the set
    private char getLowestASCIIChar(Set<Character> charSet) {
        if (charSet == null || charSet.isEmpty()) {
            throw new IllegalArgumentException
                    ("Character set is empty. Cannot retrieve a character.");
        }
        char result = '~';
        for (Character c : charSet) {
            if (c < result) {
                result = c;
            }
        }
        return result;
    }

    // building original brightnesses Map
    private void buildOriginalBrightnessesMap(char[] charset){
        for (char c : charset) {
            double currentBrightness = calculateBrightness(c);
            this.originalBrightnesses.put(c,currentBrightness);
            updateMinMax(currentBrightness);
        }
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

//    public void printTreeMap(TreeMap<Double, HashSet<Character>> treeMap) {
//        if (treeMap == null || treeMap.isEmpty()) {
//            System.out.println("The TreeMap is empty.");
//            return;
//        }
//
//        System.out.println("TreeMap Content:");
//        System.out.println("================");
//
//        for (HashMap.Entry<Double, HashSet<Character>> entry : treeMap.entrySet()) {
//            Double key = entry.getKey();
//            HashSet<Character> valueSet = entry.getValue();
//
//            System.out.print("Key: " + key + " -> Values: { ");
//            for (Character c : valueSet) {
//                System.out.print(c + " ");
//            }
//            System.out.println("}");
//        }
//    }
//
//    public void printHashMap(){
//        for (HashMap.Entry<Character, Double> entry : originalBrightnesses.entrySet()) {
//            System.out.printf("Character: '%c' -> Brightness: %.4f%n", entry.getKey(), entry.getValue());
//        }
//    }


}

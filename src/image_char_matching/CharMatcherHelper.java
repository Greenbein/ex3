package image_char_matching;

public class CharMatcherHelper {
    private SubImgCharMatcher matcher;
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


}

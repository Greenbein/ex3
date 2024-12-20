package image_char_matching;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.HashSet;

public class SubImgCharMatcher {
    // check line 9 later
    private final CharMatcherHelper charMatcherHelper;
    private static final int ROUND_ABS = 0;

    /**
     * constructor
     * @param charset set of chars we would use
     */
    public SubImgCharMatcher(char[] charset){
        charMatcherHelper = new CharMatcherHelper(charset);
    }

    /**
     *
     * @param brightness value of brightness
     * @return char with the closest brightness value and with the smallest
     * ascii value
     */
    public char getCharByImageBrightness(double brightness){
        return charMatcherHelper.getCharByImageBrightness(brightness, ROUND_ABS);
    }

    /**
     * adding char to tree map
     * @param c char we are adding
     */
    public void addChar(char c){
        charMatcherHelper.addChar(c);
    }

    /**
     * remove char from tree map
     * @param c char we are removing
     */
    public void removeChar(char c){
        charMatcherHelper.removeChar(c);
    }

    // ------------------ API extension ------------------
    /**
     * This function helps us to implement chars function in Shell
     * @return sorted chars array of originalBrightnesses
     */
    public char[] getCharSet(){
        return charMatcherHelper.getCharSet();
    }

}

package ascii_art;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageProcessing;
import image_char_matching.SubImgCharMatcher;
import java.awt.*;
import java.io.IOException;

public class Shell {
    //Default parameters for AsciiAlgorithm constructor
    private static final char[] DEFAULT_CHARSET =
            {'0','1','2','3','4','5','6','7','8','9'};
    private static final int DEFAULT_RESOLUTION = 2;
    private static final int MIN_ASCII_INDEX = 32;
    private static final int MAX_ASCII_INDEX = 127;
    private static final int RESOLUTION_FACTOR = 2;
    private static final int ROUND_ABS = 0;
    private static final int ROUND_UP = 1;
    private static final int ROUND_DOWN = 2;
    private static final int MIN_CHARSET_LENGTH = 2;

    //separators
    private static final String INPUT_SEPARATOR = " ";
    private static final String PRINT_SEPARATOR = " ";
    private static final char CHAR_SPACE = ' ';
    private static final char CHAR_RANGE_SEPARATOR = '-';


    //Commands in terminal
    private static final String EXIT = "exit";
    private static final String CHARS = "chars";
    private static final String ADD = "add";
    private static final String START_COMMAND = ">>> ";
    private static final String REMOVE = "remove";
    private static final String RES = "res";
    private static final String OUTPUT = "output";
    private static final String ROUND = "round";
    private static final String ASCII_ART = "asciiArt";

    //add subcommands
    private static final String ALL = "all";
    private static final String SPACE = "space";

    //res and round subcommands
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String ABS = "abs";

    //output subcommands
    private static final String HTML = "html";
    private static final String CONSOLE = "console";

    //additional constants for output
    private static final String FILENAME = "out.html";
    private static final String FONT_NAME = "New Courier";

    //Error notifications
    private static final String INCORRECT_COMMAND = "Did not execute due to incorrect command.";
    private static final String INCORRECT_FORMAT = "Did not add due to incorrect format.";
    private static final String SMALL_CHARSET = "Did not execute. Charset is too small.";



    // the array that contains chars that represent the output for the image,
    // current resolution and current charSet in the matcher
    private char[][] currentCharImage;
    private final SubImgCharMatcher matcher;
    private int maxResolution;
    private int minResolution;
    private Color[][] imgArr;
    private int currentResolution;
    private String output;

    /**
     * Constructor os Shell
     */
    public Shell() {
        this.matcher = new SubImgCharMatcher(DEFAULT_CHARSET);
        this.currentResolution = DEFAULT_RESOLUTION;
        this.output = CONSOLE;
    }

    //
    public void run(String imageName)throws IOException{
        prepareBeforeRun(imageName);
        boolean ok = true;
        while(ok){
            System.out.print(START_COMMAND);
            String input = KeyboardInput.readLine();
            String[] words = input.split(INPUT_SEPARATOR);
            String firstWord = words[0];
            switch(firstWord){
                case EXIT:
                    ok = false;
                    break;
                case CHARS:
                    printAllChars();
                    break;
                case ADD:
                    add(words);
                    break;
                case REMOVE:
                    remove(words);
                    break;
                case RES:
                    res(words);
                    break;
                case ROUND:
                    round(words);
                    break;
                case OUTPUT:
                    output(words);
                    break;
                case ASCII_ART:
                    asciiArt();
                    break;
                default:
                    System.out.println(INCORRECT_COMMAND);
            }
        }

    }

    //
    private void prepareBeforeRun(String imageName) throws IOException {
        Image image;
        try{
            image = new Image(imageName);
            ImageProcessing ip = new ImageProcessing();
            this.imgArr = ip.convertImageToColorArr(image);
            AsciiArtAlgorithm a =  new AsciiArtAlgorithm(this.imgArr,
                    DEFAULT_RESOLUTION, DEFAULT_CHARSET);
            this.currentCharImage = a.run();
            this.maxResolution = image.getWidth();
            this.minResolution = Math.max(1,
                    (image.getWidth()/image.getHeight()));
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    //----------------------------------ADD------------------------------------------
    // use this function in the case of add command
    private void add(String[] inputWords){
        if(inputWords.length >= 2){
            processAddCommand(inputWords[1]);
        }
        else{
            System.out.println(INCORRECT_FORMAT);
        }
    }

    //add new items to matcher's char treeMap, according to add command type
    private void processAddCommand(String secondWord){
        if(secondWord.length() == 1){
            char c = secondWord.charAt(0);
            addChar(c);
        }
        else if(secondWord.equals(ALL)){
            addAllChars();
        }
        else if(secondWord.equals(SPACE)){
            addSpace();
        }
        else if(isRange(secondWord)){
            addRange(secondWord);
        }
        else{
            System.out.println(INCORRECT_FORMAT);
        }
    }

    //add new char to HashMap of characters in the matcher
    private void addChar(char c){
       if(c<MIN_ASCII_INDEX ||c>MAX_ASCII_INDEX-1){
           System.out.println(INCORRECT_FORMAT);
       }
       matcher.addChar(c);
    }

    // add all chars from ' ' to '~' to HashMap of characters in the matcher
    private void addAllChars(){
        for(int i = MIN_ASCII_INDEX; i<MAX_ASCII_INDEX;i++){
            matcher.addChar((char)i);
        }
    }

    // add ' ' to HashMap of characters in the matcher
    private void addSpace(){
        matcher.addChar(CHAR_SPACE);
    }

    //check if the second word after 'add' is a range of chars
    private boolean isRange(String str){
        if(str.length()==3){
            if(str.charAt(1)==CHAR_RANGE_SEPARATOR){
                char first = str.charAt(0);
                char second = str.charAt(1);
                boolean firstOk = first>MIN_ASCII_INDEX-1 && first<MAX_ASCII_INDEX;
                boolean secondOk = second>MIN_ASCII_INDEX-1&& second<MAX_ASCII_INDEX;
                if(firstOk && secondOk) {
                    return true;
                }
            }
        }
        return false;
    }

    // add all chars form the first char in word (word[0]) to the last char (word[2])
    private void addRange(String word){
        int cMin = word.charAt(0);
        int cMax = word.charAt(2);
        if(cMin > cMax){
            int temp = cMin;
            cMin = cMax;
            cMax = temp;
        }
        for(int c = cMin; c<=cMax; c++){
            matcher.addChar((char)c);
        }
    }

    //---------------------------------REMOVE----------------------------------------
    private void remove(String[] inputWords){
        if(inputWords[1].length() == 1){
            char c = inputWords[1].charAt(0);
            matcher.removeChar(c);
        }
    }
    //----------------------------------CHARS------------------------------------------
    //print all chars from the matcher
    private void printAllChars(){
        for (Character key : this.matcher.getCharSet()) {
            System.out.print(key + PRINT_SEPARATOR);
        }
        System.out.println();
    }

    //----------------------------------RES------------------------------------------
    // the function processes the input for res command
    private void res(String[] words){
        if(words.length == 1){
            printResolution();
            return;
        }
        if(words[1].equals(UP)){
            if(this.currentResolution*RESOLUTION_FACTOR<=maxResolution){
                this.currentResolution *= RESOLUTION_FACTOR;
                printResolution();
            }
            return;
        }
        if(words[1].equals(DOWN)){
            if(this.currentResolution/RESOLUTION_FACTOR>=minResolution){
                this.currentResolution /= RESOLUTION_FACTOR;
                printResolution();
            }
            return;
        }
        System.out.println(INCORRECT_FORMAT);
    }

    //print current resolution
    private void printResolution(){
        System.out.println("Resolution set to"+this.currentResolution+".");
    }

    //----------------------------------OUTPUT------------------------------------------
    private void output(String[]words){
        if(words.length >= 2){
            if(words[1].equals(HTML)){
                this.output = HTML;
            }
            else if(words[1].equals(CONSOLE)){
                this.output = CONSOLE;
            }
            else{
                System.out.println(INCORRECT_FORMAT);
            }
        }
        System.out.println(INCORRECT_FORMAT);
    }

    //----------------------------------ROUND-------------------------------------------
    private void round(String[] words){
       if(words.length >= 2){
           switch (words[1]){
               case ABS:
                   matcher.setRoundFlag(ROUND_ABS);
                   break;
               case UP:
                   matcher.setRoundFlag(ROUND_UP);
                   break;
               case DOWN:
                   matcher.setRoundFlag(ROUND_DOWN);
                   break;
               default:
                   System.out.println(INCORRECT_FORMAT);
                   break;
           }
       }
       System.out.println(INCORRECT_FORMAT);
    }

    //----------------------------------ASCII------------------------------------------
    private void asciiArt(){
        char[] charSet = this.matcher.getCharSet();
        if(charSet.length<MIN_CHARSET_LENGTH){
            System.out.println(SMALL_CHARSET);
            return;
        }
        AsciiArtAlgorithm ascii = new AsciiArtAlgorithm(this.imgArr,this.currentResolution,charSet);
        this.currentCharImage = ascii.run();
        switch (this.output){
            case HTML:
                HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput(FILENAME,FONT_NAME);
                htmlAsciiOutput.out(this.currentCharImage);
                break;
            default:
                ConsoleAsciiOutput consoleAsciiOutput = new ConsoleAsciiOutput();
                consoleAsciiOutput.out(this.currentCharImage);
                break;
        }
    }

    public static void main(String[] args) throws IOException {
        String givenImageName = args[0];
        Shell myShell = new Shell();
        myShell.run(givenImageName);
    }
}

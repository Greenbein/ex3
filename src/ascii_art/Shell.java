package ascii_art;

import image.Image;
import image.ImageProcessing;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
// rtattatatttatatatattatat

public class Shell {
    private static final char[] DEFAULT_CHARSET =
            {'0','1','2','3','4','5','6','7','8','9'};
    private static final int DEFAULT_RESOLUTION = 2;

    // the array that contains chars that represent the output for the image,
    // current resolution and current charSet in the matcher
    private char[][] currentOutput;

    private SubImgCharMatcher matcher;
    private int maxResolution;
    private int minResolution;
    private Color[][] imgArr;
    private int currentResolution;
    public Shell() {
        char [] charset = DEFAULT_CHARSET;
        this.matcher = new SubImgCharMatcher(charset);
        this.currentResolution = DEFAULT_RESOLUTION;
    }

    public void run(String imageName)throws IOException{
        Image image;
        try{
            image = new Image(imageName);
            ImageProcessing ip = new ImageProcessing();
            this.imgArr = ip.convertImageToColorArr(image);
            AsciiArtAlgorithm a =  new AsciiArtAlgorithm(this.imgArr,
                    DEFAULT_RESOLUTION, DEFAULT_CHARSET);
            this.currentOutput = a.run();
            this.maxResolution = image.getWidth();
            this.minResolution = Math.max(1,
                    (image.getWidth()/image.getHeight()));
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

        while(true){
            System.out.print(">>> ");
            String input = KeyboardInput.readLine();
            String[] words = input.split(" ");

            String firstWord = words[0];

            if(firstWord.equals("exit")){
                break;
            }
            else if(firstWord.equals("chars")){
                for (char key : this.matcher.getCharSet()) {
                    System.out.print(key + " " );
                }
                System.out.println();
            }
            else if(firstWord.equals("add")){
                if(words.length >= 2){
                    words = new String[]{words[0],words[1]};
                    processAddCommand(words[1]);
                }
                else{
                    System.out.println("Did not add due to incorrect format.");
                }
            }
            else if(firstWord.equals("remove")){
                if(words[1].length() == 1){
                    char c = words[1].charAt(0);
                    matcher.removeChar(c);
                }
            }
            else if(firstWord.equals("res")){
                res(words);
            }
            else{
                System.out.println("Did not execute due to incorrect command.");
            }
        }

    }

    private void processAddCommand(String secondWord){
        if(secondWord.length() == 1){
            char c = secondWord.charAt(0);
            addChar(c);
        }
        else if(secondWord.equals("all")){
            addAllChars();
        }
        else if(secondWord.equals("space")){
            addSpace();
        }
        else if(isRange(secondWord)){
            addRange(secondWord);
        }
        else{
            System.out.println("Did not add due to incorrect format.");
        }
    }

    //add new char to HashMap of characters in the matcher
    private void addChar(char c){
       if((int)c<32 ||(int)c>126){
           System.out.println("Did not add due to incorrect format.");
       }
       matcher.addChar(c);
    }
    // add all chars from ' ' to '~' to HashMap of characters in the matcher
    private void addAllChars(){
        for(int i = 32; i<127;i++){
            matcher.addChar((char)i);
        }
    }

    // add ' ' to HashMap of characters in the matcher
    private void addSpace(){
        matcher.addChar(' ');
    }

    //check if the second word after 'add' is a range of chars
    private boolean isRange(String str){
        if(str.length()==3){
            if(str.charAt(1)=='-'){
                char first = str.charAt(0);
                char second = str.charAt(1);
                boolean firstOk = (int)first>31 && (int)first<127;
                boolean secondOk = (int)second>31 && (int)second<127;
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

   // the function processes the input for res command
    private void res(String[] words){
        if(words.length == 1){
            System.out.println("Resolution set to"+this.currentResolution+".");
            return;
        }
        if(words[1].equals("up")){
            if(this.currentResolution*2<=maxResolution){
                this.currentResolution *= 2;
                AsciiArtAlgorithm resUp =  new AsciiArtAlgorithm(this.imgArr,
                        this.currentResolution,this.matcher.getCharSet());
                this.currentOutput = resUp.run();
                System.out.println("Resolution set to"+this.currentResolution+".");
            }
            return;
        }
        if(words[1].equals("down")){
            if(this.currentResolution/2>=minResolution){
                this.currentResolution /= 2;
                AsciiArtAlgorithm resDown =  new AsciiArtAlgorithm(this.imgArr,
                        this.currentResolution,this.matcher.getCharSet());
                this.currentOutput = resDown.run();
                System.out.println("Resolution set to"+this.currentResolution+".");
            }
            return;
        }
         System.out.println("Did not change resolution due to incorrect format.");
    }


    public static void main(String[] args) throws IOException {
        String givenImageName = args[0];
        Shell myShell = new Shell();
        myShell.run(givenImageName);
    }

}

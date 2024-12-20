package ascii_art;

import image.ImageProcessing;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;

public class AsciiArtAlgorithm {
    private Color[][]image;
    private int resolution;
    private char[] charset;

    /**
     * constructor
     * @param image 2d array of colors
     * @param resolution number of squares in a row
     * @param charset given set of chars
     */
    public AsciiArtAlgorithm(Color[][]image, int resolution, char[] charset) {
        this.image = image;
        this.resolution = resolution;
        this.charset = charset;
    }

    /**
     * function that implements ascii algorithm
     * @return array of chars that represents brightnesses according
     * to the algorithm
     */
    public char [][] run(){
        ImageProcessing ip = new ImageProcessing();
        SubImgCharMatcher sim = new SubImgCharMatcher(this.charset);
        this.image = ip.imagePadding(this.image);
        Color[][][] subImages = ip.createSubImagesArray(this.image,
                this.resolution);
        char[] tempResult = new char[subImages.length];
        for(int i = 0; i < subImages.length; i++) {
            double currentBrightness = ip.calculateImageBrightness(subImages[i]);
            char currentChar = sim.getCharByImageBrightness(currentBrightness);
            tempResult[i] = currentChar;
        }
        return convertToDoubleDimension(tempResult,this.resolution);
    }

    // convert one dimension array back to two dimension array
    private char[][] convertToDoubleDimension(char[] tempResult, int resolution){
        int rows = tempResult.length / resolution;
        char [][] charSet = new char[rows][resolution];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < resolution; j++){
                charSet[i][j] = tempResult[i*resolution+j];
            }
        }
        return charSet;
    }
}

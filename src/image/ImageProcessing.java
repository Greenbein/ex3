package image;

import java.awt.*;

public class ImageProcessing {
    private static final int POWER_OF_TWO = 2;
    private static final double FIRST_COEFFICIENT = 0.2126;
    private static final double SECOND_COEFFICIENT = 0.7152;
    private static final double THIRD_COEFFICIENT = 0.0722;
    private static final double MAX_COLOR_VALUE = 255;
    public ImageProcessing() {}
    /**
     * creates padded image
     * @param image image before padding
     * @return new padded image
     */
    public Color[][]imagePadding(Color[][]image){
        if(image == null) return null;
        int originalRowsNumber = image.length;
        int originalColsNumber = image[0].length;
        int newRowsNumber = pixelsAmountAfterPadding(originalRowsNumber);
        int newColumnsNumber = pixelsAmountAfterPadding(originalColsNumber);
        if(originalRowsNumber != newRowsNumber ||
                originalColsNumber != newColumnsNumber){
            Color[][] newImage =
                    createWhiteImage(newRowsNumber, newColumnsNumber);
            for(int i = 0; i < image.length; i++){
                insertOriginalColors(newImage[i],image[i]);
            }
            return newImage;
        }
        return image;
    }

    /**
     *
     * @param image 2D array of colors
     * @param resolution sub image resolution
     * @return array of all the sub images
     */
    public Color[][][] createSubImagesArray(Color[][]image, int resolution){
        int squareWidth = image[0].length/resolution;
        int anmountOfSquaesInColumns = image.length/squareWidth;
        Color [][][] subImageArray = new Color
                [anmountOfSquaesInColumns*resolution][squareWidth][squareWidth];
        int pictureIndex = 0;
        for(int row = 0; row < image.length; row += squareWidth){
            for(int col = 0; col < image[0].length; col += squareWidth){
                subImageArray[pictureIndex] = getSubImage(image,row,col,squareWidth);
                pictureIndex++;
            }
        }
        return subImageArray;
    }

    /**
     *
     * @param image - 2D array of pixels (Color objects)
     * @return normalized average grey shade of the image
     */
    public double calculateImageBrightness(Color[][] image){
        int rowsAmount = image.length;
        int colsAmount = image[0].length;
        double[] greyPixels = new double[rowsAmount*colsAmount];

        for(int i = 0; i < rowsAmount; i++){
            for(int j = 0; j < colsAmount; j++){
                greyPixels[i*colsAmount+j] = getGrayPixel(image[i][j]);
            }
        }
        double sum = 0;
        for (double greyPixel : greyPixels) {
            sum += greyPixel;
        }
        double average = sum / greyPixels.length;
        return average/MAX_COLOR_VALUE;
    }

    //===============private helper functions============================

    // helpers for imagePaddingMethod

    // calculates amount of pixels after padding
    private int pixelsAmountAfterPadding(int pixelsAmount) {
        int padeddPixelsNumber = POWER_OF_TWO;
        while(pixelsAmount > padeddPixelsNumber) {
            padeddPixelsNumber *= POWER_OF_TWO;
        }
        return padeddPixelsNumber;
    }

    // inserts original colors into each white row that relevant
    private void insertOriginalColors(Color[]newRow, Color[]originalRow){
        for(int i=0; i<originalRow.length; i++){
            newRow[i] = originalRow[i];
        }
    }

    // creates white image with the size of padded values
    private Color[][] createWhiteImage(int rows, int cols){
        Color[][] whiteImage = new Color[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                whiteImage[i][j] = Color.WHITE;
            }
        }
        return whiteImage;
    }

    //helpers for createSubImagesArray

    //extract a sub image from the original one
    private Color[][] getSubImage(Color[][]image,int rowPosition, int colPosition,
                                  int squareWidth){
        Color[][] subImage = new Color[squareWidth][squareWidth];
        for(int i = 0; i < squareWidth; i++){
            for(int j = 0; j < squareWidth; j++){
                subImage[i][j] = image[rowPosition+i][colPosition+j];
            }
        }
        return subImage;
    }

    //helpers for calculateImageBrightness
    private double getGrayPixel(Color color){
        return color.getRed() * FIRST_COEFFICIENT
                + color.getGreen() * SECOND_COEFFICIENT
                + color.getBlue() * THIRD_COEFFICIENT;
    }

    ///orbhehewonvnvjeqnipnef
    /// CHECK WIDTH AND HEIGHT PROBLEM
    public Color[][] convertImageToColorArr(Image image){
        Color[][] picture = new Color[image.getWidth()][image.getHeight()];
        for (int row = 0; row <image.getWidth() ; row++) {
          for (int col = 0; col < image.getHeight(); col++) {
              picture[row][col] = image.getPixel(row, col);
          }
        }
        return picture;
    }

    //print sub images array
//    public void printSubImages(Color[][][]subImagesArray, int squareWidth){
//        int numSquares = subImagesArray.length;
//        int squaresPerRow = (int) Math.sqrt(numSquares); // Assuming it's a perfect square layout
//        if (squaresPerRow * squaresPerRow != numSquares) {
//            System.out.println("Squares are not arranged in a perfect grid.");
//            return;
//        }
//
//        for (int rowBlock = 0; rowBlock < squaresPerRow; rowBlock++) {
//            for (int subRow = 0; subRow < squareWidth; subRow++) { // Each sub-row of the square
//                for (int squareInRow = 0; squareInRow < squaresPerRow; squareInRow++) {
//                    int squareIndex = rowBlock * squaresPerRow + squareInRow;
//                    for (int col = 0; col < squareWidth; col++) {
//                        Color pixel = subImagesArray[squareIndex][subRow][col];
//                        if (pixel != null) {
//                            System.out.print(getColorChar(pixel));
//                        } else {
//                            System.out.print(" ");
//                        }
//                    }
//                    System.out.print("        "); // Space between squares
//                }
//                System.out.println();
//                // End of sub-row
//            }
//            System.out.println();
//            System.out.println();
//            System.out.println();
//        }
//    }

//    private char getColorChar(Color color) {
//        int brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
//        if (brightness == 255) {
//            return 'w';
//        } else{
//            return 'b';
//        }
//    }

}

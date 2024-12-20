//import ascii_art.AsciiArtAlgorithm;
//import image.Image;
//
//import java.awt.*;
//import java.io.IOException;
//
//public class MainTest {
//    public static void main(String[] args) throws IOException {
//      Image image = new Image("C:\\Users\\User\\Documents\\OOP_projects\\ex3\\examples\\board.jpeg");
//      int width = image.getWidth();
//      int height = image.getHeight();
//      Color[][] picture = new Color[width][height];
//      char [] charset = {'m','o'};
//      for (int row = 0; row < width; row++) {
//          for (int col = 0; col < height; col++) {
//              picture[row][col] = image.getPixel(row, col);
//          }
//      }
//      AsciiArtAlgorithm a = new AsciiArtAlgorithm(picture,2, charset);
//      char[][] result = a.run();
//      for (int row = 0; row < result.length; row++) {
//          for (int col = 0; col < result[0].length; col++) {
//              System.out.print(result[row][col]);
//          }
//      }
//    }
//}

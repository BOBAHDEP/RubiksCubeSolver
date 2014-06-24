package Cube;
//import com.sun.imageio.*;
import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.bmp.BMPImageReaderSpi;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import com.sun.imageio.plugins.png.PNGImageReader;
import com.sun.imageio.plugins.png.PNGImageReaderSpi;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class PhotoRead {

    /*public static final IntColor WHITE_COLOR_1 = new IntColor(255, 255, 255);

    public static final IntColor YELLOW_COLOR_2 = new IntColor(255, 255, 0);

    public static final IntColor BLUE_COLOR_3 = new IntColor(0, 0, 255);

    public static final IntColor GREEN_COLOR_4 = new IntColor(0, 255, 0);

    public static final IntColor RED_COLOR_5 = new IntColor(255, 0, 0);

    public static final IntColor ORANGE_COLOR_0 = new IntColor(255, 165, 0); */

    public static final IntColor[] COLORS_STATIC = {new IntColor(255, 255, 255), new IntColor(255, 255, 0),
            new IntColor(0, 0, 255), new IntColor(0, 255, 0), new IntColor(255, 0, 0), new IntColor(255, 165, 0)};

    BufferedImage img;

    String photoName;

    PhotoRead(String name) throws IOException{
        img = getBufferedImage(name);
        photoName = name;
    }

    private static ImageReader getImageReaderByFileExtension(String fileName) {
        if(fileName.toLowerCase().endsWith(".png")) {
            return new PNGImageReader(new PNGImageReaderSpi());
        } else if(fileName.toLowerCase().endsWith(".gif")) {
            return new GIFImageReader(new GIFImageReaderSpi());
        } else if(fileName.toLowerCase().endsWith(".bmp")) {
            return new BMPImageReader(new BMPImageReaderSpi());
        } else {
            return new JPEGImageReader(new JPEGImageReaderSpi());
        }
    }

    private static BufferedImage getBufferedImage(String fileName) throws IOException {
        File file = new File(fileName);
        ImageReader r = getImageReaderByFileExtension(fileName);
        r.setInput(new FileImageInputStream(file));
        ImageReadParam param = new ImageReadParam();
        return  r.read(0, param);
    }

    private int getHeight(){
        return img.getHeight();
    }

    private int getWidth(){
        return img.getWidth();
    }

    public String getPhotoName(){
        return photoName;
    }

    private IntColor[][] colorField(int ny) throws Exception{
        int xMaxCoordinate = getWidth();
        int yMaxCoordinate = getHeight();
        if (xMaxCoordinate < yMaxCoordinate || yMaxCoordinate < 10*ny){
            throw new Exception("Error in colorField");
        }
        int dy = yMaxCoordinate/ny;
        int nx = xMaxCoordinate/dy;
        IntColor[][] res = new IntColor[nx][ny];

        for (int i = 0; i < nx; i++){
            for (int j = 0; j < ny; j++){
                res[i][j] = new IntColor(img, i * dy, j * dy);
                for (int k = 0; k < 10; k++){
                    res[i][j].plus(new IntColor(img, i * dy + k, j * dy + k));
                }
                res[i][j].divide(10);
            }
        }
        return res;
    }

    private static IntColor[][] colorDifferentialField(IntColor[][] intColors){
        for (int i = 0; i < intColors.length-1; i++){
            for (int j = 0; j < intColors[0].length; j++){
                intColors[i][j].minus(intColors[i+1][j]);

            }

        }
        for (int j = 0; j < intColors[0].length; j++){
            intColors[intColors.length-1][j] = new IntColor(0,0,0);
        }
        return intColors;
    }

    private static void intColorToFile(IntColor[][] intColors){
        PrintWriter printWriter = null;
        try{
            printWriter = new PrintWriter(new FileOutputStream("my_test_file.txt"));
        }catch(FileNotFoundException e){
            System.out.println("Ошибка открытия файла my_test_file.txt");
            System.exit(0);
        }
        for (int j = 0; j < intColors[0].length; j++){
            for (int i = 0; i < intColors.length; i++){
                if (intColors[i][j].getNumber() /10 == 0){
                    printWriter.print(0);
                }
                if (intColors[i][j].getNumber() /100 == 0){
                    printWriter.print(0);
                }
                printWriter.print(intColors[i][j] + " ");
            }
            printWriter.println();
        }

        printWriter.close();
    }

    public static void saveImage(BufferedImage image, File file, ImageWriter w) throws IOException {
        w.setOutput(new FileImageOutputStream(file));
        w.write(image);
        ((FileImageOutputStream)w.getOutput()).close();
    }

    public void draw(String fileName, int x, int y) throws IOException{
        BufferedImage img = getBufferedImage(fileName);
        for (int i = 0; i < 25; i++){
            for (int j = 0; j < 25; j++){
            img.setRGB(x + i, y + j, Color.WHITE.getRGB());
            }
        }
        saveImage(img, new File(fileName.substring(0, fileName.lastIndexOf(".")) + "1.JPG"), new JPEGImageWriter(new JPEGImageWriterSpi()));
    }

    public void draw(String fileName, int x1, int y1, int x2, int y2) throws IOException{
        BufferedImage img = getBufferedImage(fileName);
        int dx = (x2 - x1)/3;
        int dy = (y2 - y1)/3;
        for (int k = 0; k < 4; k++){
            for (int i = x1; i < x2; i++){
                for (int j = 0; j < 25; j++){
                    img.setRGB(i, y1 + j + k*dy, Color.RED.getRGB());
                }
            }
        }
        for (int k = 0; k < 4; k++){
            for (int i = 0; i < 25; i++){
                for (int j = y1; j < y2; j++){
                    img.setRGB(x1 + i + k*dx, j, Color.RED.getRGB());
                }
            }
        }
        saveImage(img, new File(fileName.substring(0, fileName.lastIndexOf(".")) + "_changed.JPG"), new JPEGImageWriter(new JPEGImageWriterSpi()));
    }

    private int[] findFirstPoint() throws Exception{
        int [] res = new int[4];
        IntColor intColors[][] = colorDifferentialField(colorField(200));
        //intColorToFile(intColors);
        int xMin = intColors.length, yMin = intColors[0].length; // координаты первого максимума
        int xMax = 0, yMax = 0; // координаты второго максимума
        for (int j = 0; j < intColors[0].length; j++){
            for (int i = 0; i < intColors.length; i++){
                if (intColors[i][j].getNumber() > 100 && i < xMin){
                    xMin = i;
                }
                if (intColors[i][j].getNumber() > 100 && j < yMin){
                    yMin = j;
                }
                if (intColors[i][j].getNumber() > 100 && i > xMax){
                    xMax = i;
                }
                if (intColors[i][j].getNumber() > 100 && j > yMax){
                    yMax = j;
                }
            }
        }
        if (xMin <= 10 || yMin <= 10 || xMax > intColors.length - 10 || yMax > intColors[0].length - 10 ||
                Math.abs(xMax-xMin - (yMax-yMin)) > Math.min(xMax-xMin, yMax-yMin)){
            throw new Exception("xMin < 10 or yMin < 0 or in findFirstPoint");
        }
        //System.out.println(xMin*getWidth()/100 + " **** " + yMin*getHeight()/100 + " *** " + getHeight());
        res[0] = xMin*getWidth()/intColors.length;
        res[1] = yMin*getHeight()/intColors[0].length;
        res[2] = xMax*getWidth()/intColors.length;
        res[3] = yMax*getHeight()/intColors[0].length;
        draw(photoName, res[0], res[1], res[2], res[3]);
        return res;
    }

    private int getIntFromIntColor(IntColor intColor) throws Exception{

        if (intColor.getBlue() > 50 && intColor.getGreen() > 50 && intColor.getRed() > 50 &&
                Math.abs(intColor.getRed() - intColor.getGreen()) < 35 &&
                Math.abs(intColor.getBlue() - intColor.getGreen()) < 35 &&
                Math.abs(intColor.getBlue() - intColor.getRed()) < 35 ){
            return 0;
        }

        if (intColor.getRed() > 50 && intColor.getGreen() > 50 && intColor.getBlue() < intColor.getGreen()/3 &&
                intColor.getBlue() < intColor.getRed()/3 &&
                Math.abs(intColor.getRed() - intColor.getGreen()*1.2) < 35){
            return 1;
        }

        if (intColor.getBlue() > 50 && intColor.getGreen() < intColor.getBlue()/1.5 &&
                intColor.getRed() < intColor.getBlue()/2 &&
                Math.abs(intColor.getRed()*3 - intColor.getGreen()) < 35){
            return 2;
        }

        if (intColor.getGreen() > 50 && intColor.getBlue() < intColor.getGreen()/2 &&
                intColor.getRed() < intColor.getGreen()/2 &&
                Math.abs(intColor.getRed() - intColor.getBlue()) < 35){
            return 3;
        }

        if (intColor.getRed() > 50 && intColor.getBlue() < intColor.getRed()/3 &&
                intColor.getGreen() < intColor.getRed()/3 &&
                Math.abs(intColor.getGreen() - intColor.getBlue()) < 35){
            return 4;
        }

        if (intColor.getRed() > 50 && intColor.getGreen() > 30 && intColor.getBlue() < intColor.getRed()/3 &&
                intColor.getBlue() < intColor.getGreen()/1.5 &&
                Math.abs(intColor.getGreen()*2 - intColor.getRed()) < 80){
            return 5;
        }

        /*for (int i = 0; i < COLORS_STATIC.length; i++){
            if (Math.abs(intColor.getBlue() - COLORS_STATIC[i].getBlue()) < 100 &&
                    Math.abs(intColor.getGreen() - COLORS_STATIC[i].getGreen()) < 100 &&
                    Math.abs(intColor.getRed() - COLORS_STATIC[i].getRed()) < 100){
                return i;
            }
        }*/
        //System.out.println(intColor);
        //return -1;

        throw new Exception("in getIntFromIntColor didn't get number");
    }

    public int[][] getNumbers() throws  Exception{
        int[] points = findFirstPoint();
        int dx = (points[2] - points[0])/3;
        int dy = (points[3] - points[1])/3;
        int[][] res = new int[3][3];
        System.out.print(new IntColor(img, points[0], points[1], points[0] + dx, points[1] + dy));
        System.out.print(". " + new IntColor(img, points[0] + dx, points[1], points[0] + 2*dx, points[1] + dy));
        System.out.println(". " +new IntColor(img, points[0] + 2*dx, points[1], points[0] + 3*dx, points[1] + dy));
        System.out.print(new IntColor(img, points[0], points[1] + dy, points[0] + dx, points[1] + 2*dy));
        System.out.print(". " + new IntColor(img, points[0] + dx, points[1] + dy, points[0] + 2*dx, points[1] + 2*dy));
        System.out.println(". " + new IntColor(img, points[0] + 2*dx, points[1] + dy, points[0] + 3*dx, points[1] + 2*dy));
        System.out.print(new IntColor(img, points[0], points[1] + 2*dy, points[0] + dx, points[1] + 3*dy));
        System.out.print(". " + new IntColor(img, points[0] + dx, points[1] + 2*dy, points[0] + 2*dx, points[1] + 3*dy));
        System.out.println(". " + new IntColor(img, points[0] + 2*dx, points[1] + 2*dy, points[0] + 3*dx, points[1] + 3*dy));

        res[0][0] = getIntFromIntColor(new IntColor(img, points[0], points[1], points[0] + dx, points[1] + dy));
        res[0][1] = getIntFromIntColor(new IntColor(img, points[0] + dx, points[1], points[0] + 2*dx, points[1] + dy));
        res[0][2] = getIntFromIntColor(new IntColor(img, points[0] + 2*dx, points[1], points[0] + 3*dx, points[1] + dy));
        res[1][0] = getIntFromIntColor(new IntColor(img, points[0], points[1] + dy, points[0] + dx, points[1] + 2*dy));
        res[1][1] = getIntFromIntColor(new IntColor(img, points[0] + dx, points[1] + dy, points[0] + 2*dx, points[1] + 2*dy));
        res[1][2] = getIntFromIntColor(new IntColor(img, points[0] + 2*dx, points[1] + dy, points[0] + 3*dx, points[1] + 2*dy));
        res[2][0] = getIntFromIntColor(new IntColor(img, points[0], points[1] + 2*dy, points[0] + dx, points[1] + 3*dy));
        res[2][1] = getIntFromIntColor(new IntColor(img, points[0] + dx, points[1] + 2*dy, points[0] + 2*dx, points[1] + 3*dy));
        res[2][2] = getIntFromIntColor(new IntColor(img, points[0] + 2*dx, points[1] + 2*dy, points[0] + 3*dx, points[1] + 3*dy));
        return res;
    }

    public static void main(String[] args) throws Exception {
        PhotoRead photoRead = new PhotoRead("C:\\IDEA_Projects\\RubicsCube\\Photo\\IMG4.JPG");

        int[][] res = photoRead.getNumbers();
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                System.out.print(res[i][j]);
            }
            System.out.println();
        }


    }
}


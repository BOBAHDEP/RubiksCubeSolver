package Cube;

import java.awt.*;
import java.awt.image.BufferedImage;

public class IntColor {
    private int red;
    private int blue;
    private int green;

    IntColor(int red, int blue, int green){
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    IntColor(BufferedImage img, int x, int y){
        Color color = new Color(img.getRGB(x, y));
        red = color.getRed();
        blue = color.getBlue();
        green = color.getGreen();
    }

    IntColor(BufferedImage img, int x1, int y1, int x2, int y2){
        int sum = 0;
        IntColor color = new IntColor(0,0,0);
        IntColor color1 = new IntColor(0,0,0);
        for (int i = x1 + 15; i < x2 - 15; i++){
            for (int j = y1 + 15; j < y2 - 15; j++){
                color1.set(new Color(img.getRGB(i, j)));
                color.plus(color1);
                sum++;
            }
        }
        color.divide(sum);
        this.red = color.red;
        this.blue = color.blue;
        this.green = color.green;
    }

    boolean compare(IntColor intColor){
        return (Math.abs(this.red-intColor.red) < 15 && Math.abs(this.green-intColor.green) < 15 && Math.abs(this.blue-intColor.blue) < 15);
    }

    public void set(Color color){
        this.red = color.getRed();
        this.blue = color.getBlue();
        this.green = color.getGreen();
    }

    public void plus(IntColor color){
        this.red += color.red;
        this.blue += color.blue;
        this.green += color.green;
    }

    public void minus(IntColor color){
        this.red -= color.red;
        this.blue -= color.blue;
        this.green -= color.green;
    }

    public void divide(int number){
        this.red /= number;
        this.blue /= number;
        this.green /= number;
    }

    public int getNumber(){
        return Math.abs(red + blue + green);
    }

    public int getRed(){
        return this.red;
    }

    public int getBlue(){
        return this.blue;
    }

    public int getGreen(){
        return this.green;
    }

    @Override
    public String toString()
    {
        return getRed() + " " + getGreen() + " " + getBlue();
    }
}

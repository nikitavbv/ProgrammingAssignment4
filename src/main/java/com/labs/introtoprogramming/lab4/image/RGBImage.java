package com.labs.introtoprogramming.lab4.image;

public class RGBImage {

  private int width;
  private int height;

  private byte[][] red;
  private byte[][] green;
  private byte[][] blue;

  public RGBImage(int width, int height) {
    this(width, height, new byte[height][width], new byte[height][width], new byte[height][width]);
  }
  
  public RGBImage(int width, int height, byte[][] red, byte[][] green, byte[][] blue) {
    this.width = width;
    this.height = height;
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public Pixel getPixel(int x, int y) {
    return new Pixel(red[x][y], green[x][y], blue[x][y]);
  }

  public void setPixel(int x, int y, Pixel p) {
    red[x][y] = p.red();
    green[x][y] = p.green();
    blue[x][y] = p.blue();
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }
}

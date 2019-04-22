package com.labs.introtoprogramming.lab4.image;

public class RGBImage {

  private int width;
  private int height;

  private byte[][] red;
  private byte[][] green;
  private byte[][] blue;

  public RGBImage(int width, int height) {
    this.width = width;
    this.height = height;
    red = new byte[height][width];
    green = new byte[height][width];
    blue = new byte[height][width];
  }

  public Pixel getPixel(int row, int column) {
    return new Pixel(red[row][column], green[row][column], blue[row][column]);
  }

  public void setPixel(int row, int column, Pixel p) {
    red[row][column] = p.red();
    green[row][column] = p.green();
    blue[row][column] = p.blue();
  }

  public int height() {
    return height;
  }

  public int width() {
    return width;
  }
}

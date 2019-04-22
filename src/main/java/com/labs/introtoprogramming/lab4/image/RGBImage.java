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

  public Pixel getPixel(int x, int y) {
    return new Pixel(red[x][y], green[x][y], blue[x][y]);
  }

  public void setPixel(int x, int y, Pixel p) {
    red[x][y] = p.red();
    green[x][y] = p.green();
    blue[x][y] = p.blue();
  }

  public int height() {
    return height;
  }

  public int width() {
    return width;
  }
}

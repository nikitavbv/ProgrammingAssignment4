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

  /**
   * Return pixel data at position (x, y).
   *
   * @param x x coordinate (number of column)
   * @param y y coordinate (number of row)
   * @return pixel with rgb data
   */
  public Pixel getPixel(int x, int y) {
    return new Pixel(red[y][x], green[y][x], blue[y][x]);
  }

  /**
   * Set pixel at position (x, y).
   *
   * @param x x coordinate (number of column)
   * @param y y coordinate (number of row)
   * @param p pixel data to set
   */
  public void setPixel(int x, int y, Pixel p) {
    red[y][x] = p.red();
    green[y][x] = p.green();
    blue[y][x] = p.blue();
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  public byte[][] redChannel() {
    return red;
  }

  public byte[][] greenChannel() {
    return green;
  }

  public byte[][] blueChannel() {
    return blue;
  }
}

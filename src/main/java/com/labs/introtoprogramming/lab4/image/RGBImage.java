package com.labs.introtoprogramming.lab4.image;

public class RGBImage {

  private int width;
  private int height;

  private byte[][] red;
  private byte[][] green;
  private byte[][] blue;

  public RGBImage(int width, int height, byte[][] red, byte[][] green, byte[][] blue) {
    this.width = width;
    this.height = height;
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }
}

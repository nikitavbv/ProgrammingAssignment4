package com.labs.introtoprogramming.lab4.image.io.bmp;

public class BMPImageHeader {
  private int width;
  private int height;
  private int bytesPerPixel;

  public BMPImageHeader(int width, int height, int bytesPerPixel) {
    this.width = width;
    this.height = height;
    this.bytesPerPixel = bytesPerPixel;
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  public int bytesPerPixel() {
    return bytesPerPixel;
  }
}

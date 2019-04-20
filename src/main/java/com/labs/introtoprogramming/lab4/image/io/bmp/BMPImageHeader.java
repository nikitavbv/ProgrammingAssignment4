package com.labs.introtoprogramming.lab4.image.io.bmp;

class BMPImageHeader {
  private int width;
  private int height;
  private int bytesPerPixel;

  BMPImageHeader(int width, int height, int bytesPerPixel) {
    this.width = width;
    this.height = height;
    this.bytesPerPixel = bytesPerPixel;
  }

  int width() {
    return width;
  }

  int height() {
    return height;
  }

  int bytesPerPixel() {
    return bytesPerPixel;
  }
}

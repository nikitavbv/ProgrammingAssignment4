package com.labs.introtoprogramming.lab4.image;

public class Pixel {
  private byte red;
  private byte green;
  private byte blue;

  public Pixel(byte red, byte green, byte blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public byte red() {
    return red;
  }

  public byte green() {
    return green;
  }

  public byte blue() {
    return blue;
  }
}

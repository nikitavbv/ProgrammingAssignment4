package com.labs.introtoprogramming.lab4.image;

public class Pixel {
  private byte red;
  private byte green;
  private byte blue;

  /**
   * Container for color data.
   *
   * @param red red value of color
   * @param green green value of color
   * @param blue blue value of color
   */
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

  @Override
  public String toString() {
    return "Pixel{"
            + "r=" + red
            + ", g=" + green
            + ", b=" + blue
            + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pixel)) {
      return false;
    }
    Pixel pixel = (Pixel) o;
    return red == pixel.red
            && green == pixel.green
            && blue == pixel.blue;
  }

  @Override
  public int hashCode() {
    return (red << 16) & 0x00ff0000 | (green << 8) & 0x0000ff00 | blue & 0x000000ff;
  }
}

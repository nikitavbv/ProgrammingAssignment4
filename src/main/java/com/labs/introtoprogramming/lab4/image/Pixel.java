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

  public Pixel brighter(double factor) {
    int intensity = (int)(1.0/(1.0-factor));
    if ( red == 0 && green == 0 && blue == 0) {
      return new Pixel((byte) intensity, (byte) intensity, (byte) intensity);
    }
    int r = red & 0xFF;
    int g = green & 0xFF;
    int b = blue & 0xFF;
    if (r > 0 && r < intensity) {
      r = intensity;
    }
    if (g > 0 && g < intensity) {
      g = intensity;
    }
    if (b > 0 && b < intensity) {
      b = intensity;
    }

    return new Pixel((byte) Math.min((int)(r/factor), 255),
            (byte) Math.min((int)(g/factor), 255),
            (byte) Math.min((int)(b/factor), 255));
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

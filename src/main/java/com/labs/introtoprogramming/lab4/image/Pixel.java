package com.labs.introtoprogramming.lab4.image;

import java.util.Objects;

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

  @Override
  public String toString() {
    return "Pixel{" +
            "r=" + red +
            ", g=" + green +
            ", b=" + blue +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Pixel)) return false;
    Pixel pixel = (Pixel) o;
    return red == pixel.red &&
            green == pixel.green &&
            blue == pixel.blue;
  }
}

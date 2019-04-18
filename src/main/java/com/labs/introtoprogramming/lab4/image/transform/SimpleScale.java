package com.labs.introtoprogramming.lab4.image.transform;

import com.labs.introtoprogramming.lab4.image.RGBImage;

public class SimpleScale implements RGBImageTransformation {

  private final int times;

  public SimpleScale(int times) {
    this.times = times;
  }

  public RGBImage applyTo(RGBImage image) {
    // TODO: implement this
    return new RGBImage();
  }
}

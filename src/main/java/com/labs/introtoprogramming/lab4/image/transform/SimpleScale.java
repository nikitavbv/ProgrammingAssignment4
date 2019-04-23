package com.labs.introtoprogramming.lab4.image.transform;

import com.labs.introtoprogramming.lab4.image.Pixel;
import com.labs.introtoprogramming.lab4.image.RGBImage;

public class SimpleScale implements RGBImageTransformation {

  private final int times;

  public SimpleScale(int times) {
    this.times = times;
  }

  public RGBImage applyTo(RGBImage image) {
    int h = image.height();
    int w = image.width();
    RGBImage scaledImage = new RGBImage(w * times, h * times);
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        Pixel p = image.getPixel(i, j);
        setScaledPixel(scaledImage, i, j, p);
      }
    }
    return scaledImage;
  }

  private void setScaledPixel(RGBImage image, int row, int column, Pixel p) {
    for (int i = row * times; i < (row + 1) * times; i++) {
      for (int j = column * times; j < (column + 1)  * times; j++) {
        image.setPixel(i, j, p);
      }
    }
  }
}

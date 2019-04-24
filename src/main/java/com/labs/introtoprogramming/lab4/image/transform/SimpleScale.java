package com.labs.introtoprogramming.lab4.image.transform;

import com.labs.introtoprogramming.lab4.image.Pixel;
import com.labs.introtoprogramming.lab4.image.RGBImage;

public class SimpleScale implements RGBImageTransformation {

  private final int times;

  public SimpleScale(int times) {
    this.times = times;
  }

  /**
   * Scale image by times in both directions.
   *
   * @param image image to scale
   * @return scaled image
   */
  public RGBImage applyTo(RGBImage image) {
    int h = image.height();
    int w = image.width();
    RGBImage scaledImage = new RGBImage(w * times, h * times);
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        Pixel p = image.getPixel(j, i);
        int newJ = times < 0 ? w * times - j - 1 : j;
        int newI = times < 0 ? h * times - i - 1 : i;
        setScaledPixel(scaledImage, newJ, newI, p);
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

  public int times() {
    return times;
  }
}

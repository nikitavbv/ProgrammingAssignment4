package com.labs.introtoprogramming.lab4.image.transform;

import com.labs.introtoprogramming.lab4.image.Pixel;
import com.labs.introtoprogramming.lab4.image.RGBImage;

public class SimpleBrightness implements RGBImageTransformation {

  private final double brightness;

  public SimpleBrightness(double brightness) {
    this.brightness = brightness;
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
    RGBImage brighterImage = new RGBImage(w, h);
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        Pixel p = image.getPixel(j, i);
        if (p.red() != 0 && p.green() != 0 && p.blue() != 0) {
          brighterImage.setPixel(j, i, p.brighter(brightness));
        } else {
          brighterImage.setPixel(j, i, p);
        }
      }
    }
    return brighterImage;
  }

}

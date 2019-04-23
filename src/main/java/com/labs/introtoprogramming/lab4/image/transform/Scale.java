package com.labs.introtoprogramming.lab4.image.transform;

import com.labs.introtoprogramming.lab4.image.Pixel;
import com.labs.introtoprogramming.lab4.image.RGBImage;

public class Scale implements RGBImageTransformation {

  private double horizontalScale;
  private double verticalScale;

  public Scale(double horizontalScale, double verticalScale) {
    this.horizontalScale = horizontalScale;
    this.verticalScale = verticalScale;
  }

  public RGBImage applyTo(RGBImage image) {
    int w = image.width();
    int h = image.height();
    int scaledWidth = (int) (w * horizontalScale);
    int scaledHeight = (int) (h * verticalScale);
    RGBImage scaledImage = new RGBImage(scaledWidth, scaledHeight);
    double xRation = (double) (w - 1) / scaledWidth;
    double yRation = (double) (h - 1) / scaledHeight;
    for (int i = 0; i < scaledHeight; i++) {
      for (int j = 0; j < scaledWidth; j++) {
        double x = j * xRation;
        double y = i * yRation;
        int realX = (int) x;
        int realY = (int) y;
        Pixel leftUpperCorner = image.getPixel(realX, realY);
        Pixel rightUpperCorner = image.getPixel(realX + 1, realY);
        Pixel leftLowerCorner = image.getPixel(realX, realY + 1);
        Pixel rightLowerCorner = image.getPixel(realX + 1, realY + 1);
        Pixel xInterpolationUpper = colorLerp(x, realX, realX + 1, leftUpperCorner, rightUpperCorner);
        Pixel xInterpolationLower = colorLerp(x, realX, realX + 1, leftLowerCorner, rightLowerCorner);
        Pixel value = colorLerp(y, realY, realY + 1, xInterpolationUpper, xInterpolationLower);
        scaledImage.setPixel(j, i, value);
      }
    }
    return scaledImage;
  }

  private Pixel colorLerp(double coord, int coord1, int coord2, Pixel v1, Pixel v2) {
    return new Pixel(
            (byte) lerp(coord, coord1, coord2, toUnsignedInt(v1.red()), toUnsignedInt(v2.red())),
            (byte) lerp(coord, coord1, coord2, toUnsignedInt(v1.green()), toUnsignedInt(v2.green())),
            (byte) lerp(coord, coord1, coord2, toUnsignedInt(v1.blue()), toUnsignedInt(v2.blue()))
    );
  }

  private int toUnsignedInt(byte b) {
    return (b + 256) % 256;
  }

  private double lerp(double coord, int coord1, int coord2, int v1, int v2) {
    return (coord2 - coord) / (coord2 - coord1) * v1 + (coord - coord1) / (coord2 - coord1) * v2;
  }
}

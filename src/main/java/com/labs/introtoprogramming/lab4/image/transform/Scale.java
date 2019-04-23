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
    double xRation = (double) (w - 1) / (scaledWidth - 1 > 1 ? scaledWidth - 1 : scaledWidth);
    double yRation = (double) (h - 1) / (scaledHeight - 1 > 1 ? scaledHeight - 1 : scaledHeight);
    for (int i = 0; i < scaledHeight; i++) {
      for (int j = 0; j < scaledWidth; j++) {
        double x = j * xRation;
        double y = i * yRation;
        int leftX = (int) x;
        int leftY = (int) y;
        int rightX = leftX + 1 < w ? leftX + 1 : leftX;
        int rightY = leftY + 1 < h ? leftY + 1 : leftY;
        Pixel leftXCorner = image.getPixel(leftX, leftY);
        Pixel rightXCorner = image.getPixel(rightX, leftY);
        Pixel leftYCorner = image.getPixel(leftX, rightY);
        Pixel rightYCorner = image.getPixel(rightX, rightY);
        Pixel xInterpolationUpper = colorLerp(x, leftX, leftX + 1, leftXCorner, rightXCorner);
        Pixel xInterpolationLower = colorLerp(x, leftX, leftX + 1, leftYCorner, rightYCorner);
        Pixel value = colorLerp(y, leftY, leftY + 1, xInterpolationUpper, xInterpolationLower);
        scaledImage.setPixel(j, i, value);
      }
    }
    return scaledImage;
  }

  Pixel colorLerp(double coord, int coord1, int coord2, Pixel v1, Pixel v2) {
    return new Pixel(
            (byte) lerp(coord, coord1, coord2, toUnsignedInt(v1.red()), toUnsignedInt(v2.red())),
            (byte) lerp(coord, coord1, coord2, toUnsignedInt(v1.green()), toUnsignedInt(v2.green())),
            (byte) lerp(coord, coord1, coord2, toUnsignedInt(v1.blue()), toUnsignedInt(v2.blue()))
    );
  }

  int toUnsignedInt(byte b) {
    return (b + 256) % 256;
  }

  double lerp(double coord, int coord1, int coord2, int v1, int v2) {
    return (coord2 - coord) / (coord2 - coord1) * v1 + (coord - coord1) / (coord2 - coord1) * v2;
  }
}

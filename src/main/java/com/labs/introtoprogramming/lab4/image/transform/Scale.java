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

  /**
   * Scale image by horizontalScale in x direction and by verticalScale in y direction.
   *
   * @param image image to scale
   * @return scaled image
   */
  public RGBImage applyTo(RGBImage image) {
    int w = image.width();
    int h = image.height();
    int scaledWidth = (int) (w * horizontalScale);
    int scaledHeight = (int) (h * verticalScale);
    RGBImage scaledImage = new RGBImage(scaledWidth, scaledHeight);
    double widthRation = (double) (w - 1)
            / (scaledWidth - 1 > 1 ? scaledWidth - 1 : scaledWidth);
    double heightRation = (double) (h - 1)
            / (scaledHeight - 1 > 1 ? scaledHeight - 1 : scaledHeight);
    for (int i = 0; i < scaledHeight; i++) {
      for (int j = 0; j < scaledWidth; j++) {
        double x = j * widthRation;
        double y = i * heightRation;
        int leftX = (int) x;
        int leftY = (int) y;
        int rightX = leftX + 1 < w ? leftX + 1 : leftX;
        int rightY = leftY + 1 < h ? leftY + 1 : leftY;
        Pixel leftUpperCorner = image.getPixel(leftX, leftY);
        Pixel rightUpperCorner = image.getPixel(rightX, leftY);
        Pixel leftLowerCorner = image.getPixel(leftX, rightY);
        Pixel rightLowerCorner = image.getPixel(rightX, rightY);
        Pixel interpolationUpper = colorLerp(x, leftX, leftX + 1,
                leftUpperCorner, rightUpperCorner);
        Pixel interpolationLower = colorLerp(x, leftX, leftX + 1,
                leftLowerCorner, rightLowerCorner);
        Pixel value = colorLerp(y, leftY, leftY + 1, interpolationUpper, interpolationLower);
        scaledImage.setPixel(j, i, value);
      }
    }
    return scaledImage;
  }

  Pixel colorLerp(double coord, int coord1, int coord2, Pixel v1, Pixel v2) {
    return new Pixel(
            (byte) lerp(coord, coord1, coord2,
                    toUnsignedInt(v1.red()), toUnsignedInt(v2.red())),
            (byte) lerp(coord, coord1, coord2,
                    toUnsignedInt(v1.green()), toUnsignedInt(v2.green())),
            (byte) lerp(coord, coord1, coord2,
                    toUnsignedInt(v1.blue()), toUnsignedInt(v2.blue()))
    );
  }

  int toUnsignedInt(byte b) {
    return (b + 256) % 256;
  }

  double lerp(double coord, int coord1, int coord2, int v1, int v2) {
    return (coord2 - coord) / (coord2 - coord1) * v1 + (coord - coord1) / (coord2 - coord1) * v2;
  }
}

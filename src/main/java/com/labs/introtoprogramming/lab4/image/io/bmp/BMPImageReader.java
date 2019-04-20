package com.labs.introtoprogramming.lab4.image.io.bmp;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.io.ImageLoadException;
import com.labs.introtoprogramming.lab4.image.io.ImageReader;

import java.io.IOException;
import java.io.InputStream;

public class BMPImageReader implements ImageReader {

  private final InputStream in;
  private BMPParser parser = new BMPParser();
  int height;
  int width;
  int bytesPerPixel;
  int offset;
  byte[][] red;
  byte[][] green;
  byte[][] blue;

  public BMPImageReader(InputStream in) {
    this.in = in;
  }

  @Override
  public RGBImage read() throws IOException {
    loadHeaderInfo();
    loadImageHeaderInfo();
    in.reset();
    in.skip(offset);
    loadPixelData();
    sortRows();
    return new RGBImage(width, height, red, green, blue);
  }

  void loadHeaderInfo() {
    try {
      byte[] header = new byte[14];
      int byteRead = in.read(header);
      if (byteRead == -1) {
        throw new ImageLoadException("Empty stream");
      }
      offset = parser.parseHeader(header);
    } catch (Exception e) {
      throw new ImageLoadException("Cannot read file header info");
    }
  }

  void loadImageHeaderInfo() {
    try {
      byte[] byteNumberPerSizeUnit = new byte[4];
      in.mark(4);
      in.read(byteNumberPerSizeUnit);
      in.reset();
      byte[] imageHeader = new byte[parser.sumUpBytes(byteNumberPerSizeUnit, 0, 4)];
      in.read(imageHeader);
      BMPImageHeader header = parser.parseImageHeader(imageHeader);
      width = header.width();
      height = header.height();
      bytesPerPixel = header.bytesPerPixel();
    } catch (Exception e) {
      throw new ImageLoadException("Cannot read image header info");
    }
  }

  void loadPixelData() throws ImageLoadException, IOException {
    red = new byte[height][width];
    green = new byte[height][width];
    blue = new byte[height][width];
    int bytesInRow = bytesPerPixel * width;
    int padding = (4 - bytesInRow % 4) % 4;
    byte[] pixel = new byte[bytesPerPixel];
    for (int i = 0; i < Math.abs(height); i++) {
      in.skip(padding);
      for (int j = 0; j < width; j++) {
        in.read(pixel);
        red[i][j] = pixel[0];
        green[i][j] = pixel[1];
        blue[i][j] = pixel[2];
      }
    }
  }

  /**
   * Change rows of matrix according to order of scan lines in file
   */
  void sortRows() {
    if (height < 0) {
      return;
    }
    for (int i = 0, j = height - 1; i <= j; i++, j--) {
      swapRow(red, i, j);
      swapRow(green, i, j);
      swapRow(blue, i, j);
    }
  }

  void swapRow(byte[][] color, int i, int j) {
    if (color.length == 0) return;
    byte[] temp = color[i];
    color[i] = color[j];
    color[j] = temp;
  }
}

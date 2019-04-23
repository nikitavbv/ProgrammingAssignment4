package com.labs.introtoprogramming.lab4.image.io.bmp;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.io.ImageReadException;
import com.labs.introtoprogramming.lab4.image.io.ImageReader;
import com.labs.introtoprogramming.lab4.image.io.UnsupportedDataFormatException;

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
  public RGBImage read() throws IOException, UnsupportedDataFormatException {
    loadHeaderInfo();
    loadImageHeaderInfo();
    System.out.println(offset);
    if (in.skip(offset) < offset) {
      throw new ImageReadException("Unexpected end of stream");
    }
    loadPixelData();
    sortRows();
    return new RGBImage(width, height, red, green, blue);
  }

  @Override
  public void close() throws IOException {
    in.close();
  }

  void loadHeaderInfo() throws IOException, UnsupportedDataFormatException {
    byte[] header = new byte[14];
    if (in.read(header) < 14) {
      throw new ImageReadException("Unexpected end of file header");
    }
    offset = parser.parseHeader(header) - header.length;
  }

  void loadImageHeaderInfo() throws IOException, UnsupportedDataFormatException {
    byte[] byteNumberPerSizeUnit = new byte[4];
    in.read(byteNumberPerSizeUnit);
    int size = parser.sumUpBytes(byteNumberPerSizeUnit, 0, 4);
    size = size - 4 > 0 ? size - 4 : 0;
    byte[] imageHeader = new byte[size];
    if (in.read(imageHeader) < size) {
      throw new ImageReadException("Unexpected end of image header");
    }
    imageHeader = concatArrays(byteNumberPerSizeUnit, imageHeader);
    BMPImageHeader header = parser.parseImageHeader(imageHeader);
    width = header.width();
    height = header.height();
    bytesPerPixel = header.bytesPerPixel();
    offset = offset - imageHeader.length;
  }

  void loadPixelData() throws IOException {
    int h = Math.abs(height);
    red = new byte[h][width];
    green = new byte[h][width];
    blue = new byte[h][width];
    int bytesInRow = bytesPerPixel * width;
    int padding = (4 - bytesInRow % 4) % 4;
    byte[] pixel = new byte[bytesPerPixel];
    for (int i = 0; i < h; i++) {
      if (in.skip(padding) < padding) {
        throw new ImageReadException("Unexpected scan line padding");
      }
      for (int j = 0; j < width; j++) {
        if (in.read(pixel) < bytesPerPixel) {
          throw new ImageReadException("Unexpected end of pixel data");
        }
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

  private byte[] concatArrays(byte[] a, byte[] b) {
    int firstLen = a.length;
    int secondLen = b.length;
    byte[] res = new byte[firstLen + secondLen];
    System.arraycopy(a, 0, res, 0, firstLen);
    System.arraycopy(b, 0, res, firstLen, secondLen);
    return res;
  }
}

package com.labs.introtoprogramming.lab4.image.io.bmp;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.formats.bmp.BMPImageHeader;
import com.labs.introtoprogramming.lab4.image.formats.bmp.BMPParser;
import com.labs.introtoprogramming.lab4.image.io.ImageReader;

import java.io.IOException;
import java.io.InputStream;

public class BMPImageReader implements ImageReader {

  private final InputStream in;
  BMPImageHeader header;
  int offset;
  byte[][] red;
  byte[][] green;
  byte[][] blue;

  public BMPImageReader(InputStream in) {
    this.in = in;
  }

  @Override
  public RGBImage read() {
    RGBImage image = null;
    try {
      loadHeaderInfo();
      loadImageHeaderInfo();
      in.reset();
      in.skip(offset);
      loadPixelData();
      sortRows();
      image = new RGBImage(header.width, header.height, red, green, blue);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Failed to load BMP image");
    }
    return image;
  }

  void loadHeaderInfo() throws IOException {
    try {
      byte[] header = new byte[14];
      int byteRead = in.read(header);
      if (byteRead == -1) {
        throw new IOException();
      }
      offset = BMPParser.parseHeader(header);
    } catch (Exception e) {
      throw new IOException("Cannot read file header info");
    }
  }

  void loadImageHeaderInfo() throws IOException {
    try {
      byte[] byteNumberPerSizeUnit = new byte[4];
      in.mark(4);
      in.read(byteNumberPerSizeUnit);
      in.reset();
      byte[] imageHeader = new byte[BMPParser.sumUpBytes(byteNumberPerSizeUnit, 0, 4)];
      in.read(imageHeader);
      header = BMPParser.parseImageHeader(imageHeader);
    } catch (Exception e) {
      throw new IOException("Cannot read image header info");
    }
  }

  void loadPixelData() throws IOException {
    red = new byte[header.height][header.width];
    green = new byte[header.height][header.width];
    blue = new byte[header.height][header.width];
    int bytesInRow = header.bytesPerPixel * header.width;
    int padding = (4 - bytesInRow % 4) % 4;
    byte[] pixel = new byte[header.bytesPerPixel];
    int height = Math.abs(header.height);
    for (int i = 0; i < height; i++) {
      in.skip(padding);
      for (int j = 0; j < header.width; j++) {
        in.read(pixel);
        red[i][j] = pixel[0];
        green[i][j] = pixel[1];
        blue[i][j] = pixel[2];
      }
    }
  }

  void sortRows() {
    if (header.height < 0) {
      return;
    }
    for (int i = 0, j = header.height - 1; i <= j; i++, j--) {
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

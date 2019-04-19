package com.labs.introtoprogramming.lab4.image.io.bmp;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.formats.bmp.BMPImageHeader;
import com.labs.introtoprogramming.lab4.image.formats.bmp.BMPParser;
import com.labs.introtoprogramming.lab4.image.io.ImageReader;

import java.io.IOException;
import java.io.InputStream;

public class BMPImageReader implements ImageReader {

  private final InputStream in;
  private BMPImageHeader header;
  private int offset;
  private byte r[][];
  private byte g[][];
  private byte b[][];

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
      image = new RGBImage(header.width, header.height, r, g, b);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Failed to load BMP image");
    }
    return image;
  }

  private void loadHeaderInfo() throws IOException {
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

  private void loadImageHeaderInfo() throws IOException {
    try {
      byte[] byteNumberPerSizeUnit = new byte[4];
      byte[] imageHeader;
      in.mark(4);
      in.read(byteNumberPerSizeUnit);
      in.reset();
      imageHeader = new byte[BMPParser.sumUpBytes(byteNumberPerSizeUnit, 0, 4)];
      in.read(imageHeader);
      header = BMPParser.parseImageHeader(imageHeader);
    } catch (Exception e) {
      throw new IOException("Cannot read image header info");
    }
  }

  private void loadPixelData() throws IOException {
    r = new byte[header.height][header.width];
    g = new byte[header.height][header.width];
    b = new byte[header.height][header.width];
    int bytesInRow = header.bytesPerPixel * header.width;
    int padding = (4 - bytesInRow % 4) % 4;
    byte[] pixel = new byte[header.bytesPerPixel];
    int height = Math.abs(header.height);
    for (int i = 0; i < height; i++) {
      in.skip(padding);
      for (int j = 0; j < header.width; j++) {
        in.read(pixel);
        r[i][j] = pixel[0];
        g[i][j] = pixel[1];
        b[i][j] = pixel[2];
      }
    }
  }

  private void sortRows() {
    if (header.height < 0) return;
    for (int i = 0, j = header.height - 1; i <= j; i++, j--) {
      swapRow(r, i, j);
      swapRow(g, i, j);
      swapRow(b, i, j);
    }
  }

  private void swapRow(byte[][] color, int i, int j) {
    byte[] temp = color[i];
    color[i] = color[j];
    color[j] = temp;
  }
}

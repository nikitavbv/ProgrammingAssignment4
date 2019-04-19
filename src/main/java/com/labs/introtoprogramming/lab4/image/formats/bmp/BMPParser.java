package com.labs.introtoprogramming.lab4.image.formats.bmp;

import java.util.Arrays;
import java.util.stream.IntStream;

public class BMPParser {

  public static int parseHeader(byte[] headerInfo) throws Exception {
    if (headerInfo.length != 14) {
      throw new Exception("File format is not a BMP");
    }
    if (headerInfo[0] != 66 || headerInfo[1] != 77) {
      throw new Exception("File format is not a BMP");
    }

    int size = sumUpBytes(headerInfo, 2, 6);
    int offset = sumUpBytes(headerInfo, 10, 14);
    if (offset < 64 || offset > size) {
      throw new Exception("File format is not a BMP");
    }
    return offset;
  }

  public static BMPImageHeader parseImageHeader(byte[] headerInfo) throws Exception {
    int headerSize = sumUpBytes(headerInfo, 0, 4);
    if (headerSize != headerInfo.length) {
      throw new Exception("Incorrect image header");
    }

    BMPImageHeader header = new BMPImageHeader();
    header.width = sumUpBytes(headerInfo, 4, 8);
    header.height = sumUpBytes(headerInfo, 8, 12);
    header.bytesPerPixel = sumUpBytes(headerInfo, 14, 16) / 8;

    if (header.bytesPerPixel != 3) {
      throw new Exception("Incorrect image format");
    }
    return header;
  }

  public static int sumUpBytes(byte[] headerInfo, int start, int end) {
    byte[] copy = Arrays.copyOfRange(headerInfo, start, end);
    return IntStream.range(0, copy.length).map(i -> copy[i]).sum();
  }
}

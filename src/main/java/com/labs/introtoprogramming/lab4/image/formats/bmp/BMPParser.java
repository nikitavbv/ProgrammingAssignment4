package com.labs.introtoprogramming.lab4.image.formats.bmp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

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
    if (offset < 54 || offset > size) {
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
    header.bytesPerPixel = sumUpBytes(headerInfo, 14, 16);

    if (header.bytesPerPixel != 24) {
      throw new Exception("Incorrect image format");
    }
    header.bytesPerPixel /= 8;
    return header;
  }

  public static int sumUpBytes(byte[] headerInfo, int start, int end) {
    byte[] copy = Arrays.copyOfRange(headerInfo, start, end);
    int len = copy.length;
    if (len < 4 || len > 4) {
      copy = Arrays.copyOfRange(copy, 0, 4);
      for (int i = len; i < 4; i++) {
        copy[i] = 0;
      }
    }
    ByteBuffer bb = ByteBuffer.wrap(copy);
    bb.order(ByteOrder.LITTLE_ENDIAN);
    return bb.getInt();
  }
}

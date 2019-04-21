package com.labs.introtoprogramming.lab4.image.io.bmp;

import com.labs.introtoprogramming.lab4.image.io.UnsupportedDataFormatException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

class BMPParser {

  int parseHeader(byte[] headerInfo) throws UnsupportedDataFormatException {
    if (headerInfo.length != 14) {
      throw new UnsupportedDataFormatException("Data format is not a BMP");
    }
    if (headerInfo[0] != 0x42 || headerInfo[1] != 0x4D) {
      throw new UnsupportedDataFormatException("Data format is not a BMP");
    }

    int size = sumUpBytes(headerInfo, 2, 6);
    int offset = sumUpBytes(headerInfo, 10, 14);
    if (offset < 54 || offset > size) {
      throw new UnsupportedDataFormatException("Invalid value for offset");
    }
    return offset;
  }

  BMPImageHeader parseImageHeader(byte[] headerInfo) throws UnsupportedDataFormatException {
    if (headerInfo.length < 4) {
      throw new UnsupportedDataFormatException("Incorrect image header");
    }
    int headerSize = sumUpBytes(headerInfo, 0, 4);
    if (headerSize != headerInfo.length) {
      throw new UnsupportedDataFormatException("Incorrect image header");
    }

    int width = sumUpBytes(headerInfo, 4, 8);
    int height = sumUpBytes(headerInfo, 8, 12);
    int bytesPerPixel = sumUpBytes(headerInfo, 14, 16);

    if (bytesPerPixel != 24) {
      throw new UnsupportedDataFormatException("Unsupported bits per pixel format");
    }
    bytesPerPixel /= 8;

    return new BMPImageHeader(width, height, bytesPerPixel);
  }

  /**
   * Get int value of 4 bytes in subarray of bytes in littleEndian byte order.
   * If subarray length is greater than 4 return value of first 4 bytes.
   * If subarray length is less than 4 append 0 values to the end.
   *
   * @param bytes array of byte values
   * @param start beginning of subarray
   * @param end end of subarray
   * @return int represented by four bytes
   */
  int sumUpBytes(byte[] bytes, int start, int end) {
    byte[] copy = Arrays.copyOfRange(bytes, start, end);
    int len = copy.length;
    if (len != 4) {
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

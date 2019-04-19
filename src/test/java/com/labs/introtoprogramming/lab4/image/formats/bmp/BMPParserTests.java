package com.labs.introtoprogramming.lab4.image.formats.bmp;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class BMPParserTests {
  private static final List<byte[]> DUMB_BYTES_TO_SUM = Arrays.asList(
          new byte[]{1, 0, 0, 0},
          new byte[4],
          new byte[]{-1, -1, -1, -1},
          new byte[0],
          new byte[]{1, 1},
          new byte[]{1, 1, 0, 0, 0},
          new byte[]{1}
  );

  private static final List<byte[]> DUMB_FILE_HEADER = Arrays.asList(
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0},
          new byte[1],
          new byte[]{0, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
          new byte[]{66, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
          new byte[14],
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1},
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0},
          new byte[]{66, 77, -4, -1, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0}
  );

  private static final List<byte[]> DUMB_IMAGE_HEADER = Arrays.asList(
          new byte[]{40, 0, 0, 0, 65, 0, 0, 0, 65, 0, 0, 0, 1, 0, 24, 0, 0, 0, 0, 0,
                  -58, 49, 0, 0, 18, 11, 0, 0, 18, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
          },
          new byte[0],
          new byte[]{
                  0, 0, 0, 0, 65, 0, 0, 0, 65, 0, 0, 0, 1, 0, 24, 0,0, 0, 0, 0,
                  -58, 49, 0, 0, 18, 11, 0, 0, 18, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
          },
          new byte[]{0, 0, 0, 0, 65, 0, 0, 0, 65, 0, 0, 0, 1, 0, 25, 0, 0, 0, 0, 0,
                  -58, 49, 0, 0, 18, 11, 0, 0, 18, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
          }
  );

  @Test
  public void sumUpBytesTest() {
    byte[] arr = DUMB_BYTES_TO_SUM.get(0);
    int sum = BMPParser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(1, sum);
  }

  @Test
  public void sumUpBytesEmptyArrayTest() {
    byte[] arr = DUMB_BYTES_TO_SUM.get(1);
    int sum = BMPParser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(0, sum);
  }

  @Test
  public void sumUpBytesMaxValueTest() {
    byte[] arr = DUMB_BYTES_TO_SUM.get(2);
    int sum = BMPParser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(-1, sum);
  }

  @Test
  public void sumUpBytesZeroLengthTest() {
    byte[] arr = DUMB_BYTES_TO_SUM.get(3);
    int sum = BMPParser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(0, sum);
  }

  @Test
  public void sumUpBytesLessFourBytesTest() {
    byte[] arr = DUMB_BYTES_TO_SUM.get(4);
    int sum = BMPParser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(257, sum);
  }

  @Test
  public void sumUpBytesBiggerFourBytesTest() {
    byte[] arr = DUMB_BYTES_TO_SUM.get(5);
    int sum = BMPParser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(257, sum);
  }

  @Test
  public void sumUpBytesOneValueTest() {
    byte[] arr = DUMB_BYTES_TO_SUM.get(6);
    int sum = BMPParser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(arr[0], sum);
  }

  @Test
  public void parseHeaderTest() throws Exception {
    int res = BMPParser.parseHeader(DUMB_FILE_HEADER.get(0));
    Assert.assertEquals(54, res);
  }

  @Test(expected = Exception.class)
  public void parseHeaderSmallSizeTest() throws Exception {
    BMPParser.parseHeader(DUMB_FILE_HEADER.get(1));
  }

  @Test(expected = Exception.class)
  public void parseHeaderNoBTest() throws Exception {
    BMPParser.parseHeader(DUMB_FILE_HEADER.get(2));
  }

  @Test(expected = Exception.class)
  public void parseHeaderNoMTest() throws Exception {
    BMPParser.parseHeader(DUMB_FILE_HEADER.get(3));
  }

  @Test(expected = Exception.class)
  public void parseHeaderEmptyArrayTest() throws Exception {
    BMPParser.parseHeader(DUMB_FILE_HEADER.get(4));
  }

  @Test(expected = Exception.class)
  public void parseHeaderNoOffsetTest() throws Exception {
    BMPParser.parseHeader(DUMB_FILE_HEADER.get(5));
  }

  @Test(expected = Exception.class)
  public void parseHeaderTooLargeOffsetTest() throws Exception {
    BMPParser.parseHeader(DUMB_FILE_HEADER.get(6));
  }

  @Test
  public void parseHeader100Test() throws Exception {
    int res = BMPParser.parseHeader(DUMB_FILE_HEADER.get(7));
    Assert.assertEquals(100, res);
  }

  @Test
  public void parseHeaderOffsetEqualSizeTest() throws Exception {
    int res = BMPParser.parseHeader(DUMB_FILE_HEADER.get(8));
    Assert.assertEquals(255, res);
  }

  @Test
  public void parseImageHeaderTest() throws Exception {
    BMPImageHeader header = BMPParser.parseImageHeader(DUMB_IMAGE_HEADER.get(0));
    Assert.assertEquals(65, header.width);
    Assert.assertEquals(65, header.height);
    Assert.assertEquals(3, header.bytesPerPixel);
  }

  @Test(expected = Exception.class)
  public void parseImageHeaderEmptyArrayTest() throws Exception {
    BMPImageHeader header = BMPParser.parseImageHeader(DUMB_IMAGE_HEADER.get(1));
  }

  @Test(expected = Exception.class)
  public void parseImageHeaderWrongSizeArrayTest() throws Exception {
    BMPImageHeader header = BMPParser.parseImageHeader(DUMB_IMAGE_HEADER.get(2));
  }

  @Test(expected = Exception.class)
  public void parseImageHeaderWrongBytesForPixelTest() throws Exception {
    BMPImageHeader header = BMPParser.parseImageHeader(DUMB_IMAGE_HEADER.get(3));
  }
}

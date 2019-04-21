package com.labs.introtoprogramming.lab4.image.io.bmp;

import java.util.Arrays;
import java.util.List;

import com.labs.introtoprogramming.lab4.image.io.UnsupportedDataFormat;
import org.junit.Assert;
import org.junit.Test;

public class BMPParserTests {

  private static final BMPParser parser = new BMPParser();

  private static final List<byte[]> DUMMY_BYTES_TO_SUM = Arrays.asList(
          new byte[]{1, 0, 0, 0},
          new byte[4],
          new byte[]{-1, -1, -1, -1},
          new byte[0],
          new byte[]{1, 1},
          new byte[]{1, 1, 0, 0, 0},
          new byte[]{1}
  );

  private static final List<byte[]> DUMMY_FILE_HEADER = Arrays.asList(
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0},
          new byte[1],
          new byte[]{0, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
          new byte[]{66, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
          new byte[14],
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0},
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, 100, 100, 0, 0},
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0},
          new byte[]{66, 77, -4, -1, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0}
  );

  private static final List<byte[]> DUMMY_IMAGE_HEADER = Arrays.asList(
          new byte[]{40, 0, 0, 0, 65, 0, 0, 0, 65, 0, 0, 0, 1, 0, 24, 0, 0, 0, 0, 0,
                  -58, 49, 0, 0, 18, 11, 0, 0, 18, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
          },
          new byte[0],
          new byte[]{
                  0, 0, 0, 0, 65, 0, 0, 0, 65, 0, 0, 0, 1, 0, 24, 0,0, 0, 0, 0,
                  -58, 49, 0, 0, 18, 11, 0, 0, 18, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
          },
          new byte[]{40, 0, 0, 0, 65, 0, 0, 0, 65, 0, 0, 0, 1, 0, 25, 0, 0, 0, 0, 0,
                  -58, 49, 0, 0, 18, 11, 0, 0, 18, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
          }
  );

  @Test
  public void sumUpBytesTest() {
    byte[] arr = DUMMY_BYTES_TO_SUM.get(0);
    int sum = parser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(1, sum);
  }

  @Test
  public void sumUpBytesEmptyArrayTest() {
    byte[] arr = DUMMY_BYTES_TO_SUM.get(1);
    int sum = parser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(0, sum);
  }

  @Test
  public void sumUpBytesMaxValueTest() {
    byte[] arr = DUMMY_BYTES_TO_SUM.get(2);
    int sum = parser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(-1, sum);
  }

  @Test
  public void sumUpBytesZeroLengthTest() {
    byte[] arr = DUMMY_BYTES_TO_SUM.get(3);
    int sum = parser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(0, sum);
  }

  @Test
  public void sumUpBytesLessFourBytesTest() {
    byte[] arr = DUMMY_BYTES_TO_SUM.get(4);
    int sum = parser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(257, sum);
  }

  @Test
  public void sumUpBytesBiggerFourBytesTest() {
    byte[] arr = DUMMY_BYTES_TO_SUM.get(5);
    int sum = parser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(257, sum);
  }

  @Test
  public void sumUpBytesOneValueTest() {
    byte[] arr = DUMMY_BYTES_TO_SUM.get(6);
    int sum = parser.sumUpBytes(arr, 0, arr.length);
    Assert.assertEquals(arr[0], sum);
  }

  @Test
  public void parseHeaderTest() throws Exception {
    int res = parser.parseHeader(DUMMY_FILE_HEADER.get(0));
    Assert.assertEquals(54, res);
  }

  @Test(expected = UnsupportedDataFormat.class)
  public void parseHeaderSmallSizeTest() throws Exception {
    parser.parseHeader(DUMMY_FILE_HEADER.get(1));
  }

  @Test(expected = UnsupportedDataFormat.class)
  public void parseHeaderNoBTest() throws Exception {
    parser.parseHeader(DUMMY_FILE_HEADER.get(2));
  }

  @Test(expected = UnsupportedDataFormat.class)
  public void parseHeaderNoMTest() throws Exception {
    parser.parseHeader(DUMMY_FILE_HEADER.get(3));
  }

  @Test(expected =UnsupportedDataFormat.class)
  public void parseHeaderEmptyArrayTest() throws Exception {
    parser.parseHeader(DUMMY_FILE_HEADER.get(4));
  }

  @Test(expected = UnsupportedDataFormat.class)
  public void parseHeaderNoOffsetTest() throws Exception {
    parser.parseHeader(DUMMY_FILE_HEADER.get(5));
  }

  @Test(expected = UnsupportedDataFormat.class)
  public void parseHeaderTooLargeOffsetTest() throws Exception {
    parser.parseHeader(DUMMY_FILE_HEADER.get(6));
  }

  @Test
  public void parseHeader100Test() throws Exception {
    int res = parser.parseHeader(DUMMY_FILE_HEADER.get(7));
    Assert.assertEquals(100, res);
  }

  @Test
  public void parseHeaderOffsetEqualSizeTest() throws Exception {
    int res = parser.parseHeader(DUMMY_FILE_HEADER.get(8));
    Assert.assertEquals(255, res);
  }

  @Test
  public void parseImageHeaderTest() throws Exception {
    BMPImageHeader header = parser.parseImageHeader(DUMMY_IMAGE_HEADER.get(0));
    Assert.assertEquals(65, header.width());
    Assert.assertEquals(65, header.height());
    Assert.assertEquals(3, header.bytesPerPixel());
  }

  @Test(expected = UnsupportedDataFormat.class)
  public void parseImageHeaderEmptyArrayTest() throws Exception {
    parser.parseImageHeader(DUMMY_IMAGE_HEADER.get(1));
  }

  @Test(expected = UnsupportedDataFormat.class)
  public void parseImageHeaderWrongSizeArrayTest() throws Exception {
    parser.parseImageHeader(DUMMY_IMAGE_HEADER.get(2));
  }

  @Test(expected = UnsupportedDataFormat.class)
  public void parseImageHeaderWrongBytesForPixelTest() throws Exception {
    parser.parseImageHeader(DUMMY_IMAGE_HEADER.get(3));
  }
}

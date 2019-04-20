package com.labs.introtoprogramming.lab4.image.io.bmp;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import org.junit.Assert;
import org.junit.Test;

public class BMPImageReaderTests {
  private static final List<byte[]> DUMMY_FILE_HEADER = Arrays.asList(
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0},
          new byte[0],
          new byte[14]
  );

  private static final List<byte[]> DUMMY_IMAGE_HEADER = Arrays.asList(
          new byte[]{40, 0, 0, 0, 65, 0, 0, 0, 65, 0, 0, 0, 1, 0, 24, 0, 0, 0, 0, 0,
                  -58, 49, 0, 0, 18, 11, 0, 0, 18, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
          },
          new byte[0],
          new byte[40]
  );

  private static final List<byte[]> DUMMY_PIXEL_DATA = Arrays.asList(
          new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
          },
          new byte[]{0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1,
                  0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1
          }
  );

  private static final List<byte[][]> DUMMY_MATRIX_TO_SWAP_ROWS = Arrays.asList(
          new byte[][]{
                  {1},
                  {2},
                  {3}
          },
          new byte[][] {}
  );

  private static final List<byte[][]> DUMMY_MATRIX_TO_SORT = Arrays.asList(
          new byte[][]{
                  {1},
                  {2},
                  {3}
          },
          new byte[][] {}
  );

  private static final List<byte[]> DUMMY_DATA_TO_READ = Arrays.asList(
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0,
                  40, 0, 0, 0, 4, 0, 0, 0, 3, 0, 0, 0, 1, 0, 24, 0, 0, 0, 0, 0,
                  -58, 49, 0, 0, 18, 11, 0, 0, 18, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1

          },
          new byte[] {}
  );

  @Test
  public void loadHeaderInfoTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_FILE_HEADER.get(0)));
    reader.loadHeaderInfo();
    Assert.assertEquals(54, reader.offset);
  }

  @Test(expected = Exception.class)
  public void loadHeaderInfoNoStreamDataTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_FILE_HEADER.get(1)));
    reader.loadHeaderInfo();
  }

  @Test(expected = Exception.class)
  public void loadHeaderInfoEmptyStreamTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_FILE_HEADER.get(2)));
    reader.loadHeaderInfo();
  }


  @Test
  public void loadImageHeaderInfoTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_IMAGE_HEADER.get(0)));
    reader.loadImageHeaderInfo();
    Assert.assertEquals(65, reader.width);
    Assert.assertEquals(65, reader.height);
    Assert.assertEquals(3, reader.bytesPerPixel);
  }

  @Test(expected = Exception.class)
  public void loadImageHeaderInfoNoStreamDataTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_IMAGE_HEADER.get(1)));
    reader.loadImageHeaderInfo();
  }

  @Test(expected = Exception.class)
  public void loadImageHeaderInfoEmptyStreamTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_IMAGE_HEADER.get(2)));
    reader.loadImageHeaderInfo();
  }

  @Test
  public void loadPixelDataTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_PIXEL_DATA.get(0)));
    reader.width = 4;
    reader.height = 3;
    reader.bytesPerPixel = 3;
    reader.loadPixelData();
    Assert.assertEquals(reader.red.length, 3);
    Assert.assertEquals(reader.red[0].length, 4);
    Assert.assertEquals(-1, reader.red[0][0]);
  }

  @Test
  public void loadPixelDataPaddingTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_PIXEL_DATA.get(1)));
    reader.width = 2;
    reader.height = 4;
    reader.bytesPerPixel = 3;
    reader.loadPixelData();
    Assert.assertEquals(reader.red.length, 4);
    Assert.assertEquals(reader.red[0].length, 2);
    Assert.assertEquals(-1, reader.red[0][0]);
  }

  @Test
  public void swapRowTest() {
    byte[][] matrix = DUMMY_MATRIX_TO_SWAP_ROWS.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.swapRow(matrix, 0, matrix.length - 1);
    Assert.assertArrayEquals(new byte[]{3}, matrix[0]);
    Assert.assertArrayEquals(new byte[]{1}, matrix[matrix.length - 1]);
  }

  @Test
  public void swapSameRowTest() {
    byte[][] matrix = DUMMY_MATRIX_TO_SWAP_ROWS.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.swapRow(matrix, 1, 1);
    Assert.assertArrayEquals(new byte[]{2}, matrix[1]);
  }

  @Test
  public void swapRowEmptyMatrixTest() {
    byte[][] matrix = DUMMY_MATRIX_TO_SWAP_ROWS.get(1);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.swapRow(matrix, 0, matrix.length - 1);
  }

  @Test
  public void sortRowsTest() {
    byte[][] matrix = DUMMY_MATRIX_TO_SORT.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.red = copyMatrix(matrix);
    reader.green = copyMatrix(matrix);
    reader.blue = copyMatrix(matrix);
    reader.height = 3;
    reader.sortRows();
    Assert.assertArrayEquals(new byte[]{3}, reader.red[0]);
    Assert.assertArrayEquals(new byte[]{2}, reader.red[1]);
    Assert.assertArrayEquals(new byte[]{1}, reader.red[2]);
  }

  @Test
  public void sortRowsNegativeHeightTest() {
    byte[][] matrix = DUMMY_MATRIX_TO_SORT.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.red = reader.green = reader.blue = matrix;
    reader.height = -3;
    reader.sortRows();
    Assert.assertArrayEquals(new byte[]{1}, reader.red[0]);
    Assert.assertArrayEquals(new byte[]{2}, reader.red[1]);
    Assert.assertArrayEquals(new byte[]{3}, reader.red[2]);
  }

  @Test
  public void sortRowsEmptyMatrixTest() {
    byte[][] matrix = DUMMY_MATRIX_TO_SORT.get(1);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.red = reader.green = reader.blue = matrix;
    reader.height = 1;
    reader.sortRows();
  }

  @Test
  public void readTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(DUMMY_DATA_TO_READ.get(0)));
    RGBImage image = reader.read();
    Assert.assertEquals(4, image.width());
    Assert.assertEquals(3, image.height());
  }

  @Test(expected = Exception.class)
  public void readEmptyStreamTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(DUMMY_DATA_TO_READ.get(1)));
    RGBImage image = reader.read();
  }

  private static byte[][] copyMatrix(byte[][] matrix) {
    return Arrays.copyOfRange(matrix, 0, matrix.length);
  }
}

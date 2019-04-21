package com.labs.introtoprogramming.lab4.image.io.bmp;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.io.ImageLoadException;
import com.labs.introtoprogramming.lab4.image.io.UnsupportedDataFormat;
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
          new byte[40],
          new byte[]{40, 0, 0, 0}
  );

  private static final List<byte[]> DUMMY_PIXEL_DATA = Arrays.asList(
          new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
          },
          new byte[]{0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1,
                  0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1
          },
          new byte[]{0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1,
                  0, 0, -1, -1, -1, -1, -1, -1, 0,
          },
          new byte[]{0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1,
                  0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1
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
          new byte[] {},
          new byte[]{66, 77, -4, 49, 0, 0, 0, 0, 0, 0, 55, 0, 0, 0,
                  40, 0, 0, 0, 4, 0, 0, 0, 3, 0, 0, 0, 1, 0, 24, 0, 0, 0, 0, 0,
                  -58, 49, 0, 0, 18, 11, 0, 0, 18, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          }
  );

  @Test
  public void loadHeaderInfoTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_FILE_HEADER.get(0)));
    reader.loadHeaderInfo();
    assertEquals(40, reader.offset);
  }

  @Test(expected = ImageLoadException.class)
  public void loadHeaderInfoNoStreamDataTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_FILE_HEADER.get(1)));
    reader.loadHeaderInfo();
  }

  @Test(expected = UnsupportedDataFormat.class)
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
    assertEquals(65, reader.width);
    assertEquals(65, reader.height);
    assertEquals(3, reader.bytesPerPixel);
  }

  @Test(expected = ImageLoadException.class)
  public void loadImageHeaderInfoNoStreamDataTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_IMAGE_HEADER.get(1)));
    reader.loadImageHeaderInfo();
  }

  @Test(expected = UnsupportedDataFormat.class)
  public void loadImageHeaderInfoEmptyStreamTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_IMAGE_HEADER.get(2)));
    reader.loadImageHeaderInfo();
  }

  @Test(expected = ImageLoadException.class)
  public void loadImageHeaderNoDataTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_IMAGE_HEADER.get(3)));
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
    assertEquals(reader.red.length, 3);
    assertEquals(reader.red[0].length, 4);
    assertEquals(-1, reader.red[0][0]);
  }

  @Test
  public void loadPixelDataPaddingTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_PIXEL_DATA.get(1)));
    reader.width = 2;
    reader.height = 4;
    reader.bytesPerPixel = 3;
    reader.loadPixelData();
    assertEquals(reader.red.length, 4);
    assertEquals(reader.red[0].length, 2);
    assertEquals(-1, reader.red[0][0]);
  }

  @Test(expected = ImageLoadException.class)
  public void loadPixelDataWrongPaddingEndTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_PIXEL_DATA.get(2)));
    reader.width = 2;
    reader.height = 4;
    reader.bytesPerPixel = 3;
    reader.loadPixelData();
  }

  @Test(expected = ImageLoadException.class)
  public void loadPixelDataWrongPixelDataLengthTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_PIXEL_DATA.get(3)));
    reader.width = 2;
    reader.height = 4;
    reader.bytesPerPixel = 3;
    reader.loadPixelData();
  }

  @Test
  public void swapRowTest() {
    byte[][] matrix = DUMMY_MATRIX_TO_SWAP_ROWS.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.swapRow(matrix, 0, matrix.length - 1);
    assertArrayEquals(new byte[]{3}, matrix[0]);
    assertArrayEquals(new byte[]{1}, matrix[matrix.length - 1]);
  }

  @Test
  public void swapSameRowTest() {
    byte[][] matrix = DUMMY_MATRIX_TO_SWAP_ROWS.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.swapRow(matrix, 1, 1);
    assertArrayEquals(new byte[]{2}, matrix[1]);
  }

  @Test
  public void swapRowEmptyMatrixNoExceptionTest() {
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
    assertArrayEquals(new byte[]{3}, reader.red[0]);
    assertArrayEquals(new byte[]{2}, reader.red[1]);
    assertArrayEquals(new byte[]{1}, reader.red[2]);
  }

  @Test
  public void sortRowsNegativeHeightTest() {
    byte[][] matrix = DUMMY_MATRIX_TO_SORT.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.red = reader.green = reader.blue = matrix;
    reader.height = -3;
    reader.sortRows();
    assertArrayEquals(new byte[]{1}, reader.red[0]);
    assertArrayEquals(new byte[]{2}, reader.red[1]);
    assertArrayEquals(new byte[]{3}, reader.red[2]);
  }

  @Test
  public void sortRowsEmptyMatrixNoExceptionTest() {
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
    assertEquals(4, image.width());
    assertEquals(3, image.height());
  }

  @Test(expected = ImageLoadException.class)
  public void readEmptyStreamTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(DUMMY_DATA_TO_READ.get(1)));
    reader.read();
  }

  @Test(expected = ImageLoadException.class)
  public void readStreamLessThenOffsetTest() throws Exception {
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(DUMMY_DATA_TO_READ.get(2)));
    reader.read();
  }

  // Shallow copy of matrix.
  // Used for sortRows tests.
  // No need for deep copy due to sortRows does not change values in rows only their order.
  private static byte[][] copyMatrix(byte[][] matrix) {
    return Arrays.copyOfRange(matrix, 0, matrix.length);
  }
}

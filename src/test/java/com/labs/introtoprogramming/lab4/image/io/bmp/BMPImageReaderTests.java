package com.labs.introtoprogramming.lab4.image.io.bmp;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import com.labs.introtoprogramming.lab4.image.Pixel;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.io.ImageReadException;
import com.labs.introtoprogramming.lab4.image.io.UnsupportedDataFormatException;
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
          new byte[40],
          new byte[]{40, 0, 0, 0}
  );

  private static final List<byte[]> DUMMY_PIXEL_DATA = Arrays.asList(
          new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
          },
          new byte[]{-1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1,
                  0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1, 0, 0
          },
          new byte[]{-1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1,
                  0, 0, -1, -1, -1, -1, -1, -1, 0,
          },
          new byte[]{-1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1,
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
  public void loadHeaderInfoTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_FILE_HEADER.get(0)));
    reader.loadHeaderInfo();
    assertEquals(40, reader.offset);
    reader.close();
  }

  @Test(expected = ImageReadException.class)
  public void loadHeaderInfoNoStreamDataTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_FILE_HEADER.get(1)));
    reader.loadHeaderInfo();
    reader.close();
  }

  @Test(expected = UnsupportedDataFormatException.class)
  public void loadHeaderInfoEmptyStreamTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_FILE_HEADER.get(2)));
    reader.loadHeaderInfo();
    reader.close();
  }


  @Test
  public void loadImageHeaderInfoTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_IMAGE_HEADER.get(0)));
    reader.loadImageHeaderInfo();
    assertEquals(65, reader.width);
    assertEquals(65, reader.height);
    assertEquals(3, reader.bytesPerPixel);
    reader.close();
  }

  @Test(expected = ImageReadException.class)
  public void loadImageHeaderInfoNoStreamDataTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_IMAGE_HEADER.get(1)));
    reader.loadImageHeaderInfo();
    reader.close();
  }

  @Test(expected = UnsupportedDataFormatException.class)
  public void loadImageHeaderInfoEmptyStreamTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_IMAGE_HEADER.get(2)));
    reader.loadImageHeaderInfo();
    reader.close();
  }

  @Test(expected = ImageReadException.class)
  public void loadImageHeaderNoDataTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_IMAGE_HEADER.get(3)));
    reader.loadImageHeaderInfo();
    reader.close();
  }

  @Test
  public void loadPixelDataTest() throws IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_PIXEL_DATA.get(0)));
    reader.width = 4;
    reader.height = 3;
    reader.bytesPerPixel = 3;
    reader.loadPixelData();
    assertEquals(reader.red.length, 3);
    assertEquals(reader.red[0].length, 4);
    assertEquals(-1, reader.red[0][0]);
    reader.close();
  }

  @Test
  public void loadPixelDataPaddingTest() throws IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_PIXEL_DATA.get(1)));
    reader.width = 2;
    reader.height = 4;
    reader.bytesPerPixel = 3;
    reader.loadPixelData();
    assertEquals(reader.red.length, 4);
    assertEquals(reader.red[0].length, 2);
    assertEquals(-1, reader.red[0][0]);
    reader.close();
  }

  @Test(expected = ImageReadException.class)
  public void loadPixelDataWrongPaddingEndTest() throws IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_PIXEL_DATA.get(2)));
    reader.width = 2;
    reader.height = 4;
    reader.bytesPerPixel = 3;
    reader.loadPixelData();
    reader.close();
  }

  @Test(expected = ImageReadException.class)
  public void loadPixelDataWrongPixelDataLengthTest() throws IOException {
    BMPImageReader reader = new BMPImageReader(
            new ByteArrayInputStream(DUMMY_PIXEL_DATA.get(3)));
    reader.width = 2;
    reader.height = 4;
    reader.bytesPerPixel = 3;
    reader.loadPixelData();
    reader.close();
  }

  @Test
  public void swapRowTest() throws IOException {
    byte[][] matrix = DUMMY_MATRIX_TO_SWAP_ROWS.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.swapRow(matrix, 0, matrix.length - 1);
    assertArrayEquals(new byte[]{3}, matrix[0]);
    assertArrayEquals(new byte[]{1}, matrix[matrix.length - 1]);
    reader.close();
  }

  @Test
  public void swapSameRowTest() throws IOException {
    byte[][] matrix = DUMMY_MATRIX_TO_SWAP_ROWS.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.swapRow(matrix, 1, 1);
    assertArrayEquals(new byte[]{2}, matrix[1]);
    reader.close();
  }

  @Test
  public void swapRowEmptyMatrixNoExceptionTest() throws IOException {
    byte[][] matrix = DUMMY_MATRIX_TO_SWAP_ROWS.get(1);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.swapRow(matrix, 0, matrix.length - 1);
    reader.close();
  }

  @Test
  public void sortRowsTest() throws IOException {
    byte[][] matrix = DUMMY_MATRIX_TO_SORT.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.red = copyMatrix(matrix);
    reader.green = copyMatrix(matrix);
    reader.blue = copyMatrix(matrix);
    reader.height = 3;
    reader.sortRows();
    assertArrayEquals(new byte[]{1}, reader.red[0]);
    assertArrayEquals(new byte[]{2}, reader.red[1]);
    assertArrayEquals(new byte[]{1}, reader.red[2]);
    reader.close();
  }

  @Test
  public void sortRowsNegativeHeightTest() throws IOException {
    byte[][] matrix = DUMMY_MATRIX_TO_SORT.get(0);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.red = copyMatrix(matrix);
    reader.green = copyMatrix(matrix);
    reader.blue = copyMatrix(matrix);
    reader.height = -3;
    reader.sortRows();
    assertArrayEquals(new byte[]{3}, reader.red[0]);
    assertArrayEquals(new byte[]{2}, reader.red[1]);
    assertArrayEquals(new byte[]{3}, reader.red[2]);
    reader.close();
  }

  @Test
  public void sortRowsEmptyMatrixNoExceptionTest() throws IOException {
    byte[][] matrix = DUMMY_MATRIX_TO_SORT.get(1);
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(new byte[0]));
    reader.red = reader.green = reader.blue = matrix;
    reader.height = 1;
    reader.sortRows();
    reader.close();
  }

  @Test
  public void readTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(DUMMY_DATA_TO_READ.get(0)));
    RGBImage image = reader.read();
    assertEquals(4, image.width());
    assertEquals(3, image.height());
    reader.close();
  }

  @Test(expected = ImageReadException.class)
  public void readEmptyStreamTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(DUMMY_DATA_TO_READ.get(1)));
    reader.read();
    reader.close();
  }

  @Test(expected = ImageReadException.class)
  public void readStreamLessThenOffsetTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(DUMMY_DATA_TO_READ.get(2)));
    reader.read();
    reader.close();
  }

  @Test
  public void readImageTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(new FileInputStream("assets/tru256.BMP"));
    RGBImage image = reader.read();
    Assert.assertEquals(256, image.width());
    Assert.assertEquals(64, image.height());
    reader.close();
  }

  @Test
  public void read2x2ImageTest() throws UnsupportedDataFormatException, IOException {
    BMPImageReader reader = new BMPImageReader(new FileInputStream("assets/test2x2.bmp"));
    RGBImage image = reader.read();
    Assert.assertEquals(2, image.width());
    Assert.assertEquals(2, image.height());
    assertEquals(new Pixel((byte) 255, (byte) 0, (byte) 0), image.getPixel(0, 0));
    assertEquals(new Pixel((byte) 0, (byte) 187, (byte) 0), image.getPixel(0, 1));
    assertEquals(new Pixel((byte) 0, (byte) 0, (byte) 221), image.getPixel(1, 0));
    assertEquals(new Pixel((byte) 170, (byte) 204, (byte) 238), image.getPixel(1, 1));
  }

  /**
   * Shallow copy of matrix.
   * Used for sortRows tests.
   * No need for deep copy due to sortRows does not change values in rows only their order.
   */
  private static byte[][] copyMatrix(byte[][] matrix) {
    return Arrays.copyOfRange(matrix, 0, matrix.length);
  }
}

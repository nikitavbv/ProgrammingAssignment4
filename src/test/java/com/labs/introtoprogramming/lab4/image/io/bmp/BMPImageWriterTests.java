package com.labs.introtoprogramming.lab4.image.io.bmp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.io.UnsupportedDataFormatException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.junit.Test;

public class BMPImageWriterTests {

  private static final RGBImage DUMMY_IMAGE = new RGBImage(2, 2, new byte[][] {
          { (byte) 0xFF, 0 },
          { 0, (byte) 0xAA}
  }, new byte[][] {
          { 0, (byte) 0xBB},
          { 0, (byte) 0xCC}
  }, new byte[][] {
          { 0, 0 },
          { (byte) 0xDD, (byte) 0xEE}
  });

  @Test
  public void testPixelStorageRowSizeForZeroWidthImage() throws IOException {
    BMPImageWriter writer = new BMPImageWriter(new ByteArrayOutputStream());
    assertEquals(0, writer.getPixelStorageRowSize(
            new RGBImage(0, 0, new byte[][]{}, new byte[][]{}, new byte[][]{})
    ));
    writer.close();
  }

  @Test
  public void testPixelStorageRowSizeForSinglePixelWidthImage() throws IOException {
    BMPImageWriter writer = new BMPImageWriter(new ByteArrayOutputStream());
    assertEquals(4, writer.getPixelStorageRowSize(
            new RGBImage(1, 1, new byte[][]{{0}}, new byte[][]{{0}}, new byte[][]{{0}})
    ));
    writer.close();
  }

  @Test
  public void testPixelStorageRowSizeDummyImage() throws IOException {
    BMPImageWriter writer = new BMPImageWriter(new ByteArrayOutputStream());
    assertEquals(8, writer.getPixelStorageRowSize(DUMMY_IMAGE));
    writer.close();
  }

  @Test
  public void testPixelStorageRowByteBuffer() throws IOException {
    BMPImageWriter writer = new BMPImageWriter(new ByteArrayOutputStream());
    assertArrayEquals(
            new byte[] { 0, 0, (byte) 0xFF, 0, (byte) 0xBB, 0, 0, 0 },
            writer.pixelStorageRowAsByteBuffer(DUMMY_IMAGE, 0).array()
    );
    writer.close();
  }

  @Test
  public void testPixelStorageByteBuffer() throws IOException {
    BMPImageWriter writer = new BMPImageWriter(new ByteArrayOutputStream());
    assertArrayEquals(
            new byte[] {
              (byte) 0xDD, 0, 0,
              (byte) 0xEE, (byte) 0xCC, (byte) 0xAA,
              0, 0, 0,
              0, (byte) 0xFF, 0,
              (byte) 0xBB, 0, 0, 0
            },
            writer.pixelStorageAsByteBuffer(DUMMY_IMAGE).array()
    );
    writer.close();
  }

  @Test
  public void testDIBHeaderByteBuffer() throws IOException {
    BMPImageWriter writer = new BMPImageWriter(new ByteArrayOutputStream());
    assertArrayEquals(
            new byte[] {
              40, 0, 0, 0, 2, 0, 0, 0, 2, 0,
              0, 0, 1, 0, 24, 0, 0, 0, 0, 0,
              16, 0, 0, 0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            },
            writer.dibHeaderAsByteBuffer(DUMMY_IMAGE, 16).array()
    );
    writer.close();
  }

  @Test
  public void testHeaderByteBuffer() throws IOException {
    BMPImageWriter writer = new BMPImageWriter(new ByteArrayOutputStream());
    assertArrayEquals(
            new byte[] {
              0x42, 0x4D,
              14 + 40 + 16, 0, 0, 0,
              0, 0, 0, 0,
              14 + 40, 0, 0, 0
            },
            writer.headerAsByteBuffer(40, 16).array()
    );
    writer.close();
  }

  @Test
  public void testWritingImage() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    BMPImageWriter writer = new BMPImageWriter(output);
    writer.write(DUMMY_IMAGE);
    writer.close();

    ImageReader reader = ImageIO.getImageReadersByFormatName("bmp").next();
    ImageInputStream imageInputStream = ImageIO.createImageInputStream(
            new ByteArrayInputStream(output.toByteArray()));
    reader.setInput(imageInputStream, true);

    BufferedImage image = reader.read(0);
    assertEquals(new Color(255, 0, 0).getRGB(), image.getRGB(0, 0));
    assertEquals(new Color(0, 187, 0).getRGB(), image.getRGB(1, 0));
    assertEquals(new Color(0, 0, 221).getRGB(), image.getRGB(0, 1));
    assertEquals(new Color(170, 204, 238).getRGB(), image.getRGB(1, 1));
  }

  @Test
  public void testWriteAndReadImage() throws IOException, UnsupportedDataFormatException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    BMPImageWriter writer = new BMPImageWriter(output);
    writer.write(DUMMY_IMAGE);
    writer.close();

    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(output.toByteArray()));
    RGBImage image = reader.read();
    assertEquals(DUMMY_IMAGE.width(), image.width());
    assertEquals(DUMMY_IMAGE.height(), image.height());
    assertArrayEquals(DUMMY_IMAGE.redChannel(), image.redChannel());
    assertArrayEquals(DUMMY_IMAGE.greenChannel(), image.greenChannel());
    assertArrayEquals(DUMMY_IMAGE.blueChannel(), image.blueChannel());
  }

}

package com.labs.introtoprogramming.lab4.image.io.bmp;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.io.ImageWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BMPImageWriter implements ImageWriter {

  private static final int NUMBER_OF_BITS_PER_PIXEL = 24;
  private static final int COMPRESSION_METHOD = 0; // none
  private static final int IMAGE_HORIZONTAL_RESOLUTION = 0; // pixel per metre
  private static final int IMAGE_VERTICAL_RESOLUTION = 0; // pixel per metre
  private static final int NUMBERS_IN_COLOR_PALETTE = 0; // default to 2^n
  private static final int NUMBER_OF_IMPORTANT_COLORS_USED = 0; // every color is important

  private static final int HEADER_LENGTH = 14;
  private static final int DIB_HEADER_LENGTH = 40;

  private final OutputStream out;

  public BMPImageWriter(OutputStream out) {
    this.out = out;
  }

  @Override
  public void write(RGBImage image) throws IOException {
    ByteBuffer pixelStorage = pixelStorageAsByteBuffer(image);
    ByteBuffer dibHeader = dibHeaderAsByteBuffer(image, pixelStorage.position());
    ByteBuffer header = headerAsByteBuffer(dibHeader.position(), pixelStorage.position());

    out.write(header.array());
    out.write(dibHeader.array());
    out.write(pixelStorage.array());
  }

  private ByteBuffer headerAsByteBuffer(int dibHeaderSize, int pixelStorageSize) {
    int fileSize = HEADER_LENGTH + dibHeaderSize + pixelStorageSize;
    int dataOffset = fileSize - pixelStorageSize;

    ByteBuffer buffer = ByteBuffer.allocate(HEADER_LENGTH);
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    buffer.put(new byte[]{ 0x42, 0x4D }); // header field
    buffer.putInt(fileSize);
    buffer.put(new byte[]{ 0, 0 }); // reserved
    buffer.put(new byte[]{ 0, 0 }); // reserved
    buffer.putInt(dataOffset);

    return buffer;
  }

  private ByteBuffer dibHeaderAsByteBuffer(RGBImage image, int pixelStorageSize) {
    ByteBuffer buffer = ByteBuffer.allocate(DIB_HEADER_LENGTH);
    buffer.putInt(DIB_HEADER_LENGTH);
    buffer.putInt(image.width());
    buffer.putInt(image.height());
    buffer.putShort((short) 1); // the number of color planes
    buffer.putShort((short) NUMBER_OF_BITS_PER_PIXEL);
    buffer.putInt(COMPRESSION_METHOD);
    buffer.putInt(pixelStorageSize);
    buffer.putInt(IMAGE_HORIZONTAL_RESOLUTION);
    buffer.putInt(IMAGE_VERTICAL_RESOLUTION);
    buffer.putInt(NUMBERS_IN_COLOR_PALETTE);
    buffer.putInt(NUMBER_OF_IMPORTANT_COLORS_USED);

    return buffer;
  }

  private ByteBuffer pixelStorageAsByteBuffer(RGBImage image) {
    ByteBuffer pixelStorage = ByteBuffer.allocate(getPixelStorageRowSize(image) * image.height());
    for (int i = 0; i < image.height(); i++) {
      pixelStorage.put(pixelStorageRowAsByteBuffer(image, i));
    }
    return pixelStorage;
  }

  private ByteBuffer pixelStorageRowAsByteBuffer(RGBImage image, int row) {
    ByteBuffer rowData = ByteBuffer.allocate(getPixelStorageRowSize(image));
    byte[] red = image.redChannel()[row];
    byte[] green = image.greenChannel()[row];
    byte[] blue = image.blueChannel()[row];
    for (int i = 0; i < image.width(); i++) {
      rowData.put(red[i]);
      rowData.put(green[i]);
      rowData.put(blue[i]);
    }
    return rowData;
  }

  private int getPixelStorageRowSize(RGBImage image) {
    return (int) Math.ceil(NUMBER_OF_BITS_PER_PIXEL * image.width() / 32) * 4;
  }

}

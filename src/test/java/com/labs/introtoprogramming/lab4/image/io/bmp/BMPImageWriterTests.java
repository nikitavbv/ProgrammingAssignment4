package com.labs.introtoprogramming.lab4.image.io.bmp;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import java.io.ByteArrayOutputStream;
import org.junit.Test;

public class BMPImageWriterTests {

  @Test
  public void testImageWrite() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    BMPImageWriter writer = new BMPImageWriter(output);
    writer.write(new RGBImage(0, 0, null, null, null));
  }

}

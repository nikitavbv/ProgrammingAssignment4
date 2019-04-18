package com.labs.introtoprogramming.lab4.image.io.bmp;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import java.io.ByteArrayInputStream;
import org.junit.Test;

public class BMPImageReaderTests {

  @Test
  public void testReadImage() {
    byte[] imageData = new byte[]{};
    BMPImageReader reader = new BMPImageReader(new ByteArrayInputStream(imageData));
    RGBImage image = reader.read();
  }

}

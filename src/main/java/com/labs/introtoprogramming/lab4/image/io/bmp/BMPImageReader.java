package com.labs.introtoprogramming.lab4.image.io.bmp;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.io.ImageReader;
import java.io.InputStream;

public class BMPImageReader implements ImageReader {

  private final InputStream in;

  public BMPImageReader(InputStream in) {
    this.in = in;
  }

  @Override
  public RGBImage read() {
    // TODO: implement this
    return new RGBImage(0, 0);
  }
}

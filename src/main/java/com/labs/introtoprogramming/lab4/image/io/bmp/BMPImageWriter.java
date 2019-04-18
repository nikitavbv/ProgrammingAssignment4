package com.labs.introtoprogramming.lab4.image.io.bmp;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.io.ImageWriter;
import java.io.OutputStream;

public class BMPImageWriter implements ImageWriter {

  private final OutputStream out;

  public BMPImageWriter(OutputStream out) {
    this.out = out;
  }

  @Override
  public void write(RGBImage image) {
    // TODO: implement this
  }
}

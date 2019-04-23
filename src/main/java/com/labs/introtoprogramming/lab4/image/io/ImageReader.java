package com.labs.introtoprogramming.lab4.image.io;

import com.labs.introtoprogramming.lab4.image.RGBImage;

import java.io.IOException;

public interface ImageReader {

  RGBImage read() throws IOException, UnsupportedDataFormatException;
  void close() throws IOException;
}

package com.labs.introtoprogramming.lab4.image.io;

import com.labs.introtoprogramming.lab4.image.RGBImage;

import java.io.IOException;

public interface ImageReader {

  public RGBImage read() throws IOException;

}

package com.labs.introtoprogramming.lab4.image.io;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import java.io.IOException;

public interface ImageWriter {

  public void write(RGBImage image) throws IOException;

}

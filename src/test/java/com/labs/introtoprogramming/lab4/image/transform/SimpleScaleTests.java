package com.labs.introtoprogramming.lab4.image.transform;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import org.junit.Test;

public class SimpleScaleTests {

  @Test
  public void testScale() {
    RGBImage input = new RGBImage(0, 0, null, null, null);
    SimpleScale scale = new SimpleScale(2);
    RGBImage output = scale.applyTo(input);
  }

}

package com.labs.introtoprogramming.lab4.image.transform;

import static org.junit.Assert.assertEquals;

import com.labs.introtoprogramming.lab4.image.Pixel;
import com.labs.introtoprogramming.lab4.image.RGBImage;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SimpleScaleTests {
  private static byte w = -1;
  private static byte b = 0;
  private static List<RGBImage> DUMMY_IMAGES = Arrays.asList(
          new RGBImage(2, 2),
          new RGBImage(2, 2),
          new RGBImage(0, 0),
          new RGBImage(3, 1)
  );

  @BeforeClass
  public static void initImages() {
    RGBImage image = DUMMY_IMAGES.get(0);
    image.setPixel(0, 0, new Pixel(w, w, w));
    image.setPixel(0, 1, new Pixel(w, w, w));
    image.setPixel(1, 0, new Pixel(b, b, b));
    image.setPixel(1, 1, new Pixel(b, b, b));
    image = DUMMY_IMAGES.get(1);
    image.setPixel(0, 0, new Pixel(w, w, w));
    image.setPixel(0, 1, new Pixel(w, w, w));
    image.setPixel(1, 0, new Pixel(b, b, b));
    image.setPixel(1, 1, new Pixel(b, b, b));
    image = DUMMY_IMAGES.get(3);
    image.setPixel(0, 0, new Pixel(w, w, w));
    image.setPixel(0, 1, new Pixel(b, b, b));
    image.setPixel(0, 2, new Pixel(w, w, w));
  }

  @Test
  public void simpleScaleTest() {
    SimpleScale scale = new SimpleScale(2);
    RGBImage scaledImage = scale.applyTo(DUMMY_IMAGES.get(0));
    assertEquals(4, scaledImage.width());
    assertEquals(4, scaledImage.height());
    assertEquals(w, scaledImage.getPixel(0, 0).red());
    assertEquals(w, scaledImage.getPixel(0, 3).red());
    assertEquals(b, scaledImage.getPixel(3, 0).red());
    assertEquals(b, scaledImage.getPixel(3, 3).red());
  }

  @Test
  public void simpleScaleByOneTest() {
    SimpleScale scale = new SimpleScale(1);
    RGBImage scaledImage = scale.applyTo(DUMMY_IMAGES.get(1));
    assertEquals(2, scaledImage.width());
    assertEquals(2, scaledImage.height());
    assertEquals(w, scaledImage.getPixel(0, 0).red());
    assertEquals(w, scaledImage.getPixel(0, 1).red());
    assertEquals(b, scaledImage.getPixel(1, 0).red());
    assertEquals(b, scaledImage.getPixel(1, 1).red());
  }

  @Test
  public void simpleScaleEmptyDataTest() {
    SimpleScale scale = new SimpleScale(100);
    RGBImage scaledImage = scale.applyTo(DUMMY_IMAGES.get(2));
    assertEquals(0, scaledImage.width());
    assertEquals(0, scaledImage.height());
  }

  @Test
  public void simpleScaleRectangleTest() {
    SimpleScale scale = new SimpleScale(2);
    RGBImage scaledImage = scale.applyTo(DUMMY_IMAGES.get(3));
    assertEquals(6, scaledImage.width());
    assertEquals(2, scaledImage.height());
    assertEquals(w, scaledImage.getPixel(0, 0).red());
    assertEquals(b, scaledImage.getPixel(0, 2).red());
    assertEquals(w, scaledImage.getPixel(0, 4).red());
    assertEquals(w, scaledImage.getPixel(1, 0).red());
    assertEquals(b, scaledImage.getPixel(1, 2).red());
    assertEquals(w, scaledImage.getPixel(1, 4).red());  }
}

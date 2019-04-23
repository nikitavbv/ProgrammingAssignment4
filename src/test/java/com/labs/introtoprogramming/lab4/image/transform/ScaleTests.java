package com.labs.introtoprogramming.lab4.image.transform;

import static org.junit.Assert.assertEquals;

import com.labs.introtoprogramming.lab4.image.Pixel;
import com.labs.introtoprogramming.lab4.image.RGBImage;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ScaleTests {
  private static final List<Byte> DUMMY_BYTES = Arrays.asList(
          (byte) 100,
          (byte) 0,
          (byte) 255
  );

  private static final Pixel DUMMY_LEFT_PIXEL = new Pixel((byte) 255, (byte) 0, (byte) 0);
  private static final Pixel DUMMY_RIGHT_PIXEL = new Pixel((byte) 0, (byte) 0, (byte) 255);

  private static final RGBImage DUMMY_IMAGE = new RGBImage(2, 2, new byte[][] {
          { (byte) 0xFF, 0 },
          { 0, (byte) 0xAA}
  }, new byte[][] {
          { 0, (byte) 0xBB},
          { 0, (byte) 0xCC}
  }, new byte[][] {
          { 0, 0 },
          { (byte) 0xDD, (byte) 0xEE}
  });

  @Test
  public void toUnsignedIntTest() {
    assertEquals(100, new Scale(1, 1).toUnsignedInt(DUMMY_BYTES.get(0)));
  }

  @Test
  public void toUnsignedIntMinValueTest() {
    assertEquals(0, new Scale(1, 1).toUnsignedInt(DUMMY_BYTES.get(1)));
  }

  @Test
  public void toUnsignedIntMaxValueIntTest() {
    assertEquals(255, new Scale(1, 1).toUnsignedInt(DUMMY_BYTES.get(2)));
  }

  @Test
  public void lerpTest() {
    assertEquals(204,
            (int) new Scale(1,1).lerp(0.2, 0, 1, 255, 0));
  }

  @Test
  public void lerpLeftLeaningTest() {
    assertEquals(255,
            (int) new Scale(1,1).lerp(0, 0, 1, 255, 0));
  }

  @Test
  public void lerpRightLeaningTest() {
    assertEquals(0,
            (int) new Scale(1,1).lerp(1, 0, 1, 255, 0));
  }

  @Test
  public void lerpLongDistanceTest() {
    assertEquals(242,
            (int) new Scale(1,1).lerp(0.5, 0, 10, 255, 0));
  }

  @Test
  public void colorLerpTest() {
    assertEquals(new Pixel((byte) 204, (byte) 0, (byte) 51),
            new Scale(1, 1).colorLerp(0.2, 0, 1, DUMMY_LEFT_PIXEL, DUMMY_RIGHT_PIXEL));
  }

  @Test
  public void colorLerpLeftLeaningTest() {
    assertEquals(DUMMY_LEFT_PIXEL,
            new Scale(1, 1).colorLerp(0, 0, 1, DUMMY_LEFT_PIXEL, DUMMY_RIGHT_PIXEL));
  }

  @Test
  public void colorLerpLongDistanceTest() {
    assertEquals(new Pixel((byte) 229.5, (byte) 0, (byte) 25.5),
            new Scale(1, 1).colorLerp(1, 0, 10, DUMMY_LEFT_PIXEL, DUMMY_RIGHT_PIXEL));
  }

  @Test
  public void colorLerpRightLeaningTest() {
    assertEquals(DUMMY_RIGHT_PIXEL,
            new Scale(1, 1).colorLerp(1, 0, 1, DUMMY_LEFT_PIXEL, DUMMY_RIGHT_PIXEL));
  }

  @Test
  public void scaleByIntegerTest() {
    Scale scale = new Scale(2, 2);
    RGBImage scaledImage = scale.applyTo(DUMMY_IMAGE);
    assertEquals(4, scaledImage.width());
    assertEquals(4, scaledImage.height());
    assertEquals(scaledImage.getPixel(0, 0), DUMMY_IMAGE.getPixel(0, 0));
    assertEquals(scaledImage.getPixel(3, 0), DUMMY_IMAGE.getPixel(1, 0));
    assertEquals(scaledImage.getPixel(0, 3), DUMMY_IMAGE.getPixel(0, 1));
    assertEquals(scaledImage.getPixel(3, 3), DUMMY_IMAGE.getPixel(1, 1));
  }

  @Test
  public void scaleNotEqualAxisScaleTest() {
    Scale scale = new Scale(1.5, 2.5);
    RGBImage scaledImage = scale.applyTo(DUMMY_IMAGE);
    assertEquals(3, scaledImage.width());
    assertEquals(5, scaledImage.height());
    assertEquals(scaledImage.getPixel(0, 0), DUMMY_IMAGE.getPixel(0, 0));
    assertEquals(scaledImage.getPixel(2, 0), DUMMY_IMAGE.getPixel(1, 0));
    assertEquals(scaledImage.getPixel(0, 4), DUMMY_IMAGE.getPixel(0, 1));
    assertEquals(scaledImage.getPixel(2, 4), DUMMY_IMAGE.getPixel(1, 1));
  }

  @Test
  public void scaleByZeroTest() {
    Scale scale = new Scale(0, 0);
    RGBImage scaledImage = scale.applyTo(DUMMY_IMAGE);
    assertEquals(0, scaledImage.width());
    assertEquals(0, scaledImage.height());
  }
}

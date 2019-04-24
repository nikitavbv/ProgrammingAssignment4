package com.labs.introtoprogramming.lab4.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PixelTest {
  private static List<Pixel> DUMMY_PIXELS = Arrays.asList(
          new Pixel((byte) 0, (byte) 0, (byte) 0),
          new Pixel((byte) 0, (byte) 0, (byte) 0),
          new Pixel((byte) 255, (byte) 255, (byte) 255),
          new Pixel((byte) 255, (byte) 0, (byte) 0),
          new Pixel((byte) 0, (byte) 255, (byte) 0),
          new Pixel((byte) 0, (byte) 0, (byte) 255)
  );

  @Test
  public void equalsTest() {
    assertEquals(DUMMY_PIXELS.get(0), DUMMY_PIXELS.get(1));
  }

  @Test
  public void equalsSameObjectTest() {
    assertEquals(DUMMY_PIXELS.get(0), DUMMY_PIXELS.get(0));
  }

  @Test
  public void equalsNotInstanceOfPixelTest() {
    assertFalse(DUMMY_PIXELS.get(0).equals(new RGBImage(0, 0)));
  }

  @Test
  public void equalsDifferentDataTest() {
    assertNotEquals(DUMMY_PIXELS.get(0), DUMMY_PIXELS.get(2));
  }

  @Test
  public void equalsDifferentRedDataTest() {
    assertNotEquals(DUMMY_PIXELS.get(0), DUMMY_PIXELS.get(3));
  }

  @Test
  public void equalsDifferentGreenDataTest() {
    assertNotEquals(DUMMY_PIXELS.get(0), DUMMY_PIXELS.get(4));
  }

  @Test
  public void equalsDifferentBlueDataTest() {
    assertNotEquals(DUMMY_PIXELS.get(0), DUMMY_PIXELS.get(5));
  }

  @Test
  public void hashCodeTest() {
    assertEquals(0, DUMMY_PIXELS.get(0).hashCode());
  }

  @Test
  public void hashCodeMaxValueTest() {
    assertEquals(16777215, DUMMY_PIXELS.get(2).hashCode());
  }

  @Test
  public void toStringTest() {
    assertEquals("Pixel{r=0, g=0, b=0}", DUMMY_PIXELS.get(0).toString());
  }

  @Test
  public void toStringNegativeValueTest() {
    assertEquals("Pixel{r=-1, g=-1, b=-1}", DUMMY_PIXELS.get(2).toString());
  }
}

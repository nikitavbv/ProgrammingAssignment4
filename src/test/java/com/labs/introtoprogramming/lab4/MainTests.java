package com.labs.introtoprogramming.lab4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.labs.introtoprogramming.lab4.image.io.ImageReader;
import com.labs.introtoprogramming.lab4.image.io.ImageWriter;
import com.labs.introtoprogramming.lab4.image.io.bmp.BMPImageReader;
import com.labs.introtoprogramming.lab4.image.io.bmp.BMPImageWriter;
import com.labs.introtoprogramming.lab4.image.transform.RGBImageTransformation;
import com.labs.introtoprogramming.lab4.image.transform.Scale;
import com.labs.introtoprogramming.lab4.image.transform.SimpleScale;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.junit.Test;

public class MainTests {

  @Test
  public void testMainStarts() throws UnsupportedEncodingException {
    PrintStream stderr = System.err;
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setErr(new PrintStream(output, true, StandardCharsets.UTF_8.name()));

    Main.main(new String[]{});
    assertTrue(new String(output.toByteArray(), StandardCharsets.UTF_8).contains("Usage"));

    System.setErr(stderr);
  }

  @Test
  public void testGetImageTransformationByAction() {
    RGBImageTransformation transformation = new Main().getTransformationByAction("scale", "42")
            .orElseThrow(() -> new AssertionError("No transformation found for \"scale\" action"));
    assertTrue(transformation instanceof SimpleScale);
    assertEquals(42, ((SimpleScale) transformation).times());
  }

  @Test
  public void testGetImageBilinearScaleTransformation() {
    RGBImageTransformation transformation = new Main().getTransformationByAction("bilinear-scale", "42")
            .orElseThrow(() -> new AssertionError("No transformation found for \"bilinear-scale\" action"));
    assertTrue(transformation instanceof Scale);
    assertEquals( 42, Math.round(((Scale) transformation).verticalScale()));
    assertEquals( 42, Math.round(((Scale) transformation).horizontalScale()));
  }

  @Test
  public void testGetImageBilinearScaleTransformationDifferentScales() {
    RGBImageTransformation transformation = new Main().getTransformationByAction("bilinear-scale", "2x3")
            .orElseThrow(() -> new AssertionError("No transformation found for \"bilinear-scale\" action"));
    assertTrue(transformation instanceof Scale);
    assertEquals( 2, Math.round(((Scale) transformation).horizontalScale()));
    assertEquals( 3, Math.round(((Scale) transformation).verticalScale()));
  }

  @Test
  public void testGetImageTransformationByUnknownAction() {
    assertFalse(new Main().getTransformationByAction("unknown-action", "42").isPresent());
  }

  @Test
  public void testGetImageWriterByExtension() {
    ImageWriter writer = new Main().getImageWriterByExtension("bmp", new ByteArrayOutputStream())
            .orElseThrow(() -> new AssertionError("No writer found for \"bmp\" extension"));
    assertTrue(writer instanceof BMPImageWriter);
  }

  @Test
  public void testGetImageWriterByUnknownExtension() {
    assertFalse(new Main().getImageWriterByExtension("unknown", new ByteArrayOutputStream()).isPresent());
  }

  @Test
  public void testGetImageReaderByExtension() {
    ImageReader reader = new Main().getImageReaderByExtension("bmp", new ByteArrayInputStream(new byte[]{}))
            .orElseThrow(() -> new AssertionError("No reader found for \"bmp\" extension"));
    assertTrue(reader instanceof BMPImageReader);
  }

  @Test
  public void testGetImageReaderByUnknownExtension() {
    Optional<ImageReader> reader = new Main()
            .getImageReaderByExtension("unknown", new ByteArrayInputStream(new byte[]{}));
    assertFalse(reader.isPresent());
  }

  @Test
  public void testParseArgs() throws ArgumentsException {
    Main main = new Main();
    main.parseArgs(new String[]{"assets", "assets_test_out", "--scale", "4"});
    assertArrayEquals(new String[]{"assets"}, main.sources.stream().map(File::getName).toArray());
    assertEquals("assets_test_out", main.destination.getName());
    assertEquals(1, main.transformations.size());
    assertTrue(main.transformations.get(0) instanceof SimpleScale);
    assertEquals(4, ((SimpleScale) main.transformations.get(0)).times());
  }

  /** No exceptions in this test = successful pass. */
  @Test
  public void testSourceAndDestinationAreCorrect() throws ArgumentsException {
    Main main = new Main();
    main.parseArgs(new String[]{"assets", "assets_test_out", "--scale", "4"});
    main.checkIfSourceAndDestinationAreCorrect();
  }

  @Test(expected = ArgumentsException.class)
  public void testNoSources() throws ArgumentsException {
    Main main = new Main();
    main.parseArgs(new String[]{"assets", "assets_test_out", "--scale", "4"});
    main.sources.clear();
    main.checkIfSourceAndDestinationAreCorrect();
  }

  @Test(expected = ArgumentsException.class)
  public void testDestinationNotADirectory() throws ArgumentsException, IOException {
    Main main = new Main();
    main.parseArgs(new String[]{"assets", "assets_test_out", "--scale", "4"});
    main.destination = File.createTempFile(".temp_test", "tmp");
    main.checkIfSourceAndDestinationAreCorrect();
  }

  @Test(expected = ArgumentsException.class)
  public void testUnableToCreateDestinationDirectory() throws ArgumentsException {
    Main main = new Main();
    main.parseArgs(new String[]{"assets", "assets_test_out", "--scale", "4"});
    main.destination = new File("\\/\\/\\/\\/\\ invalid \0 path !!!");
    main.checkIfSourceAndDestinationAreCorrect();
  }

  @Test
  public void testProcessFile() {
    Main main = new Main();
    main.destination = new File("assets_test_out");
    if (!main.destination.exists() && !main.destination.mkdirs()) {
      throw new RuntimeException("Failed to create test directory");
    }
    main.transformations.add(new SimpleScale(2));
    assertEquals(0, Objects.requireNonNull(main.destination.listFiles()).length);
    main.processFile(new File("assets/test2x2.bmp"));
    assertEquals(1, Objects.requireNonNull(main.destination.listFiles()).length);

    Arrays.stream(Objects.requireNonNull(main.destination.listFiles()))
            .forEach(file -> {
              if (!file.delete()) {
                throw new RuntimeException("Failed to delete: " + file.getPath());
              }
            });
    if (!main.destination.delete()) {
      throw new RuntimeException("Failed to delete test directory");
    }
  }

  @Test
  public void testProcessFileNoExtension() throws IOException {
    Main main = new Main();
    main.destination = new File("assets_test_out");
    if (!main.destination.exists() && !main.destination.mkdirs()) {
      throw new RuntimeException("Failed to create test directory");
    }
    assertEquals(0, Objects.requireNonNull(main.destination.listFiles()).length);
    main.processFile(File.createTempFile("assets/file_no_ext", ""));
    assertEquals(0, Objects.requireNonNull(main.destination.listFiles()).length);

    if (!main.destination.delete()) {
      throw new RuntimeException("Failed to delete test directory");
    }
  }

  @Test
  public void testProcessFileUnknownExtension() throws IOException {
    Main main = new Main();
    main.destination = new File("assets_test_out");
    if (!main.destination.exists() && !main.destination.mkdirs()) {
      throw new RuntimeException("Failed to create test directory");
    }
    assertEquals(0, Objects.requireNonNull(main.destination.listFiles()).length);
    main.processFile(File.createTempFile("assets/file.unknown_ext", "unknown_ext"));
    assertEquals(0, Objects.requireNonNull(main.destination.listFiles()).length);

    if (!main.destination.delete()) {
      throw new RuntimeException("Failed to delete test directory");
    }
  }

  @Test
  public void testSaveToDirectory() throws UnsupportedEncodingException {
    PrintStream stderr = System.err;
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setErr(new PrintStream(output, true, StandardCharsets.UTF_8.name()));

    Main main = new Main();
    main.destination = new File("assets_test_out");
    if (!main.destination.exists() && !main.destination.mkdirs()) {
      throw new RuntimeException("Failed to create test directory");
    }
    if (!new File("assets_test_out/test2x2.bmp").mkdirs()) {
      throw new RuntimeException("Failed to create test sub-directory");
    }
    main.processFile(new File("assets/test2x2.bmp"));

    assertTrue(new String(output.toByteArray(), StandardCharsets.UTF_8).contains("Warning"));

    Arrays.stream(Objects.requireNonNull(main.destination.listFiles()))
            .forEach(file -> {
              if (!file.delete()) {
                throw new RuntimeException("Failed to delete: " + file.getPath());
              }
            });
    if (!main.destination.delete()) {
      throw new RuntimeException("Failed to delete test directory");
    }

    System.setErr(stderr);
  }

  @Test
  public void testProcessDirectory() {
    Main main = new Main();
    main.destination = new File("assets_test_out");
    if (!main.destination.exists() && !main.destination.mkdirs()) {
      throw new RuntimeException("Failed to create test directory");
    }
    assertEquals(0, Objects.requireNonNull(main.destination.listFiles()).length);
    main.processFile(new File("assets"));
    assertEquals(Objects.requireNonNull(new File("assets").listFiles()).length,
            Objects.requireNonNull(main.destination.listFiles()).length);

    Arrays.stream(Objects.requireNonNull(main.destination.listFiles()))
            .forEach(file -> {
              if (!file.delete()) {
                throw new RuntimeException("Failed to delete: " + file.getPath());
              }
            });
    if (!main.destination.delete()) {
      throw new RuntimeException("Failed to delete test directory");
    }
  }

  @Test
  public void testProcessFileNoOutputExtension() {
    Main main = new Main();
    main.destination = new File("assets_test_out/test2x2");
    if (!main.destination.getParentFile().exists() && !main.destination.getParentFile().mkdirs()) {
      throw new RuntimeException("Failed to create test directory");
    }
    main.transformations.add(new SimpleScale(2));
    assertEquals(0, Objects.requireNonNull(main.destination.getParentFile().listFiles()).length);
    main.processFile(new File("assets/test2x2.bmp"));
    assertEquals(0, Objects.requireNonNull(main.destination.getParentFile().listFiles()).length);

    File[] files = main.destination.getParentFile().listFiles();
    if (files != null) {
      Arrays.stream(files)
              .forEach(file -> {
                if (!file.delete()) {
                  throw new RuntimeException("Failed to delete: " + file.getPath());
                }
              });
    }
    if (!main.destination.getParentFile().delete()) {
      throw new RuntimeException("Failed to delete test directory");
    }
  }

  @Test
  public void testMain() {
    File destination = new File("assets_test_out");
    if (!destination.exists() && !destination.mkdirs()) {
      throw new RuntimeException("Failed to create test directory");
    }
    assertEquals(0, Objects.requireNonNull(destination.listFiles()).length);
    Main.main(new String[]{"assets", "assets_test_out", "--scale", "2"});
    assertEquals(Objects.requireNonNull(new File("assets").listFiles()).length,
            Objects.requireNonNull(destination.listFiles()).length);

    Arrays.stream(Objects.requireNonNull(destination.listFiles()))
            .forEach(file -> {
              if (!file.delete()) {
                throw new RuntimeException("Failed to delete: " + file.getPath());
              }
            });
    if (!destination.delete()) {
      throw new RuntimeException("Failed to delete test directory");
    }
  }
}

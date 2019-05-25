package com.labs.introtoprogramming.lab4;

import com.labs.introtoprogramming.lab4.image.RGBImage;
import com.labs.introtoprogramming.lab4.image.io.ImageReader;
import com.labs.introtoprogramming.lab4.image.io.ImageWriter;
import com.labs.introtoprogramming.lab4.image.io.UnsupportedDataFormatException;
import com.labs.introtoprogramming.lab4.image.io.bmp.BMPImageReader;
import com.labs.introtoprogramming.lab4.image.io.bmp.BMPImageWriter;
import com.labs.introtoprogramming.lab4.image.transform.RGBImageTransformation;
import com.labs.introtoprogramming.lab4.image.transform.Scale;
import com.labs.introtoprogramming.lab4.image.transform.SimpleScale;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Main {

  List<File> sources = new ArrayList<>();
  File destination = null;
  List<RGBImageTransformation> transformations = new ArrayList<>();

  private Set<String> ignoredExtensions = new HashSet<>();

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();

    if (args.length < 3) {
      System.err.println("Usage: [source file(s)] [destination file(s)] [action]");
      return;
    }

    Main main = new Main();
    try {
      main.parseArgs(args);
      main.checkIfSourceAndDestinationAreCorrect();
    } catch(ArgumentsException e) {
      System.err.println(e.getMessage());
      return;
    }
    main.run();

    System.out.printf("All done in %dms.%n", System.currentTimeMillis() - startTime);
  }

  private void run() {
    sources.parallelStream().forEach(this::processFile);
  }

  void processFile(File source) {
    if (source.isDirectory()) {
      processDirectory(source);
      return;
    }

    File dest = destination;
    if (destination.isDirectory()) {
      dest = new File(destination, source.getName());
    }

    if (dest.exists() && dest.isDirectory()) {
      System.err.printf("Warning: output path \"%s\" is a directory.%n", dest.getPath());
      return;
    }

    System.out.printf("Processing file \"%s\"...%n", source.getPath());
    Optional<String> extension = getFileExtension(source);
    if (!extension.isPresent()) {
      return;
    }
    Optional<String> outputExtension = extension;
    if (!destination.isDirectory()) {
      outputExtension = getFileExtension(destination);
    }
    if (!outputExtension.isPresent()) {
      return;
    }

    try {
      transformImage(source, dest, extension.get(), outputExtension.get());
    } catch(IOException e) {
      System.err.printf("IOException while processing file \"%s\"%n", source.getPath());
      e.printStackTrace();
    } catch (UnsupportedDataFormatException e) {
      e.printStackTrace();
    }
  }

  private void transformImage(File source, File dest, String extension, String outputExtension)
          throws IOException, UnsupportedDataFormatException {
    Optional<ImageReader> reader = getImageReaderByExtension(extension, new FileInputStream(source));
    if (!reader.isPresent()) {
      return;
    }
    Optional<ImageWriter> writer = getImageWriterByExtension(outputExtension, new FileOutputStream(dest));
    if (!writer.isPresent()) {
      return;
    }

    RGBImage image = reader.get().read();
    for (RGBImageTransformation transformation : transformations) {
      image = transformation.applyTo(image);
    }
    writer.get().write(image);

    reader.get().close();
    writer.get().close();
  }

  private void processDirectory(File source) {
    File[] fileList = source.listFiles();
    if (fileList == null) {
      throw new AssertionError();
    }
    Arrays.stream(fileList)
            .parallel()
            .forEach(this::processFile);
  }

  private Optional<String> getFileExtension(File file) {
    String fileName = file.getName();
    int extensionIndex = fileName.indexOf('.');

    if (extensionIndex <= 0) {
      return Optional.empty();
    }

    return Optional.of(fileName.substring(extensionIndex + 1));
  }

  void parseArgs(String[] args) throws ArgumentsException {
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (!arg.startsWith("--")) {
        parseFileNameParameter(arg);
      } else {
        String action = arg.replaceFirst("--", "");
        if (i + 1 >= args.length || args[i+1].startsWith("--")) {
          getTransformationByAction(action, null).ifPresent(transformations::add);
        } else {
          getTransformationByAction(action, args[i+1]).ifPresent(transformations::add);
          i++;
        }
      }
    }
  }

  void checkIfSourceAndDestinationAreCorrect() throws ArgumentsException {
    if (sources.size() == 0) {
      throw new ArgumentsException("Error: no files to process specified.");
    }

    if(sources.size() > 1 || sources.get(0).isDirectory()) {
      if (destination.exists()) {
        if (!destination.isDirectory()) {
          throw new ArgumentsException(String.format("Error: \"%s\" is not a directory", destination.getPath()));
        }
      } else {
        if (!destination.mkdirs()) {
          throw new ArgumentsException("Failed to create destination directory");
        }
      }
    }
  }

  private void parseFileNameParameter(String arg) {
    if (destination != null) {
      sources.add(destination);
    }
    destination = new File(arg);
  }

  Optional<ImageReader> getImageReaderByExtension(String extension, InputStream in) {
    switch (extension.toLowerCase()) {
      case "bmp":
        return Optional.of(new BMPImageReader(in));
      default:
        if (!ignoredExtensions.contains(extension)) {
          System.err.printf("Warning: ignoring files with extension \"%s\"%n", extension);
          ignoredExtensions.add(extension);
        }
        return Optional.empty();
    }
  }

  Optional<ImageWriter> getImageWriterByExtension(String extension, OutputStream out) {
    switch (extension.toLowerCase()) {
      case "bmp":
        return Optional.of(new BMPImageWriter(out));
      default:
        if (!ignoredExtensions.contains(extension)) {
          System.err.printf("Warning: ignoring files with extension \"%s\"%n", extension);
          ignoredExtensions.add(extension);
        }
        return Optional.empty();
    }
  }

  Optional<RGBImageTransformation> getTransformationByAction(String action, String param) {
    switch (action) {
      case "scale":
        return Optional.of(new SimpleScale(Integer.parseInt(param)));
      case "bilinear-scale":
        double horizontalScale = 1;
        double verticalScale = 1;
        if (param.contains("x")) {
          String[] spl = param.split("x");
          horizontalScale = Double.parseDouble(spl[0]);
          verticalScale = Double.parseDouble(spl[1]);
        } else {
          horizontalScale = verticalScale = Double.parseDouble(param);
        }
        return Optional.of(new Scale(horizontalScale, verticalScale));
      case "mirror-v":
        return Optional.of(new Scale(-1, 1));
      case "mirror-h":
        return Optional.of(new Scale(1, -1));
      default:
        System.err.printf("Unknown action \"%s\"%n", action);
        return Optional.empty();
    }
  }
}

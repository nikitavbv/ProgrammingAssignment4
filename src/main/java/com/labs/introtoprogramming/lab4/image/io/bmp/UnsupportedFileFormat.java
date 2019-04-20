package com.labs.introtoprogramming.lab4.image.io.bmp;

public class UnsupportedFileFormat extends Exception {
  public UnsupportedFileFormat(String message) {
    super(message);
  }

  public UnsupportedFileFormat(Throwable err) {
    super(err);
  }

  public UnsupportedFileFormat(String message, Throwable err) {
    super(message, err);
  }
}

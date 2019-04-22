package com.labs.introtoprogramming.lab4.image.io;

@SuppressWarnings("unused")
public class ImageReadException extends RuntimeException {
  public ImageReadException(String errorMessage) {
    super(errorMessage);
  }

  public ImageReadException(Throwable err) {
    super(err);
  }

  public ImageReadException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }
}

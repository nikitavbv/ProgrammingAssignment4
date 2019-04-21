package com.labs.introtoprogramming.lab4.image.io;

@SuppressWarnings("unused")
public class ImageLoadException extends RuntimeException {
  public ImageLoadException(String errorMessage) {
    super(errorMessage);
  }

  public ImageLoadException(Throwable err) {
    super(err);
  }

  public ImageLoadException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }
}

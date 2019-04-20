package com.labs.introtoprogramming.lab4.image.io;

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

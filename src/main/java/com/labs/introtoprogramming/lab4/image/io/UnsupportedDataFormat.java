package com.labs.introtoprogramming.lab4.image.io;

public class UnsupportedDataFormat extends Exception {
  public UnsupportedDataFormat(String message) {
    super(message);
  }

  public UnsupportedDataFormat(Throwable err) {
    super(err);
  }

  public UnsupportedDataFormat(String message, Throwable err) {
    super(message, err);
  }
}

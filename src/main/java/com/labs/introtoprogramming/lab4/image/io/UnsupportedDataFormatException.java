package com.labs.introtoprogramming.lab4.image.io;

@SuppressWarnings("unused")
public class UnsupportedDataFormatException extends Exception {
  public UnsupportedDataFormatException(String message) {
    super(message);
  }
  
  public UnsupportedDataFormatException(Throwable err) {
    super(err);
  }

  public UnsupportedDataFormatException(String message, Throwable err) {
    super(message, err);
  }
}

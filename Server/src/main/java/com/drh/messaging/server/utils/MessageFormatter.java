package com.drh.messaging.server.utils;

public final class MessageFormatter {

  private MessageFormatter() {}

  public static byte[] formatMessage(String message, boolean endWithNewLine) {
    String formattedMessage = message + (endWithNewLine ? "\r\n" : "\r");
    return formattedMessage.getBytes();
  }
}

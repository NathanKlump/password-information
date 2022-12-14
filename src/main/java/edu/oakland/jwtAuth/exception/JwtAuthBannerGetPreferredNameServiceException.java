package edu.oakland.jwtAuth.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class JwtAuthBannerGetPreferredNameServiceException extends Exception {
  public final Exception exception;
  public final BannerGetPreferredNameServiceExceptionEnum errorCode;

  public enum BannerGetPreferredNameServiceExceptionEnum {
    COULD_NOT_GET_PREFERRED_NAME,
    A_OK
  };

  public JwtAuthBannerGetPreferredNameServiceException(
      Exception exception, BannerGetPreferredNameServiceExceptionEnum errorCode) {
    super("BannerGetPreferredNameServiceException");
    this.exception = exception == null ? new Exception("Generic Exception") : exception;
    this.errorCode = errorCode;
  }

  public String toString() {
    String toString =
        "Exception: " + saveStackTraceToString(exception) + "\r\n\r\n" + "Error Code: " + errorCode;
    return toString;
  }

  private String saveStackTraceToString(Exception e) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    e.printStackTrace(printWriter);
    return stringWriter.toString();
  }
}

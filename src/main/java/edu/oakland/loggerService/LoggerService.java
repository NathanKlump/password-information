package edu.oakland.loggerService;

import java.util.Base64;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {
  private final Logger frameworkLogger = LoggerFactory.getLogger("FRAMEWORK");
  private final Logger applicationLogger = LoggerFactory.getLogger("APPLICATION");
  private final Logger accessLogger = LoggerFactory.getLogger("ACCESS");
  private final Logger splunkLogger = LoggerFactory.getLogger("SPLUNK");

  public void logAccess(HttpServletRequest request, String message) {
    String ipAddr = request.getHeader("X-FORWARDED-FOR");
    String authHeader = request.getHeader("Authorization");

    String requester =
        (authHeader == null)
            ? "NoAuthProvided"
            : (authHeader.contains("Basic")) ? getRequester(request) : "Unknown";
    ipAddr = (ipAddr == null) ? request.getRemoteAddr() : ipAddr;

    accessLogger.info(
        message
            + " - Request URL: "
            + request.getServletPath()
            + ", Request from IP: "
            + ipAddr
            + " (by: "
            + requester
            + ")");
    splunkLogger.info(
        "BannerApi," + request.getServletPath() + "," + message + "," + requester + "," + ipAddr);
  }

  /** Used for SUCCESS access logging. */
  public void logAccess(HttpServletRequest request) {
    logAccess(request, "SUCCESS");
  }

  private String getRequester(HttpServletRequest request) {
    String[] parts = request.getHeader("Authorization").split(" ");
    String credentials = new String(Base64.getDecoder().decode(parts[1]));

    return credentials.substring(0, credentials.indexOf(':'));
  }

  public void logErrorMessage(String errorMessage, Throwable exception) {
    applicationLogger.error(errorMessage, exception);
  }

  public void logGeneralInfo(String msg) {
    applicationLogger.info(msg);
  }
}

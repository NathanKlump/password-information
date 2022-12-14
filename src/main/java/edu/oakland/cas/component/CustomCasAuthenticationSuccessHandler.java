package edu.oakland.cas.component;

import edu.oakland.cas.service.SuccessfulCasAuthService;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomCasAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  private final SuccessfulCasAuthService successfulCasAuthService;

  @Autowired
  public CustomCasAuthenticationSuccessHandler(SuccessfulCasAuthService successfulCasAuthService) {
    this.successfulCasAuthService = successfulCasAuthService;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Authentication authentication)
      throws IOException, ServletException {
    successfulCasAuthService.successfulCasAuthService(
        httpServletRequest, httpServletResponse, authentication);
  }
}

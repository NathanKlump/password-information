package edu.oakland.cas.pojo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.cas.web.CasAuthenticationEntryPoint;

public class CustomCasAuthenticationEntryPoint extends CasAuthenticationEntryPoint {
  @Override
  protected void preCommence(
      final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
    httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
    httpServletResponse.addHeader("Vary", "Origin");
    httpServletResponse.addHeader("Vary", "Access-Control-Request-Method");
    httpServletResponse.addHeader("Vary", "Access-Control-Request-Headers");
    HttpSession httpSession = httpServletRequest.getSession();
    httpServletResponse.addHeader("Set-Cookie", httpSession.getId());
  }
}

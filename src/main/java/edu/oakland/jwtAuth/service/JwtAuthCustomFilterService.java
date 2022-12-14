package edu.oakland.jwtAuth.service;

import edu.oakland.jwtAuth.exception.JwtAuthBannerGetPreferredNameServiceException;
import edu.oakland.jwtAuth.exception.SoffitAuthException;
import edu.oakland.jwtAuth.model.JwtJsonModel;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthCustomFilterService extends GenericFilterBean {
  private JwtAuthService jwtAuthService;
  private JwtAuthBannerGetPreferredNameService jwtAuthBannerGetPreferredNameService;

  public JwtAuthCustomFilterService(
      JwtAuthService jwtAuthService,
      JwtAuthBannerGetPreferredNameService jwtAuthBannerGetPreferredNameService) {
    this.jwtAuthService = jwtAuthService;
    this.jwtAuthBannerGetPreferredNameService = jwtAuthBannerGetPreferredNameService;
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    if (!isRequestForACasifiedUrl((HttpServletRequest) servletRequest)) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }
    String encryptedJwtToken =
        getBearerTokenValueIfAuthorizationHeaderPresent((HttpServletRequest) servletRequest);
    if (encryptedJwtToken == null) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }
    JwtJsonModel jwtJsonModel = getJwtJsonModelFromEncryptedJwtToken(encryptedJwtToken);
    String preferredName = getPreferredNameFromBanner(Integer.parseInt(jwtJsonModel.pidm));
    saveJwtAndPreferredNameIntoSession(
        (HttpServletRequest) servletRequest, jwtJsonModel, preferredName);
    forwardUserToRequestedUrl(
        (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
  }

  private boolean isRequestForACasifiedUrl(HttpServletRequest httpServletRequest) {
    return httpServletRequest.getRequestURI().contains("/casified/");
  }

  private String getBearerTokenValueIfAuthorizationHeaderPresent(
      HttpServletRequest httpServletRequest) {
    String authorizationHeader = httpServletRequest.getHeader("authorization");
    if (authorizationHeader == null || authorizationHeader.isEmpty()) {
      return null;
    }
    return authorizationHeader.split(" ")[1];
  }

  private JwtJsonModel getJwtJsonModelFromEncryptedJwtToken(String encryptedJwtToken)
      throws IOException {
    String jsonPayloadString = getJsonPayloadStringFromEncryptedJwtToken(encryptedJwtToken);
    return new Gson().fromJson(jsonPayloadString, JwtJsonModel.class);
  }

  private String getPreferredNameFromBanner(int pidm) throws IOException {
    try {
      return jwtAuthBannerGetPreferredNameService.bannerGetPreferredNameService(pidm);
    } catch (JwtAuthBannerGetPreferredNameServiceException e) {
      throw new IOException(e);
    }
  }

  private void saveJwtAndPreferredNameIntoSession(
      HttpServletRequest httpServletRequest, JwtJsonModel jwtJsonModel, String preferredName)
      throws IOException {
    jwtJsonModel.preferredName = preferredName;
    httpServletRequest.getSession().setAttribute("jwtJsonModel", jwtJsonModel);
  }

  private void forwardUserToRequestedUrl(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
      throws IOException, ServletException {
    httpServletRequest
        .getRequestDispatcher(httpServletRequest.getServletPath())
        .forward(httpServletRequest, httpServletResponse);
  }

  private String getJsonPayloadStringFromEncryptedJwtToken(String encryptedJwtToken)
      throws IOException {
    try {
      return jwtAuthService.getJsonBodyFromJWT(encryptedJwtToken);
    } catch (SoffitAuthException e) {
      throw new IOException("JWT failed to decrypt");
    }
  }
}

package edu.oakland.cas.service;

import edu.oakland.cas.dao.RetrieveUserInfoFromNetIdDao;
import edu.oakland.cas.exception.BannerGetPreferredNameServiceException;
import edu.oakland.cas.model.LdapUserModel;
import edu.oakland.cas.model.SessionDataModel;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

@Service
public class SuccessfulCasAuthService {
  private final RetrieveUserInfoFromNetIdDao retrieveUserInfoFromNetIdDao;
  private final BannerGetPreferredNameService bannerGetPreferredNameService;

  @Autowired
  public SuccessfulCasAuthService(
      RetrieveUserInfoFromNetIdDao retrieveUserInfoFromNetIdDao,
      BannerGetPreferredNameService bannerGetPreferredNameService) {
    this.retrieveUserInfoFromNetIdDao = retrieveUserInfoFromNetIdDao;
    this.bannerGetPreferredNameService = bannerGetPreferredNameService;
  }

  public void successfulCasAuthService(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Authentication authentication)
      throws IOException, ServletException {
    HttpSession httpSession = loadHttpSession(httpServletRequest);
    setSessionDataModel(httpSession);
    String netId = getNetId(authentication);
    LdapUserModel ldapUserModel = callDaoToGetLdapUserModel(netId, httpSession);
    String preferredName = callBannerGetPreferredNameService(ldapUserModel.pidm, httpSession);
    savePersonDataIntoSessionDataModel(httpSession, ldapUserModel, preferredName);
    forwardUserToTheUrlTheyWentToPriorToAuthentication(httpServletResponse, httpSession);
  }

  private HttpSession loadHttpSession(HttpServletRequest httpServletRequest) {
    return httpServletRequest.getSession();
  }

  private void setSessionDataModel(HttpSession httpSession) {
    if (httpSession.getAttribute("sessionDataModel") == null) {
      httpSession.setAttribute("sessionDataModel", new SessionDataModel());
    }
  }

  private String getNetId(Authentication authentication) {
    User user = (User) (authentication.getPrincipal());
    return user.getUsername();
  }

  private LdapUserModel callDaoToGetLdapUserModel(String netId, HttpSession httpSession)
      throws ServletException {
    List<LdapUserModel> ldapUserModelList =
        retrieveUserInfoFromNetIdDao.getLdapUserInfoFromNetId(netId);
    if (ldapUserModelList == null || ldapUserModelList.size() != 1) {
      throwModelAndViewExceptionWhenErrorOccurs(httpSession);
    }
    return ldapUserModelList.get(0);
  }

  private String callBannerGetPreferredNameService(int pidm, HttpSession httpSession)
      throws ModelAndViewDefiningException {
    try {
      return bannerGetPreferredNameService.bannerGetPreferredNameService(pidm);
    } catch (BannerGetPreferredNameServiceException e) {
      throwModelAndViewExceptionWhenErrorOccurs(httpSession);
    }
    return null;
  }

  private void savePersonDataIntoSessionDataModel(
      HttpSession httpSession, LdapUserModel ldapUserModel, String preferredName) {
    SessionDataModel sessionDataModel =
        (SessionDataModel) (httpSession.getAttribute("sessionDataModel"));
    sessionDataModel.ldapUserModel = ldapUserModel;
    sessionDataModel.preferredName = preferredName;
    httpSession.setAttribute("sessionDataModel", sessionDataModel);
  }

  private void forwardUserToTheUrlTheyWentToPriorToAuthentication(
      HttpServletResponse httpServletResponse, HttpSession httpSession) throws IOException {
    DefaultSavedRequest defaultSavedRequest =
        (DefaultSavedRequest) (httpSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST"));
    httpServletResponse.sendRedirect(defaultSavedRequest.getRedirectUrl());
  }

  private void throwModelAndViewExceptionWhenErrorOccurs(HttpSession httpSession)
      throws ModelAndViewDefiningException {
    httpSession.invalidate();
    ModelAndView modelAndView = new ModelAndView("LdapErrorPage");
    throw new ModelAndViewDefiningException(modelAndView);
  }
}

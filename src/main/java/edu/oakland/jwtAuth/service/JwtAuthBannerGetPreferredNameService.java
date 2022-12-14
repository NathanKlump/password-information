package edu.oakland.jwtAuth.service;

import edu.oakland.jwtAuth.dao.JwtAuthBannerGetPreferredNameDao;
import edu.oakland.jwtAuth.exception.JwtAuthBannerGetPreferredNameServiceException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthBannerGetPreferredNameService {
  private final JwtAuthBannerGetPreferredNameDao jwtAuthBannerGetPreferredNameDao;

  @Autowired
  public JwtAuthBannerGetPreferredNameService(
      JwtAuthBannerGetPreferredNameDao jwtAuthBannerGetPreferredNameDao) {
    this.jwtAuthBannerGetPreferredNameDao = jwtAuthBannerGetPreferredNameDao;
  }

  public String bannerGetPreferredNameService(int pidmIn)
      throws JwtAuthBannerGetPreferredNameServiceException {
    String pidm = convertIntPidmToString(pidmIn);
    List<String> preferredNamesFromBanner = callBannerGetPreferredNameDao(pidm);
    processResults(preferredNamesFromBanner);
    return preferredNamesFromBanner.get(0);
  }

  private String convertIntPidmToString(int pidm) {
    return Integer.toString(pidm);
  }

  private List<String> callBannerGetPreferredNameDao(String pidm) {
    return jwtAuthBannerGetPreferredNameDao.getPreferredNameFromBanner(pidm);
  }

  private void processResults(List<String> preferredNamesFromBanner)
      throws JwtAuthBannerGetPreferredNameServiceException {
    if (preferredNamesFromBanner == null || preferredNamesFromBanner.isEmpty()) {
      throwExceptionWhenPreferredNameNotInBanner();
    }
  }

  private void throwExceptionWhenPreferredNameNotInBanner()
      throws JwtAuthBannerGetPreferredNameServiceException {
    throw new JwtAuthBannerGetPreferredNameServiceException(
        null,
        JwtAuthBannerGetPreferredNameServiceException.BannerGetPreferredNameServiceExceptionEnum
            .COULD_NOT_GET_PREFERRED_NAME);
  }
}

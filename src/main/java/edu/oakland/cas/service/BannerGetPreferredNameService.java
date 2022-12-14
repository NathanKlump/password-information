package edu.oakland.cas.service;

import edu.oakland.cas.dao.BannerGetPreferredNameDao;
import edu.oakland.cas.exception.BannerGetPreferredNameServiceException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerGetPreferredNameService {
  private final BannerGetPreferredNameDao bannerGetPreferredNameDao;

  @Autowired
  public BannerGetPreferredNameService(BannerGetPreferredNameDao bannerGetPreferredNameDao) {
    this.bannerGetPreferredNameDao = bannerGetPreferredNameDao;
  }

  public String bannerGetPreferredNameService(int pidmIn)
      throws BannerGetPreferredNameServiceException {
    String pidm = convertIntPidmToString(pidmIn);
    List<String> preferredNamesFromBanner = callBannerGetPreferredNameDao(pidm);
    processResults(preferredNamesFromBanner);
    return preferredNamesFromBanner.get(0);
  }

  private String convertIntPidmToString(int pidm) {
    return Integer.toString(pidm);
  }

  private List<String> callBannerGetPreferredNameDao(String pidm) {
    return bannerGetPreferredNameDao.getPreferredNameFromBanner(pidm);
  }

  private void processResults(List<String> preferredNamesFromBanner)
      throws BannerGetPreferredNameServiceException {
    if (preferredNamesFromBanner == null || preferredNamesFromBanner.isEmpty()) {
      throwExceptionWhenPreferredNameNotInBanner();
    }
  }

  private void throwExceptionWhenPreferredNameNotInBanner()
      throws BannerGetPreferredNameServiceException {
    throw new BannerGetPreferredNameServiceException(
        null,
        BannerGetPreferredNameServiceException.BannerGetPreferredNameServiceExceptionEnum
            .COULD_NOT_GET_PREFERRED_NAME);
  }
}

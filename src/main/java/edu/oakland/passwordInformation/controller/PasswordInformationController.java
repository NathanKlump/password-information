package edu.oakland.passwordInformation.controller;

import edu.oakland.cas.model.SessionDataModel;
import edu.oakland.jwtAuth.model.JwtJsonModel;
import edu.oakland.passwordInformation.dao.BannerDao;
import edu.oakland.passwordInformation.model.LastChange;
import edu.oakland.passwordInformation.service.BannerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/casified/v1")
public class PasswordInformationController {
  protected final Logger log = LoggerFactory.getLogger("PasswordInformation");
  @Autowired private BannerDao dao;
  @Autowired private BannerService service;

  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal Arguments given")
  @ExceptionHandler({IllegalArgumentException.class, DataAccessException.class})
  public void illegalArgumentError(Exception e) {
    log.error("Throwing Illegal Argument or Data Access error");
    log.error("", e);
  }

  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unspecified exception")
  @ExceptionHandler(Exception.class)
  public void generalError(Exception e) {
    log.error("Unspecified exception");
    log.error("", e);
  }

  @GetMapping("status-check")
  public boolean statusCheck() {
    return true;
  }

  @GetMapping("PasswordInformation")
  public ResponseEntity<LastChange> getLastChange(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    LastChange lastChange = new LastChange();
    lastChange.lastChange = getShadowLastChangeFromModel(httpServletRequest, httpServletResponse);
    return ResponseEntity.status(200).body(lastChange);
  }

  private String getShadowLastChangeFromModel(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    SessionDataModel sessionDataModel =
        ((SessionDataModel) httpServletRequest.getSession().getAttribute("sessionDataModel"));
    if (sessionDataModel != null) {
      return sessionDataModel.ldapUserModel.shadowLastChange;
    }
    JwtJsonModel jwtJsonModel =
        ((JwtJsonModel) httpServletRequest.getSession().getAttribute("jwtJsonModel"));
    if (jwtJsonModel != null) {
      return jwtJsonModel.shadowLastChange;
    }
    return null;
  }
}

package edu.oakland.passwordInformation.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/casified/ReactPage")
public class ReactPageController {

  public ReactPageController() {}

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView displayMainPage(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    ModelAndView modelAndView = new ModelAndView("ReactPage");
    return modelAndView;
  }
}

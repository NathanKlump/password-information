package edu.oakland.passwordInformation.service;

import edu.oakland.passwordInformation.dao.BannerDao;
import edu.oakland.passwordInformation.model.Adviser;
import edu.oakland.passwordInformation.model.Student;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class BannerService {

  public BannerService(BannerDao dao, BadWordsService badWords) {
    this.dao = dao;
    this.badWords = badWords;
  }

  private final BannerDao dao;
  private final BadWordsService badWords;

  protected final Logger logger = LoggerFactory.getLogger("passwordInformation");

  // This regex is designed to only allow letters, periods, spaces, apostrophes, and dashes.
  // [A-Za-z]+ forces the string to begin with a letter, also forcing the string to be at least one
  // character in length.
  // The string can end in any amount of the allowed characters mentioned above.
  private static final Pattern pattern = Pattern.compile("[A-Za-z]+((\\s|\'|-|[.])[A-Za-z]*)*");

  public boolean checkPreferredName(String prefName) {
    if (prefName == null || prefName.length() > 60) {
      return false;
    }

    prefName = prefName.trim();
    Matcher m = pattern.matcher(prefName);

    return m.matches() && !badWords.isOnList(prefName);
  }

  public Student getStudentInfo(String pidm) {
    Student student = new Student();
    student.setCurriculums(dao.getCurriculums(pidm));
    student.setAdviser(getAdviserInfo(pidm));
    return student;
  }
  /*
    public LastChange getLastChangeInfo(String pidm) {
      return LastChange;
    }
  */
  private Adviser getAdviserInfo(String pidm) {
    String mostRecentTerm = dao.getMostRecentTerm(pidm);

    try {
      return dao.getAdviserInfo(pidm, mostRecentTerm);
    } catch (EmptyResultDataAccessException e) {
      return new Adviser();
    }
  }
}

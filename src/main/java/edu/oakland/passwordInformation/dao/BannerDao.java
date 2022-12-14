package edu.oakland.passwordInformation.dao;

import edu.oakland.passwordInformation.model.Adviser;
import edu.oakland.passwordInformation.model.Curriculum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BannerDao {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public BannerDao(
      @Qualifier("oakland.banner-jdbc-connection.banner-jdbc-template") JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public String getPreferredName(String pidm) throws DataAccessException {
    return jdbcTemplate.queryForObject(
        Constants.GET_PREF_FIRST_NAME, new Object[] {pidm}, String.class);
  }

  public List<Curriculum> getCurriculums(String pidm) throws DataAccessException {
    return jdbcTemplate.query(
        Constants.GET_DEGREES,
        new Object[] {pidm, pidm, pidm, pidm, pidm, pidm},
        Curriculum.mapper);
  }

  public String getMostRecentTerm(String pidm) throws DataAccessException {
    return jdbcTemplate.queryForObject(
        Constants.GET_MOST_RECENT_TERM, new Object[] {pidm}, String.class);
  }

  public Adviser getAdviserInfo(String pidm, String termCode) throws DataAccessException {
    return jdbcTemplate.queryForObject(
        Constants.GET_ADVISER_STRING, new Object[] {termCode, pidm}, Adviser.mapper);
  }

  public Adviser getLastChangeInfo(String pidm, String termCode) throws DataAccessException {
    return jdbcTemplate.queryForObject(
        Constants.GET_LASTCHANGE_STRING, new Object[] {termCode, pidm}, Adviser.mapper);
  }
}

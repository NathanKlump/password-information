package edu.oakland.passwordInformation.model;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.jdbc.core.RowMapper;

public class Curriculum {
  private String degreeStatus;
  private String standing;
  private String college;
  private String degree;
  private String level;
  private List<String> majors;
  private List<String> minors;
  private List<String> concentrations;

  public void setDegreeStatus(String degreeStatus) {
    this.degreeStatus = degreeStatus;
  }

  public String getDegreeStatus() {
    return degreeStatus;
  }

  public void setStanding(String standing) {
    this.standing = standing;
  }

  public String getStanding() {
    return standing;
  }

  public void setCollege(String college) {
    this.college = college;
  }

  public String getCollege() {
    return college;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public String getDegree() {
    return degree;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getLevel() {
    return level;
  }

  public void setMajors(List<String> majors) {
    this.majors = majors;
  }

  public List<String> getMajors() {
    return majors;
  }

  public void setMinors(List<String> minors) {
    this.minors = minors;
  }

  public List<String> getMinors() {
    return minors;
  }

  public void setConcentrations(List<String> concentrations) {
    this.concentrations = concentrations;
  }

  public List<String> getConcentrations() {
    return concentrations;
  }

  public static RowMapper<Curriculum> mapper =
      (rs, rowNum) -> {
        Curriculum curriculum = new Curriculum();
        curriculum.setDegreeStatus(rs.getString("degree_status"));
        curriculum.setStanding(rs.getString("class_standing"));
        curriculum.setCollege(rs.getString("primary_college"));
        curriculum.setDegree(rs.getString("primary_degree"));
        curriculum.setLevel(rs.getString("primary_level"));

        curriculum.setMajors(
            Stream.of(rs.getString("primary_major"), rs.getString("secondary_major"))
                .filter(Objects::nonNull)
                .collect(toList()));
        curriculum.setMinors(
            Stream.of(rs.getString("primary_minor"), rs.getString("secondary_minor"))
                .filter(Objects::nonNull)
                .collect(toList()));

        curriculum.setConcentrations(
            Stream.of(
                    rs.getString("primary_concentration_1"),
                    rs.getString("secondary_concentration_1"),
                    rs.getString("tertiary_concentration_1"),
                    rs.getString("primary_concentration_2"),
                    rs.getString("secondary_concentration_2"),
                    rs.getString("tertiary_concentration_2"))
                .filter(Objects::nonNull)
                .collect(toList()));

        return curriculum;
      };
}

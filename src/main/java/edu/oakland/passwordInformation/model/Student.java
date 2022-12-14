package edu.oakland.passwordInformation.model;

import java.util.List;

public class Student {
  private List<Curriculum> curriculums;
  private Adviser adviser;

  public void setCurriculums(List<Curriculum> curriculums) {
    this.curriculums = curriculums;
  }

  public List<Curriculum> getCurriculums() {
    return curriculums;
  }

  public void setAdviser(Adviser adviser) {
    this.adviser = adviser;
  }

  public Adviser getAdviser() {
    return adviser;
  }
}

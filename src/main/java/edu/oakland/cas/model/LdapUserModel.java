package edu.oakland.cas.model;

import java.util.LinkedList;
import java.util.List;
import javax.validation.constraints.Size;

import com.google.gson.Gson;

public class LdapUserModel {
  @Size(min = 2, max = 60)
  public String firstName;

  public String lastName;
  public String gId;
  public String netId;
  public String primaryAffiliation;
  public List<String> affiliation;
  public int pidm;
  public String dateAccountCreated;
  public String address;
  public String telephoneNumber;
  public String shadowLastChange;

  public boolean isFaculty;
  public boolean isStaff;
  public boolean isStudent;

  public LdapUserModel() {
    affiliation = new LinkedList<String>();
  }

  public String toString() {
    return new Gson().toJson(this);
  }
}

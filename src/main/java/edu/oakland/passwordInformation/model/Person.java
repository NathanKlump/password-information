package edu.oakland.passwordInformation.model;

import edu.oakland.cas.model.LdapUserModel;

public class Person {
  private String pidm;
  private String legalName;
  private String prefName;
  private String gid;
  private String email;
  private String address;
  private String phone;

  public Person() {}

  public static Person getPersonFromLdapUserModel(
      LdapUserModel ldapUserModel, String preferredName) {
    Person person = new Person();
    person.pidm = Integer.toString(ldapUserModel.pidm);
    person.legalName = ldapUserModel.firstName;
    person.prefName = preferredName == null ? person.legalName : preferredName;
    person.gid = ldapUserModel.gId;
    person.email = ldapUserModel.netId + "@oakland.edu";
    person.address = ldapUserModel.address;
    person.phone = ldapUserModel.telephoneNumber;
    return person;
  }

  public void setPidm(String pidm) {
    this.pidm = pidm;
  }

  public String getPidm() {
    return pidm;
  }

  public void setLegalName(String legalName) {
    this.legalName = legalName;
  }

  public String getLegalName() {
    return legalName;
  }

  public void setPrefName(String prefName) {
    this.prefName = prefName;
  }

  public String getPrefName() {
    return prefName;
  }

  public void setGid(String gid) {
    this.gid = gid;
  }

  public String getGid() {
    return gid;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPhone() {
    return phone;
  }
}

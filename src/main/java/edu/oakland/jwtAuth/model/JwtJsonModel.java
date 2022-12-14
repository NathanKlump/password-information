package edu.oakland.jwtAuth.model;

import java.util.List;

public class JwtJsonModel {
  public String iss;
  public String sub;
  public String jti;
  public String agentDevice;
  public String telephoneNumber;
  public String gid;
  public String mail;
  public String eduPersonAffiliation;
  public String displayName;
  public String givenName;
  public String serverName;
  public String impersonating;
  public String cn;
  public String shadowLastChange;
  public String uid;
  public String eduPersonPrimaryAffiliation;
  public String pidm;
  public String sn;
  public String username;
  public List<String> groups;

  // not part of JWT Token. Added in JwtAuthCustomFilterService from Banner call
  public String preferredName;
}

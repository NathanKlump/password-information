package edu.oakland.cas.dao;

import edu.oakland.cas.model.LdapUserModel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

public class LdapAttributesToPojoConverter implements AttributesMapper<LdapUserModel> {
  public LdapAttributesToPojoConverter() {}

  public LdapUserModel mapFromAttributes(Attributes attributes) throws NamingException {
    LdapUserModel ldapUserModel = new LdapUserModel();
    setFirstName(attributes, ldapUserModel);
    setLastName(attributes, ldapUserModel);
    setGId(attributes, ldapUserModel);
    setNetId(attributes, ldapUserModel);
    setPidm(attributes, ldapUserModel);
    setDateAccountCreated(attributes, ldapUserModel);
    setAddress(attributes, ldapUserModel);
    setTelephoneNumber(attributes, ldapUserModel);
    setShadowLastChange(attributes, ldapUserModel);
    setPrimaryAffiliation(attributes, ldapUserModel);
    setAffiliation(attributes, ldapUserModel);
    return ldapUserModel;
  }

  private void setFirstName(Attributes attributes, LdapUserModel ldapUserModel)
      throws NamingException {
    ldapUserModel.firstName = (String) attributes.get("givenname").get();
  }

  private void setLastName(Attributes attributes, LdapUserModel ldapUserModel)
      throws NamingException {
    ldapUserModel.lastName = (String) attributes.get("sn").get();
  }

  private void setGId(Attributes attributes, LdapUserModel ldapUserModel) throws NamingException {
    ldapUserModel.gId = (String) attributes.get("ouedupersonbannergid").get();
  }

  private void setNetId(Attributes attributes, LdapUserModel ldapUserModel) throws NamingException {
    ldapUserModel.netId = (String) attributes.get("uid").get();
  }

  private void setPidm(Attributes attributes, LdapUserModel ldapUserModel) throws NamingException {
    Attribute attribute = (Attribute) attributes.get("ouEduPersonUUID");
    if (attribute != null) {
      String pidmAsString = (String) attribute.get();
      ldapUserModel.pidm = convertStringToInt(pidmAsString);
    }
  }

  private void setDateAccountCreated(Attributes attributes, LdapUserModel ldapUserModel)
      throws NamingException {
    Timestamp timestamp =
        convertStringToTimestamp((String) attributes.get("createTimestamp").get());
    ldapUserModel.dateAccountCreated = timestamp.toString();
  }

  private void setAddress(Attributes attributes, LdapUserModel ldapUserModel) {
    Attribute attribute = (Attribute) attributes.get("postalAddress");
    try {
      ldapUserModel.address = attribute == null ? "" : (String) attribute.get();
    } catch (NamingException e) {
      ldapUserModel.address = "";
    }
  }

  private void setTelephoneNumber(Attributes attributes, LdapUserModel ldapUserModel) {
    Attribute attribute = (Attribute) attributes.get("telephoneNumber");
    try {
      ldapUserModel.telephoneNumber = attribute == null ? "" : (String) attribute.get();
    } catch (NamingException e) {
      ldapUserModel.telephoneNumber = "";
    }
  }

  private void setShadowLastChange(Attributes attributes, LdapUserModel ldapUserModel)
      throws NamingException {
    ldapUserModel.shadowLastChange = (String) attributes.get("shadowLastChange").get();
  }

  private void setPrimaryAffiliation(Attributes attributes, LdapUserModel ldapUserModel)
      throws NamingException {
    Attribute attribute = (Attribute) attributes.get("edupersonprimaryaffiliation");
    if (attribute != null) {
      ldapUserModel.primaryAffiliation = (String) attribute.get();
      setModelAffiliationFromString(ldapUserModel.primaryAffiliation, ldapUserModel);
    }
  }

  private void setAffiliation(Attributes attributes, LdapUserModel ldapUserModel)
      throws NamingException {
    Attribute attribute = (Attribute) attributes.get("eduPersonAffiliation");
    if (attribute != null) {
      NamingEnumeration namingEnumeration = attribute.getAll();
      while (namingEnumeration.hasMore()) {
        String next = namingEnumeration.next().toString();
        ldapUserModel.affiliation.add(next);
        setModelAffiliationFromString(next, ldapUserModel);
      }
    }
  }

  private int convertStringToInt(String numberToConvert) {
    try {
      return Integer.parseInt(numberToConvert);
    } catch (Exception e) {
      return 0;
    }
  }

  private Timestamp convertStringToTimestamp(String dateAsString) {
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
      Date date = simpleDateFormat.parse(dateAsString);
      return new Timestamp(date.getTime());
    } catch (Exception e) {
      return null;
    }
  }

  private void setModelAffiliationFromString(String affiliationType, LdapUserModel ldapUserModel) {
    if (affiliationType.compareTo("student") == 0) {
      ldapUserModel.isStudent = true;
    } else if (affiliationType.compareTo("staff") == 0) {
      ldapUserModel.isStaff = true;
    } else if (affiliationType.compareTo("faculty") == 0) {
      ldapUserModel.isFaculty = true;
    }
  }
}

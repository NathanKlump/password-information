package edu.oakland.cas.dao;

import edu.oakland.cas.model.LdapUserModel;

import java.util.List;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Repository;

@Repository
public class RetrieveUserInfoFromNetIdDao {
  private final LdapTemplate ldapTemplate;

  @Autowired
  public RetrieveUserInfoFromNetIdDao(
      @Qualifier(value = "cas-auth.ldap-query-auth.ldap-template") LdapTemplate ldapTemplate) {
    this.ldapTemplate = ldapTemplate;
  }

  public List<LdapUserModel> getLdapUserInfoFromNetId(String netId) {
    return ldapTemplate.search(
        "",
        new EqualsFilter("uid", netId).encode(),
        getSearchControlsForLdapUser(),
        new LdapAttributesToPojoConverter());
  }

  private SearchControls getSearchControlsForLdapUser() {
    SearchControls searchControls = new SearchControls();
    searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    searchControls.setReturningObjFlag(true);
    searchControls.setReturningAttributes(
        new String[] {
          "ouEduPersonUUID",
          "givenName",
          "sn",
          "ouEduPersonBannerGID",
          "uid",
          "edupersonprimaryaffiliation",
          "eduPersonAffiliation",
          "postalAddress",
          "telephoneNumber",
          "createTimestamp",
          "shadowLastChange"
        });
    return searchControls;
  }
}

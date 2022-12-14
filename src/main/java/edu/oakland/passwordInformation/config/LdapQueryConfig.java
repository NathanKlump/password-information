package edu.oakland.passwordInformation.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapQueryConfig {
  @Bean(name = "oakland.ldap-query-auth.ldap-context-source")
  @ConfigurationProperties(prefix = "oakland.ldap-query-auth")
  public LdapContextSource ldapContextSource() {
    return new LdapContextSource();
  }

  @Bean(name = "oakland.ldap-query-auth.ldap-template")
  public LdapTemplate ldapTemplateStandard(
      @Qualifier(value = "oakland.ldap-query-auth.ldap-context-source")
          LdapContextSource ldapContextSource) {
    return new LdapTemplate(ldapContextSource);
  }

  @Bean(name = "cas-auth.ldap-query-auth.ldap-template")
  public LdapTemplate casLdapQueryJdbcTemplate(
      @Qualifier(value = "oakland.ldap-query-auth.ldap-template") LdapTemplate ldapTemplate) {
    return ldapTemplate;
  }
}

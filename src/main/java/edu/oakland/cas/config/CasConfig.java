package edu.oakland.cas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CasConfig {
  public final String oaklandCasAuthCasServerRootUrl;
  public final String oaklandCasAuthCasServerLoginUrl;
  public final String oaklandCasAuthAppCallbackUrl;

  public CasConfig(
      @Value("${oakland.cas-auth.cas-server-root-url}") String oaklandCasAuthCasServerRootUrl,
      @Value("${oakland.cas-auth.cas-server-login-url}") String oaklandCasAuthCasServerLoginUrl,
      @Value("${oakland.cas-auth.app-callback-url}") String oaklandCasAuthAppCallbackUrl) {
    this.oaklandCasAuthCasServerRootUrl = oaklandCasAuthCasServerRootUrl;
    this.oaklandCasAuthCasServerLoginUrl = oaklandCasAuthCasServerLoginUrl;
    this.oaklandCasAuthAppCallbackUrl = oaklandCasAuthAppCallbackUrl;
  }
}

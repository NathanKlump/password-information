package edu.oakland.cas.config;

import edu.oakland.cas.component.CustomCasAuthenticationSuccessHandler;
import edu.oakland.cas.pojo.CustomCasAuthenticationEntryPoint;
import edu.oakland.jwtAuth.service.JwtAuthBannerGetPreferredNameService;
import edu.oakland.jwtAuth.service.JwtAuthCustomFilterService;
import edu.oakland.jwtAuth.service.JwtAuthService;

import java.util.Arrays;

import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Order(3)
public class CustomCasWebSecurityConfig extends WebSecurityConfigurerAdapter {
  private final CustomCasAuthenticationSuccessHandler customCasAuthenticationSuccessHandler;
  private final CasConfig casConfig;
  private JwtAuthService jwtAuthService;
  private JwtAuthBannerGetPreferredNameService jwtAuthBannerGetPreferredNameService;
  private String oaklandJwtAuthCorsAllowedUrl;

  @Autowired
  public CustomCasWebSecurityConfig(
      CustomCasAuthenticationSuccessHandler customCasAuthenticationSuccessHandler,
      CasConfig casConfig,
      JwtAuthService jwtAuthService,
      JwtAuthBannerGetPreferredNameService jwtAuthBannerGetPreferredNameService,
      @Value("${oakland.jwt-auth.cors-allowed-url}") String oaklandJwtAuthCorsAllowedUrl) {
    this.customCasAuthenticationSuccessHandler = customCasAuthenticationSuccessHandler;
    this.casConfig = casConfig;
    this.jwtAuthService = jwtAuthService;
    this.jwtAuthBannerGetPreferredNameService = jwtAuthBannerGetPreferredNameService;
    this.oaklandJwtAuthCorsAllowedUrl = oaklandJwtAuthCorsAllowedUrl;
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    configureCasAndJwtAuth(httpSecurity);
  }

  private void configureCasAndJwtAuth(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable();
    //    httpSecurity.cors().disable();
    httpSecurity
        .cors()
        .configurationSource(corsConfigurationSource())
        .and()
        .authorizeRequests()
        .antMatchers("/casified/**")
        .fullyAuthenticated()
        .and()
        .httpBasic()
        .authenticationEntryPoint(casAuthenticationEntryPoint(serviceProperties()))
        .and()
        .addFilterBefore(
            new JwtAuthCustomFilterService(jwtAuthService, jwtAuthBannerGetPreferredNameService),
            BasicAuthenticationFilter.class);
  }

  private void configureCasAuth(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable();
    httpSecurity.cors().disable();
    httpSecurity
        .authorizeRequests()
        .antMatchers("/casified/**")
        .fullyAuthenticated()
        .and()
        .httpBasic()
        .authenticationEntryPoint(casAuthenticationEntryPoint(serviceProperties()));
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder.authenticationProvider(
        casAuthenticationProvider(serviceProperties(), ticketValidator()));
  }

  @Bean(name = "customCasAuthenticationFilter")
  public CasAuthenticationFilter casAuthenticationFilter(
      @Qualifier("customServiceProperties") ServiceProperties serviceProperties) throws Exception {
    CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
    casAuthenticationFilter.setServiceProperties(serviceProperties);
    casAuthenticationFilter.setAuthenticationManager(authenticationManager());
    casAuthenticationFilter.setAuthenticationSuccessHandler(customCasAuthenticationSuccessHandler);
    return casAuthenticationFilter;
  }

  @Bean(name = "customServiceProperties")
  public ServiceProperties serviceProperties() {
    ServiceProperties serviceProperties = new ServiceProperties();
    serviceProperties.setService(casConfig.oaklandCasAuthAppCallbackUrl);
    serviceProperties.setSendRenew(false);
    return serviceProperties;
  }

  @Bean(name = "customCasAuthenticationEntryPoint")
  public CasAuthenticationEntryPoint casAuthenticationEntryPoint(
      @Qualifier("customServiceProperties") ServiceProperties serviceProperties) {
    CasAuthenticationEntryPoint casAuthenticationEntryPoint =
        new CustomCasAuthenticationEntryPoint();
    casAuthenticationEntryPoint.setLoginUrl(casConfig.oaklandCasAuthCasServerLoginUrl);
    casAuthenticationEntryPoint.setServiceProperties(serviceProperties);
    return casAuthenticationEntryPoint;
  }

  @Bean(name = "customTicketValidator")
  public TicketValidator ticketValidator() {
    return new Cas20ServiceTicketValidator(casConfig.oaklandCasAuthCasServerRootUrl);
  }

  @Bean(name = "customCasAuthenticationProvider")
  public CasAuthenticationProvider casAuthenticationProvider(
      @Qualifier("customServiceProperties") ServiceProperties serviceProperties,
      @Qualifier("customTicketValidator") TicketValidator ticketValidator) {
    CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
    casAuthenticationProvider.setServiceProperties(serviceProperties);
    casAuthenticationProvider.setTicketValidator(ticketValidator);
    casAuthenticationProvider.setUserDetailsService(
        username ->
            new User(
                username,
                username,
                true,
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList("CAS_AUTHORITY")));
    casAuthenticationProvider.setKey("CAS_KEY");
    return casAuthenticationProvider;
  }

  @Override
  public AuthenticationManager authenticationManager() throws Exception {
    return new ProviderManager(
        Arrays.asList(casAuthenticationProvider(serviceProperties(), ticketValidator())));
  }

  private CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOrigins(Arrays.asList(oaklandJwtAuthCorsAllowedUrl));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/casified/**", configuration);
    return (CorsConfigurationSource) source;
  }
}

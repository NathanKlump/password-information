package edu.oakland.passwordInformation.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class BannerJdbcConfig {
  @Bean(name = "oakland.banner-jdbc-connection.banner-data-source")
  @ConfigurationProperties("oakland.banner-jdbc-connection")
  public DataSource bannerDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "oakland.banner-jdbc-connection.banner-jdbc-template")
  public JdbcTemplate bannerJdbcTemplate(
      @Qualifier(value = "oakland.banner-jdbc-connection.banner-data-source")
          DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean(name = "cas-auth.banner-jdbc-template")
  public JdbcTemplate casAuthBannerJdbcTemplate(
      @Qualifier(value = "oakland.banner-jdbc-connection.banner-jdbc-template")
          JdbcTemplate jdbcTemplate) {
    return jdbcTemplate;
  }

  @Bean(name = "jwt-auth.banner-jdbc-template")
  public JdbcTemplate jwtAuthBannerJdbcTemplate(
      @Qualifier(value = "oakland.banner-jdbc-connection.banner-jdbc-template")
          JdbcTemplate jdbcTemplate) {
    return jdbcTemplate;
  }
}

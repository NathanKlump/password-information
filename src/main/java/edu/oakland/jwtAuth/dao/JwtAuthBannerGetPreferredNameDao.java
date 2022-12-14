package edu.oakland.jwtAuth.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JwtAuthBannerGetPreferredNameDao {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JwtAuthBannerGetPreferredNameDao(
      @Qualifier("jwt-auth.banner-jdbc-template") JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<String> getPreferredNameFromBanner(String pidm) {
    String query = "SELECT BANINST1.GWKGBIO.F_GET_PREF_FIRST_NAME(?) " + "  FROM dual";
    return jdbcTemplate.query(
        query,
        new RowMapper<String>() {
          @Override
          public String mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getString(1);
          }
        },
        pidm);
  }
}

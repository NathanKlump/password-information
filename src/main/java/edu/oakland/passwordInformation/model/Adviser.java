package edu.oakland.passwordInformation.model;

import org.springframework.jdbc.core.RowMapper;

public class Adviser {
  private String name = "N/A";
  private String email = "N/A";

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public static RowMapper<Adviser> mapper =
      (rs, rowNum) -> {
        Adviser adviser = new Adviser();
        adviser.setEmail(rs.getString("adviseremail"));
        adviser.setName(rs.getString("adviser"));

        return adviser;
      };
}

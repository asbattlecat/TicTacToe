package com.asadaker.tictactoe.web.model;

public class SignUpRequest {
  private String login;
  private String password;

  public SignUpRequest() {
    this.login = null;
    this.password = null;
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

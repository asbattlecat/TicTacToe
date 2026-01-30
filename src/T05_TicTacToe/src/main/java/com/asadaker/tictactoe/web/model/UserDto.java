package com.asadaker.tictactoe.web.model;

import com.asadaker.tictactoe.domain.other.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDto {
  private UUID id;
  private String login;
  private String password;
  private List<Role> roles;

  public UserDto() {
    id = UUID.randomUUID();
    roles = new ArrayList<>();
    roles.add(Role.USER);
  }

  public UUID getId() {
    return id;
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }
}

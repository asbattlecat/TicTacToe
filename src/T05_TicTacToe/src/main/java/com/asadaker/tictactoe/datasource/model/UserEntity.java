package com.asadaker.tictactoe.datasource.model;

import com.asadaker.tictactoe.domain.other.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class UserEntity {
  @Id
  private UUID id;

  @Column(nullable = false, unique = true)
  private String login;
  @Column(nullable = false)
  private String password;

  @ElementCollection(targetClass = Role.class)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "role_name")
  private List<Role> roles;


  public UserEntity() {
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

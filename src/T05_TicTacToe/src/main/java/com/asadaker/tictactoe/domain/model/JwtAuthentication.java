package com.asadaker.tictactoe.domain.model;

import com.asadaker.tictactoe.domain.other.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class JwtAuthentication implements Authentication {
  private final UUID id;
  private final List<Role> roles;
  private boolean isAuthenticated;

  public JwtAuthentication(UUID id, List<Role> roles) {
    this.id = id;
    this.roles = roles;
    isAuthenticated = true;
  }

  public JwtAuthentication() {
    this(null, Collections.emptyList());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return id;
  }

  @Override
  public boolean isAuthenticated() {
    return isAuthenticated && id != null;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    this.isAuthenticated = isAuthenticated;
  }

  @Override
  public String getName() {
    return id.toString();
  }
}

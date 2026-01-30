package com.asadaker.tictactoe.domain.service;

import com.asadaker.tictactoe.domain.model.JwtAuthentication;
import com.asadaker.tictactoe.domain.other.Role;
import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.UUID;

public class JwtUtil {
  public static JwtAuthentication createJwtAuthentication(Claims claims) {
    List<String> roleStrings = claims.get("roles", List.class);
    List<Role> roles = roleStrings.stream().map(r -> r.replace("ROLE_", "")).map(Role::valueOf).toList();
    UUID id = UUID.fromString(claims.getSubject());

    return new JwtAuthentication(id, roles);
  }
}

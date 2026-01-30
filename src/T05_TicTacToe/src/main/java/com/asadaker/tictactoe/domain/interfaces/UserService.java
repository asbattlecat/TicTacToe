package com.asadaker.tictactoe.domain.interfaces;

import com.asadaker.tictactoe.domain.model.UserDomain;
import com.asadaker.tictactoe.domain.other.Role;

import java.util.List;
import java.util.UUID;

public interface UserService {
  UserDomain findUserById(UUID id);
  UserDomain findUserByLogin(String login);
  void save(UserDomain userDomain);
  void delete(UserDomain userDomain);
  void validateCredentials(String login, String password);
  UUID getIdByLogin(String login);
  String getLoginById(UUID id);
  List<Role> getRolesById(UUID id);
  UUID getIdFromToken(String accessToken);
  UUID userIdFromSecurityContextHolder();
}

package com.asadaker.tictactoe.domain.service;

import com.asadaker.tictactoe.datasource.mapper.DomainEntityMapper;
import com.asadaker.tictactoe.datasource.model.UserEntity;
import com.asadaker.tictactoe.datasource.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.asadaker.tictactoe.domain.model.UserDomain;
import com.asadaker.tictactoe.domain.other.Role;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;

public class ImplUserService implements com.asadaker.tictactoe.domain.interfaces.UserService {
  private final UserRepository userRepository;
  private final DomainEntityMapper domainEntityMapper;
  private final JwtProvider jwtProvider;

  public ImplUserService(UserRepository userRepository, DomainEntityMapper domainEntityMapper, JwtProvider jwtProvider) {
    this.userRepository = userRepository;
    this.domainEntityMapper = domainEntityMapper;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public UserDomain findUserById(UUID id) {
    Optional<UserEntity> userEntityOptional = userRepository.findById(id);
    if (userEntityOptional.isEmpty()) {
      throw new NoSuchElementException("There is no user with such id");
    }

    return domainEntityMapper.toDomain(userEntityOptional.get());
  }

  @Override
  public UserDomain findUserByLogin(String login) {
    Optional<UserEntity> userEntityOptional = userRepository.findByLogin(login);
    if (userEntityOptional.isEmpty()) {
      throw new NoSuchElementException("There is no user with such login");
    }

    return domainEntityMapper.toDomain(userEntityOptional.get());
  }

  @Override
  public void save(UserDomain userDomain) {
    userRepository.save(domainEntityMapper.toEntity(userDomain));
  }

  @Override
  public void delete(UserDomain userDomain) {
    userRepository.delete(domainEntityMapper.toEntity(userDomain));
  }

  @Override
  public void validateCredentials(String login, String password) {
    UserDomain userDomain = findUserByLogin(login);
    String userPassword = userDomain.getPassword();
    if (!userPassword.equals(password)) {
      throw new IllegalArgumentException("Invalid password");
    }
  }

  @Override
  public UUID getIdByLogin(String login) {
    UserDomain user = findUserByLogin(login);
    return user.getId();
  }

  /**
   * Метод для получения информации о пользователе по ID
   * @param id ID пользователя в системе
   * @return login пользователя
   */
  @Override
  public String getLoginById(UUID id) {
    UserDomain user = findUserById(id);
    return user.getLogin();
  }

  @Override
  public List<Role> getRolesById(UUID id) {
    UserDomain user = findUserById(id);
    return user.getRoles();
  }

  @Override
  public UUID getIdFromToken(String accessToken) {
    if (!jwtProvider.validateAccessToken(accessToken)) {
      throw new IllegalArgumentException("Invalid access token");
    }
    Claims claims = jwtProvider.getClaims(accessToken);
    return UUID.fromString(claims.getSubject());
  }

  public UUID userIdFromSecurityContextHolder() {
    return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}

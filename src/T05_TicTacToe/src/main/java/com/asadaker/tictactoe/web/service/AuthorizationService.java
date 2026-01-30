package com.asadaker.tictactoe.web.service;

import com.asadaker.tictactoe.domain.model.JwtAuthentication;
import com.asadaker.tictactoe.domain.model.UserDomain;
import com.asadaker.tictactoe.domain.service.JwtProvider;
import com.asadaker.tictactoe.domain.service.JwtUtil;
import com.asadaker.tictactoe.domain.service.ImplUserService;
import com.asadaker.tictactoe.web.mapper.DomainWebMapper;
import com.asadaker.tictactoe.web.model.SignUpRequest;
import com.asadaker.tictactoe.web.model.UserDto;
import com.asadaker.tictactoe.web.model.jwt.JwtRequest;
import com.asadaker.tictactoe.web.model.jwt.JwtResponse;
import io.jsonwebtoken.Claims;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
  private final ImplUserService userService;
  private final DomainWebMapper domainWebMapper;
  private final JwtProvider jwtProvider;

  public AuthorizationService(
          ImplUserService userService, DomainWebMapper domainWebMapper, JwtProvider jwtProvider) {
    this.userService = userService;
    this.domainWebMapper = domainWebMapper;
    this.jwtProvider = jwtProvider;
  }

  /**
   * Метод для регистрации нового пользователя с проверкой его существования в базе данных
   * @param signUpRequest хранит данные: login, password
   */
  public void registerUser(SignUpRequest signUpRequest) {
    try {
      UserDto userDto = new UserDto();
      userDto.setLogin(signUpRequest.getLogin());
      userDto.setPassword(signUpRequest.getPassword());

      UserDomain userDomain = domainWebMapper.toDomain(userDto);
      userService.save(userDomain);
    } catch (DataIntegrityViolationException e) {
      throw new IllegalArgumentException(
          "AuthorizationService.registerUser: User with this login already exists!");
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "AuthorizationService.registerUser: unexpected error, message: " + e.getMessage());
    }
  }

  public JwtResponse authorizeUser(JwtRequest request) {
    String methodName = "AuthorizationService.authorizeUser";
    if (request == null) {
      throw new IllegalArgumentException(methodName + ": request cannot be null!");
    }

    String login = request.getLogin();

    userService.validateCredentials(login, request.getPassword());
    UserDomain user = userService.findUserByLogin(login);
    String accessToken = jwtProvider.generateAccessToken(user);
    String refreshToken = jwtProvider.generateRefreshToken(user);

    return new JwtResponse(accessToken, refreshToken);
  }

  public JwtResponse newAccessToken(String refreshToken) {
    checkRefreshToken(refreshToken);

    UUID userId = getUserIdFromToken(refreshToken);
    UserDomain user = userService.findUserById(userId);

    String newAccessToken = jwtProvider.generateAccessToken(user);

    return new JwtResponse(newAccessToken, refreshToken);
  }

  public JwtResponse newRefreshToken(String refreshToken) {
    checkRefreshToken(refreshToken);

    UUID userId = getUserIdFromToken(refreshToken);
    UserDomain user = userService.findUserById(userId);

    String newRefreshToken = jwtProvider.generateRefreshToken(user);
    String newAccessToken = jwtProvider.generateAccessToken(user);

    return new JwtResponse(newAccessToken, newRefreshToken);
  }

  public JwtAuthentication createJwtAuthentication(String accessToken) {
    checkAccessToken(accessToken);

    Claims claims = jwtProvider.getClaims(accessToken);
    return JwtUtil.createJwtAuthentication(claims);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // ----------------------------------- приватные методы --------------------------------------- //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Метод для валидации refresh токена
   * @param refreshToken проверяемый токен
   */
  private void checkRefreshToken(String refreshToken) {
    if (!jwtProvider.validateRefreshToken(refreshToken)) {
      throw new IllegalArgumentException("Invalid refresh token");
    }
  }

  /**
   * Метод для валидации access токена
   * @param accessToken проверяемый токен
   */
  private void checkAccessToken(String accessToken) {
    if (!jwtProvider.validateAccessToken(accessToken)) {
      throw new IllegalArgumentException("Invalid access token");
    }
  }

  private UUID getUserIdFromToken(String token) {
    return UUID.fromString(jwtProvider.getClaims(token).getSubject());
  }
}

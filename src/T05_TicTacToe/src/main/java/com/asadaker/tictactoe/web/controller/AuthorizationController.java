package com.asadaker.tictactoe.web.controller;

import com.asadaker.tictactoe.web.model.TokenRequest;
import com.asadaker.tictactoe.web.model.jwt.JwtRequest;
import com.asadaker.tictactoe.web.model.jwt.JwtResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.asadaker.tictactoe.web.service.AuthorizationService;
import com.asadaker.tictactoe.web.model.SignUpRequest;

@RestController
@RequestMapping("/user")
public class AuthorizationController {
  private final AuthorizationService service;

  public AuthorizationController(AuthorizationService service) {
    this.service = service;
  }

  /**
   * Метод для логина пользователя
   * @param jwtRequest структура, получаемая из тела запроса. Состоит из login, password
   * @return UUID авторизованного пользователя
   */
  @GetMapping("/login")
  public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) {
    JwtResponse response = service.authorizeUser(jwtRequest);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/requestForm")
  public ResponseEntity<JwtRequest> requestForm() {
    return ResponseEntity.ok(new JwtRequest());
  }

  /**
   * Метод для регистрации пользователя
   * @param signUpRequest форма, содержащая логин и пароль (не зашифрованные)
   * @return Ответ, произошла ли регистрация успешно
   */
  @PostMapping("/signup")
  public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest) {
    service.registerUser(signUpRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * endpoint для получения формата тела запроса для регистрации пользователя
   * @return Объект класса SignUpRequest
   */
  @GetMapping("/signupForm")
  public ResponseEntity<SignUpRequest> signUpRequestForm() {
    return ResponseEntity.ok(new SignUpRequest());
  }

  @GetMapping("/newAccessToken")
  public ResponseEntity<JwtResponse> newAccessToken(@RequestBody TokenRequest request) {
    return ResponseEntity.ok(service.newAccessToken(request.token()));
  }

  @GetMapping("/newRefreshToken")
  public ResponseEntity<JwtResponse> newRefreshToken(@RequestBody TokenRequest request) {
    return ResponseEntity.ok(service.newRefreshToken(request.token()));
  }
}

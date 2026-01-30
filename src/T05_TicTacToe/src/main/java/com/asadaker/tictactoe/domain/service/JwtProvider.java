package com.asadaker.tictactoe.domain.service;

import com.asadaker.tictactoe.domain.model.UserDomain;
import com.asadaker.tictactoe.domain.other.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

public class JwtProvider {
  // А ЭТО НЕ БЕЗОПАСНО, Я ЗНАЮ! но это пет проект :))
  private final String SECRET;
  private final SecretKey key;

  public JwtProvider() {
    SECRET = "abc_abc_abc_abc_abc_abc_abc_abc_";
    key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(UserDomain user) {
    Instant now = Instant.now();
    Date issuedAt = Date.from(now);
    Date expireAt = Date.from(now.plus(15, ChronoUnit.MINUTES));

    return Jwts.builder()
            .subject(user.getId().toString())
            .claim("roles", user.getRoles()
                    .stream()
                    .map(Role::getAuthority)
                    .toList())
            .claim("type", "access")
            .issuedAt(issuedAt)
            .expiration(expireAt)
            .signWith(key, Jwts.SIG.HS256)
            .compact();
  }

  public String generateRefreshToken(UserDomain user) {
    Instant now = Instant.now();
    Date issuedAt = Date.from(now);
    Date expireAt = Date.from(now.plus(1, ChronoUnit.HOURS));

    return Jwts.builder()
            .subject(user.getId().toString())
            .claim("type", "refresh")
            .issuedAt(issuedAt)
            .expiration(expireAt)
            .signWith(key, Jwts.SIG.HS256)
            .compact();
  }

  public boolean validateAccessToken(String accessToken) {
    try {
      Claims claims = getClaims(accessToken);
      boolean isAccess = "access".equals(claims.get("type", String.class));
      return isTokenValid(claims) && isAccess;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean validateRefreshToken(String refreshToken) {
    try {
      Claims claims = getClaims(refreshToken);
      boolean isRefresh = "refresh".equals(claims.get("type", String.class));
      return isTokenValid(claims) && isRefresh;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Метод проверят, что claims не были изменены, и возвращает сами claims
   * @param token access or refresh token
   * @return Claims из токена
   */
  public Claims getClaims(String token) {
    return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  public UUID claimsToUUID(Claims claims) {
    return UUID.fromString(claims.getSubject());
  }

  /**
   * Метод для проверки валидности переданного токена
   * @param claims claims из токена
   * @return <code>true</code> - срок токена не истек, <code>false</code> - срок токена истек
   */
  private boolean isTokenValid(Claims claims) {
    Date expiration = claims.getExpiration();
    return expiration != null && expiration.after(new Date());
  }
}

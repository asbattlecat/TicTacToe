package com.asadaker.tictactoe.web.service;

import com.asadaker.tictactoe.domain.interfaces.UserService;
import com.asadaker.tictactoe.domain.model.JwtAuthentication;
import com.asadaker.tictactoe.domain.service.JwtProvider;
import com.asadaker.tictactoe.domain.service.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class AuthFilter extends GenericFilterBean {
  private final UserService userService;
  private final JwtProvider jwtProvider;

  public AuthFilter(UserService userService, JwtProvider jwtProvider) {
    this.userService = userService;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

    String path = httpRequest.getRequestURI();

    try {
      String authHeader = httpRequest.getHeader("Authorization");
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        writeUnauthorized(httpResponse, "Missing or invalid Authorization header");
        return;
      }

      String accessToken = authHeader.substring("Bearer ".length());

      if (!jwtProvider.validateAccessToken(accessToken)) {
        writeUnauthorized(httpResponse, "Invalid access token");
        return;
      }

      JwtAuthentication jwtAuthentication = JwtUtil.createJwtAuthentication(jwtProvider.getClaims(accessToken));

      // передаем информацию (в данном случае в SecurityConfiguration), что валидация прошла
      // успешно, и privateFilterChain доволен
      SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);

      filterChain.doFilter(servletRequest, servletResponse);
    } catch (Exception e) {
      writeUnauthorized(httpResponse, "Authentication failed: " + e.getMessage());

    }
  }

  private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
    if (response.isCommitted()) {
      return;
    }
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String body = """
        {
          "status": 401,
          "error": "Unauthorized",
          "message": "%s"
        }
        """.formatted(message.replace("\"", "\\\""));

    response.getWriter().write(body);
    response.getWriter().flush();
  }
}
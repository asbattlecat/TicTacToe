package com.asadaker.tictactoe.di;

import com.asadaker.tictactoe.domain.interfaces.UserService;
import com.asadaker.tictactoe.domain.service.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.asadaker.tictactoe.web.service.AuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  private final UserService userService;
  private final JwtProvider jwtProvider;

  public SecurityConfiguration(UserService userService, JwtProvider jwtProvider) {
    this.userService = userService;
    this.jwtProvider = jwtProvider;
  }

  @Bean
  @Order(1)
  public SecurityFilterChain publicFilterChain(final HttpSecurity http) throws Exception {
    return http
            .securityMatcher("/user/login",
                    "/user/signup",
                    "/user/signUpRequestForm",
                    "/user/newAccessToken",
                    "/user/requestForm")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
            .build();
  }


  @Bean
  @Order(2)
  public SecurityFilterChain privateFilterChain(final HttpSecurity http) throws Exception {
    return http
            .securityMatcher("/game/**",
                    "/user/newRefreshToken",
                    "/user/info/{userId}",
                    "/user/info")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .addFilterAfter(new AuthFilter(userService, jwtProvider), UsernamePasswordAuthenticationFilter.class)
            .build();
  }
}

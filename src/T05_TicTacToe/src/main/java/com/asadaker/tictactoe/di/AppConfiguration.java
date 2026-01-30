package com.asadaker.tictactoe.di;

import com.asadaker.tictactoe.datasource.mapper.DomainEntityMapper;
import com.asadaker.tictactoe.datasource.repository.GameRepository;
import com.asadaker.tictactoe.datasource.repository.UserRepository;
import com.asadaker.tictactoe.domain.interfaces.*;
import com.asadaker.tictactoe.domain.interfaces.GameManager;
import com.asadaker.tictactoe.domain.interfaces.GameRepositoryService;
import com.asadaker.tictactoe.domain.interfaces.GameService;
import com.asadaker.tictactoe.domain.interfaces.UserService;
import com.asadaker.tictactoe.domain.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.asadaker.tictactoe.web.mapper.DomainWebMapper;

@Configuration
public class AppConfiguration {
  @Bean
  public DomainEntityMapper domainEntityMapper() {
    return new DomainEntityMapper();
  }

  @Bean
  public DomainWebMapper domainWebMapper() {
    return new DomainWebMapper();
  }

  @Bean
  public JwtProvider jwtProvider() {
    return new JwtProvider();
  }

  @Bean
  public AiAlgorithm aiAlgorithm() {
    return new Minimax();
  }

  @Bean
  public GameRepositoryService gameRepositoryService(GameRepository gameRepository, DomainEntityMapper domainEntityMapper) {
    return new ImplGameRepositoryService(gameRepository, domainEntityMapper);
  }

  @Bean
  public GameService gameService(GameRepositoryService gameRepositoryService, AiAlgorithm aiAlgorithm, DomainEntityMapper domainEntityMapper) {
    return new ImplGameService(gameRepositoryService, aiAlgorithm, domainEntityMapper);
  }

  @Bean
  public GameManager gameManager(UserRepository userRepository, GameRepositoryService gameRepositoryService, DomainEntityMapper domainEntityMapper) {
    return new ImplGameManager(userRepository, gameRepositoryService, domainEntityMapper);
  }

  @Bean
  public UserService userService(UserRepository userRepository, DomainEntityMapper domainEntityMapper, JwtProvider jwtProvider) {
    return new ImplUserService(userRepository, domainEntityMapper, jwtProvider);
  }
}

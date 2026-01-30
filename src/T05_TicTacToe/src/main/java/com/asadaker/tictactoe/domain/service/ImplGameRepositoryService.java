package com.asadaker.tictactoe.domain.service;

import com.asadaker.tictactoe.datasource.mapper.DomainEntityMapper;
import com.asadaker.tictactoe.datasource.model.GameEntity;
import com.asadaker.tictactoe.datasource.model.UserWinrate;
import com.asadaker.tictactoe.datasource.repository.GameRepository;
import com.asadaker.tictactoe.domain.interfaces.GameRepositoryService;
import com.asadaker.tictactoe.domain.model.GameDomain;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class ImplGameRepositoryService implements GameRepositoryService {
  private final GameRepository gameRepository;
  private final DomainEntityMapper domainEntityMapper;

  public ImplGameRepositoryService(GameRepository gameRepository, DomainEntityMapper domainEntityMapper) {
    this.gameRepository = gameRepository;
    this.domainEntityMapper = domainEntityMapper;
  }

  @Override
  public List<GameDomain> findAvailableGames(UUID lookingFor) {
    List<GameEntity> availableGameEntities =
            gameRepository.findAvailableGames(lookingFor);
    return availableGameEntities.stream().map(domainEntityMapper::toDomain).toList();
  }

  @Override
  public boolean gameExistsById(UUID gameId) {
    return gameRepository.existsById(gameId);
  }

  public GameDomain findById(UUID gameId) {
    Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
    if (gameEntityOptional.isEmpty()) {
      throw new NoSuchElementException("GameRepositorySevice.findById: game with such gameId does not exist");
    }

    return domainEntityMapper.toDomain(gameEntityOptional.get());
  }

  @Override
  public List<GameDomain> findFinishedGames(UUID userId) {
    List<GameEntity> finishedGames = gameRepository.findFinishedGames(userId);
    return finishedGames.stream().map(domainEntityMapper::toDomain).toList();
  }

  @Override
  public void saveGame(GameDomain game) {
    GameEntity gameEntity = domainEntityMapper.toEntity(game);
    gameRepository.save(gameEntity);
  }

  @Override
  public List<UserWinrate> getLeaderBoard(int amount) {
    return gameRepository.findUserWinrateLeaders(amount)
            .stream()
            .map(row -> new UserWinrate(
                    (UUID) row[0],           // userId
                    ((Number) row[1]).doubleValue()  // winrate
            ))
            .toList();
  }
}

package com.asadaker.tictactoe.domain.service;

import com.asadaker.tictactoe.datasource.mapper.DomainEntityMapper;
import com.asadaker.tictactoe.datasource.repository.UserRepository;
import com.asadaker.tictactoe.domain.interfaces.GameRepositoryService;
import com.asadaker.tictactoe.domain.model.GameDomain;
import com.asadaker.tictactoe.domain.other.Constants;
import com.asadaker.tictactoe.domain.other.GameState;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class ImplGameManager implements com.asadaker.tictactoe.domain.interfaces.GameManager {
  private final UserRepository userRepository;
  private final GameRepositoryService gameRepositoryService;
  private Random random;

  public ImplGameManager(UserRepository userRepository, GameRepositoryService gameRepositoryService, DomainEntityMapper domainEntityMapper) {
    this.userRepository = userRepository;
    this.gameRepositoryService = gameRepositoryService;
    random = new Random();
  }

  @Override
  @Transactional
  public GameDomain createGamePvp(UUID playerId) {
    String methodName = "GameService.createGamePvc";
    if (!userRepository.existsById(playerId)) {
      throw new NoSuchElementException(methodName + ": user with such id does not exist");
    }
    GameDomain game = new GameDomain();
    game.setFirstPlayerId(playerId);
    game.setFirstPlayerConnected(true);
    randomPlayersSimbol(game);
    game.setPvp(true);

    return game;
  }

  @Override
  @Transactional
  public GameDomain createGamePvc(UUID playerId) {
    String methodName = "GameService.createGamePvc";
    if (!userRepository.existsById(playerId)) {
      throw new NoSuchElementException(methodName + ": user with such id does not exist");
    }
    GameDomain game = new GameDomain();
    game.setGameState(GameState.PLAYING);
    game.setFirstPlayerId(playerId);
    game.setFirstPlayerConnected(true);
    randomPlayersSimbol(game);
    game.setPvp(false);

    return game;
  }

  @Override
  @Transactional
  public GameDomain joinToGame(UUID gameId, UUID userId) {
    String methodName = "GameService.joinToGame";
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException(methodName + ": user with such userId does not exist");
    }
    GameDomain game = gameRepositoryService.findById(gameId);

    boolean ableToJoin = game.isPvp() && game.isFirstPlayerConnected()
            && !game.isSecondPlayerConnected() && game.getSecondPlayerId() == null
            && game.getGameState() == GameState.WAITING_PLAYERS
            && !game.getFirstPlayerId().equals(userId);

    if (ableToJoin) {
      game.setSecondPlayerId(userId);
      game.setSecondPlayerConnected(true);
      randomPlayersTurn(game);
      game.setGameState(GameState.PLAYING);
      gameRepositoryService.saveGame(game);
      return game;
    } else {
      throw new IllegalArgumentException(
              methodName + ": specified user cannot join to specified game");
    }
  }

  @Override
  @Transactional
  public GameDomain updateGamePvp(GameDomain game) {
    if (game.getGameState() != GameState.PLAYING) {
      throw new IllegalArgumentException("GameService.updateGamePvp: cannot update game, game is over or didn't start");
    }
    GameDomain gameDomainCopy = new GameDomain(game);
    switchCurrentPlayerId(gameDomainCopy);
    gameRepositoryService.saveGame(gameDomainCopy);

    return gameDomainCopy;
  }


  @Override
  public boolean isFirstMoveByAi(GameDomain game) {
    if (!game.isPvp()) {
      int randomNum = randomOneOrTwo();

      return randomNum == 2;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // ----------------------------------- приватные методы --------------------------------------- //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private void randomPlayersSimbol(GameDomain game) {
    int num = randomOneOrTwo();
    if (num == 1) {
      game.setFirstPlayerSimbol(Constants.O);
      game.setSecondPlayerSimbol(Constants.X);
    } else {
      game.setFirstPlayerSimbol(Constants.X);
      game.setSecondPlayerSimbol(Constants.O);
    }
  }

  private void randomPlayersTurn(GameDomain game) {
    int num = randomOneOrTwo();
    if (num == 1) {
      game.setCurrentTurnPlayerId(game.getFirstPlayerId());
    } else {
      game.setCurrentTurnPlayerId(game.getSecondPlayerId());
    }
  }

  private void switchCurrentPlayerId(GameDomain game) {
    if (game.isPvp()) {
      if (game.getCurrentTurnPlayerId().equals(game.getFirstPlayerId())) {
        game.setCurrentTurnPlayerId(game.getSecondPlayerId());
      } else {
        game.setCurrentTurnPlayerId(game.getFirstPlayerId());
      }
    } else {
      throw new IllegalArgumentException("GameService.switchCurrentPlayerId: game is PvC, cannot switch currentPlayerId");
    }
  }

  private int randomOneOrTwo() {
    return random.nextInt(0, 2) + 1;
  }
}

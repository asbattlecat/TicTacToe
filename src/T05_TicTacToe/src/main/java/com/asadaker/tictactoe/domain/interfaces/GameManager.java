package com.asadaker.tictactoe.domain.interfaces;

import com.asadaker.tictactoe.domain.model.GameDomain;

import java.util.UUID;

public interface GameManager {
  GameDomain createGamePvp(UUID playerId);
  GameDomain createGamePvc(UUID playerId);
  GameDomain joinToGame(UUID gameId, UUID userId);
  GameDomain updateGamePvp(GameDomain game);
  boolean isFirstMoveByAi(GameDomain game);
}

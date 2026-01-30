package com.asadaker.tictactoe.domain.interfaces;

import com.asadaker.tictactoe.domain.model.GameDomain;

import java.util.UUID;

public interface GameService {
  GameDomain getNextMoveAi(GameDomain game, int moveValue);
  boolean validateField(GameDomain game, UUID gameId, UUID playerId);
  boolean isGameOver(GameDomain game);
}

package com.asadaker.tictactoe.domain.interfaces;

import com.asadaker.tictactoe.datasource.model.UserWinrate;
import com.asadaker.tictactoe.domain.model.GameDomain;

import java.util.List;
import java.util.UUID;

public interface GameRepositoryService {
  List<GameDomain> findAvailableGames(UUID lookingFor);
  boolean gameExistsById(UUID gameId);
  GameDomain findById(UUID gameId);
  List<GameDomain> findFinishedGames(UUID userId);
  void saveGame(GameDomain game);
  List<UserWinrate> getLeaderBoard(int leadersAmount);
}

package com.asadaker.tictactoe.datasource.repository;

import com.asadaker.tictactoe.datasource.model.GameEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends CrudRepository<GameEntity, UUID> {

  @Query(value = """
    SELECT game
    FROM GameEntity AS game
    WHERE
        game.isPvp IS TRUE
        AND game.isFirstPlayerConnected IS TRUE
        AND game.isSecondPlayerConnected IS FALSE
        AND game.secondPlayerId IS NULL
        AND game.gameState = 'WAITING_PLAYERS'
        AND game.firstPlayerId != :lookingFor
  """)
  List<GameEntity> findAvailableGames(@Param("lookingFor") UUID lookingFor);

  // **********************************************************************************************

  @Query("SELECT game FROM GameEntity AS game " +
          "WHERE (game.gameState = 'WIN' OR game.gameState = 'DRAW') " +
          "AND " +
          "(game.firstPlayerId = :id OR game.secondPlayerId = :id)")
  List<GameEntity> findFinishedGames(@Param("id") UUID id);

  // **********************************************************************************************

  @Query(value = """
      WITH wins AS (
         SELECT u.id AS playerId, COUNT(game.id) AS amount
         FROM game AS game
         JOIN user_entity AS u ON (u.id = game.first_player_id OR u.id = game.second_player_id)
         WHERE game.game_state = 'WIN'
           AND ((game.first_player_id = u.id AND game.winner = 'FIRST_PLAYER')
             OR (game.second_player_id = u.id AND game.winner = 'SECOND_PLAYER'))
         GROUP BY u.id
      ), notWins AS (
         SELECT u.id AS playerId, COUNT(game.id) AS amount
         FROM game AS game
         JOIN user_entity AS u ON (u.id = game.first_player_id OR u.id = game.second_player_id)
         WHERE game.game_state = 'DRAW'
            OR (game.first_player_id = u.id AND game.winner = 'SECOND_PLAYER')
            OR (game.second_player_id = u.id AND game.winner = 'FIRST_PLAYER')
         GROUP BY u.id
      ), allPlayers AS (
      SELECT id
      FROM user_entity
      )
      SELECT
      ap.id AS userId,
         CASE
             WHEN (COALESCE(w.amount, 0) + COALESCE(nw.amount, 0)) = 0 THEN 0.0
             ELSE (COALESCE(w.amount, 0)::numeric / COALESCE(nw.amount, 1))
         END AS winrate
      FROM allPlayers AS ap
      LEFT JOIN wins AS w ON w.playerId = ap.id
      LEFT JOIN notWins AS nw ON nw.playerId = ap.id
      ORDER BY winrate DESC NULLS LAST
      LIMIT :amount
  """, nativeQuery = true)
  List<Object[]> findUserWinrateLeaders(@Param("amount") int leadersAmount);
}



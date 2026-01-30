package com.asadaker.tictactoe.web.model;

import com.asadaker.tictactoe.domain.other.GameState;
import com.asadaker.tictactoe.domain.other.Winner;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class GameDto {
  private FieldDto field;
  private UUID id;

  private GameState gameState;

  private UUID firstPlayerId; // always != null
  private UUID secondPlayerId; // может быть null, if Pvp false

  private boolean firstPlayerConnected;
  private boolean secondPlayerConnected; // может быть false if Pvp false

  private int firstPlayerSimbol;
  private int secondPlayerSimbol;

  private UUID currentTurnPlayerId; // null, если Pvc
  private Winner winner;

  private boolean pvp;

  private Date creationDate;

  public GameDto() {
    field = new FieldDto();
    id = UUID.randomUUID();
    gameState = GameState.WAITING_PLAYERS;

    firstPlayerId = null;
    secondPlayerId = null;
    currentTurnPlayerId = null;
    winner = Winner.NONE;

    firstPlayerConnected = false;
    secondPlayerConnected = false;

    creationDate = new Date();
  }

  public GameDto(GameDto game) {
    field = new FieldDto(game.getField());
    id = game.id;
    gameState = game.gameState;

    firstPlayerId = game.firstPlayerId;
    secondPlayerId = game.secondPlayerId;

    firstPlayerConnected = game.firstPlayerConnected;
    secondPlayerConnected = game.secondPlayerConnected;

    firstPlayerSimbol = game.firstPlayerSimbol;
    secondPlayerSimbol = game.secondPlayerSimbol;
    currentTurnPlayerId = game.currentTurnPlayerId;
    winner = game.winner;

    pvp = game.pvp;

    creationDate = game.creationDate;
  }

}

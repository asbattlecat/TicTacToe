package com.asadaker.tictactoe.domain.model;

import com.asadaker.tictactoe.domain.other.GameState;
import com.asadaker.tictactoe.domain.other.Winner;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class GameDomain {
  private FieldDomain field;
  private UUID id;

  private GameState gameState;

  private UUID firstPlayerId; // always not null
  private UUID secondPlayerId; // null, if Pvp false

  private boolean isFirstPlayerConnected;
  private boolean isSecondPlayerConnected; // can be false if Pvp false

  private int firstPlayerSimbol;
  private int secondPlayerSimbol;

  private UUID currentTurnPlayerId; // null, если Pvc
  private Winner winner;

  private boolean isPvp;

  private Date creationDate;

  public GameDomain() {
    field = new FieldDomain();
    id = UUID.randomUUID();
    gameState = GameState.WAITING_PLAYERS;

    firstPlayerId = null;
    secondPlayerId = null;
    currentTurnPlayerId = null;
    winner = Winner.NONE;

    isFirstPlayerConnected = false;
    isSecondPlayerConnected = false;

    creationDate = new Date();
  }

  public GameDomain(GameDomain gameDomain) {
    field = new FieldDomain(gameDomain.getField());
    id = gameDomain.id;
    gameState = gameDomain.gameState;

    firstPlayerId = gameDomain.getFirstPlayerId();
    secondPlayerId = gameDomain.getSecondPlayerId();

    isFirstPlayerConnected = gameDomain.isFirstPlayerConnected();;
    isSecondPlayerConnected = gameDomain.isSecondPlayerConnected();

    firstPlayerSimbol = gameDomain.firstPlayerSimbol;
    secondPlayerSimbol = gameDomain.secondPlayerSimbol;;
    currentTurnPlayerId = gameDomain.currentTurnPlayerId;
    winner = gameDomain.winner;

    isPvp = gameDomain.isPvp;

    creationDate = gameDomain.creationDate;
  }

}

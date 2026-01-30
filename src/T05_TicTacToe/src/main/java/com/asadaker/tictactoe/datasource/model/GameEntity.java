package com.asadaker.tictactoe.datasource.model;

import com.asadaker.tictactoe.domain.other.GameState;
import com.asadaker.tictactoe.domain.other.Winner;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "Game")
public class GameEntity {
  @Id
  private UUID id;

  @Embedded
  private FieldEntity field;

  @Enumerated(EnumType.STRING)
  private GameState gameState;

  private UUID firstPlayerId; // always not null
  private UUID secondPlayerId; // null, if Pvp false

  private boolean isFirstPlayerConnected;
  private boolean isSecondPlayerConnected; // can be false if Pvp false

  private int firstPlayerSimbol;
  private int secondPlayerSimbol;

  private UUID currentTurnPlayerId; // null, если Pvc

  @Enumerated(EnumType.STRING)
  private Winner winner;

  private boolean isPvp;

  private Date creationDate;

  public GameEntity() {
    field = new FieldEntity();
    id = UUID.randomUUID();

    currentTurnPlayerId = null;
    gameState = GameState.WAITING_PLAYERS;
    firstPlayerId = null;
    secondPlayerId = null;
    winner = Winner.NONE;

    isFirstPlayerConnected = false;
    isSecondPlayerConnected = false;

    creationDate = new Date();
  }
}

package com.asadaker.tictactoe.domain.service;

import com.asadaker.tictactoe.datasource.mapper.DomainEntityMapper;
import com.asadaker.tictactoe.domain.checker.ArgumentChecker;
import com.asadaker.tictactoe.domain.checker.FieldChecker;
import com.asadaker.tictactoe.domain.interfaces.AiAlgorithm;
import com.asadaker.tictactoe.domain.interfaces.GameRepositoryService;
import com.asadaker.tictactoe.domain.model.Cell;
import com.asadaker.tictactoe.domain.model.FieldDomain;
import com.asadaker.tictactoe.domain.model.GameDomain;
import com.asadaker.tictactoe.domain.other.*;

import java.util.*;

public class ImplGameService implements com.asadaker.tictactoe.domain.interfaces.GameService {
  private final GameRepositoryService gameRepositoryService;
  private final AiAlgorithm aiAlgorithm;

  public ImplGameService(GameRepositoryService gameRepositoryService,
                         AiAlgorithm aiAlgorithm, DomainEntityMapper domainEntityMapper) {
    this.gameRepositoryService = gameRepositoryService;
    this.aiAlgorithm = aiAlgorithm;
  }

  /**
   * Метод для получения следующего хода алгоритмом Минимакс
   * @param game объект текущей игры
   * @param moveSimbol символ, которым обозначен компьютер (О или Х)
   * @return возвращает объект класса Cell, содержащий координату и значение
   */
  @Override
  public GameDomain getNextMoveAi(GameDomain game, int moveSimbol) {
    ArgumentChecker.checkNotNull(game, "GameService.getNextMove");
    ArgumentChecker.checkPlayerSimbol(moveSimbol, "GameService.getNextMove");
    ArgumentChecker.isGameInProcess(game);

    if (emptyCellsCount(game.getField()) == 0) {
      return game;
    }

    GameDomain gameCopy = new GameDomain(game);

    FieldDomain fieldDomain = gameCopy.getField();
    Cell bestMove = aiAlgorithm.getBestMove(fieldDomain, true, moveSimbol);
    gameCopy.getField().setCell(bestMove);

    return gameCopy;
  }

  /**
   * Метод для валидации хода, в котором предложенный вариант игры сравнивается с последней
   * сохраненной версией игры из репозитория. Валидация происходит в 5 этапов. <b>1 этап</b>: ход был сделан корректным
   * игроком (тот, что сейчас ходит). <b>2 этап</b>: предыдущее состояние поля не было изменено. <b>3 этап</b>:
   * проверка на то, был ли вообще сделан ход. <b>4 этап</b>: было ли сделано более одного хода. <b>5 этап:</b> был
   * расположен ожидаемый от пользователя символ
   * @param updatedGameDomain предложенная версия игры
   * @param gameId UUID игры, с которой происходит сравнение
   * @param playerId UUID пользователя, который сделал ход
   * @return <code>true</code> - проблем с валидацией не возникло
   */
  @Override
  public boolean validateField(GameDomain updatedGameDomain, UUID gameId, UUID playerId) {
    String methodName = "GameService.validateField";
    ArgumentChecker.checkNotNull(updatedGameDomain, methodName);

    GameDomain oldGameDomain = gameRepositoryService.findById(gameId);

    FieldDomain oldField = oldGameDomain.getField();
    FieldDomain newField = updatedGameDomain.getField();

    if (!moveWasMadeByCorrectPlayer(updatedGameDomain, playerId)) {
      throw new IllegalStateException("Move been made by wrong player");
    }

    if (!cellsWereNotChanged(oldField, newField)) {
      throw new IllegalArgumentException("Previous field state was changed");
    }

    if (emptyCellsCount(newField) == emptyCellsCount(oldField)) {
      throw new IllegalArgumentException("No move was made");
    }

    if (emptyCellsCount(newField) != emptyCellsCount(oldField) - 1) {
      throw new IllegalArgumentException("Was made more than one move");
    }

    int moveSimbol = getCurrentMoveSimbol(updatedGameDomain);

    if (!correctSymbolWasPlaced(newField, oldField, moveSimbol)) {
      throw new IllegalArgumentException("Incorrect symbol was placed on field");
    }

    return true;
  }

  @Override
  public boolean isGameOver(GameDomain game) {
    ArgumentChecker.checkNotNull(game, "GameService.isGameOver");

    if (game.getGameState() == GameState.WIN || game.getGameState() == GameState.DRAW)
      return true;

    FieldDomain fieldDomain = game.getField();

    if (FieldChecker.isWin(fieldDomain, game.getFirstPlayerSimbol())) {
      game.setGameState(GameState.WIN);
      game.setWinner(Winner.FIRST_PLAYER);
      return true;
    } else if (FieldChecker.isWin(fieldDomain, game.getSecondPlayerSimbol())) {
      game.setGameState(GameState.WIN);
      if (game.isPvp()) {
        game.setWinner(Winner.SECOND_PLAYER);
      } else {
        game.setWinner(Winner.AI);
      }
      return true;
    } else if (FieldChecker.isDraw(fieldDomain)) {
      game.setGameState(GameState.DRAW);
      return true;
    }

    return false;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // ----------------------------------- приватные методы --------------------------------------- //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Метод для проверки того, что старые заполненные клетки поля не были изменены
   * @param oldOne поле из репозитория
   * @param newOne поле, присланное со стороны пользователя
   * @return <code>true</code> - клетки не были изменены; <code>false</code> - клетки были изменены
   */
  private boolean cellsWereNotChanged(FieldDomain oldOne, FieldDomain newOne) {
    for (int i = 0; i < oldOne.getRows(); i++) {
      for (int j = 0; j < oldOne.getColumns(); j++) {
        // заполненные клетки старого поля не совпадают с заполненными клетками нового поля (то есть
        // старые были изменены)
        if (oldOne.get(i, j) != Constants.EMPTY && oldOne.get(i, j) != newOne.get(i, j)) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean moveWasMadeByCorrectPlayer(GameDomain game, UUID playerId) {
    if ((game.isPvp() && !playerId.equals(game.getCurrentTurnPlayerId()))
        || (!game.isPvp() && !playerId.equals(game.getFirstPlayerId()))) {
      return false;
    }

    return true;
  }

  private int getCurrentMoveSimbol(GameDomain game) {
    int moveSimbol = game.getFirstPlayerSimbol();

    if (game.isPvp() && game.getCurrentTurnPlayerId().equals(game.getSecondPlayerId())) {
      moveSimbol = game.getSecondPlayerSimbol();
    }
    return moveSimbol;
  }

  private int emptyCellsCount(FieldDomain field) {
    int count = 0;

    for (int i = 0; i < Constants.FIELD_ROWS; i++) {
      for (int j = 0; j < Constants.FIELD_COLUMNS; j++) {
        if (field.get(i, j) == Constants.EMPTY) count++;
      }
    }

    return count;
  }

  private boolean correctSymbolWasPlaced(FieldDomain newField, FieldDomain oldField, int currentSimbol) {
    for (int i = 0; i < Constants.FIELD_ROWS; i++) {
      for (int j = 0; j < Constants.FIELD_COLUMNS; j++) {

        if (oldField.get(i, j) == Constants.EMPTY && newField.get(i, j) == currentSimbol)
          return true;

      }
    }

    return false;
  }
}

package com.asadaker.tictactoe.domain.checker;

import com.asadaker.tictactoe.domain.model.GameDomain;
import com.asadaker.tictactoe.domain.other.Constants;
import com.asadaker.tictactoe.domain.other.GameState;

public class ArgumentChecker {
  public static void checkPlayerSimbol(int simbol, String methodName) {
    if (simbol != Constants.O && simbol != Constants.X) {
      throw new IllegalArgumentException(methodName + ": simbol must be O or X!");
    }
  }

  public static void checkNotNull(Object obj, String methodName) {
    if (obj == null) {
      throw new IllegalArgumentException(methodName + ": object cannot be null!");
    }
  }

  public static void isGameInProcess(GameDomain game) {
    if (game.getGameState() == GameState.WAITING_PLAYERS) {
      throw new IllegalArgumentException("Game has not started");
    } else if (game.getGameState() == GameState.DRAW || game.getGameState() == GameState.WIN) {
      throw new IllegalArgumentException("Game is over");
    }
  }
}

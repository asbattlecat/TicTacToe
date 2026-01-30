package com.asadaker.tictactoe.domain.service;

import com.asadaker.tictactoe.domain.interfaces.AiAlgorithm;
import com.asadaker.tictactoe.domain.model.Cell;
import com.asadaker.tictactoe.domain.model.FieldDomain;
import com.asadaker.tictactoe.domain.other.Constants;
import com.asadaker.tictactoe.domain.checker.FieldChecker;
import java.util.ArrayList;
import java.util.List;

public class Minimax implements AiAlgorithm {
  /**
   * Метод для поиска наилучшего хода со стороны компьютера
   * @param field поле, на котором ищем лучших ход
   * @param isMaximizing булевое условие, проверяющее, максимизируем или минимизируем мы шанс
   *     выигрыша
   * @param maximizingValue значение (О или Х), которым обозначен ход
   * @return возвращаем клетку, которая будет наилучшим ходом
   */
  @Override
  public Cell getBestMove(FieldDomain field, boolean isMaximizing, int maximizingValue) {
    checkMaximizingValue(maximizingValue);
    int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    Cell bestMove = null;
    List<Cell> emptyCells = getEmptyCells(field);
    FieldDomain fieldCopy = new FieldDomain(field);

    for (Cell c : emptyCells) {
      // ставим значение в соответствие игроком, шансы на победу которого делаем высокими
      if (maximizingValue == Constants.X) {
        fieldCopy.setX(c.getRow(), c.getColumn());
      } else {
        fieldCopy.setO(c.getRow(), c.getColumn());
      }

      int score = minimax(fieldCopy, !isMaximizing, maximizingValue);

      // отменяем ход
      fieldCopy.setCell(c);

      if (isMaximizing && score > bestScore) {
        bestScore = score;
        bestMove = new Cell(c.getRow(), c.getColumn(), maximizingValue);
      } else if (!isMaximizing && score < bestScore) {
        bestScore = score;
        int minimizingValue = (maximizingValue == Constants.X) ? Constants.O : Constants.X;
        bestMove = new Cell(c.getRow(), c.getColumn(), minimizingValue);
      }
    }

    return bestMove;
  }

  /**
   * Метод для анализа хода, поиск наилучшего
   * @param field поле игры Крестики-Нолики
   * @param isMaximizing переменная для перехода на другого игрока
   * @param maximizingValue значение (Х или О), к победе которого мы стримимся
   * @return получаем значение, которое было рассчитано на основе ходов
   */
  private int minimax(FieldDomain field, boolean isMaximizing, int maximizingValue) {
    // проверяем состояние игры
    if (FieldChecker.isWin(field, Constants.X)) {
      return (maximizingValue == Constants.X) ? 10 : -10;
    } else if (FieldChecker.isWin(field, Constants.O)) {
      return (maximizingValue == Constants.X) ? -10 : 10;
    } else if (FieldChecker.isDraw(field)) {
      return 0;
    }

    int minimizingValue = (maximizingValue == Constants.X) ? Constants.O : Constants.X;

    List<Cell> emptyCells = getEmptyCells(field);
    if (isMaximizing) {
      int maxEval = Integer.MIN_VALUE;
      for (Cell c : emptyCells) {
        field.setCell(c.getRow(), c.getColumn(), maximizingValue);
        int eval = minimax(field, false, maximizingValue);
        field.setCell(c);
        maxEval = Math.max(maxEval, eval);
      }
      return maxEval;
    } else {
      int minEval = Integer.MAX_VALUE;
      for (Cell c : emptyCells) {
        field.setCell(c.getRow(), c.getColumn(), minimizingValue);
        int eval = minimax(field, true, maximizingValue);
        field.setCell(c);
        minEval = Math.min(minEval, eval);
      }
      return minEval;
    }
  }

  private List<Cell> getEmptyCells(FieldDomain field) {
    List<Cell> list = new ArrayList<>();

    for (int i = 0; i < field.getRows(); i++) {
      for (int j = 0; j < field.getColumns(); j++) {
        if (field.get(i, j) == Constants.EMPTY)
          list.add(new Cell(i, j, Constants.EMPTY));
      }
    }

    return list;
  }

  private void checkMaximizingValue(int maximizingValue) {
    if (maximizingValue != Constants.X && maximizingValue != Constants.O) {
      throw new IllegalArgumentException(
          "Expecting another maximizingValue! (expected vale: X or O)");
    }
  }
}
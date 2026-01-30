package com.asadaker.tictactoe.domain.checker;

import com.asadaker.tictactoe.domain.model.FieldDomain;
import com.asadaker.tictactoe.domain.other.Constants;

public class FieldChecker {
  public static boolean isWin(FieldDomain field, int value) {
    return checkFullLine(field, value);
  }

  public static boolean isDraw(FieldDomain field) {
    return !hasFieldEmptyCell(field) && !isWin(field, Constants.O)
            && !isWin(field, Constants.X);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // ----------------------------------- приватные методы --------------------------------------- //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private static boolean hasFieldEmptyCell(FieldDomain field) {
    for (int row = 0; row < field.getRows(); row++) {
      for (int column = 0; column < field.getColumns(); column++) {
        if (field.get(row, column) == Constants.EMPTY) return true;
      }
    }

    return false;
  }

  private static boolean checkFullLine(FieldDomain field, int value) {
    boolean isFull = false;
    for (int row = 0; row < field.getRows() && !isFull; row++) {
      if (isRowFull(field, row, value)) return true;
    }

    for (int column = 0; column < field.getColumns() && !isFull; column++) {
      if (isColumnFull(field, column, value)) return true;
    }

    return isUpperLeftDiagonalFull(field, value) || isUpperRightDiagonalFull(field, value);
  }

  private static boolean isRowFull(FieldDomain field, int row, int value) {
    int firstCell = field.get(row, 0);
    if (firstCell != value) return false;

    for (int column = 1; column < field.getColumns(); column++) {
      if (field.get(row, column) != firstCell)
        return false;
    }

    return true;
  }

  private static boolean isColumnFull(FieldDomain field, int column, int value) {
    int firstCell = field.get(0, column);
    if (firstCell != value)
      return false;

    for (int row = 1; row < field.getRows(); row++) {
      if (field.get(row, column) != firstCell)
        return false;
    }

    return true;
  }

  /*
    * . .
    . * .
    . . *
   */
  private static boolean isUpperLeftDiagonalFull(FieldDomain field, int value) {
    int firstCell = field.get(0, 0);
    if (firstCell != value)
      return false;

    for (int i = 1; i < field.getRows(); i++) {
      if (field.get(i, i) != firstCell)
        return false;
    }

    return true;
  }

  /*
    . . *
    . * .
    * . .
  */
  private static boolean isUpperRightDiagonalFull(FieldDomain field, int value) {
    int firstCell = field.get(0, field.getColumns() - 1);
    if (firstCell != value)
      return false;

    for (int i = 1; i < field.getRows(); i++) {
      if (field.get(i, field.getColumns() - 1 - i) != firstCell)
        return false;
    }

    return true;
  }
}

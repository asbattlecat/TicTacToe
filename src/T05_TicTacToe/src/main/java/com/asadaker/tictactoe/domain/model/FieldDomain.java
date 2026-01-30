package com.asadaker.tictactoe.domain.model;

import com.asadaker.tictactoe.domain.other.Constants;

public class FieldDomain {
  private int[][] gameField;
  private int rows;
  private int columns;

  public FieldDomain(FieldDomain field) {
    copyFieldObject(field);
  }

  public FieldDomain() {
    gameField = new int[Constants.FIELD_ROWS][Constants.FIELD_COLUMNS];
    rows = Constants.FIELD_ROWS;
    columns = Constants.FIELD_COLUMNS;
  }

  public int get(int row, int column) {
    return gameField[row][column];
  }

  public int[][] getGameField() {
    return gameField;
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  public void setCell(int row, int column, int value) {
    if (value != Constants.X && value != Constants.EMPTY && value != Constants.O) {
      throw new IllegalArgumentException("Value must only be Constants.X, Constants.O or Constants.EMPTY!");
    }
    checkFieldIndexes(row, column, "setCell");

    gameField[row][column] = value;
  }

  public void setCell(Cell cell) {
    checkNullException(cell, "setCell");
    checkFieldIndexes(cell.getRow(), cell.getColumn(), "setCell");
    gameField[cell.getRow()][cell.getColumn()] = cell.getValue();
  }

  public void setX(int row, int column) {
    checkFieldIndexes(row, column, "setX");
    
    gameField[row][column] = Constants.X;
  }

  public void setO(int row, int column) {
    checkFieldIndexes(row, column, "setO");
    
    gameField[row][column] = Constants.O;
  }

  public void setGameField(int[][] gameField) {
    copyField(gameField);
  }

  public void cleanField() {
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        gameField[row][column] = Constants.EMPTY;
      }
    }
  }
  
  private void checkFieldIndexes(int row, int column, String methodName) {
    if (row < 0 || row >= rows || column < 0 || column >= columns) {
      throw new IllegalArgumentException(methodName + ": column or row is less then 0 or more than field size!");
    }
  }

  private void copyFieldObject(FieldDomain fieldObject) {
    checkNullException(fieldObject, "copyFieldObject");

    rows = fieldObject.getRows();
    columns = fieldObject.getColumns();

    int[][] another = fieldObject.getGameField();

    copyField(another);
  }

  private void copyField(int[][] another) {
    if (another == null) {
      throw new IllegalArgumentException("array is null, expecting initialized value!");
    }

    if (another.length != rows || another[0].length != columns) {
      throw new IllegalArgumentException("gameField size is not correct!");
    }
    gameField = new int[rows][columns];
    for (int i = 0; i < rows; i++) {
      System.arraycopy(another[i], 0, gameField[i], 0, columns);
    }
  }

  private void checkNullException(Object obj, String methodName) {
    if (obj == null) {
      throw new IllegalArgumentException(methodName + ":current value is null, expecting initialized value!");
    }
  }
}

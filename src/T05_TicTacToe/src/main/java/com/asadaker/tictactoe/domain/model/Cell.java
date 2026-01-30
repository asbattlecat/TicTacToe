package com.asadaker.tictactoe.domain.model;

import com.asadaker.tictactoe.domain.other.Constants;

public class Cell {
  private int row;
  private int column;
  private int value;

  public Cell(int row, int column, int value) {
    if (value != Constants.X && value != Constants.O && value != Constants.EMPTY) {
      throw new IllegalArgumentException("Cell value is unknown!");
    }

    this.row = row;
    this.column = column;
    this.value = value;
  }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  public int getValue() {
    return value;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public void setValue(int value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;

    if (object == null || getClass() != object.getClass()) return false;

    Cell that = (Cell) object;
    return row == that.row && column == that.column && value == that.value;
  }
}


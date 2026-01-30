package com.asadaker.tictactoe.domain.interfaces;

import com.asadaker.tictactoe.domain.model.Cell;
import com.asadaker.tictactoe.domain.model.FieldDomain;

public interface AiAlgorithm {
  Cell getBestMove(FieldDomain field, boolean isMaximizing, int moveSimbol);
}

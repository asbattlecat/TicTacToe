package com.asadaker.tictactoe.datasource.model;

import java.util.UUID;

public record UserWinrate(UUID userId, double winrate) {
}

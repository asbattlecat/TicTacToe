package com.asadaker.tictactoe.web.model;

import com.asadaker.tictactoe.web.other.GameMode;
import jakarta.validation.constraints.NotNull;

public record NewGameRequest(
        @NotNull(message = "gameMode is required") GameMode gameMode
) {}

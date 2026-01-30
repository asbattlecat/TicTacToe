package com.asadaker.tictactoe.web.model;

import java.util.UUID;

public record LeaderboardResponse(UUID userId, String login, double winrate) {
}

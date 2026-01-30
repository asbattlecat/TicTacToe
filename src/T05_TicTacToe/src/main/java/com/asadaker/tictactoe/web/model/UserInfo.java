package com.asadaker.tictactoe.web.model;

import com.asadaker.tictactoe.domain.other.Role;

import java.util.List;
import java.util.UUID;

public record UserInfo(
        UUID id,
        List<Role> roles,
        String login) {
}

package com.asadaker.tictactoe.web.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshJwtRequest {
  private String refreshToken;
}

package com.asadaker.tictactoe.web.mapper;

import com.asadaker.tictactoe.domain.model.FieldDomain;
import com.asadaker.tictactoe.domain.model.GameDomain;
import com.asadaker.tictactoe.domain.model.UserDomain;
import com.asadaker.tictactoe.web.model.FieldDto;
import com.asadaker.tictactoe.web.model.GameDto;
import com.asadaker.tictactoe.web.model.UserDto;

public class DomainWebMapper {

  public GameDomain toDomain(GameDto gameDto) {
    GameDomain game = new GameDomain();
    game.setId(gameDto.getId());
    game.setField(toDomain(gameDto.getField()));

    game.setGameState(gameDto.getGameState());

    game.setFirstPlayerId(gameDto.getFirstPlayerId());
    game.setSecondPlayerId(gameDto.getSecondPlayerId());

    game.setFirstPlayerConnected(gameDto.isFirstPlayerConnected());
    game.setSecondPlayerConnected(gameDto.isSecondPlayerConnected());

    game.setFirstPlayerSimbol(gameDto.getFirstPlayerSimbol());
    game.setSecondPlayerSimbol(gameDto.getSecondPlayerSimbol());

    game.setCurrentTurnPlayerId(gameDto.getCurrentTurnPlayerId());
    game.setWinner(gameDto.getWinner());
    game.setPvp(gameDto.isPvp());

    game.setCreationDate(gameDto.getCreationDate());

    return game;
  }

  public GameDto toDto(GameDomain gameDomain) {
    GameDto game = new GameDto();
    game.setId(gameDomain.getId());
    game.setField(toDto(gameDomain.getField()));

    game.setGameState(gameDomain.getGameState());

    game.setFirstPlayerId(gameDomain.getFirstPlayerId());
    game.setSecondPlayerId(gameDomain.getSecondPlayerId());

    game.setFirstPlayerConnected(gameDomain.isFirstPlayerConnected());
    game.setSecondPlayerConnected(gameDomain.isSecondPlayerConnected());

    game.setFirstPlayerSimbol(gameDomain.getFirstPlayerSimbol());
    game.setSecondPlayerSimbol(gameDomain.getSecondPlayerSimbol());

    game.setCurrentTurnPlayerId(gameDomain.getCurrentTurnPlayerId());
    game.setWinner(gameDomain.getWinner());
    game.setPvp(gameDomain.isPvp());

    game.setCreationDate(gameDomain.getCreationDate());

    return game;
  }

  public FieldDomain toDomain(FieldDto fieldDto) {
    FieldDomain field = new FieldDomain();
    field.setGameField(fieldDto.getGameField());
    return field;
  }

  public FieldDto toDto(FieldDomain fieldDomain) {
    FieldDto field = new FieldDto();
    field.setGameField(fieldDomain.getGameField());
    return field;
  }

  public UserDomain toDomain(UserDto userDto) {
    UserDomain userDomain = new UserDomain();
    userDomain.setId(userDto.getId());
    userDomain.setLogin(userDto.getLogin());
    userDomain.setPassword(userDto.getPassword());
    return userDomain;
  }

  public UserDto toDto(UserDomain userDomain) {
    UserDto userDto = new UserDto();
    userDto.setId(userDomain.getId());
    userDto.setLogin(userDomain.getLogin());
    userDto.setPassword(userDomain.getPassword());
    return userDto;
  }
}

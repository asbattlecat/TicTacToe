package com.asadaker.tictactoe.datasource.mapper;

import com.asadaker.tictactoe.datasource.model.FieldEntity;
import com.asadaker.tictactoe.datasource.model.GameEntity;
import com.asadaker.tictactoe.datasource.model.UserEntity;
import com.asadaker.tictactoe.domain.model.GameDomain;
import com.asadaker.tictactoe.domain.model.FieldDomain;
import com.asadaker.tictactoe.domain.model.UserDomain;

public class DomainEntityMapper {
  public GameEntity toEntity(GameDomain game) {
    GameEntity entity = new GameEntity();
    entity.setId(game.getId());
    entity.setField(toEntity(game.getField()));

    entity.setGameState(game.getGameState());

    entity.setFirstPlayerId(game.getFirstPlayerId());
    entity.setSecondPlayerId(game.getSecondPlayerId());

    entity.setFirstPlayerConnected(game.isFirstPlayerConnected());
    entity.setSecondPlayerConnected(game.isSecondPlayerConnected());

    entity.setFirstPlayerSimbol(game.getFirstPlayerSimbol());
    entity.setSecondPlayerSimbol(game.getSecondPlayerSimbol());

    entity.setCurrentTurnPlayerId(game.getCurrentTurnPlayerId());
    entity.setWinner(game.getWinner());
    entity.setPvp(game.isPvp());

    entity.setCreationDate(game.getCreationDate());

    return entity;
  }

  public GameDomain toDomain(GameEntity entity) {
    GameDomain game = new GameDomain();
    game.setId(entity.getId());
    game.setField(toDomain(entity.getField()));

    game.setGameState(entity.getGameState());

    game.setFirstPlayerId(entity.getFirstPlayerId());
    game.setSecondPlayerId(entity.getSecondPlayerId());

    game.setFirstPlayerConnected(entity.isFirstPlayerConnected());
    game.setSecondPlayerConnected(entity.isSecondPlayerConnected());

    game.setFirstPlayerSimbol(entity.getFirstPlayerSimbol());
    game.setSecondPlayerSimbol(entity.getSecondPlayerSimbol());

    game.setCurrentTurnPlayerId(entity.getCurrentTurnPlayerId());
    game.setWinner(entity.getWinner());
    game.setPvp(entity.isPvp());

    game.setCreationDate(entity.getCreationDate());

    return game;
  }

  public FieldEntity toEntity(FieldDomain field) {
    FieldEntity entity = new FieldEntity();
    entity.setGameField(field.getGameField());
    return entity;
  }

  public FieldDomain toDomain(FieldEntity entity) {
    FieldDomain field = new FieldDomain();
    field.setGameField(entity.getGameField());
    return field;
  }

  public UserDomain toDomain(UserEntity userEntity) {
    UserDomain userDomain = new UserDomain();
    userDomain.setId(userEntity.getId());
    userDomain.setLogin(userEntity.getLogin());
    userDomain.setPassword(userEntity.getPassword());
    return userDomain;
  }

  public UserEntity toEntity(UserDomain userDomain) {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(userDomain.getId());
    userEntity.setLogin(userDomain.getLogin());
    userEntity.setPassword(userDomain.getPassword());
    return userEntity;
  }
}

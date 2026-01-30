package com.asadaker.tictactoe.web.controller;

import com.asadaker.tictactoe.datasource.model.UserWinrate;
import com.asadaker.tictactoe.domain.interfaces.GameManager;
import com.asadaker.tictactoe.domain.interfaces.GameRepositoryService;
import com.asadaker.tictactoe.domain.interfaces.UserService;
import com.asadaker.tictactoe.domain.model.GameDomain;
import com.asadaker.tictactoe.domain.interfaces.GameService;
import com.asadaker.tictactoe.domain.other.GameState;
import com.asadaker.tictactoe.web.model.*;
import com.asadaker.tictactoe.web.other.GameMode;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.asadaker.tictactoe.web.mapper.DomainWebMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
public class WebController {
  private final GameService gameService;
  private final UserService userService;
  private final GameManager gameManager;
  private final GameRepositoryService gameRepositoryService;
  private final DomainWebMapper domainWebMapper;

  public WebController(GameService gameService, UserService userService, GameManager gameManager,
                       GameRepositoryService gameRepositoryService, DomainWebMapper domainWebMapper) {
    this.gameService = gameService;
    this.userService = userService;
    this.gameManager = gameManager;
    this.gameRepositoryService = gameRepositoryService;
    this.domainWebMapper = domainWebMapper;
  }

  /**
   * Создание новой игры
   * @param request минимальное количество данных (вид игры и UUID пользователя)
   * @return Созданную игру
   */
  @PostMapping("/game/new")
  public ResponseEntity<GameDto> createGame(@Valid @RequestBody NewGameRequest request) {
    UUID userId = userService.userIdFromSecurityContextHolder();
    GameDomain gameDomain = (request.gameMode() == GameMode.PVP)
            ? gameManager.createGamePvp(userId) : gameManager.createGamePvc(userId);

    // на основе рандома либо ИИ делает ход, либо первый ход остается за пользователем
    if (gameManager.isFirstMoveByAi(gameDomain)) {
      gameDomain = gameService.getNextMoveAi(gameDomain, gameDomain.getSecondPlayerSimbol());
    }

    gameRepositoryService.saveGame(gameDomain);

    return ResponseEntity.ok(domainWebMapper.toDto(gameDomain));
  }

  /**
   * Получение формы для создания новой игры
   * @return Форму для создания новой игры
   */
  @GetMapping("/game/newForm")
  public ResponseEntity<NewGameRequest> newGameRequestForm() {
    return ResponseEntity.ok(new NewGameRequest(GameMode.PVC));
  }

  /**
   * Обновления игры после хода пользователя с валидацией присланных данных
   * @param gameId id игры, которую нужно обновить
   * @param newFieldDto присылаемое обновленное поле
   * @return обновленное поле (если игра с компьютером, то ход компьютера, а если с пользователем - меняется текущий ход)
   */
  @PatchMapping("/game/{gameId}")
  public ResponseEntity<GameDto> updateGame(@PathVariable UUID gameId, @RequestBody FieldDto newFieldDto) {
    if (!gameRepositoryService.gameExistsById(gameId)) {
      throw new NoSuchElementException("Game not found");
    }

    GameDomain newGameDomain = gameRepositoryService.findById(gameId);
    if (newGameDomain.getGameState() != GameState.PLAYING) {
      throw new IllegalArgumentException("This game is not in process. In was not started, or is ended, currentGameState: " + newGameDomain.getGameState());
    }
    newGameDomain.setField(domainWebMapper.toDomain(newFieldDto));

    // проверка хода пользователя
    UUID playerId = userService.userIdFromSecurityContextHolder();
    gameService.validateField(newGameDomain, gameId, playerId);

    if (!newGameDomain.isPvp()) {
      newGameDomain = gameService.getNextMoveAi(newGameDomain, newGameDomain.getSecondPlayerSimbol());
    } else {
      newGameDomain = gameManager.updateGamePvp(newGameDomain);
    }
    gameService.isGameOver(newGameDomain);
    gameRepositoryService.saveGame(newGameDomain);

    // отправляем ответ
    return ResponseEntity.ok(domainWebMapper.toDto(newGameDomain));
  }

  /**
   * Получение списка игр, к которым можно подключиться (pvp режим)
   * @return Список игр
   */
  @GetMapping("/game/available")
  public ResponseEntity<List<UUID>> availableGames() {
    UUID lookingFor = userService.userIdFromSecurityContextHolder();
    List<GameDomain> availableGamesDomain = gameRepositoryService.findAvailableGames(lookingFor);
//    return ResponseEntity.ok(availableGamesDomain.stream().map(domainWebMapper::toDto).toList());
    return ResponseEntity.ok(availableGamesDomain.stream().map(GameDomain::getId).toList());
  }

  /**
   * Подключение пользователя к игре
   * @param request запись RJoinRequest(gameId, userId). gameId - id игры, к которой происходит подключение; userId -
   *                пользователь, подключаемый к игре
   * @return Модель игры, к которой произошло подключение при успешных данных
   */
  @PatchMapping("/game/connection")
  public ResponseEntity<GameDto> joinToGame(@RequestBody JoinRequest request) {
    UUID userId = userService.userIdFromSecurityContextHolder();
    GameDomain gameDomain = gameManager.joinToGame(request.gameId(), userId);
    return ResponseEntity.ok(domainWebMapper.toDto(gameDomain));
  }

  @GetMapping("/game/connectionForm")
  public ResponseEntity<JoinRequest> joinRequestForm() {
    return ResponseEntity.ok(new JoinRequest(null));
  }

  /**
   * Получение данных об игре
   * @param gameId id игры, о которой получаются данные
   * @return Модель запрашиваемой игры
   */
  @GetMapping("/game/info/{gameId}")
  public ResponseEntity<GameDto> getGame(@PathVariable UUID gameId) {
    GameDomain gameDomain = gameRepositoryService.findById(gameId);
    return ResponseEntity.ok(domainWebMapper.toDto(gameDomain));
  }

  /**
   * Получение завершенных игр, по accessToken пользователя
   * @param request токен пользователя, по которому получается список завершенных игр
   * @return Список моделей завершенных игр
   */
  @GetMapping("/game/info/finished")
  public ResponseEntity<List<UUID>> finishedGamesByPlayer(@RequestBody TokenRequest request) {
    UUID id = userService.getIdFromToken(request.token());
    List<GameDomain> finishedGames = gameRepositoryService.findFinishedGames(id);
    return ResponseEntity.ok(finishedGames.stream().map(GameDomain::getId).toList());
  }

  @GetMapping("/game/leaderboard")
  public ResponseEntity<List<LeaderboardResponse>> getLeaderBoard(@RequestBody int leadersAmount) {
    List<UserWinrate> leaderBoard = gameRepositoryService.getLeaderBoard(leadersAmount);
    return ResponseEntity.ok(leaderBoard.stream().map(l -> new LeaderboardResponse(l.userId(), userService.getLoginById(l.userId()), l.winrate())).toList());
  }

  ///////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////// users //////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Получение данных о пользователе
   * @param userId id запрашиваемого пользователя
   * @return RUserInfo(userId, roles, login)
   */
  @GetMapping("/user/info/{userId}")
  public ResponseEntity<UserInfo> userInfo(@PathVariable UUID userId) {
    return ResponseEntity.ok(new UserInfo(userId, userService.getRolesById(userId), userService.getLoginById(userId)));
  }

  /**
   * Получение данных о пользователе
   * @param request токен запрашиваемого пользователя
   * @return RUserInfo(userId, roles, login)
   */
  @GetMapping("/user/info")
  public ResponseEntity<UserInfo> userInfo(@RequestBody TokenRequest request) {
    UUID userId = userService.getIdFromToken(request.token());
    return ResponseEntity.ok(new UserInfo(userId, userService.getRolesById(userId), userService.getLoginById(userId)));
  }
}
package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.dto.GameDto;
import com.codeoftheweb.salvo.dto.GamePlayerDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SalvoControllerTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GamePlayerRepository gamePlayerRepository;

    @Mock
    private PlayerRepository playerRepository;

    private SalvoController salvoController;

    @Before
    public void setUp() {
        salvoController = new SalvoController(gameRepository, gamePlayerRepository, playerRepository);
    }

    @Test
    public void getAllGames_ReturnsDtoForEachGame() {
        Game game1 = buildDefaultGame();
        game1.setId(1L);

        Game game2 = buildDefaultGame();
        game2.setId(2L);

        List<Game> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);

        when(gameRepository.findAll()).thenReturn(games);

        List<GameDto> resultDtos = salvoController.getAllGames();

        assertThat(resultDtos).hasSize(2);
    }

    @Test
    public void getAllGames_ReturnsDtoWithCorrectData() {
        long expectedId = 123L;
        Date expectedCreatedTime = new Date();

        Game game = buildDefaultGame();
        game.setId(expectedId);
        game.setCreationTime(expectedCreatedTime);

        List<Game> games = new ArrayList<>();
        games.add(game);

        when(gameRepository.findAll()).thenReturn(games);

        GameDto resultDto = salvoController.getAllGames().get(0);

        assertThat(resultDto.getId()).isEqualTo(expectedId);
        assertThat(resultDto.getCreated()).isEqualTo(expectedCreatedTime);
    }

    @Test
    public void makeGameDto_ReturnsDtoWithGamePlayers() {

        GamePlayer expectedGamePlayer1 = buildDefaultGamePlayer();
        GamePlayer expectedGamePlayer2 = buildDefaultGamePlayer();

        Set<GamePlayer> gamePlayers = new HashSet<>();
        gamePlayers.add(expectedGamePlayer1);
        gamePlayers.add(expectedGamePlayer2);

        Game game = buildDefaultGame();
        game.setGamePlayers(gamePlayers);

        List<Game> games = new ArrayList<>();
        games.add(game);

        when(gameRepository.findAll()).thenReturn(games);

        GameDto resultDto = salvoController.getAllGames().get(0);

        assertThat(resultDto.getGamePlayers()).hasSize(2);
    }

    @Test
    public void makeGameDto_ReturnsDtoWithCorrectPlayerData() {

        long expectedGamePlayerId = 123L;
        long expectedPlayerId = 456L;

        GamePlayer gamePlayer = buildDefaultGamePlayer();
        gamePlayer.setId(expectedGamePlayerId);
        gamePlayer.getPlayer().setId(expectedPlayerId);

        Set<GamePlayer> gamePlayers = new HashSet<>();
        gamePlayers.add(gamePlayer);

        Game game = buildDefaultGame();
        game.setGamePlayers(gamePlayers);

        List<Game> games = new ArrayList<>();
        games.add(game);

        when(gameRepository.findAll()).thenReturn(games);

        GameDto gameDto = salvoController.getAllGames().get(0);
        GamePlayerDto resultDto = gameDto.getGamePlayers().get(0);

        assertThat(resultDto.getId()).isEqualTo(expectedGamePlayerId);
    }

    private Game buildDefaultGame() {
        Game game = new Game();
        game.setId(1L);
        game.setCreationTime(new Date());

        GamePlayer gamePlayerTest1 = buildDefaultGamePlayer();
        GamePlayer gamePlayerTest2 = buildDefaultGamePlayer();

        Set<GamePlayer> gamePlayerSet = new HashSet<>();
        gamePlayerSet.add(gamePlayerTest1);
        gamePlayerSet.add(gamePlayerTest2);

        game.setGamePlayers(gamePlayerSet);

        return game;
    }

    private GamePlayer buildDefaultGamePlayer() {

        GamePlayer gamePlayerTest1 = new GamePlayer();
        gamePlayerTest1.setId(1L);
        gamePlayerTest1.setPlayer(buildDefaultPlayer());

        return gamePlayerTest1;
    }

    private Player buildDefaultPlayer() {
        return new Player("playerTest1@gmail.com");
    }

    private Salvo buildDefaultSalvo() {

        Salvo salvo = new Salvo();
        List<String> locations = new ArrayList<>(Arrays.asList("A1", "A2", "A3"));
        salvo.setLocations(locations);

        return salvo;
    }

    private Ship buildDefaultShip() {

        Ship ship = new Ship("destroyer");
        List<String> locations = new ArrayList<>(Arrays.asList("B1", "B2", "B3"));
        ship.setLocations(locations);

        return ship;
    }
}
package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    private GameRepository gameRepository;
    private GamePlayerRepository gamePlayerRepository;
    private PlayerRepository playerRepository;
    private PasswordEncoder passwordEncoder;

    public SalvoController(
            GameRepository gameRepository,
            GamePlayerRepository gamePlayerRepository,
            PlayerRepository playerRepository,
            PasswordEncoder passwordEncoder) {
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/username")
    public CurrentPlayerDto getCurrentPlayer(Authentication authentication) {
        return makeCurrentPlayerDto(playerRepository.findByUsername(authentication.getName()));
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<GameDto> checkPlayerId(Authentication authentication, @PathVariable Long gamePlayerId) {

        Player player = playerRepository.findByUsername(authentication.getName());

        if (player.getId() != gamePlayerId) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(getOneGame(gamePlayerId), HttpStatus.CREATED);
    }

    public GameDto getOneGame(Long gamePlayerId) {

        GameDto dto = new GameDto();
        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);

        List<GamePlayerDto> gamePlayerDtos = gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(player -> makeGamePlayerDto(player))
                .collect(toList());

        List<ShipDto> shipDtos = gamePlayer.getShips()
                .stream()
                .map(ship -> makeShipDto(ship))
                .collect(toList());

        List<ScoreDto> scoreDtos = gamePlayer.getGame().getScores()
                .stream()
                .map(score -> makeScoreDto(score))
                .collect(toList());

        dto.setId(gamePlayer.getGame().getId());
        dto.setCreated(gamePlayer.getGame().getCreationTime());
        dto.setGamePlayers(gamePlayerDtos);
        dto.setShips(shipDtos);
        dto.setScores(scoreDtos);

        return dto;
    }
    
    @RequestMapping(value = "/games", method = RequestMethod.GET)
    private GamesDto getGames() {
        return makeGamesDto();
    }

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<String> createPlayer(@RequestParam String username, @RequestParam String password) {
        if (username.isEmpty()) {
            return new ResponseEntity<>("No username given", HttpStatus.FORBIDDEN);
        }

        Player player = playerRepository.findByUsername(username);

        if (player != null) {
            return new ResponseEntity<>("Username already used", HttpStatus.CONFLICT);
        }

        playerRepository.save(new Player(username, passwordEncoder.encode(password)));

        return new ResponseEntity<>("Named added", HttpStatus.CREATED);
    }

    private List<GameDto> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGameDto(game))
                .collect(toList());
    }

    private GamePlayerDto makeGamePlayerDto(GamePlayer gamePlayer) {

        List<SalvoDto> salvoDtos = gamePlayer.getSalvoes()
                .stream()
                .map(salvo -> makeSalvoDto(salvo))
                .collect(toList());

        GamePlayerDto dto = new GamePlayerDto();
        dto.setId(gamePlayer.getId());
        dto.setPlayer(makePlayerDto(gamePlayer.getPlayer()));
        dto.setSalvoes(salvoDtos);

        return dto;
    }

    private GamesDto makeGamesDto() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        GamesDto dto = new GamesDto();
        dto.setCurrentPlayer(makeCurrentPlayerDto(playerRepository.findByUsername(auth.getName())));
        dto.setGames(getAllGames());

        return dto;
    }

    private GameDto makeGameDto(Game game) {

        List<GamePlayerDto> gamePlayerDtos = game.getGamePlayers()
                .stream()
                .map(gamePlayer -> makeGamePlayerDto(gamePlayer))
                .collect(toList());

        List<ScoreDto> scoreDtos = game.getScores()
                .stream()
                .map(score -> makeScoreDto(score))
                .collect(toList());

        GameDto dto = new GameDto();
        dto.setId(game.getId());
        dto.setCreated(game.getCreationTime());
        dto.setGamePlayers(gamePlayerDtos);
        dto.setScores(scoreDtos);

        return dto;
    }

    private PlayerDto makePlayerDto(Player player) {

        PlayerDto dto = new PlayerDto();
        dto.setId(player.getId());
        dto.setEmail(player.getUserName());

        return dto;
    }

    private SalvoDto makeSalvoDto(Salvo salvo) {

        SalvoDto dto = new SalvoDto();
        dto.setTurn(salvo.getTurn());
        dto.setLocations(salvo.getLocations());

        return dto;
    }

    private ShipDto makeShipDto(Ship ship) {

        ShipDto dto = new ShipDto();
        dto.setType(ship.getType());
        dto.setLocations(ship.getLocations());

        return dto;
    }

    private ScoreDto makeScoreDto(Score score) {

        ScoreDto dto = new ScoreDto();
        dto.setPlayerId(score.getPlayer().getId());
        dto.setPlayerName(score.getPlayer().getUserName());
        dto.setResult(score.getResult());
        dto.setFinishDate(score.getFinishDate());

        return dto;
    }

    private CurrentPlayerDto makeCurrentPlayerDto(Player player) {

        CurrentPlayerDto dto = new CurrentPlayerDto();
        dto.setName(player.getUserName());
        dto.setId(player.getId());

        return dto;
    }
}

package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    private GameRepository gameRepository;
    private GamePlayerRepository gamePlayerRepository;
    private PlayerRepository playerRepository;
    private PasswordEncoder passwordEncoder;
    private ShipRepository shipRepository;
    private SalvoRepository salvoRepository;

    public SalvoController(
            GameRepository gameRepository,
            GamePlayerRepository gamePlayerRepository,
            PlayerRepository playerRepository,
            PasswordEncoder passwordEncoder,
            ShipRepository shipRepository,
            SalvoRepository salvoRepository) {
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.shipRepository = shipRepository;
        this.salvoRepository = salvoRepository;
    }

    @RequestMapping(value = "/username")
    public CurrentPlayerDto getCurrentPlayer(Authentication authentication) {
        return makeCurrentPlayerDto(playerRepository.findByUsername(authentication.getName()));
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<GameDto> checkPlayer(@PathVariable Long gamePlayerId, Authentication authentication) {

        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);

        if (playerRepository.findByUsername(authentication.getName()) != gamePlayer.getPlayer() ) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        GameDto gameDto = getOneGame(gamePlayerId);

        return new ResponseEntity<>(gameDto, HttpStatus.CREATED);
    }

    private GameDto getOneGame(Long gamePlayerId) {

        GameDto dto = new GameDto();
        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);

        if (gamePlayer.getGame().getGamePlayers().size() == 1) {
            gamePlayer.setFirstPlayer(true);
            gamePlayerRepository.save(gamePlayer);
        }

        // TODO split out?
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
        dto.setTurn(gamePlayer.getGame().getTurn());

        return dto;
    }

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    private GamesDto getGames() {
        return makeGamesDto();
    }

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<GamePlayerDto> createGame(@RequestParam String username) {

        Player currentUser = playerRepository.findByUsername(username);

        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Game game = new Game();
        GamePlayer gamePlayer1 = new GamePlayer(currentUser, game);

        gameRepository.save(game);
        gamePlayerRepository.save(gamePlayer1);

        Map<String, Object> gamePlayerIds = new HashMap<>();

        gamePlayerIds.put("gpid", gamePlayer1.getId());

        GamePlayerDto dto = makeGamePlayerDto(gamePlayer1);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<GamePlayerDto> joinGame(@PathVariable Long gameId, Authentication authentication) {

        System.out.println("gameId " + gameId);

        Player currentUser = playerRepository.findByUsername(authentication.getName());

        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (gameRepository.findById(gameId) == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Game game = gameRepository.getOne(gameId);

        if (game.getGamePlayers().size() > 1) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (currentUser.getGames().contains(game)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        GamePlayer gamePlayer = new GamePlayer(currentUser, game);
        gamePlayerRepository.save(gamePlayer);
        gameRepository.save(game);
        GamePlayerDto dto = makeGamePlayerDto(gamePlayer);

        return new ResponseEntity<GamePlayerDto>(dto, HttpStatus.CREATED);
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

    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<GameDto> shipLocations(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Set<Ship> ships) {

        Player currentUser = playerRepository.findByUsername(authentication.getName());

        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);

        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (currentUser.getGamePlayers().contains(gamePlayer) == false) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer.getShips().size() > 0) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        for (Ship ship: ships) {
            System.out.println(ship.getType());
            System.out.println(ship.getLocations());
            shipRepository.save(ship);
            gamePlayer.addShip(ship);
        }

        gamePlayerRepository.save(gamePlayer);

        Game currentGame = gamePlayer.getGame();

        if (gamePlayer.getShips().size() == 5) {
            gamePlayer.setTurnToPlaceSalvoes(true);
            gamePlayerRepository.save(gamePlayer);
        }

        GameDto updatedGameDto = getOneGame(gamePlayer.getId());

        return new ResponseEntity<>(updatedGameDto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<GameDto> salvoLocations(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Set<Salvo> salvoes) {

        Player currentUser = playerRepository.findByUsername(authentication.getName());

        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);

        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (currentUser.getGamePlayers().contains(gamePlayer) == false) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

//        if (gamePlayer.getSalvoes().size() > 0) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }

        for (Salvo salvo: salvoes) {
            System.out.println(salvo.getLocations());
            salvoRepository.save(salvo);
            gamePlayer.addSalvo(salvo);
        }

        gamePlayerRepository.save(gamePlayer);

        Game currentGame = gamePlayer.getGame();
        long currentTurn = currentGame.getTurn();
        long updatedTurn = currentTurn + 1;
        currentGame.setTurn(updatedTurn);

        gameRepository.save(currentGame);

        gamePlayer.setTurnToPlaceSalvoes(false);
        gamePlayer.getOpponent().setTurnToPlaceSalvoes(true);

        gamePlayerRepository.save(gamePlayer);
        gamePlayerRepository.save(gamePlayer.getOpponent());

        GameDto updatedGameDto = getOneGame(gamePlayer.getId());

        return new ResponseEntity<>(updatedGameDto, HttpStatus.CREATED);
    }

    private List<GameDto> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGameDto(game))
                .collect(toList());
    }

    private Set<Ship> getOpponentShips(GamePlayer opponent) {

        Set<Ship> ships = opponent.getShips();

        return ships;
    }

    private List<String> getSalvoLocations(Long gamePlayerId) {

        List<String> salvoLocations = new ArrayList<String>();

        GamePlayer currentGamePlayer = gamePlayerRepository.getOne(gamePlayerId);

        Set<Salvo> salvoes = currentGamePlayer.getSalvoes();

        for (Salvo salvo: salvoes) {
            System.out.println("salvo locations: " + salvo.getLocations());
            salvoLocations = salvo.getLocations();

        }

        return salvoLocations;
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
        dto.setFirstPlayer(gamePlayer.isFirstPlayer());
        dto.setTurnToPlaceSalvoes(gamePlayer.isTurnToPlaceSalvoes());

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
        dto.setTurn(game.getTurn());

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
        dto.setLocations(salvo.getLocations());

        return dto;
    }

    private ShipDto makeShipDto(Ship ship) {

        List<HitDto> hitDtos;
        Set<Hit> hits = ship.getGamePlayer().getHits(ship.getGamePlayer().getOpponent().getSalvoes());

        Set<Hit> shipHits = hits.stream()
                .filter(hit -> hit.getShipType().equals(ship.getType()))
                .collect(Collectors.toSet());

        boolean isSunk = shipHits.size() >= ship.getLocations().size();

        hitDtos = shipHits
                .stream()
                .map(hit -> makeHitDto(hit))
                .collect(toList());

        ShipDto dto = new ShipDto();
        dto.setType(ship.getType());
        dto.setLocations(ship.getLocations());
        dto.setHits(hitDtos);
        dto.setSunk(isSunk);

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

    private HitDto makeHitDto(Hit hit) {

        HitDto dto = new HitDto();
        dto.setLocation(hit.getLocation());
        dto.setShipType(hit.getShipType());
        dto.setTurn(hit.getTurn());

        return dto;
    }
}

package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> getOneGame(@PathVariable Long gamePlayerId) {

        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);
        Map<String, Object> gameView = new LinkedHashMap<>();

        gameView.put("id", gamePlayer.getGame().getId());
        gameView.put("created", gamePlayer.getGame().getCreationTime());

        List<Map<String, Object>> gamePlayersList = new ArrayList<>();

        for (GamePlayer player : gamePlayer.getGame().getGamePlayers()) {
            Map<String, Object> gamePlayerMap = new LinkedHashMap<>();
            gamePlayerMap.put("id", player.getId());
            gamePlayerMap.put("player", player.makeGamePlayerDTO());
            gamePlayersList.add(gamePlayerMap);
        }

        gameView.put("gamePlayers", gamePlayersList);
        gameView.put("ships", makeShipDTO(gamePlayer));
        gameView.put("salvos", makeSalvoDTO(gamePlayer));

        return gameView;
    }

    @RequestMapping("/games")
    public List<Object> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGameDTO(game))
                .collect(toList());
    }

    public Map<String, Object> makeGameDTO(Game game) {

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationTime());

        List<Map<String, Object>> gamePlayersList = new ArrayList<>();

        for (GamePlayer gamePlayer : game.gamePlayers) {
            Map<String, Object> gamePlayerMap = new LinkedHashMap<>();
            gamePlayerMap.put("id", gamePlayer.getId());
            gamePlayerMap.put("player", gamePlayer.makeGamePlayerDTO());
            gamePlayersList.add(gamePlayerMap);
        }

        dto.put("gamePlayers", gamePlayersList);

        return dto;
    }

    public List<Map<String, Object>> makeShipDTO(GamePlayer gameplayer) {

        List<Map<String, Object>> shipList = new ArrayList<>();

        for (Ship ship : gameplayer.getShips()) {
            Map<String, Object> shipMap = new LinkedHashMap<>();
            shipMap.put("type", ship.getType());
            shipMap.put("locations", ship.getLocations());
            shipList.add(shipMap);
        }

        return shipList;
    }

    public List<Map<String, Object>> makeSalvoDTO(GamePlayer gamePlayer) {

        List<Map<String, Object>> salvoList = new ArrayList<>();

        for (Salvo salvo : gamePlayer.getSalvoes()) {
            Map<String, Object> salvoMap = new LinkedHashMap<>();
            salvoMap.put("turn", salvo.getTurn());
            salvoMap.put("id", makeSalvoIdDTO(gamePlayer));
            salvoList.add(salvoMap);
        }

        return salvoList;
    }

    public List<Map<String, Object>> makeSalvoIdDTO(GamePlayer gamePlayer) {

        List<Map<String, Object>> salvoIdList = new ArrayList<>();

        for (Salvo salvo : gamePlayer.getSalvoes()) {
            Map<String, Object> salvoIdMap = new LinkedHashMap<>();
            salvoIdMap.put("gamePlayerId", salvo.getGamePlayer().getId());
//            salvoIdMap.put("test", makeSalvoLocationsDTO(gamePlayer));
            salvoIdList.add(salvoIdMap);
        }


        return salvoIdList;
    }

    public List<Map<String, Object>> makeSalvoLocationsDTO(GamePlayer gamePlayer) {

        List<Map<String, Object>> salvoLocationsList = new ArrayList<>();

        for (Salvo salvo : gamePlayer.getSalvoes()) {
            Map<String, Object> salvoLocationsMap = new LinkedHashMap<>();
            salvoLocationsMap.put("locations", salvo.getLocations());
            salvoLocationsList.add(salvoLocationsMap);
        }

        return salvoLocationsList;
    }
}

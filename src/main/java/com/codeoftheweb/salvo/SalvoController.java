package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping("/api/game_view/{gamePlayerId}")
    public Map<String, Object> getOneGame(@PathVariable Long gamePlayerId) {

        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);
        Map<String, Object> gameView = new LinkedHashMap<>();

        gameView.put("id", gamePlayer.getId());

        return gameView;
    }

    @RequestMapping("/api/game_view/{gamePlayerId}")
    public Map<String, Object> getOneGame(@PathVariable Long gamePlayerId) {
        return gamePlayerRepository
            .

    }


    @RequestMapping("/games")
    public List<Object> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGameDTO(game))
                .collect(toList());
    }

    public Map<String, Object> makeGameViewDTO(GamePlayer gamePlayer) {

        Map<String, Object> gameView = new LinkedHashMap<>();

        gameView.put("id", gamePlayer.getGame().getId());
        gameView.put("created", gamePlayer.getGame().getCreationTime());
        gameView.put("gamePlayers", gamePlayer.getGame().getGamePlayers());

        return gameView;
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
}

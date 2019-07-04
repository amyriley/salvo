package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
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
}

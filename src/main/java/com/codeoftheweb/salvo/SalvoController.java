package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class SalvoController {
    
    @Autowired
    private GameRepository gameRepository;

    @RequestMapping("/games")
    public List<Game> getListOfGames() {

        List<Game> games = gameRepository.findAll();

        return games;
    }
}

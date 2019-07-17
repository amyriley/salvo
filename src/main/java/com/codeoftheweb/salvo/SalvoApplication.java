package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository PlayerRepository, GameRepository GameRepository, GamePlayerRepository GamePlayerRepository, ShipRepository ShipRepository, SalvoRepository SalvoRepository, ScoreRepository ScoreRepository) {
		return (args) -> {

			Player jBauer = new Player("j.bauer@ctu.gov", "24");
			Player cObrian = new Player("c.obrian@ctu.gov", "42");
			Player kBauer = new Player("kim_bauer@gmail.com", "kb");
			Player tAlmeida = new Player("t.almeida@ctu.gov", "mole");
			Player dPalmer = new Player("d.palmer@whitehouse.gov", "eagle");

			PlayerRepository.save(jBauer);
			PlayerRepository.save(cObrian);
			PlayerRepository.save(kBauer);
			PlayerRepository.save(tAlmeida);
			PlayerRepository.save(dPalmer);

			Game game1 = new Game();
			Game game2 = new Game();
			Game game3 = new Game();
			Game game4 = new Game();
			Game game5 = new Game();

			Date creationDate = new Date();
			game2.setCreationTime(Date.from(creationDate.toInstant().plusSeconds(3600)));
			game3.setCreationTime(Date.from(creationDate.toInstant().plusSeconds(7200)));

			GameRepository.save(game1);
			GameRepository.save(game2);
			GameRepository.save(game3);
			GameRepository.save(game4);
			GameRepository.save(game5);

			GamePlayer g1 = new GamePlayer(jBauer, game1);
			GamePlayer g2 = new GamePlayer(cObrian, game1);
			GamePlayer g3 = new GamePlayer(kBauer, game2);
			GamePlayer g4 = new GamePlayer(tAlmeida, game2);
			GamePlayer g5 = new GamePlayer(jBauer, game3);
			GamePlayer g6 = new GamePlayer(kBauer, game3);
			GamePlayer g7 = new GamePlayer(dPalmer, game4);

			GamePlayerRepository.save(g1);
			GamePlayerRepository.save(g2);
			GamePlayerRepository.save(g3);
			GamePlayerRepository.save(g4);
			GamePlayerRepository.save(g5);
			GamePlayerRepository.save(g6);
			GamePlayerRepository.save(g7);

			Ship ship1 = new Ship("carrier");
			Ship ship2 = new Ship("battleship");
			Ship ship3 = new Ship("submarine");
			Ship ship4 = new Ship("destroyer");
			Ship ship5 = new Ship("patrol boat");

			Ship ship6 = new Ship("carrier");
			Ship ship7 = new Ship("battleship");
			Ship ship8 = new Ship("submarine");
			Ship ship9 = new Ship("destroyer");
			Ship ship10 = new Ship("patrol boat");

			ShipRepository.save(ship1);
			ShipRepository.save(ship2);
			ShipRepository.save(ship3);
			ShipRepository.save(ship4);
			ShipRepository.save(ship5);
			ShipRepository.save(ship6);
			ShipRepository.save(ship7);
			ShipRepository.save(ship8);
			ShipRepository.save(ship9);
			ShipRepository.save(ship10);

			g3.addShip(ship5);
			g3.addShip(ship3);

			g7.addShip(ship1);

			g1.addShip(ship4);
			g1.addShip(ship3);
			g1.addShip(ship5);

			g2.addShip(ship9);
			g2.addShip(ship10);

			List<String> destroyerLocationsJack = new ArrayList<>(Arrays.asList("H2", "H3", "H4"));
			ship4.setLocations(destroyerLocationsJack);

			List<String> submarineLocationsJack = new ArrayList<>(Arrays.asList("E1", "F1", "G1"));
			ship3.setLocations(submarineLocationsJack);

			List<String> patrolBoatLocationsJack = new ArrayList<>(Arrays.asList("B4", "B5"));
			ship5.setLocations(patrolBoatLocationsJack);

			List<String> carrierLocations = new ArrayList<>(Arrays.asList("A1", "B1", "C1"));
			ship1.setLocations(carrierLocations);

			List<String> destroyerLocationsChloe = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
			ship9.setLocations(destroyerLocationsChloe);

			List<String> patrolBoatLocationsChloe = new ArrayList<>(Arrays.asList("F1", "F2"));
			ship10.setLocations(patrolBoatLocationsChloe);

			ShipRepository.save(ship5);
			ShipRepository.save(ship4);
			ShipRepository.save(ship3);
			ShipRepository.save(ship1);
			ShipRepository.save(ship9);
			ShipRepository.save(ship10);
			GamePlayerRepository.save(g3);
			GamePlayerRepository.save(g7);

			Salvo salvo1Jack = new Salvo(1);
			Salvo salvo2Jack = new Salvo(2);

			List<String> salvo1JackLocations = new ArrayList<>(Arrays.asList("B5", "C5", "F1"));
			salvo1Jack.setLocations(salvo1JackLocations);
			SalvoRepository.save(salvo1Jack);

			List<String> salvo2JackLocations = new ArrayList<>(Arrays.asList("F2", "D5"));
			salvo2Jack.setLocations(salvo2JackLocations);
			SalvoRepository.save(salvo2Jack);

			g1.addSalvo(salvo1Jack);
			g1.addSalvo(salvo2Jack);
			GamePlayerRepository.save(g1);
			SalvoRepository.save(salvo1Jack);
			SalvoRepository.save(salvo2Jack);

			Salvo salvo1Chloe = new Salvo(1);
			Salvo salvo2Chloe = new Salvo(2);

			List<String> salvo1ChloeLocations = new ArrayList<>(Arrays.asList("B4", "B5", "B6"));
			salvo1Chloe.setLocations(salvo1ChloeLocations);
			SalvoRepository.save(salvo1Chloe);

			List<String> salvo2ChloeLocations = new ArrayList<>(Arrays.asList("E1", "H3", "A2"));
			salvo2Chloe.setLocations(salvo2ChloeLocations);
			SalvoRepository.save(salvo2Chloe);

			g2.addSalvo(salvo1Chloe);
			g2.addSalvo(salvo2Chloe);
			GamePlayerRepository.save(g2);
			SalvoRepository.save(salvo1Chloe);
			SalvoRepository.save(salvo2Chloe);

			Score score1 = new Score(0);
			score1.setGame(game1);
			score1.setPlayer(jBauer);
			ScoreRepository.save(score1);

			jBauer.addScore(score1);
			PlayerRepository.save(jBauer);

			Score score2 = new Score(1);
			score2.setGame(game1);
			score2.setPlayer(cObrian);
			ScoreRepository.save(score2);
			PlayerRepository.save(cObrian);
			GameRepository.save(game1);

			Score score3 = new Score(0.5);
			Score score4 = new Score(0.5);

			Score score5 = new Score(1);
			Score score6 = new Score(1);

			score3.setGame(game2);
			score4.setGame(game2);
			score3.setPlayer(tAlmeida);
			score4.setPlayer(kBauer);

			score5.setGame(game3);
			score6.setGame(game3);
			score5.setPlayer(jBauer);
			score6.setPlayer(kBauer);

			ScoreRepository.save(score3);
			ScoreRepository.save(score4);
			ScoreRepository.save(score5);
			ScoreRepository.save(score6);

			PlayerRepository.save(tAlmeida);
			PlayerRepository.save(kBauer);
			PlayerRepository.save(jBauer);

			GameRepository.save(game2);
			GameRepository.save(game3);
		};
	}

}

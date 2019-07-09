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
	public CommandLineRunner initData(PlayerRepository PlayerRepository, GameRepository GameRepository, GamePlayerRepository GamePlayerRepository, ShipRepository ShipRepository) {
		return (args) -> {

			Player jBauer = new Player("j.bauer@ctu.gov");
			Player cObrian = new Player("c.obrian@ctu.gov");
			Player kBauer = new Player("kim_bauer@gmail.com");
			Player tAlmeida = new Player("t.almeida@ctu.gov");
			Player dPalmer = new Player("d.palmer@whitehouse.gov");

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

			ShipRepository.save(ship1);
			ShipRepository.save(ship2);
			ShipRepository.save(ship3);
			ShipRepository.save(ship4);
			ShipRepository.save(ship5);

			g3.addShip(ship5);
			g3.addShip(ship3);

			g7.addShip(ship1);

			List<String> patrolBoatLocations = new ArrayList<>(Arrays.asList("B4", "B5"));
			ship5.setLocations(patrolBoatLocations);

			List<String> submarineLocations = new ArrayList<>(Arrays.asList("E1", "F1", "G1"));
			ship3.setLocations(submarineLocations);

			List<String> carrierLocations = new ArrayList<>(Arrays.asList("A1", "B1", "C1"));
			ship1.setLocations(carrierLocations);

			ShipRepository.save(ship5);
			ShipRepository.save(ship3);
			ShipRepository.save(ship1);
			GamePlayerRepository.save(g3);
			GamePlayerRepository.save(g7);
		};
	}

}

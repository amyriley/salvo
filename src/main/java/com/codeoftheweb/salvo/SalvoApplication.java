package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository PlayerRepository, GameRepository GameRepository, GamePlayerRepository GamePlayerRepository) {
		return (args) -> {

			Player jBauer = new Player("j.bauer@ctu.gov");
			Player cObrian = new Player("c.obrian@ctu.gov");
			Player kBauer = new Player("kim_bauer@gmail.com");
			Player tAlmeida = new Player("t.almeida@ctu.gov");

			PlayerRepository.save(jBauer);
			PlayerRepository.save(cObrian);
			PlayerRepository.save(kBauer);
			PlayerRepository.save(tAlmeida);

			Game game1 = new Game();
			Game game2 = new Game();
			Game game3 = new Game();

			Date creationDate = new Date();
			game2.setCreationTime(Date.from(creationDate.toInstant().plusSeconds(3600)));
			game3.setCreationTime(Date.from(creationDate.toInstant().plusSeconds(7200)));

			GameRepository.save(game1);
			GameRepository.save(game2);
			GameRepository.save(game3);

			GamePlayer g1 = new GamePlayer(jBauer, game1);
			GamePlayer g2 = new GamePlayer(cObrian, game1);

			GamePlayerRepository.save(g1);
			GamePlayerRepository.save(g2);
		};
	}

}

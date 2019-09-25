package com.codeoftheweb.salvo;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SalvoApplication.class);
    }

	@Bean
	public PasswordEncoder passwordEncoder(){
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(
			PlayerRepository playerRepository,
			GameRepository gameRepository,
			GamePlayerRepository gamePlayerRepository,
			ShipRepository shipRepository,
			SalvoRepository salvoRepository,
			ScoreRepository scoreRepository)
	{
		return (args) -> {

			Player jBauer = new Player("j.bauer@ctu.gov", passwordEncoder.encode("24"));
			Player cObrian = new Player("c.obrian@ctu.gov", passwordEncoder.encode("42"));
			playerRepository.save(jBauer);
			playerRepository.save(cObrian);

			Game game1 = new Game();
			gameRepository.save(game1);

			GamePlayer g1 = new GamePlayer(jBauer, game1);
			GamePlayer g2 = new GamePlayer(cObrian, game1);
			gamePlayerRepository.save(g1);
			gamePlayerRepository.save(g2);
			gameRepository.save(game1);

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

			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);
			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);
			shipRepository.save(ship9);
			shipRepository.save(ship10);

			g1.addShip(ship1);
			g1.addShip(ship2);
			g1.addShip(ship3);
			g1.addShip(ship4);
			g1.addShip(ship5);

			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);

			gamePlayerRepository.save(g1);

			g2.addShip(ship6);
			g2.addShip(ship7);
			g2.addShip(ship8);
			g2.addShip(ship9);
			g2.addShip(ship10);

			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);
			shipRepository.save(ship9);
			shipRepository.save(ship10);

			gamePlayerRepository.save(g2);

			List<String> carrierLocationsJack = new ArrayList<>(Arrays.asList("A1", "B1", "C1", "D1", "E1"));
			ship1.setLocations(carrierLocationsJack);

			List<String> battleshipLocationsJack = new ArrayList<>(Arrays.asList("C1", "C2", "C3", "C4"));
			ship2.setLocations(battleshipLocationsJack);

			List<String> submarineLocationsJack = new ArrayList<>(Arrays.asList("E1", "F1", "G1"));
			ship3.setLocations(submarineLocationsJack);

			List<String> destroyerLocationsJack = new ArrayList<>(Arrays.asList("H2", "H3", "H4"));
			ship4.setLocations(destroyerLocationsJack);

			List<String> patrolBoatLocationsJack = new ArrayList<>(Arrays.asList("B4", "B5"));
			ship5.setLocations(patrolBoatLocationsJack);

			List<String> carrierLocationsChloe = new ArrayList<>(Arrays.asList("J1", "J2", "J3", "J4", "J5"));
			ship6.setLocations(carrierLocationsChloe);

			List<String> battleshipLocationsChloe = new ArrayList<>(Arrays.asList("A10", "B10", "C10", "D10"));
			ship7.setLocations(battleshipLocationsChloe);

			List<String> submarineLocationsChloe = new ArrayList<>(Arrays.asList("F8", "F9", "F10"));
			ship8.setLocations(submarineLocationsChloe);

			List<String> destroyerLocationsChloe = new ArrayList<>(Arrays.asList("B5", "C5", "D5"));
			ship9.setLocations(destroyerLocationsChloe);

			List<String> patrolBoatLocationsChloe = new ArrayList<>(Arrays.asList("F1", "F2"));
			ship10.setLocations(patrolBoatLocationsChloe);

			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);
			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);
			shipRepository.save(ship9);
			shipRepository.save(ship10);

			Player kBauer = new Player("kim_bauer@gmail.com", passwordEncoder.encode("kb"));
			Player tAlmeida = new Player("t.almeida@ctu.gov", passwordEncoder.encode("mole"));
			Player dPalmer = new Player("d.palmer@whitehouse.gov", passwordEncoder.encode("eagle"));

			playerRepository.save(kBauer);
			playerRepository.save(tAlmeida);
			playerRepository.save(dPalmer);

			Game game2 = new Game();
			Game game3 = new Game();
			Game game4 = new Game();
//			Game game5 = new Game();

			Date creationDate = new Date();
			game2.setCreationTime(Date.from(creationDate.toInstant().plusSeconds(3600)));
			game3.setCreationTime(Date.from(creationDate.toInstant().plusSeconds(7200)));

			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
//			gameRepository.save(game5);

			GamePlayer g3 = new GamePlayer(kBauer, game2);
			GamePlayer g4 = new GamePlayer(tAlmeida, game2);
			GamePlayer g5 = new GamePlayer(jBauer, game3);
			GamePlayer g6 = new GamePlayer(kBauer, game3);
			GamePlayer g7 = new GamePlayer(dPalmer, game4);

			gamePlayerRepository.save(g3);
			gamePlayerRepository.save(g4);
			gamePlayerRepository.save(g5);
			gamePlayerRepository.save(g6);
			gamePlayerRepository.save(g7);

			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
//			gameRepository.save(game5);


//			g3.addShip(ship5);
//			g3.addShip(ship3);

			shipRepository.save(ship5);
			shipRepository.save(ship4);
			shipRepository.save(ship3);
			shipRepository.save(ship1);
			shipRepository.save(ship9);
			shipRepository.save(ship10);
			gamePlayerRepository.save(g3);
			gamePlayerRepository.save(g7);

			Salvo salvo1Jack = new Salvo();
			Salvo salvo2Jack = new Salvo();
			Salvo salvo3Jack = new Salvo();
			Salvo salvo4Jack = new Salvo();

			List<String> salvo1JackLocations = new ArrayList<>(Arrays.asList("A10", "B10", "C10", "D10", "E10"));
			salvo1Jack.setLocations(salvo1JackLocations);
			salvoRepository.save(salvo1Jack);

			List<String> salvo2JackLocations = new ArrayList<>(Arrays.asList("J1", "J2", "J3", "J4", "J5"));
			salvo2Jack.setLocations(salvo2JackLocations);
			salvoRepository.save(salvo2Jack);

			List<String> salvo3JackLocations = new ArrayList<>(Arrays.asList("B5", "C5", "D5", "F1", "F2"));
			salvo3Jack.setLocations(salvo3JackLocations);
			salvoRepository.save(salvo3Jack);

			List<String> salvo4JackLocations = new ArrayList<>(Arrays.asList("F8", "F9", "F10"));
			salvo4Jack.setLocations(salvo4JackLocations);
			salvoRepository.save(salvo4Jack);

			g1.addSalvo(salvo1Jack);
			g1.addSalvo(salvo2Jack);
			g1.addSalvo(salvo3Jack);
			g1.addSalvo(salvo4Jack);
			gamePlayerRepository.save(g1);
			salvoRepository.save(salvo1Jack);
			salvoRepository.save(salvo2Jack);
			salvoRepository.save(salvo3Jack);
			salvoRepository.save(salvo4Jack);

			Salvo salvo1Chloe = new Salvo();
			Salvo salvo2Chloe = new Salvo();
			Salvo salvo3Chloe = new Salvo();
			Salvo salvo4Chloe = new Salvo();

			List<String> salvo1ChloeLocations = new ArrayList<>(Arrays.asList("A1", "A2", "B1", "D1", "E1"));
			salvo1Chloe.setLocations(salvo1ChloeLocations);
			salvoRepository.save(salvo1Chloe);

			List<String> salvo2ChloeLocations = new ArrayList<>(Arrays.asList("C1", "C2", "C3", "C4", "C5"));
			salvo2Chloe.setLocations(salvo2ChloeLocations);
			salvoRepository.save(salvo2Chloe);

			List<String> salvo3ChloeLocations = new ArrayList<>(Arrays.asList("H2", "H3", "H4"));
			salvo3Chloe.setLocations(salvo3ChloeLocations);
			salvoRepository.save(salvo3Chloe);

			List<String> salvo4ChloeLocations = new ArrayList<>(Arrays.asList("E1", "F1", "G1", "B4"));
			salvo4Chloe.setLocations(salvo4ChloeLocations);
			salvoRepository.save(salvo4Chloe);

			g2.addSalvo(salvo1Chloe);
			g2.addSalvo(salvo2Chloe);
			g2.addSalvo(salvo3Chloe);
			g2.addSalvo(salvo4Chloe);
			gamePlayerRepository.save(g2);
			salvoRepository.save(salvo1Chloe);
			salvoRepository.save(salvo2Chloe);
			salvoRepository.save(salvo3Chloe);
			salvoRepository.save(salvo4Chloe);

			playerRepository.save(tAlmeida);
			playerRepository.save(kBauer);
			playerRepository.save(jBauer);

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
		};

	}

	@Configuration
	class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(inputName -> {
				Player player = playerRepository.findByUsername(inputName);
				if (player != null) {
                    return new User(player.getUserName(), player.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Unknown user: " + inputName);
				}
			});
		}

		@Autowired
		private PlayerRepository playerRepository;

	}

	@EnableWebSecurity
	@Configuration
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private WebApplicationContext applicationContext;
		private WebSecurityConfiguration webSecurityConfiguration;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/rest/players*").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/web/home*").permitAll()
                    .antMatchers("/web/main.js").permitAll()
                    .antMatchers("/web/players*").permitAll()
                    .antMatchers("/api/players").permitAll()
                    .antMatchers("/api/game*").permitAll()
                    .antMatchers("/api/login*").permitAll()
                    .antMatchers("/web/game*").permitAll()
                    .antMatchers("/api/games/players*").permitAll()
                    .antMatchers("/web/game_view*").permitAll()
                    .antMatchers("/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginPage("/api/login")
                    .and()
                    .logout()
                    .logoutUrl("/api/logout");

			// if user is not authenticated, just send an authentication failure response
			http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> {
				System.out.println(exc);
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			});

			// if login is successful, just clear the flags asking for authentication
			http.formLogin().successHandler((req, res, auth) -> {
				clearAuthenticationAttributes(req);
			});

			// if login fails, just send an authentication failure response
			http.formLogin().failureHandler((req, res, exc) -> {
				System.out.println(exc);
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			});

			// if logout is successful, just send a success response
			http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		}

        private void clearAuthenticationAttributes(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        }
	}
}





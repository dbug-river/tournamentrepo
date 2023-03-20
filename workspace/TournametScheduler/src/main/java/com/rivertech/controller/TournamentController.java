package com.rivertech.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rivertech.model.Game;
import com.rivertech.model.Round;
import com.rivertech.model.Slot;
import com.rivertech.repositories.TournamentRepository;
import com.rivertech.services.LogFactory;
import com.rivertech.services.Planner;

/***
 * Tournament scheduler controller.
 * @author Omar Zammit
 */
@RestController
@RequestMapping("/api/tournament")
public class TournamentController {
	
	/* Add a logger to the controller class */
	final Logger logger = LogFactory.getConsoleHandler("TournamentController");
	
	/* Environment variable to access the application properties */
	@Autowired
	private Environment env;

	
	/***
	 * Get scheduled games using the custom planner class.
	 * @return A list of slots containing details about the game and the time slots.
	 */
	//just FYI: this is not typical REST URL
	@GetMapping("/getSchedules")
    public ResponseEntity<List<Slot>> getTournamentSchedules() {
		
		/* Return object data */
		List<Slot> tournamentSession = new ArrayList<>();
		HttpStatus status = HttpStatus.OK;
		
		try {
			/* Create a tournament repository */
			TournamentRepository repo = new TournamentRepository(env.getProperty("database.url"),
					env.getProperty("database.user"), env.getProperty("database.password"));
			
			/* Get all available rounds */
			List<Round> rounds = repo.getTournamentSessionByBetAmount();
			rounds.addAll(repo.getTournamentSessionByRound());
			
			/* Determine the start date as today at 9 am */
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime startDateTime = LocalDateTime.of(now.getYear(), 
					now.getMonth(), 
					now.getDayOfMonth(), 
					9, 0, 0);
			
			/* Create a planner class with 8 slots and a start date */
			Planner planner = new Planner();
			tournamentSession= planner.solve(rounds, 8, startDateTime);

			//The response from the method does not show two games per slot from 9-5pm (8 hours) but it shows 1 game per slot from 9-1am (16 hours)

			/* Return not found error if game ID does not exist */
			if(tournamentSession.isEmpty())
				status = HttpStatus.NOT_FOUND;
			
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.severe(e.getMessage());
		}
		
		return new ResponseEntity<List<Slot>>(tournamentSession, status);
    }
	
	
	/***
	 * Get top games based on their bet amount.
	 * A game should be eligible only if 10 or more players played the game.
	 * @param total The top number of games to retrieve.
	 * @return List of games by round count.
	 */
	@GetMapping("/findTopGamesByBetAmount")
    public ResponseEntity<List<Game>> findTopGamesByBetAmount(@RequestParam int total) {
		
		/* Return object data */
		List<Game> games = new ArrayList<Game>();
		HttpStatus status = HttpStatus.OK;
		
		try {
			/* Create a tournament repository */
			TournamentRepository repo = new TournamentRepository(env.getProperty("database.url"),
					env.getProperty("database.user"), env.getProperty("database.password"));
			
			/* Get game by ID */
			games = repo.getTopGamesByBetAmount(total);
			
			/* Return not found error if game ID does not exist */
			if(games.isEmpty())
				status = HttpStatus.NOT_FOUND;
			
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.severe(e.getMessage());
		}
		
		return new ResponseEntity<List<Game>>(games, status);
    }
	
	/***
	 * Get top games based on their round count.
	 * A game should be eligible only if 10 or more players played the game.
	 * @param total The top number of games to retrieve.
	 * @return List of games by round count.
	 */
	@GetMapping("/findTopGamesByRound")
    public ResponseEntity<List<Game>> findTopGamesByRound(@RequestParam int total) {
		
		/* Return object data */
		List<Game> games = new ArrayList<Game>();
		HttpStatus status = HttpStatus.OK;
		
		try {
			/* Create a tournament repository */
			TournamentRepository repo = new TournamentRepository(env.getProperty("database.url"),
					env.getProperty("database.user"), env.getProperty("database.password"));
			
			/* Get game by ID */
			games = repo.getTopGamesByCount(total);
			
			/* Return not found error if game ID does not exist */
			if(games.isEmpty())
				status = HttpStatus.NOT_FOUND;
			
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.severe(e.getMessage());
		}
		
		return new ResponseEntity<List<Game>>(games, status);
    }
	
	/***
	 * Get a game from the game dimension.
	 * @param id The game id.
	 * @return A Game entity representing a game dimension row.
	 */
	@GetMapping("/findGameById")
    public ResponseEntity<Game> findGameById(@RequestParam int id) {
		
		/* Return object data */
		Game game = null;
		HttpStatus status = HttpStatus.OK;
		
		try {
			/* Create a tournament repository */
			TournamentRepository repo = new TournamentRepository(env.getProperty("database.url"),
					env.getProperty("database.user"), env.getProperty("database.password"));
			
			/* Get game by ID */
			game = repo.getGameById(id);
			
			/* Return not found error if game ID does not exist */
			if(game == null)
				status = HttpStatus.NOT_FOUND;
			
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.severe(e.getMessage());
		}
		
		return new ResponseEntity<Game>(game, status);
    }
	
}

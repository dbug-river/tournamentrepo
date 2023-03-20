package com.rivertech.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.rivertech.model.Game;
import com.rivertech.model.Round;
import com.rivertech.services.LogFactory;

/***
 * Repository that exposes the required methods to allow the API to access
 * tournament data from the data warehouse.
 * 
 * @author Omar Zammit
 *
 */
public class TournamentRepository extends AbstractRepository {

	/* Add a logger to the controller class */
	final Logger logger = LogFactory.getConsoleHandler("TournamentRepository");

	/***
	 * Constructor that overrides the abstract class.
	 * 
	 * @param url      Database URL
	 * @param username Database username
	 * @param password Database password.
	 */
	public TournamentRepository(String url, String username, String password) {
		super(url, username, password);
	}

	/***
	 * Get the games that have the top rounds and their top players.
	 * 
	 * @return List of tournament sessions sorted by rounds
	 */
	public List<Round> getTournamentSessionByRound() {

		/* Create a connection to the database */
		Connection connection = null;
		List<Round> sessions = new ArrayList<Round>();

		try {
			connection = this.GetConnection();

			/* Query to get top rounds */
			String query = """
					SELECT
						vwr.game_id,
						vwr.game_name,
						vwr.provider,
						groupArray(vwr.user_id) AS users
					FROM(
						SELECT
							game_id,
							game_name,
							provider,
							user_id,
							rounds,
							round_ranking
						FROM VW_UserRankings
						WHERE round_ranking <=10
					) AS vwr
					GROUP BY
						vwr.game_id,
						vwr.game_name,
						vwr.provider
					HAVING
						COUNT(vwr.user_id) = 10
					ORDER BY
						SUM(vwr.rounds) DESC LIMIT 8""";
			logger.fine(query);

			/* Execute query */
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Game game = new Game(rs.getInt("game_id"), rs.getString("game_name"), rs.getString("provider"));
				Round session = new Round(game, (String[]) rs.getArray("users").getArray());
				sessions.add(session);
			}

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		/* Return a game even if it is null */
		return sessions;
	}

	/***
	 * Get the games that have the top rounds and their top players.
	 * 
	 * @return List of tournament sessions sorted by rounds
	 */
	public List<Round> getTournamentSessionByBetAmount() {

		/* Create a connection to the database */
		Connection connection = null;
		List<Round> sessions = new ArrayList<Round>();

		try {
			connection = this.GetConnection();

			/* Query to get top rounds */
			String query = """
					SELECT
						vwr.game_id,
						vwr.game_name,
						vwr.provider,
						groupArray(vwr.user_id) AS users
					FROM(
						SELECT
							game_id,
							game_name,
							provider,
							user_id,
							bet_amount,
							bet_amount_ranking
						FROM VW_UserRankings
						WHERE bet_amount_ranking <=10
					) AS vwr
					GROUP BY
						vwr.game_id,
						vwr.game_name,
						vwr.provider
					HAVING
						COUNT(vwr.user_id) = 10
					ORDER BY
						SUM(vwr.bet_amount) DESC LIMIT 8""";
			logger.fine(query);
			/* Execute query */
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Game game = new Game(rs.getInt("game_id"), rs.getString("game_name"), rs.getString("provider"));
				Round session = new Round(game, (String[]) rs.getArray("users").getArray());
				sessions.add(session);
			}

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		/* Return a game even if it is null */
		return sessions;
	}

	/***
	 * Get top games based on their bet amount. A game should be eligible only if 10
	 * or more players played the game.
	 * 
	 * @param total The top number of games to retrieve.
	 * @return List of games by bet amount.
	 */
	public List<Game> getTopGamesByBetAmount(int total) {
		/* Create a connection to the database */
		Connection connection = null;
		List<Game> games = new ArrayList<Game>();

		try {
			connection = this.GetConnection();

			/* Query to get the game from the database */
			String query = """
					SELECT
						game_id,
						game_name,
						provider
					FROM
						VW_UserRankings vur
					ORDER BY
						bet_amount DESC
					LIMIT ?
					""";

			//This query will return bet_amounts grouped by user_id and game_id

			// If you want to get the game with the most bet_amounts you would have to do :
			//			SELECT
			//					game_id,
			//					game_name,
			//					provider
			//			FROM
			//			VW_UserRankings vur
			//			ORDER BY
			//			sum(bet_amount) DESC
			//			LIMIT ?

			// In fact, you can do it on the game rounds table directly.

			//			select sum(bonus_amount_bet) as b, sum(real_amount_bet) as r, b + r, game_id, game_name
			//			from Staging_GameRound
			//			group by game_id, game_name
			//			order by b + r desc

					logger.fine(query);
			/* Execute query */
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, total);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Game game = new Game(rs.getInt("game_id"), rs.getString("game_name"), rs.getString("provider"));
				games.add(game);
			}

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		/* Return a game even if it is null */
		return games;
	}

	/***
	 * Get top games based on their round count. A game should be eligible only if
	 * 10 or more players played the game.
	 * 
	 * @param total The top number of games to retrieve.
	 * @return List of games by round count.
	 */
	public List<Game> getTopGamesByCount(int total) {
		/* Create a connection to the database */
		Connection connection = null;
		List<Game> games = new ArrayList<Game>();

		try {
			connection = this.GetConnection();

			/* Query to get the game from the database */
			String query = """
					SELECT
						game_id,
						game_name,
						provider
					FROM
						VW_UserRankings vur
					ORDER BY
						rounds DESC
					LIMIT ?
					""";
			logger.fine(query);

			//This query will return bet_counts grouped by user_id and game_id

			// If you want to get the game with the most bet_amounts you would have to do :
			//			SELECT
			//					game_id,
			//					game_name,
			//					provider
			//			FROM
			//			VW_UserRankings vur
			//			ORDER BY
			//			sum(rounds) DESC
			//			LIMIT ?

			// In fact, you can do it on the game rounds table directly.

			//			select count(*), game_id, game_name
			//			from Staging_GameRound
			//			group by game_id, game_name
			//			order by count(*) desc

			/* Execute query */
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, total);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Game game = new Game(rs.getInt("game_id"), rs.getString("game_name"), rs.getString("provider"));
				games.add(game);
			}

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		/* Return a game even if it is null */
		return games;
	}

	/***
	 * Get a game instance from the game dimension.
	 * 
	 * @param id The game ID to search.
	 * @return A game instance or NULL if the game is not found.
	 */
	public Game getGameById(int id) {
		/* Create a connection to the database */
		Connection connection = null;
		Game game = null;

		try {
			connection = this.GetConnection();

			/* Query to get the game from the database */
			String query = "SELECT game_id, game_name, provider FROM DW_GameDimension WHERE game_id = ?";
			logger.fine(query);
			/* Execute query */
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			/* Map row fields to game object */
			if (rs.next()) {
				game = new Game(rs.getInt("game_id"), rs.getString("game_name"), rs.getString("provider"));
			}

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		/* Return a game even if it is null */
		return game;
	}

}

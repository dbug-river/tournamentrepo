package com.rivertech.model;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A class that represents a tournament session.
 * 
 * @author Omar Zammit
 *
 */
public class Round {

	/***
	 * The game that will be played during the session.
	 */
	private Game game;

	/***
	 * The users that will play the tournament session.
	 */
	private String[] users;

	/***
	 * A unique identifier for the round.
	 */
	private UUID id;

	public Round(Game game, String[] users) {
		/* Generate a new ID for the round */
		this.id = UUID.randomUUID();
		this.game = game;
		this.users = users;
	}

	@JsonProperty("game_id")
	public int getGameID() {
		return game.getGameId();
	}

	@JsonProperty("players")
	public String[] getUsers() {
		return users;
	}

	@JsonIgnore
	public UUID getId() {
		return id;
	}

	@JsonIgnore
	public Game getGame() {
		return game;
	}

	@Override
	public String toString() {
		return "Game ID: " + getGame().getGameId() + " Users: " + String.join(",", getUsers());
	}
}

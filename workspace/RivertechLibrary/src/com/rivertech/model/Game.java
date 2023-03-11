package com.rivertech.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * Class that represent a game instance.
 */
public class Game {

	/***
	 * The game unique ID.
	 */
	private int gameId;

	/***
	 * The game name.
	 */
	private String gameName;

	/***
	 * The game provider name.
	 */
	private String provider;

	@JsonProperty("game_id")
	public int getGameId() {
		return gameId;
	}

	@JsonProperty("game_name")
	public String getGameName() {
		return gameName;
	}

	@JsonProperty("game_provider")
	public String getProvider() {
		return provider;
	}

	public Game(int gameId, String gameName, String provider) {
		this.gameId = gameId;
		this.gameName = gameName;
		this.provider = provider;
	}
}

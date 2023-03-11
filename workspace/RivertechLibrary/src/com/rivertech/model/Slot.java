package com.rivertech.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * Class to represent a tournament schedule slot. A slot can have two rounds
 * with a game and a group of players.
 * 
 * @author Omar Zammit
 *
 */
public class Slot {

	/* Slot start date */
	private LocalDateTime start;

	/* Slot end date */
	private LocalDateTime end;

	/* Games to be played in this slot */
	private Round[] rounds = new Round[2];

	@JsonProperty("games")
	public Round[] getRounds() {
		return rounds;
	}

	@JsonProperty("timeslot_start")
	public LocalDateTime getStart() {
		return start;
	}

	@JsonProperty("timeslot_end")
	public LocalDateTime getEnd() {
		return end;
	}

	@JsonIgnore
	public Round getOne() {
		return rounds[0];
	}

	@JsonIgnore
	public Round getTwo() {
		return rounds[1];
	}

	public void setOne(Round one) {
		this.rounds[0] = one;
	}

	public void setTwo(Round two) {
		this.rounds[1] = two;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "Slot: \n" + this.getStart() + " - " + this.getEnd() + "\n" + this.getOne() + "\n" + this.getTwo();
	}

}

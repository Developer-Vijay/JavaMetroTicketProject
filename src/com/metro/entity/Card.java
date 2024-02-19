package com.metro.entity;

import java.util.HashMap;
import java.util.Map;

public class Card {

	private int cardId;
	private int cardBalance;
	private Map<Integer, StationsTravelled> lastStations;
	static int count = 0;

	public Card() {
		super();
		count++;
		cardId = count;
		cardBalance = 100;
		lastStations=new HashMap<Integer, StationsTravelled>();
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getCardBalance() {
		return cardBalance;
	}

	public void setCardBalance(int cardBalance) {
		this.cardBalance = cardBalance;
	}

	@Override
	public String toString() {
		return "Card [cardId=" + cardId + ", cardBalance=" + cardBalance + "]";
	}

	public Map<Integer, StationsTravelled> getLastStations() {
		return lastStations;
	}

	public void setLastStations(Map<Integer, StationsTravelled> lastStations) {
		this.lastStations = lastStations;
	}

}

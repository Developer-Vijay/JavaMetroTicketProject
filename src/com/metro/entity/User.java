package com.metro.entity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.metro.exceptions.InsufficientBalanceException;

public class User {
	private static int count = 0;
	private int userId;
	private String userName;
	private double card_balance;
	private Card card;
	private List<StationsTravelled> stationsTravelled;

	public User() {
		this.userId = ++count;
		this.userName = "User" + userId; // Default username
		this.card = new Card();
		this.card_balance = 100;
		this.card.setCardId(userId); // Set card ID as user ID
		this.card.setCardBalance(100); // Initial balance
		this.card.setLastStations(new HashMap<>());
		setStationsTravelled(new LinkedList<StationsTravelled>());// Initialize last stations map
	}

	public User(int userId, String userName, Card card, double card_balance) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.card = card;
		this.card_balance = card_balance;

	}

	public double getCard_balance() {
		return card_balance;
	}

	public void setCard_balance(double card_balance) {
		this.card_balance = card_balance;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", card=" + card + "]";
	}

	public List<StationsTravelled> getStationsTravelled() {
		return stationsTravelled;
	}

	public void setStationsTravelled(List<StationsTravelled> stationsTravelled) {
		this.stationsTravelled = stationsTravelled;
	}


}

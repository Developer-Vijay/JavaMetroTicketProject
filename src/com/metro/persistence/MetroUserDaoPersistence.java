package com.metro.persistence;

import java.util.List;

import com.metro.entity.Card;
import com.metro.entity.Metro;
import com.metro.entity.StationsTravelled;
import com.metro.entity.User;
import com.metro.exceptions.DuplicateUserIdException;
import com.metro.exceptions.StationInvalidException;

public interface MetroUserDaoPersistence {

	public List<Metro> getAllMetroFare();

	public String getNextStation(String currentStation) throws StationInvalidException;

	public int getFareBetweenStations(String sourceStation, String destinationStation) throws StationInvalidException;
	
	public int addNewUser(User user) throws DuplicateUserIdException;
	
	public int createCard(Card card,int userId) throws DuplicateUserIdException;

	public User retrieveUserByUserIdAndStation(int userId, String station);
	public List<User> getAllUsers();
	
	public  User retrieveUserByUserIdOrCardId(int userId) throws DuplicateUserIdException;
	
	public List<StationsTravelled> getAllStations();
	
	
	public int calcuateTotalFare(String startStation , String endStation);
	
	public double updateUserBalance(int userId, double amountToDeduct);
}


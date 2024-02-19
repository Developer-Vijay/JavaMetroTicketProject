package com.metro.service;

import java.util.List;

import com.metro.entity.Bill;
import com.metro.entity.User;
import com.metro.exceptions.DuplicateUserIdException;
import com.metro.exceptions.InsufficientBalanceException;
import com.metro.exceptions.UserNotFoundException;

public interface MetroUserService {

	boolean createNewMetroUser(User user) throws DuplicateUserIdException;

	 User retrieveUserByIdOrCardId(int user_id) throws DuplicateUserIdException ;
	 
	 User retrieveUserByNameAndStation(int user_id, String station);
	// Method for handling user entry
	void userIn(User user, String station) throws InsufficientBalanceException;

	// Method for handling user exit
	Bill userOut(User user, String Startstation, String destinationStation);

	// Method for getting allusers

	List<User> getAllUser() throws UserNotFoundException;

}

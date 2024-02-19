package com.metro.service;

import com.metro.entity.User;

import com.metro.exceptions.DuplicateUserIdException;
import com.metro.exceptions.InsufficientBalanceException;
import com.metro.exceptions.UserNotFoundException;
import com.metro.persistence.MetroUserDaoPersistence;
import com.metro.persistence.MetroUserDaoPersistenceImp;


import java.util.LinkedList;
import java.util.List;

import com.metro.entity.Bill;
import com.metro.entity.Card;
import com.metro.entity.StationsTravelled;

public class MetroUserServiceImp implements MetroUserService {

	private MetroUserDaoPersistence metroUserDaoImp = new MetroUserDaoPersistenceImp();



	@Override
	public boolean createNewMetroUser(User user) throws DuplicateUserIdException {
		// create user
		boolean userCreated = createUser(user);

		// Create card
		boolean cardCreated = createCard(user.getCard(), user.getUserId());

		// Throw exception if either user or card creation fails
		if (!userCreated || !cardCreated) {
			throw new DuplicateUserIdException("User with ID " + user.getUserId() + " already exists");
		}

		return true;
	}

	private boolean createUser(User user) throws DuplicateUserIdException {
		try {
			return metroUserDaoImp.addNewUser(user) != 0;
			
		}
		catch (DuplicateUserIdException e)
		{
			throw new DuplicateUserIdException("Card already exits");
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean createCard(Card card, int userId) throws DuplicateUserIdException {
		try {
			
			return metroUserDaoImp.createCard(card, userId) != 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// User In Code
	@Override
	public void userIn(User user, String station) throws InsufficientBalanceException {


			if (user.getCard_balance() < 20) {
				throw new InsufficientBalanceException("Insufficient balance. Recharge your card and try again.");
			}

			List<StationsTravelled> list = new LinkedList<StationsTravelled>();
			list.add(new StationsTravelled(station, null, 0));
			user.setStationsTravelled(list);

	}

	@Override
	public Bill userOut(User user, String Startstation, String destinationStation) {
		int money = 0;
		double totalBalance = 0.0;
		try {
			money = metroUserDaoImp.calcuateTotalFare(Startstation, destinationStation);

			totalBalance = metroUserDaoImp.updateUserBalance(user.getUserId(), money);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Bill(totalBalance, money);
	}

	@Override
	public User retrieveUserByIdOrCardId(int user_id) throws DuplicateUserIdException {
		User user = metroUserDaoImp.retrieveUserByUserIdOrCardId(user_id);
		if (user == null) {
			throw new UserNotFoundException("User not found with user Id '" + user_id + "' or card id ");
		}

		return user;
	}
 
	@Override
	public User retrieveUserByNameAndStation(int user_id, String station) {
		User user = metroUserDaoImp.retrieveUserByUserIdAndStation(user_id, station);
		if (user == null) {
			throw new UserNotFoundException(
					"User not found with user Id '" + user_id + "' and station '" + station + "'");
		}
		return user;
	}

	@Override
	public List<User> getAllUser() throws UserNotFoundException {
		List<User> allUser = metroUserDaoImp.getAllUsers();
		if (allUser.size() == 0) {
			throw new UserNotFoundException("No Users are Avalible till now");
		}
		return allUser;
	}
}

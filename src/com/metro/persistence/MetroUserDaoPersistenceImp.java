package com.metro.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.metro.entity.Card;
import com.metro.entity.Metro;
import com.metro.entity.StationsTravelled;
import com.metro.entity.User;
import com.metro.exceptions.DuplicateUserIdException;
import com.metro.exceptions.StationInvalidException;

public class MetroUserDaoPersistenceImp implements MetroUserDaoPersistence {

	private Connection connection;

	// This function will return all the stations and fare between them
	@Override
	public List<Metro> getAllMetroFare() {

		List<Metro> metroListAndFare = new LinkedList<>();
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {

			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement preparedStatement;

			preparedStatement = connection.prepareStatement("SELECT * FROM FARE");

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String sourceName = resultSet.getString("source_station");
				String destName = resultSet.getString("destination_station");
				int fare = resultSet.getInt("fare");

				metroListAndFare.add(new Metro(sourceName, destName, fare));

			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return metroListAndFare;
	}

	// This function returns the station between source_station and
	// destination_station
	@Override
	public String getNextStation(String currentStation) throws StationInvalidException {
		String nextStation = null;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {

			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement preparedStatement;

			preparedStatement = connection
					.prepareStatement("SELECT destination_station FROM Fare WHERE source_station = ?");
			preparedStatement.setString(1, currentStation);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				nextStation = resultSet.getString("destination_station");
			} else {
				throw new StationInvalidException("'" + currentStation + "' station does not exists.");
			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return nextStation;
	}

	// This function returns fare between all the stations
	@Override
	public int getFareBetweenStations(String sourceStation, String destinationStation) throws StationInvalidException {
		int fare = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {

			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement preparedStatement;

			String query = "SELECT fare FROM Fare WHERE source_station = ? AND destination_station = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, sourceStation);
			statement.setString(2, destinationStation);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					fare = resultSet.getInt("fare");
				} else {
					throw new StationInvalidException("Invalid source or destination name.");
				}
			}
		}

		catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return fare;
	}

	// Creation of new user
	@Override
	public int addNewUser(User user) {

		int rows = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {

			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement preparedStatement;

			String query = "INSERT INTO USERS VALUES(?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, user.getUserId());
			statement.setString(2, user.getUserName());
			statement.setInt(3, user.getCard().getCardId());
			statement.setDouble(4, user.getCard_balance());

			rows = statement.executeUpdate();
		}

		catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			return rows;
		}

		return rows;
	}

	// creation of the card
	@Override
	public int createCard(Card card, int userId) throws DuplicateUserIdException {

		int rows = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {

			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement preparedStatement;

			String query = "INSERT INTO CARDS VALUES(?,?,?)";

			PreparedStatement statement = connection.prepareStatement(query);
			// card id
			statement.setInt(1, card.getCardId());
			// card balance
			statement.setInt(2, card.getCardBalance());
			// user id
			statement.setInt(3, userId);

			rows = statement.executeUpdate();
			if (rows == 0) {
				throw new DuplicateUserIdException("User with user ID: " + userId + " already exists.");
			}
		}

		catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return rows;
	}

	@Override
	public User retrieveUserByUserIdAndStation(int userId, String station) {
		User user = new User();
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {

			Class.forName("com.mysql.cj.jdbc.Driver");

			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT users.user_id, users.user_name, users.card_id, cards.card_balance, fare.source_station, fare.destination_station, fare.fare "
							+ "FROM users " + "JOIN cards ON users.card_id = cards.card_id "
							+ "JOIN fare ON fare.source_station = ?" + "WHERE users.user_id = ?;");

			preparedStatement.setInt(2, userId);
			preparedStatement.setString(1, station);
			Card card = new Card();
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int user_id = resultSet.getInt("user_id");
				String user_name = resultSet.getString("user_name");
				int card_id = resultSet.getInt("card_id");
				int cardBalance = resultSet.getInt("card_balance");

				card.setCardId(card_id);
				card.setCardBalance(cardBalance);
				user.setUserId(user_id);
				user.setUserName(user_name);
				user.setCard(card);
				user.setCard_balance(cardBalance);

			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return user;

	}

	// TODO: Need to integrate both Users and Card table into One
	@Override
	public List<User> getAllUsers() {
		List<User> userlist = new LinkedList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {

			Class.forName("com.mysql.cj.jdbc.Driver");

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USERS");

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Card card = new Card();
				int user_id = resultSet.getInt("User_id");
				String user_name = resultSet.getString("User_Name");
				int card_id = resultSet.getInt("CARD_ID");
				double card_balance = resultSet.getDouble("card_balance");
				card.setCardId(card_id);
				card.setCardBalance((int) card_balance);
				userlist.add(new User(user_id, user_name, card, card_balance));

			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return userlist;
	}

	@Override
	public User retrieveUserByUserIdOrCardId(int userId) throws DuplicateUserIdException {
		User user = new User();
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {

			Class.forName("com.mysql.cj.jdbc.Driver");

			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT * from users Where user_id=? or card_id=?");

			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, userId);
			Card card = new Card();
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int user_id = resultSet.getInt("user_id");
				String user_name = resultSet.getString("user_name");
				int card_id = resultSet.getInt("card_id");
				int cardBalance = resultSet.getInt("card_balance");

				card.setCardId(card_id);
				card.setCardBalance(cardBalance);
				user.setUserId(user_id);
				user.setUserName(user_name);
				user.setCard(card);
				user.setCard_balance(cardBalance);

			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return user;

	}

	@Override
	public List<StationsTravelled> getAllStations() {

		List<StationsTravelled> stationslist = new LinkedList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {

			Class.forName("com.mysql.cj.jdbc.Driver");

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM fare");

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String sourceStation = resultSet.getString("source_station");
				String destinationStation = resultSet.getString("destination_station");
				int fare = resultSet.getInt("fare");

				stationslist.add(new StationsTravelled(sourceStation, destinationStation, fare));

			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return stationslist;

	}

	@Override
	public int calcuateTotalFare(String startStation, String endStation) {

		int totalFare = 0;

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Get all possible fares
			String query = "SELECT * FROM fare WHERE (source_station = ? AND destination_station = ?) OR (source_station = ? AND destination_station = ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, startStation);
			preparedStatement.setString(2, endStation);
			preparedStatement.setString(3, endStation);
			preparedStatement.setString(4, startStation);
			ResultSet rs = preparedStatement.executeQuery();

			// Check if there is a direct connection between startStation and endStation
			boolean directConnectionFound = false;

			while (rs.next()) {
				totalFare += rs.getInt("fare");
				directConnectionFound = true;
			}

			// If no direct connection, get the fares between all stations and sum them up
			if (!directConnectionFound) {
				String allFaresQuery = "SELECT * FROM fare";
				PreparedStatement allFaresStatement = connection.prepareStatement(allFaresQuery);
				ResultSet allFaresResultSet = allFaresStatement.executeQuery();

				while (allFaresResultSet.next()) {
					String source = allFaresResultSet.getString("source_station");
					String destination = allFaresResultSet.getString("destination_station");

					if (source.equals(startStation) || destination.equals(startStation) || source.equals(endStation)
							|| destination.equals(endStation)) {
						totalFare += allFaresResultSet.getInt("fare");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return totalFare;

	}

	public double updateUserBalance(int userId, double amountToDeduct) {
		double updatedBalance = 0;

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metros", "root",
				"root");) {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String selectQuery = "SELECT card_balance FROM users WHERE user_id = ?";
			PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
			selectStatement.setInt(1, userId);
			ResultSet rs = selectStatement.executeQuery();

			if (rs.next()) {
				double currentBalance = rs.getDouble("card_balance");
				updatedBalance = currentBalance - amountToDeduct;

				String updateQuery = "UPDATE users SET card_balance = ? WHERE user_id = ?";
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
				updateStatement.setDouble(1, updatedBalance);
				updateStatement.setInt(2, userId);
				updateStatement.executeUpdate();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return updatedBalance;
	}

}

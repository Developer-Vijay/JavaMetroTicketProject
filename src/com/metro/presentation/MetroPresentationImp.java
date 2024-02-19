package com.metro.presentation;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.metro.entity.Bill;
import com.metro.entity.Card;
import com.metro.entity.StationsTravelled;
import com.metro.entity.User;
import com.metro.exceptions.DuplicateUserIdException;
import com.metro.exceptions.InsufficientBalanceException;
import com.metro.exceptions.UserNotFoundException;
import com.metro.service.MetroUserServiceImp;

public class MetroPresentationImp implements MetroPresentation {

	private MetroUserServiceImp metroUserServiceImp = new MetroUserServiceImp();
	private User user;

	@Override
	public void showMenu() {
		System.out.println("==============================");
		System.out.println("Metro Fare Management System");
		System.out.println("==============================");
		System.out.println("1. Create New User");
		System.out.println("2. Swipe In");
		System.out.println("3. Swipe Out");
		System.out.println("4. Get All User");
		System.out.println("5. Exit");
	}

	@Override
	public void performMenu(int choice) {

		Scanner scanner = new Scanner(System.in);

		switch (choice) {

		case 1: {

			try {
				User user = new User();
				Card card = new Card();

				System.out.println("Enter user Id: ");
				user.setUserId(Integer.parseInt(scanner.next()));
				System.out.println("Enter user name: ");
				user.setUserName(scanner.next());
				System.out.println("Enter cardId: ");
				card.setCardId(Integer.parseInt(scanner.next()));
				card.setCardBalance(100);
				user.setCard_balance(100);
				user.setCard(card);

				if (metroUserServiceImp.createNewMetroUser(user)) {
					System.out.println("New User Created with userId and cardId " + user.getUserId() + " "
							+ card.getCardId() + "Added!");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number.");
			} catch (DuplicateUserIdException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Something went wrong please try again.");
			}
			break;
		}
		case 2: {

			System.out.println("Enter user Id:");
			int userId = scanner.nextInt();

			System.out.println("Enter entry station From L1,L2,L3");
			String station = scanner.next();

			try {

				 user = metroUserServiceImp.retrieveUserByIdOrCardId(userId);

				if (user != null) {

					metroUserServiceImp.userIn(user, station);

					System.out.println("-------------------------------------------------------");

					System.out.println("Swipe in successful!");
				} else {
					System.out.println("User not found with the provided name and station.");
				}
			}
			catch (InsufficientBalanceException e)
			{
				System.out.println(e.getMessage());
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number.");
			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("An error occurred. Please try again later.");
			}
			break;
		}

		case 3: {

//			System.out.println("Enter user Id: ");
//			int userId = scanner.nextInt();

//			System.out.println("Enter Source Station also");
//			String sourceStation = scanner.next();

			System.out.println("Enter exit station: ");
			String destinationstation = scanner.next();

			try {

//				System.out.println("----------------------- " + user.getStationsTravelled().get(0));
				
//		User user		= metroUserServiceImp.retrieveUserByIdOrCardId(userId);
				// ----------------------------------------------------------
//				System.out.println("---------------------source-- " + source);

				// Check if the user is not null before calling userOut
				if (user != null) {
					StationsTravelled firstStationsTravelled = user.getStationsTravelled() .get(0);
					String source = firstStationsTravelled.getSource();
					Bill bill = metroUserServiceImp.userOut(user, source, destinationstation);
                    if(bill.getAmountDeducted() !=0)
					System.out.println("Successfully Swipe Out ! Amount Deducted = " + bill.getAmountDeducted()
							+ " remaining balance " + bill.getBalance());
                    else
                    	System.out.println("Something went wrong please try again");
				}
				
				else {
					throw new UserNotFoundException("First SwipeIn");
				}
				
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number.");
			} catch (UserNotFoundException e) {

				System.out.println("First SwipeIn to Swipe Out");
			} catch (Exception e) {

				System.out.println("An error occurred. Please try again later.");
			}
			break;
		}
		case 4: {
			try {
				List<User> allUser = metroUserServiceImp.getAllUser();

				for (User user : allUser) {
					System.out.println(user);

				}

			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number.");
			} catch (UserNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case 5: {
			System.out.println("Thanks for using the Metro Fare Management System.");
			System.exit(0);

			break;
		}
		default:
			try {
				throw new InputMismatchException("Invalid choice. Please enter a valid option."); // Handle
																									// InputMismatchException
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
			}
			break;
		}
	}

}
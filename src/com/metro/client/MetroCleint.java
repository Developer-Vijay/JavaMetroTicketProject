package com.metro.client;

import java.util.Scanner;

import com.metro.presentation.MetroPresentationImp;

public class MetroCleint {

	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		MetroPresentationImp metroPresentationImp = new MetroPresentationImp();
		while (true) {
			metroPresentationImp.showMenu();
			System.out.println("Enter Choice : ");
			int choice = scanner.nextInt();
			metroPresentationImp.performMenu(choice);
		}

	}
}

package de.summer_projects.webcrawler.application;

import java.util.NoSuchElementException;
import java.util.Scanner;

import de.summer_projects.webcrawler.modules.ImageParser;
import de.summer_projects.webcrawler.modules.SiteLoader;

public class CrawlyApplication {
	public static void main(String[] args) {
		initCrawly();
	}

	public static void initCrawly() {
		System.out.println("Hi! Welcome to Crawly v0.1 beta!\n");
		System.out.println("As of now, Crawly offers 2 functions, please enter your choice below\n");
		Scanner scanner = new Scanner(System.in);
		System.out.println("[1] CrawlyLoader");
		System.out.println("[2] CrawlyImageParser");
		System.out.print("\nEnter choice: ");
		String userChoice = "";
		int choice = 0;
		try {
			userChoice = scanner.nextLine();
		} catch (NoSuchElementException e) {
			System.err.println("No element found");
		}
		if (userChoice.length() == 0) {
			System.err.println("No choice was registered");
		}
		try {
			choice = Integer.parseInt(userChoice);
		} catch (NumberFormatException e) {
			System.err.println("No parseable integer was found. Please enter a valid choice (number only)");
		}

		callModule(choice);
	}
	
	public static void initCrawlyOuter() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("[1] CrawlyLoader");
		System.out.println("[2] CrawlyImageParser");
		System.out.print("\nEnter choice: ");
		String userChoice = "";
		int choice = 0;
		try {
			userChoice = scanner.nextLine();
		} catch (NoSuchElementException e) {
			System.err.println("No element found");
		}
		if (userChoice.length() == 0) {
			System.err.println("No choice was registered");
		}
		try {
			choice = Integer.parseInt(userChoice);
		} catch (NumberFormatException e) {
			System.err.println("No parseable integer was found. Please enter a valid choice (number only)");
		}

		callModule(choice);
	}

	private static void callModule(int choice) {
		if (choice == 1) {
			SiteLoader.callCrawly_SiteLoader();
		} else if (choice == 2) {
			ImageParser.callCrawly_ImageParser();
		} else {
			System.err.println("Choice could not be called for unknown reasons.");
		}
	}
}

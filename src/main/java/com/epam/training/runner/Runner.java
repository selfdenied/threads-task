package com.epam.training.runner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.epam.training.constant.Constants;
import com.epam.training.fastfood.Restaurant;
import com.epam.training.thread.Customer;

public class Runner {
	/* getting the logger reference */
	private static final Logger LOG = Logger.getLogger(Runner.class);

	/* initializing the logger configuration */
	static {
		new DOMConfigurator().doConfigure(Constants.LOGGER_CONFIG_FILE_PATH,
				LogManager.getLoggerRepository());
	}

	public static void main(String[] args) {
		LOG.info("Initializing the restaurant...");
		Restaurant restaurant = new Restaurant(Constants.NUMBER_OF_DESKS);
		System.out.println("Number of desks at the restaurant: " + restaurant.getNumberOFDesks());
		System.out.println("Number of customers at the restaurant: " + Constants.NUMBER_OF_CUSTOMERS + "\n");

		/* starting the Threads (allowing the Customers to enter the restaurant) */
		for (int i = 1; i <= Constants.NUMBER_OF_CUSTOMERS; i++) {
			new Customer(i, restaurant).start();
		}
	}
}

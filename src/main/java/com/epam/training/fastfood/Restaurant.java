package com.epam.training.fastfood;

import java.util.ArrayList;

/* Restaurant class contains a collection of CashDesk objects */
public class Restaurant {
	private final ArrayList<CashDesk> desks;
	private int numberOfDesks; // number of desks in the restaurant

	public Restaurant(int numberOfDesks) {
		this.numberOfDesks = numberOfDesks;
		this.desks = new ArrayList<CashDesk>(numberOfDesks);
		initializeRestaurant();
	}

	/* returns the number of desks in the restaurant */
	public int getNumberOFDesks() {
		return numberOfDesks;
	}

	public ArrayList<CashDesk> getDesks() {
		return desks;
	}

	/* method is used by Customer objects (Threads) */
	/* Customers choose the desk by calling this method */
	public CashDesk useCashDesk(int i) {
		/* increasing the number of Customers at this desk by one */
		desks.get(i).getNumberOfCustomers().getAndIncrement();
		return desks.get(i);
	}

	/* initializing the collection of cash desks */
	private void initializeRestaurant() {
		for (int i = 0; i < numberOfDesks; i++) {
			desks.add(new CashDesk(i + 1));
		}
	}
}

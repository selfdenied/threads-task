package com.epam.training.fastfood;

import java.util.ArrayList;

public class Restaurant {
	private final ArrayList<CashDesk> desks;
	private int numberOfDesks;

	public Restaurant(int numberOfDesks) {
		this.numberOfDesks = numberOfDesks;
		this.desks = new ArrayList<CashDesk>(numberOfDesks);
		initializeRestaurant();
	}

	public int getNumberOFDesks() {
		return numberOfDesks;
	}
	
	public ArrayList<CashDesk> getDesks() {
		return desks;
	}

	public CashDesk useCashDesk(int i) {
		/* increasing the number of customers at this desk by one */
		desks.get(i).getNumberOfCustomers().getAndIncrement();
		return desks.get(i);
	}

	private void initializeRestaurant() {
		for (int i = 0; i < numberOfDesks; i++) {
			desks.add(new CashDesk(i + 1));
		}
	}
}

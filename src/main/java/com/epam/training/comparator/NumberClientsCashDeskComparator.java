package com.epam.training.comparator;

import java.util.Comparator;

import com.epam.training.fastfood.CashDesk;

/* This class is needed to find the cash desk with a minimum number of customers */
public class NumberClientsCashDeskComparator implements Comparator<CashDesk> {

	/*
	 * sorts the collection of CashDesk objects according to the number of
	 * customers staying in the queue to the given cash desk
	 */
	public int compare(CashDesk desk1, CashDesk desk2) {
		return desk1.getNumberOfCustomers().intValue()
				- desk2.getNumberOfCustomers().intValue();
	}
}

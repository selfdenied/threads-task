package com.epam.training.comparator;

import java.util.Comparator;

import com.epam.training.fastfood.CashDesk;

public class NumberClientsCashDeskComparator implements Comparator<CashDesk> {

	public int compare(CashDesk desk1, CashDesk desk2) {
		return desk1.getNumberOfCustomers().intValue()
				- desk2.getNumberOfCustomers().intValue();
	}
}

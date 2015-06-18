package com.epam.training.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.epam.training.comparator.NumberClientsCashDeskComparator;
import com.epam.training.constant.Constants;
import com.epam.training.fastfood.CashDesk;
import com.epam.training.fastfood.Restaurant;

public class Customer extends Thread {
	/* getting the logger reference */
	private static final Logger LOG = Logger.getLogger(Customer.class);
	private long id;
	private Restaurant restaurant;

	public Customer(long id, Restaurant restaurant) {
		this.id = id;
		this.restaurant = restaurant;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void run() {
		CashDesk desk;

		try {
			/* entering the restaurant and choosing the queue randomly */
			TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50));
			desk = restaurant.useCashDesk(new Random()
					.nextInt(Constants.NUMBER_OF_DESKS));
			System.out.println("Customer #" + id + " has chosen Desk #"
					+ desk.getId());
			/* staying in a queue to the cash desk */
			stayingInQueue(desk);
		} catch (InterruptedException exception) {
			LOG.error("Error. A thread was interrupted!");
		}
	}

	private void stayingInQueue(CashDesk desk) throws InterruptedException {
		if (desk.isDeskFree()) {
			/* using the desk to order food */
			desk.serviceCustomer(this);
			/* leaving the desk with the ordered food */
			System.out.println("Customer #" + id + ": leaving Desk #"
					+ desk.getId());
		} else {
			int nextDeskId = properDeskId();
			/* waiting in the queue and looking for a queue with fewer people */
			TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50));

			/*
			 * moving to the queue with fewer people in case when you are in a
			 * different queue (if a thread is already in the queue with fewer
			 * people, he chooses to stay there)
			 */
			if (desk.getId() != nextDeskId) {
				CashDesk nextDesk = restaurant.useCashDesk(nextDeskId - 1);
				System.out.println("Customer #" + id + " moved to Desk #"
						+ nextDesk.getId());
				desk.getNumberOfCustomers().getAndDecrement();
				desk = nextDesk;
			}
			stayingInQueue(desk);
		}
	}

	private int properDeskId() {
		ArrayList<CashDesk> sortedList = new ArrayList<CashDesk>();
		sortedList.addAll(restaurant.getDesks());
		return Collections.min(sortedList,
				new NumberClientsCashDeskComparator()).getId();
	}
}

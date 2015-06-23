package com.epam.training.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.epam.training.comparator.NumberClientsCashDeskComparator;
import static com.epam.training.constant.Constants.NUMBER_OF_DESKS;
import com.epam.training.fastfood.CashDesk;
import com.epam.training.fastfood.Restaurant;

/* Customer class (Thread) stays in a queue to the chosen Cash desk */
public class Customer extends Thread {
	/* getting the logger reference */
	private static final Logger LOG = Logger.getLogger(Customer.class);
	private long id;
	private Restaurant restaurant; // the restaurant they decided to visit

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

	/* the main method invoked after calling java.lang.Thread.start() method */
	public void run() {
		CashDesk desk;

		try {
			/* entering the restaurant */
			TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50));
			/* choosing a queue randomly */
			desk = restaurant.useCashDesk(new Random().nextInt(NUMBER_OF_DESKS));
			System.out.println("Customer #" + id + " has chosen Desk #"
					+ desk.getId());
			/* staying in a queue to the chosen cash desk */
			stayingInQueue(desk);
		} catch (InterruptedException exception) {
			LOG.error("Error. A thread was interrupted!");
		}
	}

	/* invoked by the Customer who has chosen a queue to the cash desk */
	private void stayingInQueue(CashDesk desk) throws InterruptedException {
		/* if desk is free, go and order some food */
		if (desk.isDeskFree()) {
			occupyDesk(desk);
		} else {
			/* waiting in the queue for some time */
			TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50));
			/* counting people in queues and finding the one with fewer people */
			int nextDeskId = properDeskId();

			/*
			 * moving to the queue with fewer people (if the Customer is already
			 * in the queue with minimum people, he stays there)
			 */
			desk = moveToNextDesk(desk, nextDeskId);

			/*
			 * the Customer repeats the procedure, i.e. occupies the desk if
			 * it's free or stays in the chosen queue for some time and then
			 * looks for a queue with fewer people (some desks may be vacant at
			 * all)
			 */
			stayingInQueue(desk);
		}
	}

	/*
	 * supplementary method that returns the position of the desk with a minimum
	 * Customers (Threads) trying to occupy it
	 */
	private int properDeskId() {
		ArrayList<CashDesk> deskList = new ArrayList<CashDesk>();
		deskList.addAll(restaurant.getDesks());
		return Collections.min(deskList,
				new NumberClientsCashDeskComparator()).getId();
	}

	/* supplementary method (a Customer occupies the cash desk) */
	private void occupyDesk(CashDesk desk) {
		/* using the desk to order food */
		desk.serviceCustomer(this);
		/* leaving the desk with the ordered food */
		System.out.println("Customer #" + id + ": leaving Desk #"
				+ desk.getId());
	}
	
	/* supplementary method (a Customer moves to the desk with fewer people) */
	private CashDesk moveToNextDesk(CashDesk desk, int nextDeskId) {
		if (desk.getId() != nextDeskId) {
			/* moving to the desk with fewer people in the queue */
			CashDesk nextDesk = restaurant.useCashDesk(nextDeskId - 1);
			System.out.println("Customer #" + id + ": moving to Desk #"
					+ nextDesk.getId());
			/* decreasing the number of people in the queue that was left */
			desk.getNumberOfCustomers().getAndDecrement();
			desk = nextDesk;
		}
		return desk;
	}
}

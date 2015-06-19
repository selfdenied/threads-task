package com.epam.training.fastfood;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.epam.training.thread.Customer;

/* CashDesk class provides services to Customers  */
public class CashDesk {
	/* getting the logger reference */
	private static final Logger LOG = Logger.getLogger(CashDesk.class);
	private int id;
	private AtomicInteger moneyObtained = new AtomicInteger();
	private AtomicInteger numberOfCustomers = new AtomicInteger();
	private Lock lock1 = new ReentrantLock();
	private Lock lock2 = new ReentrantLock();

	public CashDesk(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/* returns the amount of money obtained by the cash desk */
	public AtomicInteger getMoneyObtained() {
		return moneyObtained;
	}

	/* returns the number of Customers staying in a queue to the desk */
	public AtomicInteger getNumberOfCustomers() {
		return numberOfCustomers;
	}

	/* serving the Customer (one at a time) */
	public void serviceCustomer(Customer customer) {
		try {
			lock1.lock();
			/* accepting the Customer's order */
			acceptOrder(customer);
			/* taking money from the Customer */
			takeMoney(1 + new Random().nextInt(99), customer);
			/* waiting for the order to be delivered */
			TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50));
			/* delivering order */
			deliverOrder(customer);
			/* calculating the current amount of money at this desk */
			System.out.println("Total amount of money at Desk #" + id + " is "
					+ moneyObtained + " USD");
			/* decreasing the number of Customers at this desk by one */
			numberOfCustomers.getAndDecrement();
		} catch (InterruptedException exception) {
			LOG.error("Error. A thread was interrupted!");
		} finally {
			lock1.unlock();
			lock2.unlock();
		}
	}

	/* checks whether the current desk is free */
	public boolean isDeskFree() {
		boolean isFree = lock2.tryLock();
		return isFree;
	}

	/* accepts the Customer's order of food */
	private void acceptOrder(Customer customer) {
		System.out.println("Desk #" + id + ": accepting order from Customer #"
				+ customer.getId());
	}

	/* takes money from the Customer */
	private void takeMoney(int amount, Customer customer) {
		System.out.println("Desk #" + id + ": taking " + amount
				+ " USD from Customer #" + customer.getId());
		moneyObtained.addAndGet(amount);
	}

	/* delivers the ordered food */
	private void deliverOrder(Customer customer) {
		System.out.println("Desk #" + id + ": delivering order to Customer #"
				+ customer.getId());
	}
}

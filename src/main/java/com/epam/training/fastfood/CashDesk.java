package com.epam.training.fastfood;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.epam.training.thread.Customer;

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

	public AtomicInteger getMoneyObtained() {
		return moneyObtained;
	}

	public AtomicInteger getNumberOfCustomers() {
		return numberOfCustomers;
	}

	public void serviceCustomer(Customer customer) {
		try {
			lock1.lock();
			/* accepting the customer's order */
			acceptOrder(customer);
			/* taking money from the customer */
			takeMoney(1 + new Random().nextInt(99), customer);
			/* waiting for the order to be delivered */
			TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50));
			deliverOrder(customer);
			/* calculating the current amount of money at this desk */
			System.out.println("Total amount of money in Desk #" + id + " is "
					+ moneyObtained + " USD");
			/* decreasing the number of customers at this desk by one */
			numberOfCustomers.getAndDecrement();
		} catch (InterruptedException exception) {
			LOG.error("Error. A thread was interrupted!");
		} finally {
			lock1.unlock();
			lock2.unlock();
		}
	}

	public boolean isDeskFree() {
		boolean isDeskFree = lock2.tryLock();
		return isDeskFree;
	}

	private void acceptOrder(Customer customer) {
		System.out.println("Desk #" + id + ": accepting order from Customer #"
				+ customer.getId());
	}

	private void takeMoney(int amount, Customer customer) {
		System.out.println("Desk #" + id + ": taking " + amount
				+ " USD from Customer #" + customer.getId());
		moneyObtained.addAndGet(amount);
	}

	private void deliverOrder(Customer customer) {
		System.out.println("Desk #" + id + ": delivering order to Customer #"
				+ customer.getId());
	}
}

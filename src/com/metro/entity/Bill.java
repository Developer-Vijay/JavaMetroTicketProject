package com.metro.entity;

public class Bill {

	private double balance;
	private int amountDeducted;

	public Bill(double balance, int amountDeducted) {
		super();
		this.balance = balance;
		this.amountDeducted = amountDeducted;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getAmountDeducted() {
		return amountDeducted;
	}

	public void setAmountDeducted(int amountDeducted) {
		this.amountDeducted = amountDeducted;
	}

	@Override
	public String toString() {
		return "Bill [balance=" + balance + ", amountDeducted=" + amountDeducted + "]";
	}

}

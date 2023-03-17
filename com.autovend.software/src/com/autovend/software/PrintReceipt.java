package com.autovend.software;

import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.Bill;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;

public class PrintReceipt {
	public PrintReceipt(Bill bill) throws OverloadException, EmptyException {
		// To-do
		// Print the details of the receipt
		System.out.print(bill.getValue());
		int amount = bill.getValue();
		
		// Update bills
		bill = new Bill(amount, Currency.getInstance("USD"));
		ReceiptPrinter receivePrinter = new ReceiptPrinter();
		receivePrinter.print('c');
	}
}

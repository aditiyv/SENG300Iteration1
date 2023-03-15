package com.autovend.software;

import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.Bill;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.ElectronicScale;

public abstract class Pay {
	
	ElectronicScale es = new ElectronicScale(2, 0);
	BarcodeScanner bs = new BarcodeScanner();
	Cart cart = new Cart(es, bs);
	Bill bill = new Bill(1000, Currency.getInstance("USD"));
	public abstract void pay(); 
	
	BigDecimal paymentDue = cart.getPrice();
	int paymentMade = bill.getValue();
	
	int comparison = paymentDue.compareTo(BigDecimal.valueOf(paymentMade));
	public void printReceipt() {
		if (comparison == 0) {
			paymentDue = BigDecimal.valueOf(0);
		} else if (comparison < 0) {
			//Payment haven't been made, amount due remains the same
		} else if (comparison > 0) {
			//Payment have been made
			paymentDue = BigDecimal.valueOf(0);
		}
	}
	
}
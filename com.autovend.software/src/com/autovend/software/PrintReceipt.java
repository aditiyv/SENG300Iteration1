package com.autovend.software;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;

import com.autovend.Bill;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.products.Product;

public class PrintReceipt {
	public PrintReceipt(ReceiptPrinter receiptPrinter, Cart cart) throws OverloadException, EmptyException {
		// To-do
		// Get items, get price, then print it
		
		// Print the bill record
		// Key of hashmap is product code
		
		HashMap<Product,Integer> map = cart.getCart();
		
		
		for (Product key : map.keySet()) {
			Integer quantity = map.get(key);
			String quantityAmount = quantity.toString();
			BigDecimal price = key.getPrice();
			String moneyAmount = price.toString();
			String s = key.toString();
			// Print char from string (receiptP);
			for (int i = 0; i < s.length(); i++) {
				receiptPrinter.print(s.charAt(i));
				receiptPrinter.print(quantityAmount.charAt(i));
				receiptPrinter.print(moneyAmount.charAt(i));
			}
			// Make observer react ink;
			
		}
			
		
		// Print the receipt
		
		
		// Signals to Customer I/O that the customer’s session is complete
		
		
		// Thanks the Customer
		String thanks = "Thanks the Customer";
		for (int i = 0; i < thanks.length(); i++) {
			receiptPrinter.print(thanks.charAt(i));
		}
		
		
		// Ready for a new customer session
		String ready = "Ready for the Customer";
		for (int i = 0; i < ready.length(); i++) {
			receiptPrinter.print(ready.charAt(i));
		}
		
	}
}

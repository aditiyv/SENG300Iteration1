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
		
		
		// Print the receipt
		for (Product key : map.keySet()) {
			Integer quantity = map.get(key);
			String quantityAmount = quantity.toString();
			BigDecimal price = key.getPrice();
			String moneyAmount = price.toString();
			String s = key.toString();
			// Print char from string (receiptP);
			for (int i = 0; i < s.length(); i++) {
				try {
				receiptPrinter.print(s.charAt(i));
				receiptPrinter.print(quantityAmount.charAt(i));
				receiptPrinter.print(moneyAmount.charAt(i));
				} catch (Exception e) { 
					if (e.getMessage().equals("Out of paper") || e.getMessage().equals("Out of ink")) {
			            // If the printer is out of paper or ink, abort the printing and suspend the station
			            receiptPrinter.disable();
			            // Inform the attendant that a duplicate receipt must be printed and that the station needs maintenance
			            receiptPrinter.notify();
			            break; // exit the loop
			        } else {
			            throw e; // re-throw the exception if it is not related to printer status
			        }
				}
			}
			// Make observer react ink;
			
		}
		
		// Signals to Customer I/O that the customer’s session is complete
		
		
		// Thanks the Customer
		String thanks = "Thanks the Customer";
		for (int i = 0; i < thanks.length(); i++) {
			receiptPrinter.print(thanks.charAt(i));
		}
		
		
		// Ready for a new customer session
		
		
	}
}

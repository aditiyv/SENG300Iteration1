package com.autovend.software;

import java.math.BigDecimal;
import java.util.HashMap;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.Product;

public class PrintReceipt implements ReceiptPrinterObserver {
	
	public PrintReceipt(ReceiptPrinter receiptPrinter, Cart cart) throws OverloadException, EmptyException {
		// To-do
		// Get items, get price, then print it
		
		// Print the bill record
		// Key of hashMap is product code
		
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
					if (e.getMessage().equals("Out of paper")) {
			            // If the printer is out of paper or ink, abort the printing and suspend the station
			            receiptPrinter.disable();
			            // Inform the attendant that a duplicate receipt must be printed and that the station needs maintenance
			            reactToOutOfPaperEvent(receiptPrinter);
			            break; // exit the loop
			        } else if (e.getMessage().equals("Out of ink")) {
			        	receiptPrinter.disable();
			            reactToOutOfInkEvent(receiptPrinter);
			        } else {
			            throw e; // re-throw the exception if it is not related to printer status
			        }
				}
			}
		}
	}
	
	// Make a method that return true
	// Signals to Customer I/O that the customer’s session is complete
	public boolean sessionComplete() {
		return true;
	}
	
	// Thanks the Customer
	public String thanksCustomer(ReceiptPrinter receiptPrinter) {
		return "Thanks the Customer";
	}
	
		
	// Ready for a new customer session
	

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}
}

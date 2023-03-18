package com.autovend.software;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;

import com.autovend.Bill;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.Product;

public class PrintReceipt implements ReceiptPrinterObserver {
	boolean outOfInk = false;
	public PrintReceipt(ReceiptPrinter receiptPrinter, Cart cart) throws OverloadException, EmptyException {
		// To-do
		// Get items, get price, then print it
		
		// Print the bill record
		// Key of hashmap is product code
		receiptPrinter.register(this);
		
		
		HashMap<Product,Integer> map = cart.getCart();
		
		// Print the receipt
		for (Product key : map.keySet()) {
			Integer quantity = map.get(key);
			String quantityAmount = quantity.toString();
			BigDecimal price = key.getPrice();
			String moneyAmount = price.toString();
			String s = key.toString();
			String result = s+"\t" + quantityAmount + "\t" + moneyAmount +"\n";
			// Print char from string (receiptP);
			for (int i = 0; i < result.length(); i++) {
				if(outOfInk) {
					throw new EmptyException();
				}
				try {
				receiptPrinter.print(result.charAt(i));
				} catch (OverloadException e) { 
					result = "\n"+result.substring(i);
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
		outOfInk = true;
		
	}

	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		outOfInk = false;
		// TODO Auto-generated method stub
		
	}
}

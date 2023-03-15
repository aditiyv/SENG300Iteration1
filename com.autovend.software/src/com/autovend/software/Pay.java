package com.autovend.software;

import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.ElectronicScale;

public abstract class Pay {
	public void printReceipt() {
		
	}
	ElectronicScale es = new ElectronicScale(2, 0);
	BarcodeScanner bs = new BarcodeScanner();
	Cart cart = new Cart(es, bs);
	public abstract void pay(); 
	
	
}
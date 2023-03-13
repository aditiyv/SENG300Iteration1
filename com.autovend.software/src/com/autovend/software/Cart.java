package com.autovend.software;

import java.util.ArrayList;

import com.autovend.SellableUnit;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.DisabledException;
import com.autovend.devices.SimulationException;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public class Cart {
	public Cart () {
	}
	//will store all the items in one transaction
	ArrayList<SellableUnit> cartOfItems;
	//adds item by scan
	public boolean AddByScan (SellableUnit bp, BarcodeScanner bs) {
		try {
		if(bs.scan(bp)){
			cartOfItems.add(bp);
			return true;
		}
		}
		catch(DisabledException e) {
			return false;
		}
		catch(SimulationException s) {
			return false;
		}
		return false;
	}
	//returns the cart
	public ArrayList<SellableUnit> getCart() {
		return cartOfItems;
	}
	
}

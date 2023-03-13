package com.autovend.software;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.DisabledException;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SimulationException;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.*;

public class Cart {
	public Cart () {
		es = new ElectronicScale(5000,50);
	}
	//will store all the items in one transaction
	ArrayList<Product> cartOfItems;
	//Total price 
	public BigDecimal total;
	private ElectronicScale es;
	//adds item by scan
	public boolean AddByScan (BarcodedProduct bp, BarcodeScanner bs) {
		BarcodedUnit unit = new BarcodedUnit(bp.getBarcode(),bp.getExpectedWeight());
		try {
		if(bs.scan(unit)){
			es.add(unit);
			cartOfItems.add(bp);
			total = total.add(bp.getPrice());
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
	public ArrayList<Product> getCart() {
		return cartOfItems;
	}
	public BigDecimal getPrice() {
		return total;
	}
	
}

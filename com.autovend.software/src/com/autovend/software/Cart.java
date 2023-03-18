// Name: Nam Nguyen Vu (UCID: 30154892)

package com.autovend.software;

import java.math.BigDecimal;

// TEST COMMIT


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.DisabledException;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.*;
import com.autovend.external.ProductDatabases;

public class Cart {
	public Cart (ElectronicScale elec, BarcodeScanner barScan) {
		total = BigDecimal.ZERO;
		es = elec;
		bs = barScan;
		bsos = new Scan();
		wc  = new WeightChecker();
		es.register(wc);
		bs.register(bsos);
	}
	//will store all the items in one transaction
	ArrayList<Product> cartOfItems;
	//Total price 
	public BigDecimal total;
	private ElectronicScale es;
	private BarcodeScanner bs; 
	private Scan bsos;
	private WeightChecker wc;
	private double currentWeight = 0;
	//adds item by scan
	//I think for this approach we might need a class that reacts to events so that 
	public boolean addByScan (SellableUnit bp) throws WeightDiscrepancyException, OverloadException {
		try {
		currentWeight = es.getCurrentWeight();
		if(bs.scan(bp)){
			bs.disable();
			es.add(bp);
			double newWeight = es.getCurrentWeight();
			if(currentWeight + bp.getWeight() !=newWeight) {
				throw new WeightDiscrepancyException();
			}
			//Ideally we would wait here for a couple seconds to see if the user has placed the items
			if(wc.isOverWeight()) {
				throw new OverloadException();
			}
			bs.enable();
			return true;
		}
		}
		catch(DisabledException e) {
			return false;
		}
		catch(SimulationException s) {
			System.out.println(s.getMessage());
			return false;
		}
		return false;
	}
	//returns the cart
	public HashMap<Product,Integer> getCart() {
		return bsos.returnCart();
	}
	
	public BigDecimal getPrice() {
		return bsos.returnTotal();
	}
	
	public double getCurrentWeight() {
		return currentWeight;
	}
	
}


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
	/**
	 * 
	 * @param elec is the electronic scale that will be used to weigh items
	 * @param barScan is the brocade scanner that will be used to scan items
	 * Will also register listeners to each to a copy of bar code scanner and electronic scanner
	 */
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
	//copy of barcode scanner and electronic scale
	private ElectronicScale es;
	private BarcodeScanner bs; 
	//listeners for each
	private Scan bsos;
	private WeightChecker wc;
	private double currentWeight = 0;
	private double expectedWeight =0;
	//adds item by scan
	/**
	 * 
	 * @param bp the sellable unit to be scanned
	 * If the bar code scans an item then it will disable the bar code scanner and then add it to the scale and finally re enables the scanner
	 * If the weight is not expected then throw an exception to get help from the attendant
	 * if the the listener detects an overweight scale throw an exception to call for help
	 * @return if a item was added to a cart
	 * @throws WeightDiscrepancyException to signal for help
	 * @throws OverloadException to signal for help
	 */
	public boolean addByScan (SellableUnit bp) throws WeightDiscrepancyException, OverloadException {
		try {
		currentWeight = es.getCurrentWeight();
		if(bs.scan(bp)){
			bs.disable();
			es.add(bp);
			double newWeight = es.getCurrentWeight();
			System.out.println(newWeight);
			if(currentWeight + bp.getWeight() !=newWeight) {
				throw new WeightDiscrepancyException();
			}
			//Ideally we would wait here for a couple seconds to see if the user has placed the item
			System.out.println(newWeight);
			currentWeight = newWeight;
			bs.enable();
			return true;
			
		}
		}
		catch(DisabledException e) {
			bs.enable();
			return false;
		}
		catch(SimulationException s) {
			bs.enable();
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
	
	public int getNumItems() {
	    HashMap<Product, Integer> cart = this.getCart();
	    int numItems = 0;
	    for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
	        numItems += entry.getValue();
	    }
	    return numItems;
	}

}


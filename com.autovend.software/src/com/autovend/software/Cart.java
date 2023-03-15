package com.autovend.software;

import java.math.BigDecimal;
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
	private Map<Barcode, BarcodedProduct> myData = ProductDatabases.BARCODED_PRODUCT_DATABASE;
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
	public boolean addByScan (SellableUnit bp) throws WeightDiscrepancyException {
		try {
		currentWeight = es.getCurrentWeight();
		if(bs.scan(bp)){
			es.add(bp);
			double newWeight = es.getCurrentWeight();
			if(currentWeight + bp.getWeight() !=newWeight) {
				throw new WeightDiscrepancyException();
			}
			//Ideally we would wait here for a couple seconds to see if the user has placed the items
			if(wc.isOverWeight()) {
				bs.disable();
			}
			return true;
		}
		}
		catch(DisabledException e) {
			return false;
		}
		catch(SimulationException s) {
			return false;
		} catch (OverloadException e) {
			bs.disable();
		}
		try {
			es.getCurrentWeight();
			bs.enable();
		}catch (OverloadException e) {
			bs.disable();
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


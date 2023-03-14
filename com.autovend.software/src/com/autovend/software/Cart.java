package com.autovend.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.DisabledException;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.*;
import com.autovend.external.ProductDatabases;

public class Cart {
	public Cart (ElectronicScale elec, BarcodeScanner barScan, AttendantTerminal attendant) {
		total = BigDecimal.ZERO;
		es = elec;
		bs = barScan;
		bsos = new Scan();
		wc  = new WeightChecker();
		es.register(wc);
		bs.register(bsos);
		at = attendant;
		at.register(this);
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
	private AttendantTerminal at;
	private boolean scanSensed =false;
	private boolean doneAdding = false;
	private boolean noBagForItem = false;
	private boolean notResolved = true;
	//adds item by scan
	//I think for this approach we might need a class that reacts to events so that 
	private boolean addByScan (SellableUnit bp) {
		try {
		if(bs.scan(bp)){
			wait(10);
			if(noBagForItem) {
				at.getHelp(this);
			while(notResolved) {
				;
			}
			}
			es.add(bp);
			//Ideally we would wait here for a couple seconds to see if the user has placed the items
			while(wc.isOverWeight()) {
				at.getHelpUnloading(es,bp);
				//how do we check if the weight changes??? so we can call wc.wieghtFixed
				//if(weight is under) {
				//wc.weightFixed	
				//}
				//we need to not let then continue until they have fixed the weight but i don't know how
				//maybe some sort of blocking for now
			}
			return true;
		}
		}
		catch(DisabledException e) {
			return false;
		}
		catch(SimulationException s) {
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	//returns the cart
	public ArrayList<Product> getCart() {
		return bsos.returnCart();
	}
	
	public BigDecimal getPrice() {
		return bsos.returnTotal();
	}
	
	public void addItemScreen(SellableUnit bp) {
		while(true) {
			
		if(scanSensed) {
			scanSensed = false;
			if(addByScan(bp)) {
				//tell user they are 
			}else {
				//tell it failed
			}
			break;
		}
		}
		
	}
	public void simulateScan() {
		scanSensed = true;
	}
	public void simulateNotPlacedOnScale() {
		noBagForItem = true;
	}
	public void simulateResolveNoItemOnScale() {
		notResolved = false;
	}
	
}


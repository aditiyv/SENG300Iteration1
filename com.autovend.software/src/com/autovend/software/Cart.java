package com.autovend.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.DisabledException;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SimulationException;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.*;
import com.autovend.external.*;

public class Cart {
	public Cart (ElectronicScale elec) {
		es = elec;
	}
	public static final Map<Barcode, BarcodedProduct> BARCODED_PRODUCT_DATABASE = new HashMap<>();
	//will store all the items in one transaction
	ArrayList<Product> cartOfItems;
	//Total price 
	public BigDecimal total;
	private ElectronicScale es;
	//adds item by scan
	public boolean AddByScan (SellableUnit bp, BarcodeScanner bs) {
		try {
		if(bs.scan(bp)){
			//Ideally we would wait here for a couple seconds to see if the user has placed the items
			es.add(bp);
			BarcodedProduct barProd =BARCODED_PRODUCT_DATABASE.get(bp);
			cartOfItems.add(barProd);
			total = total.add(barProd.getPrice());
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

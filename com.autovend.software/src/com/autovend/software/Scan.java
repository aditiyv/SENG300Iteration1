// Name: Nam Nguyen Vu (UCID: 30154892)

package com.autovend.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public class Scan implements BarcodeScannerObserver {
public boolean enabled;
public HashMap<Product,Integer> cartOfItems;
public BigDecimal total;
private Map<Barcode, BarcodedProduct> myData = ProductDatabases.BARCODED_PRODUCT_DATABASE;

	public Scan() {
		total = BigDecimal.ZERO;
	}
	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		enabled = true;
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		enabled = false;
		
	}

	@Override
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
		BarcodedProduct barProd = myData.get(barcode);
		total.add(barProd.getPrice());
		if(cartOfItems.containsKey(barProd)) {
			Integer itemNum =cartOfItems.get(barProd);
			cartOfItems.put(barProd,itemNum+1);
		}
		else {
			cartOfItems.put(barProd,1);
		}
	}
	public BigDecimal returnTotal() {
		return total;
	}
	public HashMap<Product,Integer> returnCart(){
		return cartOfItems;
	}

}

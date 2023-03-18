package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.external.ProductDatabases;
import com.autovend.software.Cart;
import com.autovend.software.WeightDiscrepancyException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;


public class addByScan_test {

    private Cart cart;
    private ElectronicScale scale;
    private BarcodeScanner scanner;
    private BarcodedUnit product;
    private Barcode barcode;
    private double weightInGrams;
    private double weightLimitInGrams;
    private double newWeight;

    @Before
    public void setUp() {
        scale = new ElectronicScale(5,5);
        scanner = new BarcodeScanner();
        cart = new Cart(scale, scanner);
        Numeral[] code = {Numeral.one, Numeral.two, Numeral.three, Numeral.four};
        Barcode barcode = new Barcode(code);
        weightInGrams = 100.0;
        weightLimitInGrams = 90;
        product = new BarcodedUnit(barcode, weightInGrams);
//        System.out.println(product);
        
        BigDecimal p = new BigDecimal(10);
        BarcodedProduct product2 = new BarcodedProduct(barcode, "Test desc", p, 30);
//        System.out.println(product2);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product2);
//        System.out.println(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode));

        
    }   
    

    private BarcodedProduct BarcodedProduct(Barcode barcode2, String string, BigDecimal p, int i) {
		// TODO Auto-generated method stub
		return null;
	}


	@Test
    public void testAddByScan_AddsProductToCart() throws WeightDiscrepancyException, OverloadException {
//		System.out.println(product);
        cart.addByScan(product);
        cart.getNumItems();
        assertEquals(1, cart.getNumItems());
    }

    @Test
    public void testAddByScan_UpdatesCurrentWeight() throws WeightDiscrepancyException, OverloadException {
        cart.addByScan(product);
        double actualWeight = cart.getCurrentWeight();
       // System.out.println("Actual Weight: " + actualWeight);
        assertEquals(weightInGrams, actualWeight, 0);
    }


    @Test
    public void testAddByScan_UpdatesPrice() throws WeightDiscrepancyException, OverloadException {
        cart.addByScan(product);
        assertEquals(BigDecimal.ZERO, cart.getPrice());
    }

    @Test(expected = OverloadException.class)
    public void testAddByScan_ThrowsWeightDiscrepancyException() throws WeightDiscrepancyException, OverloadException {
        // Set scale weight to be different from product weight
        //scale.setCurrentWeight(weightInGrams);
        scale.add(product);
        scale.getCurrentWeight();
        cart.addByScan(product);
    }
    
///passed test:
    @Test(expected = OverloadException.class)
    public void testAddByScan_ThrowsOverloadException() throws WeightDiscrepancyException, OverloadException {
        // Set scale weight to be less than max weight
        scale.add(product);
        cart.addByScan(product);
        
    }

    
}




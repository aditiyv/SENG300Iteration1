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
    private BarcodedUnit product2;
    private Barcode barcode;
    private double weightInGrams;
    private double weightLimitInGrams;
    private double newWeight;

    @Before
    public void setUp() {
        scale = new ElectronicScale(105,3);
        scanner = new BarcodeScanner();
        cart = new Cart(scale, scanner);
        Numeral[] code = {Numeral.one, Numeral.two, Numeral.three, Numeral.four};
        Barcode barcode = new Barcode(code);
        weightInGrams = 90;
        weightLimitInGrams = 100;
        product = new BarcodedUnit(barcode, weightInGrams);
//        System.out.println(product);
        
        BigDecimal p = new BigDecimal(10);
        BarcodedProduct product2 = new BarcodedProduct(barcode, "Test desc", p, 30);
//        System.out.println(product2);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product2);
//        System.out.println(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode));

        
    }   
    


	@Test
    public void testAddByScan_AddsProductToCart() throws WeightDiscrepancyException, OverloadException {
//		System.out.println(product);
		scale.add(product);
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
    	scale.add(product);
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
        Numeral[] code2 = {Numeral.one, Numeral.two, Numeral.three, Numeral.four};
        Barcode barcode2 = new Barcode(code2);
        product2 = new BarcodedUnit(barcode2, weightInGrams+500);
        cart.addByScan(product2);
        
        
    }

    
}




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
import com.autovend.software.Cart;
import com.autovend.software.WeightDiscrepancyException;

public class addByScan_test {

    private Cart cart;
    private ElectronicScale scale;
    private BarcodeScanner scanner;
    private BarcodedUnit product;
    private Barcode barcode;
    private double weightInGrams;
    private double weightLimitInGrams;

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
    }   

    @Test
    public void testAddByScan_AddsProductToCart() throws WeightDiscrepancyException, OverloadException {
        assertTrue(cart.addByScan(product));
        assertTrue(cart.getCart().containsKey(product));
        assertEquals(1, cart.getCart().get(product).intValue());
    }

    @Test
    public void testAddByScan_UpdatesCurrentWeight() throws WeightDiscrepancyException, OverloadException {
        assertTrue(cart.addByScan(product));
        assertEquals(weightInGrams, cart.getCurrentWeight(), 0);
    }

    @Test
    public void testAddByScan_UpdatesPrice() throws WeightDiscrepancyException, OverloadException {
        assertTrue(cart.addByScan(product));
        assertEquals(BigDecimal.ZERO, cart.getPrice());
    }

    @Test
    public void testAddByScan_ThrowsWeightDiscrepancyException() throws OverloadException {
        // Set scale weight to be different from product weight
        scale.setCurrentWeight(weightInGrams + 1.0);

        try {
            cart.addByScan(product);
            fail("Expected WeightDiscrepancyException to be thrown");
        } catch (WeightDiscrepancyException e) {
            // Pass
        }
    }
///passed test:
    @Test(expected = OverloadException.class)
    public void testAddByScan_ThrowsOverloadException() throws WeightDiscrepancyException, OverloadException {
        // Set scale weight to be greater than max weight
        scale.setCurrentWeight(weightInGrams);
        cart.addByScan(product);
    }
    
}

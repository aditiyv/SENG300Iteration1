package com.autovend.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
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

    @Before
    public void setUp() {
        scale = new ElectronicScale(0,1);
        scanner = new BarcodeScanner();
        cart = new Cart(scale, scanner);
        barcode = new Barcode();
        weightInGrams = 100.0;
        product = new BarcodedUnit(barcode, weightInGrams);
    }
    ////
   
}

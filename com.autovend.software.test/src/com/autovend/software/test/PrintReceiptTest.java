//BRETT LYLE 30103268
//Nam Nguyen Vu 30154892
//Aditi Yadav 30143652
//Arun Sharma 30124252

package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.products.Product;
import com.autovend.external.*;

import com.autovend.software.Cart;
import com.autovend.software.PrintReceipt;

public class PrintReceiptTest {
	Cart cart = new Cart(new ElectronicScale(5, 1), new BarcodeScanner());
	ReceiptPrinter printer = new ReceiptPrinter();
	ReceiptPrinter receiptPrinter = new ReceiptPrinter();
	PrintReceipt printReceipt;
	//= new PrintReceipt(receiptPrinter, cart);
    HashMap<Product, Integer> testMap = new HashMap<Product, Integer>();
    private ElectronicScale scale;
    private BarcodedUnit product;
    
    @Before
    public void setUp() throws Exception {
        printReceipt = new PrintReceipt(printer, cart);
        
        testMap = new HashMap<>();
        testMap = cart.getCart();
    }
    
    //PASSED 
    @Test(expected = EmptyException.class)
    public void testOutOfInk() throws EmptyException, OverloadException {
        printReceipt.reactToOutOfInkEvent(printer);
        for (int i = 0; i < ReceiptPrinter.MAXIMUM_INK; i++) {
        	receiptPrinter.print('a');
        }
    }
    
    //PASSED 
    @Test
    public void testAddedInk() throws EmptyException, OverloadException {
    	printReceipt.reactToInkAddedEvent(printer);
        receiptPrinter.addInk(3);;
        
    }

    //PASSED 
    @Test(expected = EmptyException.class)
    public void testOutOfPaper() throws EmptyException, OverloadException {
    	printReceipt.reactToOutOfPaperEvent(printer);
        for (int i = 0; i < ReceiptPrinter.MAXIMUM_PAPER; i++) {
        	receiptPrinter.print('a');
        }
    }
    
    //PASSED 
    @Test
    public void testAddedPaper() throws EmptyException, OverloadException {
    	printReceipt.reactToPaperAddedEvent(printer);
        receiptPrinter.addPaper(5);
        
    }

    //PASSED 
    @Test
    public void testThanksCustomer() {
    	printReceipt.thanksCustomer(receiptPrinter);
        assertEquals("Thanks the Customer", printReceipt.thanksCustomer(receiptPrinter));
}
    
  //PASSED  
    @Test
    public void testPrintReceipt() throws EmptyException, OverloadException {
    	testMap = new HashMap<>();
        testMap = cart.getCart();
    	
        printReceipt = new PrintReceipt(printer, cart);
        assertTrue(printReceipt.sessionComplete());
    }
    
}
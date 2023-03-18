package com.autovend.software.test;

import java.util.HashMap;

import org.junit.Test;

import com.autovend.software.Cart;
import com.autovend.software.PrintReceipt;

public class PrintReceiptTest {
	PrintReceipt printReceipt = new PrintReceipt();
	Cart cart = new Cart();
	PrintReceipt printReceipt = new PrintReceipt();
    HashMap<Product, Integer> testMap = new HashMap<Product, Integer>();
    
    @Before
    public void setUp() throws Exception {
        cart = new Cart();
        printReceipt = new PrintReceipt(printer, cart);

        // Test data
        Product product1 = new Product("1", "Product1", new BigDecimal("10.00"));
        Product product2 = new Product("2", "Product2", new BigDecimal("15.00"));

        testMap = new HashMap<>();
        testMap.put(product1, 2);
        testMap.put(product2, 1);
    }
    
    @Test(expected = EmptyException.class)
    public void testOutOfInk() throws EmptyException, OverloadException {
        printReceipt.reactToOutOfInkEvent(printer);
        cart.addProduct(new Product("3", "Product3", new BigDecimal("20.00")));
        printReceipt = new PrintReceipt(printer, cart);
    }

    @Test(expected = EmptyException.class)
    public void testOutOfPaper() throws EmptyException, OverloadException {
        printReceipt.reactToOutOfPaperEvent(printer);
        cart.addProduct(new Product("3", "Product3", new BigDecimal("20.00")));
        printReceipt = new PrintReceipt(printer, cart);
    }

    @Test
    public void testPrintReceipt() throws EmptyException, OverloadException {
        cart.addProduct(new Product("1", "Product1", new BigDecimal("10.00")));
        cart.addProduct(new Product("1", "Product1", new BigDecimal("10.00")));
        cart.addProduct(new Product("2", "Product2", new BigDecimal("15.00")));
        printReceipt = new PrintReceipt(printer, cart);
        assertTrue(printReceipt.sessionComplete);
    }

    @Test
    public void testThanksCustomer() {
        assertEquals("Thanks the Customer", printReceipt
}
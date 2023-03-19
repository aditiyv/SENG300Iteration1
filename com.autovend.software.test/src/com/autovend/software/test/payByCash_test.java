//BRETT LYLE 30103268
//Nam Nguyen Vu 30154892
//Aditi Yadav 30143652
//Alina Mansuri 30008370
//Adam Mogensen 30071819
//Arun Sharma 30124252


package com.autovend.software.test;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import com.autovend.Bill;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillStorage;
import com.autovend.devices.BillValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.software.BillCalculator;
import com.autovend.software.Cart;
import com.autovend.software.Pay;
import com.autovend.software.StorageCapacityChecker;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.BillSlot;


public class payByCash_test {
	private Pay myPay;
	Cart cart1;
	BillSlot billInput;
	BillSlot billOutput;
	BillStorage bls; 
	Bill bill;
	BillCalculator bc;
	BillValidator billValidator;
	SelfCheckoutStation scs;
	BillDispenser bDisp;
	Map<Integer, BillDispenser> bDispensers;
	StorageCapacityChecker scc;
	ElectronicScale escale;
	BarcodeScanner bcscan;

	Currency currency;
	
		
		
		@Before
		public void setUp() {
//			Cart cart1 = new Cart(escale, bcscan);
			escale = new ElectronicScale(250,5);
		    bcscan = new BarcodeScanner();
		    cart1 = new Cart(escale, bcscan);
		    int[] denominations = {5,10};
			billValidator = new BillValidator(currency.getInstance("CAD"), denominations);
			bls = new BillStorage(5);
			bDisp = new BillDispenser(5);
			billInput = new BillSlot(false);
			billOutput = new BillSlot(false);
			bDispensers = new HashMap<>();
			myPay = new Pay(cart1, billValidator, billInput, billOutput, bls, bDispensers);
			
		}

		
		@Test
		public void testInputHasSpaceFalse() throws DisabledException, OverloadException {
			
			
			
			Bill bill = new Bill(10, Currency.getInstance("CAD"));
			billInput.accept(bill);
			billInput.emit(bill);
			double amount = myPay.payWithCash(20,bill);
			assertEquals(20,amount,0);
			

		}


		@Test
		public void testStorageHasSpaceFalse() throws DisabledException, OverloadException {
			
			bls = new BillStorage(1);
			Bill bill = new Bill(10, Currency.getInstance("CAD"));
			Bill bill_a = new Bill(10, Currency.getInstance("CAD"));
			myPay.payWithCash(20,bill);
			myPay.payWithCash(20,bill_a);

		}


		@Test
		public void testPayWithCash() throws SimulationException, DisabledException, OverloadException {
			bls = new BillStorage(5);
			Bill bill = new Bill(10, Currency.getInstance("CAD"));
			double amount = myPay.payWithCash(20,bill);
			Assert.assertEquals(10, amount,0);
		
		}
		
		
		@Test
		public void testReturnChange() throws DisabledException, OverloadException {
			
			Bill change = new Bill(10, Currency.getInstance("CAD"));
			assertTrue(myPay.returnChange(bDisp,change));
		
		}	
		
		@Test
		public void testCalculateChange() throws DisabledException, OverloadException {
			

			myPay.calculateChange(20);
			assertEquals(2,bDisp.size());
			
		}	
}

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
	Map<Integer, BillDispenser> billDispensers;
	StorageCapacityChecker scc;
	

	Currency currency;
	
	int[] denominations = {1,2,5,10,20};
	
		
		@Before
		public void setUp() {
			BillValidator billValidator = new BillValidator(Currency.getInstance("CAD"),denominations );
			Map<Integer,BillDispenser> billDispensers = new HashMap<>();
			bls = new BillStorage(5);
			BillDispenser bDisp = new BillDispenser(5);
			BillSlot billInput = new BillSlot(false);
			BillSlot billOutput = new BillSlot(false);
			myPay = new Pay(cart1, billValidator, billInput, billOutput, bls, billDispensers);
			
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
			BillDispenser bDisp = new BillDispenser(5);
			Bill bill = new Bill(10, Currency.getInstance("CAD"));
			bDisp.load(bill);
			assertEquals(2,bDisp.size());
			
		
}
}



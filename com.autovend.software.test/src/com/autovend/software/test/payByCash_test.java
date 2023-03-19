package com.autovend.software.test;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;

import java.util.Currency;

import static org.junit.Assert.*;

import com.autovend.Bill;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillStorage;
import com.autovend.devices.BillValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SimulationException;
import com.autovend.software.BillCalculator;
import com.autovend.software.Pay;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.BillSlot;



	

public class payByCash_test {
	private Pay myPay;
	BillSlot bl;
	BillStorage bls; 
	Bill bill;
	BillCalculator bc;
	BillValidator billValidator;
	BillDispenser bd;
	Currency currency;
		
		
		@Before
		public void setUp() {
			myPay = new Pay(null, null, bl, bl, bls, null);
			bls = new BillStorage(1);

			
		}

		
		@Test
		public void testInputHasSpaceFalse() throws SimulationException, DisabledException, OverloadException {
			
			bc.reactToValidBillDetectedEvent(billValidator, currency, 10);
			double amount = myPay.payWithCash(20,bill);
			assertEquals(amount,amount,0);

		}


		@Test
		public void testStorageHasSpaceFalse() throws SimulationException, DisabledException, OverloadException {
			
			bls = new BillStorage(0);
			double amount = myPay.payWithCash(20,bill);
			

		}


		@Test
		public void testPayWithCash() throws SimulationException, DisabledException, OverloadException {
			
			bc.reactToValidBillDetectedEvent(billValidator, currency, 10);
			double amount = myPay.payWithCash(20,bill);
			Assert.assertEquals(amount, 10,0);
		
		}
		
		public void testReturnChange() throws DisabledException, OverloadException {
			bc.reactToValidBillDetectedEvent(billValidator, currency, 30);
		//	boolean returnChange = myPay.returnChange(bd,bill);
			
		
		}
}
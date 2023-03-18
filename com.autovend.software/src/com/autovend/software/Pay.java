package com.autovend.software;


import java.util.Map;

import java.util.ArrayList;
import java.util.Currency;

import com.autovend.Bill;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillStorage;
import com.autovend.devices.BillValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SimulationException;

public class Pay {
	Cart cart;
	BillSlot bsInput;
	BillSlot bsOutput;
	BillStorage bStorage;
	BillValidator bValidator;
	Map<Integer, BillDispenser> bDispensers;
	BillCalculator bc;
	public Pay(Cart cartl,BillValidator billval,BillSlot billInput,BillSlot billOutput, BillStorage billStorage, Map<Integer, BillDispenser> billDispensers) {
		cart = cartl;
		bsInput = billInput;
		bsOutput = billOutput;
		bStorage = billStorage;
		bDispensers = billDispensers;
		bValidator = billval;
		bc= new BillCalculator();
		bValidator.register(bc);
	} 
	
	//Will return the amount left to pay if not payed in full, if change is due then it will handle it here
	/**
	 * 
	 * @param amount the amount owed
	 * @param bill the bill that has been inserted
	 * @return Updated amount value based on valid Bill value
	 * Exceptions return the
	 */
	public double payWithCash(double amount,Bill bill)throws DisabledException, OverloadException{
		try {
			bValidator.accept(bill);
			if(!bsInput.hasSpace()){
				bc.resetValue();
				return amount;
			}else {
				bStorage.accept(bill);
				double amountInserted = bc.getValue();
				amount = amount - amountInserted;
				
				if(amount < 0) calculateChange(-amount);
			}
			
		}catch(SimulationException s){
			return amount;
		}
		if(amount > 0) return amount;
		
		else return 0.0;
	}
	
	
	/*
	 * Calculates change for customer, currently will only return change in Bills
	 * In future iterations, will include coin change calculation
	 */
	/**
	 * Calculates change for customer, currently will only calculates change in Bills
	 * In future iterations, will include coin change calculation
	 * @param amountOfChange the amount of change to be returned
	 * @param bill the bill that has been inserted
	 */
	public void calculateChange(double amountOfChange){
		
		//Finds the largest denomination of bill possible for change amount
		int temp = (int)amountOfChange;
		while(temp > 0) {
			
			if(bDispensers.containsKey((int)temp)) {
			//Will give multiple of same denomination of bill if appropriate
				while((amountOfChange - temp) > 0) {
				returnChange(bDispensers.get(temp), new Bill(temp, Currency.getInstance("CAD")));
				
				amountOfChange -= temp; 
				}
			}
			temp--;
		}
	}		
			//Do I need to add bill storage observer (or whichever) to notify system when to update balance
			//Do I need to make an observer that implements billslotobserver to notify system when bills are ejected?
	
	/**
	 * returnChange will only occur if calculateChange occurs. Ejects 
	 * appropriate bills as change to the customer
	 * @param dispenser Appropriate BillDispenser to dispense correct bill
	 * @param billChange Bill to be dispensed by BillDispenser
	 * @throws 
	 */
	void returnChange(BillDispenser dispenser, Bill billChange){
		try {
			dispenser.emit();
		} catch (DisabledException | EmptyException | OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (bsOutput.hasSpace()){
		try {
			bsOutput.emit(billChange);
		} catch (DisabledException | SimulationException | OverloadException e1) {
			//Attendant would be alerted if Exceptions occur
			e1.printStackTrace();
			}
		}
	}

}
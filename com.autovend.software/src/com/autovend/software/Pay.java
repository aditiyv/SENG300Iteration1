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
	 * @throws OverloadException Indicates billStorage is full
	 * @throws DisabledException Indicates billStorage is disabled
	 */
	public double payWithCash(double amount,Bill bill) throws DisabledException, OverloadException{
		try {
			
			bValidator.accept(bill);
			if(!bsInput.hasSpace() || !bStorage.hasSpace()){
				
				bc.resetValue();
				return amount;
			}else {
				
				bStorage.accept(bill);
				double amountInserted = bc.getValue();
				amount = amount - amountInserted;
				
				if(amount < 0) calculateChange(-amount, bill);
			}
			
		}catch(SimulationException s){
			bsInput.emit(bill); //Bill is ejected, Attendant is notified.
			return amount; //Return unchanged cart balance.
			
		}
		if(amount > 0) return amount; //Returns updated amount due to Customer I/O
		
		else return 0.0; //Cart balance paid in full
	}
	
	
	/**
	 * Calculates change for customer, currently will only calculates change in Bills
	 * In future iterations, will include coin change calculation
	 * @param amountOfChange the amount of change to be returned
	 * @param bill the bill that has been inserted
	 */
	public void calculateChange(double amountOfChange, Bill bill){
		
		//Finds the largest denomination of bill possible for change amount
		int temp = (int)amountOfChange;
		while(temp > 0) {
			
			if(bDispensers.containsKey((int)temp)) {
			//Will give multiple of same denomination of bill if appropriate
				
				while((amountOfChange - temp) > 0) {
				returnChange(bDispensers.get(temp), bill);
				
				amountOfChange -= temp; 
				}
			}
			temp--;
		}
	}		
	
	/**
	 * returnChange will only occur if calculateChange occurs. Ejects 
	 * appropriate bills as change to the customer
	 * @param dispenser Appropriate BillDispenser to dispense correct bill
	 * @param billChange Bill to be dispensed by BillDispenser
	 * @return true if bill ejection was successful; false if exception was thrown.
	 * 		   false return value will signal attendant to assist customer.
	 * @throws DisabledException; EmptyException; OverloadException
	 */
	boolean returnChange(BillDispenser dispenser, Bill billChange){
		try {
			dispenser.emit();
			
		} catch (DisabledException | EmptyException | OverloadException e) {
			return false; //Request attendant assistance 

		}
		
		if (bsOutput.hasSpace()){
		try {
			bsOutput.emit(billChange);
			
		} catch (DisabledException | SimulationException | OverloadException e1) {
			return false; //Request attendant assistance 
			}
		}
		
		else {
			return false;
		}
		
		return true;
	}
}
//BRETT LYLE 30103268
//Nam Nguyen Vu 30154892
//Aditi Yadav 30143652
//Alina Mansuri 30008370
//Arun Sharma 30124252
//Adam Mogensen 30071819

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
	StorageCapacityChecker scc;
	
	
	public Pay(Cart cartl,BillValidator billval,BillSlot billInput,BillSlot billOutput, BillStorage billStorage, Map<Integer, BillDispenser> billDispensers) {
		cart = cartl;
		bsInput = billInput;
		bsOutput = billOutput;
		bStorage = billStorage;
		bDispensers = billDispensers;
		bValidator = billval;
		bc= new BillCalculator();
		scc = new StorageCapacityChecker();
		billStorage.register(scc);
		bValidator.register(bc);
	} 
	
	//Will return the amount left to pay if not payed in full, if change is due then it will handle it here
	/**
	 * 
	 * @param amount the amount owed
	 * @param bill the bill that has been inserted
	 * @return Updated amount value based on valid Bill value
	 * Exceptions return the
	 * @throws SimulationException 
	 * @throws OverloadException Indicates billStorage is full
	 * @throws DisabledException Indicates billStorage is disabled
	 */
	public double payWithCash(double amount,Bill bill) throws DisabledException, OverloadException {
		try {
			
			bValidator.accept(bill);
			if(!bsInput.hasSpace() || !bStorage.hasSpace()){
				
				bc.resetValue();
				return amount;
			}else {
		
				bStorage.accept(bill);
				if(scc.full) {
					throw new StorageFullException();
				//Attendant will be alerted if StorageFullException is thrown
				}
				double amountInserted = bc.getValue();
				amount = amount - amountInserted;
				
				if(amount < 0){
				calculateChange(-amount);
				return 0.0; //Cart balance paid in full
				}
				else return amount; //Returns updated amount due to Customer I/O
			}
			
	
		}catch(SimulationException s){
			try {
				bsInput.emit(bill);
				//Bill is ejected, Attendant is notified.
				return amount; //Return unchanged cart balance.	
			} catch (DisabledException | SimulationException | OverloadException e) {
				throw new StorageFullException(); 
				//Can be changed to a more specific exception. Will be used to notify attendant
			} 
		}
	}
	
	
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

				while((amountOfChange - temp) > temp) {
				returnChange(bDispensers.get(temp), new Bill(temp, Currency.getInstance("USD"))); 
				//Unfortunately, BillDispenser can not return the Bill that it dispenses. "USD" will be the placeholder
				
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
	public boolean returnChange(BillDispenser dispenser, Bill billChange){

		
		if (bsOutput.hasSpace()){
			try {
				dispenser.emit();
				
			} catch (DisabledException | EmptyException | OverloadException e) {
				return false; //Request attendant assistance
			}
		}
		else {
			return false;
		}
		
		return true;
	}
}
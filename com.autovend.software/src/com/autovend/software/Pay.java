package com.autovend.software;

import java.util.Map;

import com.autovend.Bill;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillStorage;
import com.autovend.devices.BillValidator;
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
	public double payWithCash(double amount,Bill bill){
		bValidator.accept(bill);
		try {
			if(!bsInput.hasSpace()){
				bc.resetValue();
				return 0.0;
			}else {
				double amountInserted = bc.getValue();
				amount = amount - amountInserted;
				//AT THIS POINT I HAVE DETERMINED A BILL IS VALID AND INSERTED DO WHAT U WILL AND ASK ME QUESIONS IF YOU WOULD LIKE
			}
			
		}catch(SimulationException s){
			return 0.0;
		}
		return 0.0;
	}
}
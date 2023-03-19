//BRETT LYLE 30103268
//Nam Nguyen Vu 30154892
//Aditi Yadav 30143652

package com.autovend.software;

import java.util.Currency;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillValidator;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillValidatorObserver;

public class BillCalculator implements BillValidatorObserver{
	boolean valid = false;
	double valueOfCurrency = 0.0;
	public BillCalculator() {
		
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
		valid = true;
		valueOfCurrency = value;	
	}

	public double getValue() throws SimulationException {
		if(valid) {
			double temp = valueOfCurrency;
			valueOfCurrency = 0.0;
			return temp;
		}
		else {
			throw new SimulationException("currency is not valid");
		}
	}
	
	public void resetValue() {
		valueOfCurrency = 0.0;
		
	}
	
	@Override
	public void reactToInvalidBillDetectedEvent(BillValidator validator) {
		valid = false;
		
	}

}

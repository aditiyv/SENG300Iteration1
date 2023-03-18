// Name: Nam Nguyen Vu (UCID: 30154892)

package com.autovend.software;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;

public class WeightChecker implements ElectronicScaleObserver {
	public WeightChecker() {
		
	}
	private boolean overload =false;
	
	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
	
	}

	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		overload = true;
		
	}
	public boolean isOverWeight() {
		return overload;
	}
	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {
		overload = false;
		
	}

}

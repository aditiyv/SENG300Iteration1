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
		//Nothing I can think of except
		scale.enable();
	}

	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		scale.disable();
		overload = true;
		
	}
	public boolean isOverWeight() {
		return overload;
	}
	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {
		scale.enable();
		overload = false;
		
	}

}

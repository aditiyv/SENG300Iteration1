package com.autovend.software;

import java.util.ArrayList;

import com.autovend.SellableUnit;
import com.autovend.devices.ElectronicScale;

public class AttendantTerminal {
	
	public AttendantTerminal() {
		
	}
	ArrayList<Cart> responsibilities = new ArrayList<Cart>();
	public void register(Cart cart) {
		responsibilities.add(cart);
	}
	public void getHelp(Cart cart) {
		cart.simulateResolveNoItemOnScale();
	}

	public void getHelpUnloading(ElectronicScale es, SellableUnit sellableUnit) {
		es.remove(sellableUnit);
	}
}

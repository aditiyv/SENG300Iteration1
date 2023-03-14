package com.autovend.software;

import java.util.ArrayList;

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

}

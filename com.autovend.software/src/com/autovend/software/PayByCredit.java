package com.autovend.software;

import java.util.Scanner;

public class PayByCredit extends Pay {

	@Override
	public void pay() {
		// TODO Auto-generated method stub
		System.out.println("Please insert card");
		Scanner scanner = new Scanner(System.in);
		int cardNumber = scanner.nextInt();
	}

}

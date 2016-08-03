package com.turborep.turbotracker.banking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class testjava {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
BigDecimal val=new BigDecimal("219.128999999");
System.out.println(val.setScale(2, RoundingMode.FLOOR));
	}

}

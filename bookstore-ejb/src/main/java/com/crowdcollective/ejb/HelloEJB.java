package com.crowdcollective.ejb;

import javax.ejb.Stateless;

@Stateless
public class HelloEJB {

	public String hello() {
		return "Hello";
	}
}

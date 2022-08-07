package com.fedex.assessment.config;

public class RequestCounter {
	private int count;

	public void increment() {
		int temp = count;
		count = temp + 1;
	}

	public int getCount() {
		return count;
	}
	
}

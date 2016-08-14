package com.tcs.friendlier.pojo;

public enum Status {
	
    PENDING(0),
    ACCEPTED(1),
    DECLINED(2);

    private int value;

    Status(int newValue) {
        value = newValue;
    }

    public int getValue() { 
    	return value; 
    }
}
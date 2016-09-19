package com.kuci.common.annotation;

public enum ROLE {
	ONLY_IP(1),
	ANYONE(0);
	
	private int value;
	
	ROLE(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
}

package com.manuga.streamCodeGenerator.helpers.helper.collections;

public class Tuple {
	private Object val1;
	private Object val2;
	public Tuple(Object val1, Object val2){
		setVal1(val1);
        setVal2(val2);
	}
	public Object getVal1() {
		return val1;
	}
	public void setVal1(Object val1) {
		this.val1 = val1;
	}
	public Object getVal2() {
		return val2;
	}
	public void setVal2(Object val2) {
		this.val2 = val2;
	}
}

package com.timanaga.streamCodeGenerator.helpers.helper.collections;

public class Quadriple {
	private Object val1;
	private Object val2;
    private Object val3;
    private Object val4;
	public Quadriple(Object val1, Object val2, Object val3, Object val4){
        setVal1(val1);
        setVal2(val2);
        setVal3(val3);
        setVal4(val4);
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

    public Object getVal3() {
        return val3;
    }

    public void setVal3(Object val3) {
        this.val3 = val3;
    }

    public Object getVal4() {
        return val4;
    }

    public void setVal4(Object val4) {
        this.val4 = val4;
    }
}

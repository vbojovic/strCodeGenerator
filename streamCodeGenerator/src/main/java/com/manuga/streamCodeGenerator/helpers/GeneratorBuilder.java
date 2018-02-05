package com.manuga.streamCodeGenerator.helpers;

import com.manuga.streamCodeGenerator.helpers.helper.GenericHelper;

public class GeneratorBuilder{
	private StringBuilder sb = new StringBuilder();
	private boolean randomTabbing = false;
	public void append(String code,int tabCount,int lineCount) throws Exception{
		if (!randomTabbing){
			this.sb.append(GenericHelper.repeatString(lineCount,"\n"));
			this.sb.append(GenericHelper.repeatString(tabCount, "\t"));
			this.sb.append(code);
		}else{
			throw new Exception("no random tabbing yet imlpemented");
		}
	}
	public void setRandomTabbing(boolean randomTabbing) {
		this.randomTabbing = randomTabbing;
	}
	public boolean isRandomTabbing() {
		return randomTabbing;
	}
	public String getCode(){
		return sb.toString();
	} 
}

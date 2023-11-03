/**
 * 
 */
package com.timanaga.streamCodeGenerator.databases.dbModels;

import java.util.List;

public class DatabaseProcedure {
	private int oid;
	private String name;
	private String DDL;
	private String comment;
	private List<DataArguments> arguments;

	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDDL() {
		return DDL;
	}
	public void setDDL(String dDL) {
		DDL = dDL;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<DataArguments> getArguments() {
		return arguments;
	}
	public void setArguments(List<DataArguments> arguments) {
		this.arguments = arguments;
	}
}

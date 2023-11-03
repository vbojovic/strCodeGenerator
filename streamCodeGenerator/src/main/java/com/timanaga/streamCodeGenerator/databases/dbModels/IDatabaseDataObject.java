package com.timanaga.streamCodeGenerator.databases.dbModels;

import java.util.List;

public interface IDatabaseDataObject {
	public List<String> getFieldNames();
	public List<DatabaseField> getFields();
	public void setFields(List<DatabaseField> fields);
	public void addField(DatabaseField field) throws Exception;
	public void deleteField(DatabaseField field);
	public void deleteField(int index);
	public String getName();
	public void setName(String name) ;
	public String getDDL() ;
	public void setDDL(String dDL);  
}

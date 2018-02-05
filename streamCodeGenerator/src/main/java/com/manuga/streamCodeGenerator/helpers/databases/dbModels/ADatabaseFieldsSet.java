/**
 * 
 */
package com.manuga.streamCodeGenerator.helpers.databases.dbModels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author x
 *
 */
public class ADatabaseFieldsSet {
	private boolean isUnique = false;
	private List<DatabaseField> fields;
	private String name = "";
	public boolean getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}
	public List<DatabaseField> getFields() throws Exception {
		if (fields == null || fields.size()==0) this.fields= new ArrayList<DatabaseField>();
		return fields;
	}
	public void setFields(List<DatabaseField> fields) {
		this.fields = fields;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

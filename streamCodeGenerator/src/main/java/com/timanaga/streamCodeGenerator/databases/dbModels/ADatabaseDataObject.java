package com.timanaga.streamCodeGenerator.databases.dbModels;

import java.util.ArrayList;
import java.util.List;

public abstract class ADatabaseDataObject implements IDatabaseDataObject{
	private List<DatabaseField> fields;
	private String Name;
	private String DDL;
    private List<DatabaseForeignKey> foreignKeys;
	private DatabaseKey primaryKey;
    private List<DatabaseIndex> indices;
    private List<DatabaseKey> uniqueKeys;

	public List<String> getFieldNames() {
		List<String> fieldNames = new ArrayList<String>();
		for (DatabaseField field : fields) {
			fieldNames.add(field.getFieldName());
		}
		return fieldNames;
	}
    public List<DatabaseForeignKey> getForeignKeys() {
        return foreignKeys;
    }
	public List<DatabaseField> getFields() {
		return fields;
	}
	public void setFields(List<DatabaseField> fields) {
		this.fields = fields;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDDL() {
		return DDL;
	}
	public void setDDL(String dDL) {
		DDL = dDL;
	}
	public void addField(DatabaseField field) throws Exception {
		if (field == null) throw new Exception("Cannot add empty field");
		this.fields.add(field);
	}
	public void deleteField(String fieldName) {
		if (fields == null || fields.size()==0) return;
		for (int i=0;i<fields.size();i++){
			if (fields.get(i).getFieldName().equalsIgnoreCase(fieldName)) fields.remove(i);
		}
	}
	public void deleteField(int index) {
		if (this.fields != null && this.fields.size()>=index)
			this.fields.remove(index);
		
	}
	public void deleteField(DatabaseField field) {
		String fieldName = field.getFieldName();
		deleteField(fieldName);
	}
	public DatabaseKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(DatabaseKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public void addForeignKey(DatabaseForeignKey fKey) {
		this.foreignKeys.add(fKey);
	}
	public void delForeignKeyByName(String fKeyName) throws Exception {
		if (this.foreignKeys == null || this.foreignKeys.size()==0) throw new Exception("Foreign keys cannot be deleted because they don't exist!");
		for (int i=0;i<this.foreignKeys.size();i++){
			if (this.foreignKeys.get(i).getName().equalsIgnoreCase(fKeyName)) this.foreignKeys.remove(i);
		}
		
	}
	public boolean primaryKeyContainsAutoincrement() throws Exception{
		return primaryKey.containsAutoincremenField();
	}
	public void setForeignKeys(List<DatabaseForeignKey> fKeys) {
		this.foreignKeys = fKeys;
		
	}

    public List<DatabaseIndex> getIndices() {
        return indices;
    }

    public void setIndices(List<DatabaseIndex> indices) {
        this.indices = indices;
    }

    public List<DatabaseKey> getUniqueKeys() {
        return uniqueKeys;
    }

    public void setUniqueKeys(List<DatabaseKey> uniqueKeys) {
        this.uniqueKeys = uniqueKeys;
    }
}

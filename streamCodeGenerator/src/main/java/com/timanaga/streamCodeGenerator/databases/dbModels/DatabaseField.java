/**
 * 
 */
package com.timanaga.streamCodeGenerator.databases.dbModels;


/**
 * @author bv01
 *
 */
public class DatabaseField {
	private String fieldName;
	private String fieldType;
    private String genericDataType;
	private int dimensionCount;
	private int size;
	private int precision;
	private int tableOrder;
	private boolean isNullable;
	private String defaultValue;
	private boolean inPrimaryKey = false;
	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	public boolean isUsingSequence(){
		return (this.defaultValue.contains("nextval")); //TODO ovo je smao za PG bazu, al ne i za mysql bazu itd..mozda bi treba imat polja ADataField i pgField i iField
	}
	public boolean isInPrimaryKey(){
		return this.inPrimaryKey;
	}
	public boolean isAutoincrement(){
		return isUsingSequence();
	}
	//public boolean isTimestampWithDefaultFunc() throws Exception{
	//	if ((defaultValue.toLowerCase().contains("now()") || defaultValue.toLowerCase().contains("date()")) & fieldType.toGenericType().toLowerCase().contains("stamp")) return true;
	//	return false;
	//}
	public int getTableOrder() {
		return tableOrder;
	}
	public void setTableOrder(int tableOrder) {
		this.tableOrder = tableOrder;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public int getDimensionCount() {
		return dimensionCount;
	}
	public void setDimensionCount(int dimensionCount) {
		this.dimensionCount = dimensionCount;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setInPrimaryKey(boolean inPrimaryKey) {
		this.inPrimaryKey = inPrimaryKey;
	}

    public String getGenericDataType() {
        return genericDataType;
    }

    public void setGenericDataType(String genericDataType) {
        this.genericDataType = genericDataType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}

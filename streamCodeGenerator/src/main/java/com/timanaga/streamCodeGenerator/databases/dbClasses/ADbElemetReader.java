package com.timanaga.streamCodeGenerator.databases.dbClasses;


import com.timanaga.streamCodeGenerator.databases.DataFormatConverter;

import java.util.ArrayList;
import java.util.List;

public abstract class ADbElemetReader implements IDbElementReader{
    //protected HashMap<String,String> dataTypesMap = new HashMap<String,String>();
    protected DataFormatConverter converter;

    public List<String> getTargetSchemas() {
        return targetSchemas;
    }

    public void setTargetSchemas(List<String> targetSchemas) {
        this.targetSchemas = targetSchemas;
    }

    protected List<String> targetSchemas = new ArrayList<String>();
    IDb m_DataBase;
    private int followDepth;

	public IDb getM_DataBase() {
		return m_DataBase;
	}
    public String fromGenericDataType(String dataType) throws Exception {
        return converter.genericToNatives(dataType).get(0);
    }

    public String toGenericDataType(String dataType) throws Exception {
        return converter.nativeToGeneric(dataType);
    }
	
	public abstract void ReadFromDataBase() throws Exception;
    public  void setFollowConstraintsToOtherSchemasDepth(int followDepth) {
        this.followDepth=followDepth;
    }
    public  int getFollowConstraintsToOtherSchemasDepth() {
        return  followDepth;
    }
}

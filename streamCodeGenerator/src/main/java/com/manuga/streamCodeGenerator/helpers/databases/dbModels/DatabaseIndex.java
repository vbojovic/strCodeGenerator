package com.manuga.streamCodeGenerator.helpers.databases.dbModels;

public class DatabaseIndex extends ADatabaseFieldsSet{
	private String DDL = "";
	private boolean isParallel = false;
	//TODO trebalo bi mozda tipove ubacit tree/hash/btree i ostalo..al nisu sad bitni
    private String indexType;
	public boolean isParallel() {
		return isParallel;
	}
	public void setParallel(boolean isParallel) {
		this.isParallel = isParallel;
	}
	public String getDDL() {
		return DDL;
	}
	public void setDDL(String dDL) {
		DDL = dDL;
	}

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }
}

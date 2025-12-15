package com.timanaga.streamCodeGenerator.databases.dbModels;

public enum DatabaseTypeEnum {
	
	mysql(3306),
	pgOld(0),
	postgres(5432), 
	oracle(1521), 
	sqlite(0),
	mssql(1433),
	interbase(0),
	dbf(0),
	cassandra(9042); //TODO moram port 9160 mozda?
	private int m_port;

	

	DatabaseTypeEnum(int port)
	{
		this.m_port = port;
	}
	
	public int getDefaultPort() {
		return m_port;
	}
}

package com.manuga.streamCodeGenerator.helpers.databases.dbModels;

import com.manuga.streamCodeGenerator.helpers.helper.GenericHelper;

public class DatabaseSettings {
	
	private DatabaseTypeEnum dataBaseType = DatabaseTypeEnum.postgres;
	
	private String login;
	private String pass;
	private int port = 0;
	private String host;
	private String dataBase;
	private String schema;
	
	public DatabaseTypeEnum getDataBaseType() {
		return dataBaseType;
	}
	public void setDataBaseType(DatabaseTypeEnum dataBaseType) {
		this.dataBaseType = dataBaseType;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public int getPort() {		
		if(port == 0) {
			port = dataBaseType.getDefaultPort();
		}		
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getDataBase() {
		return dataBase;
	}
	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}	
	
	
	
	public void saveToFile(String fName) throws Exception{
		GenericHelper.object2xml(this, fName);
	}

	public void loadFromFile(String fName) throws Exception{
		DatabaseSettings dbs = new DatabaseSettings();
		dbs = (DatabaseSettings) GenericHelper.xml2object(fName);
		this.login = dbs.login;
		this.pass = dbs.pass;
		this.port = dbs.port;
		this.host = dbs.host;
		this.dataBase = dbs.dataBase;
	}

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}

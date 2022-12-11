/**
 * 
 */
package com.timanaga.streamCodeGenerator.helpers.databases.dbClasses;


import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.DatabaseModel;
import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.DatabaseSettings;
import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.DatabaseTypeEnum;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


/**
 * @author vb
 * 
 */
public abstract class ADb implements IDb {
	protected boolean folowConstraintsToOtherSchemas;
	protected IDbElementReader m_elementReader;
	protected DatabaseSettings dbSettings;
//	protected String login = "root";
//	protected String pass;
//	protected String host = "localhost";
//	protected int port = 3306;
	protected DatabaseTypeEnum dbType = DatabaseTypeEnum.postgres;
//	protected String dbName;
	protected Connection connection;
	protected String currentSchema;
	protected DatabaseSettings settings;
	protected List<String> schemas;
	public DatabaseModel model;


	public Connection getConnection() {
		return connection;
	}
	
	public DatabaseTypeEnum getDbType(){
		return this.dbType;
	}
		
	public IDbElementReader getElementReader() {
		return m_elementReader;
	}

	public void setElementReader(IDbElementReader m_elementReader) {
		this.m_elementReader = m_elementReader;
	}
	
	public DatabaseModel getModel() throws Exception {
		return model;
	}

	public void setModel(DatabaseModel model) {
		//this.model.setDbSettings(settings);
		this.model = model;
	}
	

	public String sql2string(String sqlTxt) throws Exception {
		Statement sql;
		sql = this.connection.createStatement();
		ResultSet results = sql.executeQuery(sqlTxt);
        //System.out.println(sqlTxt);
        while (results.next()){
		    return results.getString(1);
        }
        throw new Exception("No record found");
	}

	public ResultSet sql2resultSet(String sqlTxt) throws SQLException {
		Statement sql;
		ResultSet results;
		sql = this.connection.createStatement();
		results = sql.executeQuery(sqlTxt);
		return results;
	}

	public DatabaseSettings getSettings() {
		return settings;
	}

	public void setSettings(DatabaseSettings settings) {
//		this.setLogin(settings.getLogin());
//		this.setPass(settings.getPass());
//		this.setHost(settings.getHost());
//		this.setPort(settings.getPort());
//		this.setDbName(settings.getDataBase());
		this.settings = settings;
		//if (this.model != null) this.model.setDbSettings(settings);
	}

	public String getCurrentSchema() {
		return currentSchema;
	}

	public void setCurrentSchema(String currentSchema) {
		this.currentSchema = currentSchema;
	}


    public boolean isFolowConstraintsToOtherSchemas() {
        return folowConstraintsToOtherSchemas;
    }

    public void setFolowConstraintsToOtherSchemas(boolean folowConstraintsToOtherSchemas) {
        this.folowConstraintsToOtherSchemas = folowConstraintsToOtherSchemas;
    }

}

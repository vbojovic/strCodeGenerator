/**
 * 
 */
package com.timanaga.streamCodeGenerator.databases.dbClasses;


import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseModel;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseSettings;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseTypeEnum;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author x
 *
 */
public interface IDb { 
	
	
//	void setLogin(String login);
//	String getLogin();
//	void setPass(String login);
	void connect() throws Exception;
	void disconnect() throws Exception;
//	int getPort();
//	void setPort(int port);
//	String getHost();
//	void setHost(String host);
	DatabaseTypeEnum getDbType();
	
//	List<String> getSchemasList();
//	public List<DatabaseSchema> getSchemas() throws Exception;
//	List<String> getTableNames(String schema) throws Exception;  
//	public List<DatabaseTable> getTables(DatabasePath dbPath) throws Exception ;
//	public List<DatabaseView> getViews(DatabasePath dbPath) throws Exception ;
//	public List<DatabaseKey> getKeys(DatabasePath dbPath)throws Exception ;
//	public List<DatabaseForeignKey> getForeignKeys(DatabasePath dbPath) throws Exception ;
//	public List<DatabaseIndex> getIndexes(DatabasePath dbPath) throws Exception ;
	public void setSettings(DatabaseSettings settings);
    public   DatabaseSettings getSettings();
	String sql2string(String sqlTxt) throws Exception ;
	public DatabaseModel getModel() throws Exception;
	/*
	String getTableDDL(String tableName);
	List<String> getFunctions(String schema);
	String getFunctionDDL(String function);
	String getTableComments(String tableName);
	String getFunctionComments(String function,String schemaName);
	List<Field> getFields(String tableName,String schemaName);
	*/
	void loadFromDatabase() throws Exception;
	//void loadFromFile(String dbModelJSON);
//	public List<DatabaseField> getFields(DatabasePath dbPath) throws Exception;
//	public String dbType2GenericType(String dbType) throws Exception;
	//public DataTypes genericDataType2DataType(String genericDataType);

	public abstract Connection getConnection();
	ResultSet sql2resultSet(String sqlTxt) throws SQLException;
	IDbElementReader getElementReader();

}

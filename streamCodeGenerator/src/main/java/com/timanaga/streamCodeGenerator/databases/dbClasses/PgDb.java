/**
 * 
 */
package com.timanaga.streamCodeGenerator.databases.dbClasses;



import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseModel;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabasePath;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseSettings;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseTypeEnum;
import org.postgresql.util.PSQLException;

import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

/**
 * @author x
 * 
 */
public class PgDb extends ADb {
	
	/* Constructor */
	public PgDb() throws Exception {
		super();
//		try {
//			//this.loadGenericTypes();
//			throw new Exception("generic types will not be loaded because another approach must be taken");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		this.dbType = DatabaseTypeEnum.postgres;
		m_elementReader = new PgDbElementReader(this);
	}


	@Override
	public DatabaseTypeEnum getDbType() {
		return this.dbType;
	}
	
	@Override
	public DatabaseModel getModel() throws Exception{
		if (this.settings == null) throw new Exception("Model cannot be generated because database settings are missing!");
		this.connect();
		//this.model.setDbSettings(settings);
		loadFromDatabase();
		this.disconnect();
		return this.model;
	}
	

	 /*
	public void setDatabaseSettings(DatabaseSettings dbSettings){
		this.dbSettings = dbSettings;
	}
	public DatabaseSettings getDatabaseSettings(){
		return this.dbSettings;
	}*/
	public void loadFromDatabase() throws Exception {
		if (this.settings == null) throw new Exception("Database settings are missing!");
		this.setModel(new DatabaseModel());
		//this.model.setDbSettings(settings);

		if (this.connection.isClosed())
			throw new Exception("Connection is not opened");

//		List<DatabaseSchema> schemas = this.getSchemas();
//		for (DatabaseSchema schema : schemas) {
//			this.model.addSchema(schema);
//			//TODO ubacit da se loadaju i ostali objekti
//		}


	}


    public String getDDL(DatabasePath dbPath) throws Exception{
		if (dbPath == null) throw new Exception("N/A");
		if (dbPath.database.equals("")) throw new Exception("N/A");
		throw new Exception("N/A");
//		if (!dbPath.field.equals("")) {
//			return
//		}
		/*
		String sql = "";
		if (!dbPath.function.equals("")){
			sql = "";
		}
		return sql2string(sql);
		*/
	}
	
 
	public void connect() throws Exception {

		java.sql.Driver driver = new org.postgresql.Driver(); Class.forName(
				"org.postgresql.Driver").newInstance();
		Properties props = new Properties();
		props.setProperty("user", this.settings.getLogin());
		props.setProperty("password", this.settings.getPass());
		// props.setProperty("ssl","false");
		DriverManager.registerDriver(driver);

		String url = "jdbc:postgresql://" + this.settings.getHost() + ":"
				+ Integer.toString(this.settings.getPort()) + "/" + this.settings.getDataBase();
		if (this.settings.getLogin().isEmpty())
			throw new Exception("Login not set!");

		DriverManager.setLoginTimeout(15);
		
		try {
			this.connection = DriverManager.getConnection(url, props);
		} catch (PSQLException e) {
			throw new Exception(e.getMessage());
		}
		
		// this.dbMd = this.connection.getMetaData(); // get
	}
	
	@Override
	public void disconnect() throws Exception {
		this.connection.close();
	}

}

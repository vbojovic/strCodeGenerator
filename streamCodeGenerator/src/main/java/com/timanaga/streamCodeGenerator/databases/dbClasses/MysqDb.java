package com.timanaga.streamCodeGenerator.databases.dbClasses;

import com.timanaga.streamCodeGenerator.databases.dbModels.*;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;


public class MysqDb extends ADb implements IDb
{

	public MysqDb() throws Exception {
		super();
		this.dbType = DatabaseTypeEnum.mysql;
        m_elementReader = new MysqlDbElementReader(this);
	}

	@Override
	public void connect() throws Exception
	{
		java.sql.Driver driver = new com.mysql.cj.jdbc.Driver();
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		//TODO

		Properties props = new Properties();
		props.setProperty("user", this.settings.getLogin());
		props.setProperty("password", this.settings.getPass());
		// props.setProperty("ssl","false");
		DriverManager.registerDriver(driver);

		String url = "jdbc:mysql://" + this.settings.getHost() + ":"
				+ Integer.toString(this.settings.getPort()) + "/" + this.settings.getDataBase();
		if (this.settings.getLogin().isEmpty())
			throw new Exception("Login not set!");

		DriverManager.setLoginTimeout(15);
		StringBuilder longUrl = new StringBuilder();
		longUrl.append(url);
		longUrl.append("?user=");
		longUrl.append(this.settings.getLogin());
		longUrl.append("&password=");
		longUrl.append(this.settings.getPass());
		longUrl.append("&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");

		try {
			this.connection = DriverManager.getConnection(longUrl.toString(), props);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	@Override
	public void disconnect() throws Exception
	{
        this.connection.close();
	}


	@Override
	public void loadFromDatabase() throws Exception {
        if (this.settings == null) throw new Exception("Database settings are missing!");
        this.setModel(new DatabaseModel());
        //this.model.setDbSettings(settings);

        if (this.connection.isClosed())
            throw new Exception("Connection is not opened");

	}
    @Override
    public DatabaseTypeEnum getDbType() {
        return this.dbType;
    }
    @Override
    public void setSettings(DatabaseSettings settings) {
        this.settings = settings;
    }
    public DatabaseModel getModel() throws Exception{
        if (this.settings == null) throw new Exception("Model cannot be generated because database settings are missing!");
        this.connect();
        //this.model.setDbSettings(settings);
        loadFromDatabase();
        this.disconnect();
        return this.model;
    }
	public List<DatabaseField> getFields(DatabasePath dbPath) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

    public String getDDL(DatabasePath dbPath) throws Exception{
        throw new Exception("not yet implemented");
    }
//
//	public List<String> getTableNames(String schema) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<DatabaseTable> getTables(DatabasePath dbPath) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<DatabaseView> getViews(DatabasePath dbPath) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<DatabaseKey> getKeys(DatabasePath dbPath) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<DatabaseForeignKey> getForeignKeys(DatabasePath dbPath)
//			throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<DatabaseIndex> getIndexes(DatabasePath dbPath) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}



}

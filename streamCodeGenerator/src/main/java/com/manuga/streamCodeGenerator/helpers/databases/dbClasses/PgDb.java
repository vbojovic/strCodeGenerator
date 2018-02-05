/**
 * 
 */
package com.manuga.streamCodeGenerator.helpers.databases.dbClasses;



import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseModel;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabasePath;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseSettings;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseTypeEnum;
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


	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
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
	
	@Override
	public void setSettings(DatabaseSettings settings) {
//		this.setLogin(settings.getLogin());
//		this.setPass(settings.getPass());
//		this.setHost(settings.getHost());
//		this.setPort(settings.getPort());
//		this.setDbName(settings.getDataBase());
		this.settings = settings;
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
		throw new Exception("not yet implemented");
		/*
		String sql = "";
		if (!dbPath.function.equals("")){
			sql = "";
		}
		return sql2string(sql);
		*/
	}
	
 
//	public void setPort(int port) {
//		this.port = (port == -1) ? 5432 : port;
//	}


	
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
	

//	public List<DatabaseSchema> getSchemas() throws Exception {
//		Statement sql;
//		String sqlTxt = "select nspname as schema from pg_namespace order by nspname";
//		ResultSet results = null;
//		List<DatabaseSchema> schemas = new ArrayList<DatabaseSchema>();
//
//		sql = this.connection.createStatement();
//		results = sql.executeQuery(sqlTxt);
//		if (results != null)
//			while (results.next()) {
//				DatabaseSchema sch = new DatabaseSchema();
//				sch.setSchemaName(results.getString("schema"));
//				sch.setComment("not implemented yet");
//				sch.setDDL("N/A");
//				
//				DatabasePath dbPath = new DatabasePath();
//				dbPath.schema = sch.getSchemaName();
//				
//				List<DatabaseTable> tables= this.getTables(dbPath);
//				sch.setTables(tables);
//				
//				List<DatabaseView> views= this.getViews(dbPath);
//				sch.setViews(views);
//				
//				schemas.add(sch);
//			}
//		return schemas;
//	}
//
//	public List<String> getSchemasList() {
//		Statement sql;
//		String sqlTxt = "select nspname as schema from pg_namespace order by nspname";
//		ResultSet results = null;
//		List<String> schemas = new ArrayList<String>();
//
//		try {
//			sql = this.connection.createStatement();
//			results = sql.executeQuery(sqlTxt);
//			if (results != null)
//				while (results.next())
//					schemas.add(results.getString("schema"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return schemas;
//	}
//

//
//	public List<DatabaseField> getFields(DatabasePath dbPath) throws Exception { // TODO
//		if (dbPath.schema == null | dbPath.schema == "" ) throw new Exception("Cannot get fields, schema not set!");
//		if (dbPath.view == null && dbPath.table == null ) throw new Exception("Cannot get fields, table or view not set!");
//		
//		List<DatabaseField> fields = new ArrayList<DatabaseField>();
//		
//		StringBuilder sqlTxt = new StringBuilder();
//		sqlTxt.append(
//				"select "
//						+ "table_catalog,table_schema,table_name,column_name,ordinal_position,"
//						+ "column_default,is_nullable,data_type,character_maximum_length,"
//						+ "character_octet_length,numeric_precision,numeric_precision_radix,"
//						+ "numeric_scale,datetime_precision,interval_type,interval_precision,character_set_catalog,"
//						+ "character_set_schema,character_set_name,collation_catalog,collation_schema,collation_name,"
//						+ "domain_catalog,domain_schema,domain_name,udt_catalog,udt_name,scope_catalog,"
//						+ "scope_schema,scope_name,maximum_cardinality,dtd_identifier,is_self_referencing,"
//						+ "is_identity,identity_generation,identity_start,identity_increment,identity_maximum,"
//						+ "identity_minimum,identity_cycle,is_updatable ")
//				.append("from INFORMATION_SCHEMA.COLUMNS ")
//				.append("where table_schema = '" + dbPath.schema + "' ")
//				.append("and table_name='" + dbPath.table + "'");
//		ResultSet fieldsRes = sql2resultSet(sqlTxt.toString());
//		if (fieldsRes != null)
//			while (fieldsRes.next()) {
//				DatabaseField field = new DatabaseField();
//				/*
//				field.setFieldType(this.genericDataType2DataType(fieldsRes
//						.getString("udt_name").replace("_", "")));
//				*/ 
//				//TODO moram postavit tip polja!
//				// ispitat ovo gore
//				String udtName = fieldsRes.getString("udt_name");
//				field.setNullable((fieldsRes.getString("is_nullable")
//						.equals("YES")) ? true : false);
//				field.setTableOrder(fieldsRes.getInt("ordinal_position"));
//				field.setDefaultValue(fieldsRes.getString("column_default"));
//
//				if (udtName.equals("varchar")) {
//					field.setSize(fieldsRes.getInt("character_maximum_length"));
//				} else {
//					field.setSize(0);
//				}
//				
//				if (fieldsRes.getString("data_type").equals("ARRAY")) {
//					// dimenzije
//					//throw new Exception("Arrays not available yet!");
//				}
//				// if (udtName.equalsIgnoreCase(""))
//				// field.set
//				//field.setDimensionCount(fieldsRes.getInt("dimnum")); //ovaj dio necu ukljucivat josh jer ce broj dimenzija bit 1 za sada
//				//field.setFieldName(fieldsRes.getString("name"));  = column_name
//				field.setFieldName(fieldsRes.getString("column_name"));
//				//
//				// field.setPrecision(fieldsRes.getInt(""));
//				field.setTableOrder(fieldsRes.getInt("ordinal_position"));
//				// field.setPrecision(precision)
//				fields.add(field);
//			}
//
//		return fields;
//	}

	

//	@Override
//	public List<String> getTableNames(String schema) throws Exception {
//		if (schema.equals(""))
//			schema = "public";
//		String sqlTxt = "select tablename  from pg_tables  where schemaname = '"
//				+ schema + "' order by tablename ";
//		List<String> tableNames = new ArrayList<String>();
//		ResultSet tablesTesult = this.sql2resultSet(sqlTxt);
//		tableNames = GenericHelper.resultSetToList(tablesTesult);
//		return tableNames;
//	}
//	public List<DatabaseView> getViews(DatabasePath dbPath) throws Exception {
//		String sqlTxt = "select viewname,definition  from pg_views  where schemaname = '"
//				+dbPath.schema + "' order by viewname ";
//		List<DatabaseView> views = new ArrayList<DatabaseView>();
//		ResultSet tablesResult = this.sql2resultSet(sqlTxt);
//		if (tablesResult != null)
//			while (tablesResult.next()){
//				//r.add(tablesResult.getString(1));
//				DatabaseView view = new DatabaseView();
//				view.setDDL(tablesResult.getString("definition"));
//				view.setName(tablesResult.getString("viewname"));
//				DatabasePath tablePath = dbPath;
//				tablePath.view = view.getName();
//				view.setFields(this.getFields(dbPath));  
//				views.add(view);
//			}
//		return views;
//	}
//	public List<DatabaseTable> getTables(DatabasePath dbPath) throws Exception {
//		String sqlTxt = "select tablename  from pg_tables  where schemaname = '"
//				+dbPath.schema + "' order by tablename ";
//		List<DatabaseTable> tables = new ArrayList<DatabaseTable>();
//		ResultSet tablesResult = this.sql2resultSet(sqlTxt);
//		if (tablesResult != null)
//			while (tablesResult.next()){
//				//r.add(tablesResult.getString(1));
//				DatabaseTable table = new DatabaseTable();
//				table.setDDL("table DDL not set");
//				table.setName(tablesResult.getString("tablename"));
//				DatabasePath tablePath = dbPath;
//				tablePath.table = table.getName();
//				table.setFields(this.getFields(dbPath));  
//				tables.add(table);
//			}
//		return tables;
//	}
	
		

}

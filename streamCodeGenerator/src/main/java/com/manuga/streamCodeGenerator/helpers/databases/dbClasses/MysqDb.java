package com.manuga.streamCodeGenerator.helpers.databases.dbClasses;

import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseField;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabasePath;

import java.util.List;


public class MysqDb extends ADb implements IDb
{

	public MysqDb()
	{
		
	}
	
	@Override
	public void connect() throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect() throws Exception
	{
		// TODO Auto-generated method stub

	}

//	@Override
//	public DatabaseTypeEnum getDbType()
//	{
//		// TODO Auto-generated method stub
//		return this.dataTypes;
//	}

//	@Override
//	public void setDbType(DatabaseTypes dbType)
//	{
//		// TODO Auto-generated method stub
//
//	}

//	@Override
//	public List<String> getSchemasList()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<DatabaseSchema> getSchemas() throws Exception
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
 

	@Override
	public void loadFromDatabase()
	{
		// TODO Auto-generated method stub

	}

 

	public List<DatabaseField> getFields(DatabasePath dbPath) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
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

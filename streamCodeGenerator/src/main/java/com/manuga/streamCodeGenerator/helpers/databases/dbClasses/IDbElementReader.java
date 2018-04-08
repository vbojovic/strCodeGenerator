package com.manuga.streamCodeGenerator.helpers.databases.dbClasses;

import com.manuga.streamCodeGenerator.helpers.databases.dbModels.*;

import java.sql.SQLException;
import java.util.List;

public interface IDbElementReader {
    public abstract String fromGenericDataType(String dataType) throws Exception;
    public abstract String toGenericDataType(String dataType) throws Exception;
    public abstract List<String> getTargetSchemas() ;
    public abstract void setTargetSchemas(List<String> targetSchemas) ;

    public abstract void ReadFromDataBase() throws Exception;

	public abstract List<DatabaseSchema> readSchemas() 
			throws Exception;
    public abstract DatabaseSchema readSchema(final String schemaName)
            throws Exception;
    public abstract List<DatabaseSchema> readTargetSchemas()
            throws Exception;
	public abstract List<DatabaseKey> readKeys(DatabasePath dbPath)
			throws Exception;

	public abstract List<DatabaseForeignKey> readForeignKeys(DatabasePath dbPath)
			throws Exception;

	public abstract List<DatabaseIndex> readIndexes(DatabasePath dbPath)
			throws Exception;

	public abstract List<DatabaseField> readFields(DatabasePath dbPath)
			throws Exception;

	public abstract List<DatabaseView> readViews(DatabasePath dbPath)
			throws Exception;

	public abstract List<DatabaseTable> readTables(DatabasePath dbPath)
			throws Exception;

    public abstract List<DatabaseProcedure>   readProcedures  (DatabasePath dbPath)
            throws Exception;

    public abstract List<DatabaseFunction>   readFunctions  (DatabasePath dbPath)
            throws Exception;

	public abstract String readComment(DatabasePath dbPath) throws Exception;

	public abstract List<String> readSchemasList();
	
	public abstract List<String> readTableNames(String schema) 
			throws Exception;
	
	public abstract List<String> readTableColumns(String schema, String table) 
			throws Exception;

    public abstract List<String> readFunctionNames(String schema) throws SQLException, Exception;
    public abstract List<String> readProcedureNames(String schema) throws SQLException, Exception;
	public abstract List<String> readConstraints(String schema, String table)
			throws Exception;

    public abstract List<DatabaseSequence> readSequences(DatabasePath dbPath) throws Exception;

    public abstract String getPkey(String schema, String table) throws  Exception;

    public abstract List<String> getPkeyFields(String schema, String table) throws  Exception;

    public abstract List<String> getIndices(String schema, String table) throws  Exception;


    public abstract List<String> getForeignKeys(DatabasePath path/*String schema, String table*/) throws  Exception;

//    public abstract DatabaseIndex getIndex(String schemaName, String tableName, String indexName) throws Exception;
    public abstract DatabaseIndex getIndex(DatabasePath path) throws Exception;
    public abstract List<String> getForeignKeysFields(String schema, String fKeyName, boolean sourceTable) throws  Exception;
    public abstract String getForeignKeySourceTableName(String schemaName, String fKeyName) throws  Exception;


    public abstract void setFollowConstraintsToOtherSchemasDepth(int followDepth) ;
    public abstract int getFollowConstraintsToOtherSchemasDepth() ;
    public abstract List<DatabaseFunction> getFunctions(String schema) throws Exception;

//    List<String> getIndexFields(String indexSchema, String indexName) throws Exception;
    List<String> getIndexFields(DatabasePath path) throws Exception;
}
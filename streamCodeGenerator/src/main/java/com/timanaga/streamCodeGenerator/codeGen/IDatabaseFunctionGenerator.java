package com.timanaga.streamCodeGenerator.codeGen;

import java.util.List;

import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.DatabaseField;
import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.DatabasePath;
import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.DatabaseSchema;
import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.DatabaseTable;

public interface IDatabaseFunctionGenerator {
	public String tableToInsert(DatabaseTable dbTable) throws Exception;
	public String schemaToInsert(DatabaseSchema dbSchema) throws Exception; 
	public String schemaToUpdate(DatabaseSchema dbSchema);
	public String schemaToDelete(DatabaseSchema dbSchema) throws Exception;
	public String schemaToSwitchFields(DatabaseSchema dbSchema) throws Exception;
	public String tableToUpdate(DatabaseTable dbTable);
	public String tableToSwitchFields(DatabaseTable dbTable) throws Exception;
	public String tableToDelete(DatabaseTable dbTable) throws Exception;
	public String pathToUpdate(DatabasePath dbPath) throws Exception;
	public String pathToDelete(DatabasePath dbPath) throws Exception;
	public String pathToInsert(DatabasePath dbPath) throws Exception;
	public String pathToSwitch(DatabasePath dbPath) throws Exception;
	public String ToInsert() throws Exception;
	public String ToDelete() throws Exception;
	public String ToUpdate() throws Exception;
	public String ToSwitch() throws Exception;
	public void writeToFile(String pgSqlFilePath) throws Exception;
	public String fields2paramList(List<DatabaseField> fields) throws Exception;
	public String fields2paramNamesList(List<DatabaseField> fields) throws Exception;
	public boolean isValidationIncluded() ;

	public void setValidationIncluded(boolean validationIncluded);

}

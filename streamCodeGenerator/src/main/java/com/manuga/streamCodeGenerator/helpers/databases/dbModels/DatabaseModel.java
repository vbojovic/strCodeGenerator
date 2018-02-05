/**
 * 
 */
package com.manuga.streamCodeGenerator.helpers.databases.dbModels;


import com.cedarsoftware.util.io.JsonWriter;
import com.manuga.streamCodeGenerator.helpers.helper.GenericHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author bv01
 *
 */
public class DatabaseModel {
	private List<DatabaseSchema> schemas =  new ArrayList<DatabaseSchema>();
	private String name;
	//private DatabaseSettings dbSettings;
	private String DDL = "";
	//private List<DatabasePath> targetSchemas = new ArrayList<DatabasePath>();
	@SuppressWarnings("unchecked")
	//public void loadTargetSchemas(String filePath) throws Exception{
	//	if (!GenericHelper.fileOrDirectoryExists(filePath)) throw new Exception("File "+filePath+" doesn't exist");
	//	this.targetSchemas = (List<DatabasePath>) GenericHelper.xml2object(filePath);
	//	//this.filterSchemaNames = schemaNames;
	//}
	//public boolean isTargetSchema(DatabasePath obj) throws Exception{
	//	if (obj.schema == null) throw new Exception("No schema set");
	//	if (this.targetSchemas == null) return true;
	//	if (this.targetSchemas.size()==0) return true;
	//	for (DatabasePath sch : this.targetSchemas) {
	//		if (sch.schema == obj.schema ) return true;
	//	}
	//	return   false;
	//}
	public void exportToXml(String pathToXml) throws Exception{
		GenericHelper.object2xml(this, pathToXml);
	}
	public void saveToXml(String pathToXml) throws Exception{
		this.exportToXml(pathToXml);
	}
    public void saveToJson(String pathToJsonFile) throws Exception{
        String json = JsonWriter.objectToJson(this, new  HashMap<String, Object>());
        GenericHelper.string2fileOverWrite(json,pathToJsonFile);
    }
    public void loadFromJson(String pathToJsonFile) throws Exception{

    }
	public void importFromXml(String importFileName) throws Exception{
		this.loadFromXml(importFileName);
	}
	public void loadFromXml(String importFileName) throws Exception{
		//this=(DatabaseModel)Helper.xml2object(importFileName);
		DatabaseModel dbm = new DatabaseModel();
		dbm = (DatabaseModel) GenericHelper.xml2object(importFileName);
		//this = dbm;
		this.setSchemas(dbm.getSchemas());
		this.setName(dbm.getName());
		this.setDDL(dbm.getDDL());

		//this.setDbSettings(dbm.getDbSettings());
	}
	public DatabaseSchema getSchemaFromPath(DatabasePath dbPath) throws Exception{
		if (this.schemas == null || schemas.size()==0) throw new Exception("Model has no schemas");
		for (DatabaseSchema dbSchema : schemas) {
			if (dbSchema.getSchemaName().equalsIgnoreCase(dbPath.schema)) return dbSchema;
		}
		throw new Exception("Schema not found in model");
	}
	
	public DatabaseTable getTableFromPath(DatabasePath dbPath) throws Exception{
		if (this.schemas == null || schemas.size()==0) throw new Exception("Model has no schemas");
		DatabaseSchema dbSchema = getSchemaFromPath(dbPath);
		for (DatabaseTable table : dbSchema.getTables()) {
			if (table.getName().equalsIgnoreCase(dbPath.table)) return table;
		}
		throw new Exception("Table not found in schema");
	}
	
	public List<DatabaseSchema> getSchemas() {
		return schemas;
	}
	public String getDDL() {
		return DDL;
	}
	public void setDDL(String dDL) {
		DDL = dDL;
	}
	//public void addSchema(DatabaseSchema schema) throws Exception {
	//	DatabasePath schPath = new DatabasePath();
	//	schPath.schema = schema.getSchemaName();
	//	if (this.isTargetSchema(schPath))
	//		this.schemas.add(schema);
	//}
	public void setSchemas(List<DatabaseSchema> schemas) {
		this.schemas = schemas;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public void removeTableFromSchema (DatabasePath path){
        for (DatabaseSchema sch : this.getSchemas()){
            if (!sch.getSchemaName().equalsIgnoreCase(path.schema)) continue;
            for (int i = 0 ; i < sch.getTables().size() ; i++){
                DatabaseTable table = sch.getTables().get(i) ;
                if (!table.getName().equalsIgnoreCase(path.table)) continue;
                sch.getTables().remove(i);
                return;
            }
        }
    }
	/*
	public DatabaseSettings getDbSettings() {
		return dbSettings;
	}
	public void setDbSettings(DatabaseSettings dbSettings) {
		this.dbSettings = dbSettings;                                            a
	}
	*/

	//public List<DatabasePath> getTargetSchemas() {
	//	return targetSchemas;
	//}
	//public void setTargetSchemas(List<DatabasePath> targetSchemas) {
	//	this.targetSchemas = targetSchemas;
	//}
	//public void addTargetSchemas(String targetSchemaName) {
	//	DatabasePath t  = new DatabasePath();
	//	t.schema = targetSchemaName;
	//	this.targetSchemas.add(t);
	//}
    //
	//private void clearNonTargetedSchemas(){
    //    if (targetSchemas==null || targetSchemas.size()==0) return;
    //    List<DatabaseSchema> schemas = new ArrayList<DatabaseSchema>();
    //      for (DatabaseSchema sch : getSchemas()){
    //          if (targetSchemas.contains(sch.getSchemaName()))  {
    //              this.schemas.add(sch);
    //          }
    //      }
    //    this.getSchemas().clear();
    //    this.setSchemas(schemas);
	//}
}

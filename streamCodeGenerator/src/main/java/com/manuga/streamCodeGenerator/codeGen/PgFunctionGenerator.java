/**
 * 
 */
package com.manuga.streamCodeGenerator.codeGen;

import java.util.List;


import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseField;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseKey;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseModel;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabasePath;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseSchema;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseTable;

import static com.manuga.streamCodeGenerator.helpers.helper.GenericHelper.join;
import static com.manuga.streamCodeGenerator.helpers.helper.GenericHelper.resourceToString;
import static com.manuga.streamCodeGenerator.helpers.helper.GenericHelper.string2fileOverWrite;

/**
 * @author x
 *
 */
public class PgFunctionGenerator implements IDatabaseFunctionGenerator{
	private DatabaseModel dbModel;
	private boolean validationIncluded;
	public DatabaseModel getDbModel() {
		return dbModel;
	}

	public void setDbModel(DatabaseModel dbModel) {
		this.dbModel = dbModel;
	}
	public PgFunctionGenerator(DatabaseModel dbModel,boolean validationIncluded){
		setDbModel(dbModel);
		setValidationIncluded(validationIncluded);
	}
	public String fields2paramList(List<DatabaseField> fields) throws Exception{
		if (fields == null | fields.isEmpty()) throw new Exception("Cannot generate parameters for empty field list!");
		
		StringBuilder code = new StringBuilder();
		int i = 0;
		for (DatabaseField field : fields) {
			if (!field.isAutoincrement()){
				if (i>0) code.append(",");
				code.append(" p_")
				.append(field.getFieldName())
				.append(" ")
				.append(field.getFieldType());
			}
			i++;
		}
		
		return code.toString();
	}
	public String fields2paramNamesList(List<DatabaseField> fields) throws Exception{
		if (fields == null | fields.isEmpty()) throw new Exception("Cannot generate parameters for empty field list!");
		
		StringBuilder code = new StringBuilder();
		int i = 0;
		for (DatabaseField field : fields) {
			if (!field.isAutoincrement()){
				if (i>0) code.append(",");
				code.append(" p_")
				.append(field.getFieldName());
			}
			i++;
		}
		
		return code.toString();
	}
	public String tableToInsert(DatabaseTable dbTable) throws Exception{
		DatabaseKey pKey = dbTable.getPrimaryKey();
		if (pKey == null || pKey.getFields().size()<1) throw new Exception("Cannot create delete function for table without primary key!");
		String template = resourceToString("templates/PgInsertFunction.sql");
		String params = fields2paramList(dbTable.getFields());
		String paramNames = fields2paramNamesList(dbTable.getFields());
		String fieldNames = join(dbTable.getFieldNames(), ",");
		template=template
			.replace("#params#", params)
			.replace("#paramNames#", paramNames)
			.replace("#fieldNames#", fieldNames);
		return template;
	}
	public String schemaToInsert(DatabaseSchema dbSchema) throws Exception{
		List<DatabaseTable> tables = dbSchema.getTables();
		StringBuilder code = new StringBuilder();
		for (DatabaseTable table : tables) {
			code.append(tableToInsert(table));
		}
		return code.toString();
	}
	public String schemaToUpdate(DatabaseSchema dbSchema){
		List<DatabaseTable> tables = dbSchema.getTables();
		StringBuilder code = new StringBuilder();
		for (DatabaseTable table : tables) {
			code.append(tableToUpdate(table));
		}
		return code.toString();
	}
	public String schemaToDelete(DatabaseSchema dbSchema) throws Exception{
		List<DatabaseTable> tables = dbSchema.getTables();
		StringBuilder code = new StringBuilder();
		for (DatabaseTable table : tables) {
			code.append(tableToDelete(table));
		}
		return code.toString();
	}
	public String tableToUpdate(DatabaseTable dbTable){
		return null ; //TODO;
	}
	public String tableToSwitchFields(DatabaseTable dbTable) throws Exception{
		DatabaseKey pKey = dbTable.getPrimaryKey();
		if (pKey == null || pKey.getFields().size()<1) throw new Exception("Cannot create delete function for table without primary key!");
		
			List<DatabaseField> pkeyAttributes = dbTable.getPrimaryKey().getFields();
			
			String template = resourceToString("templates/PgSwitchFunction.sql");
			template=template.replace("#functionName#", dbTable.getName())
					.replace("#tableName#", dbTable.getName());
			
			int i =0;
			StringBuilder params = new StringBuilder();
			StringBuilder whereStatements= new StringBuilder();
			whereStatements.append("(");
				for (DatabaseField attribute : pkeyAttributes) {
					if (i>0) { 
						params.append(" , ");
						whereStatements.append(" AND ");
					}
					
					String paramName = "p_"+attribute.getFieldName();
					params.append(paramName);
					params.append(" "+attribute.getFieldType());
					
					whereStatements.append(" ( ");
					whereStatements
							.append(paramName)
							.append(" is null ")
								.append(" OR ")
							.append(attribute.getFieldName())
							.append("=")
							.append(paramName);
					
					whereStatements.append(" ) ");
					i++;
				}
			whereStatements.append(")");
			
			template=template.replace("#params#", params.toString());
			template=template.replace("#whereStatements#", whereStatements.toString());
			
			
			StringBuilder switchCode = new StringBuilder();
			for (DatabaseField field : dbTable.getFields()) {
				if (field.getGenericDataType().equalsIgnoreCase("boolean")){
					if (!(field.isInPrimaryKey() && pkeyAttributes.size()==1)){
						switchCode.append(template.toString().replace("#field#", field.getFieldName()));
					}
				}
			}
		return switchCode.toString();
	}
	public String tableToDelete(DatabaseTable dbTable) throws Exception{
		DatabaseKey pKey = dbTable.getPrimaryKey();
		if (pKey == null || pKey.getFields().size()<1) throw new Exception("Cannot create delete function for table without primary key!");
		
			List<DatabaseField> pkeyAttributes = dbTable.getPrimaryKey().getFields();
			
			String code = resourceToString("templates/PgDeleteFunction.sql");
			code=code.replace("#functionName#", dbTable.getName())
					.replace("#tableName#", dbTable.getName());
			
			int i =0;
			StringBuilder params = new StringBuilder();
			StringBuilder whereStatements= new StringBuilder();
			whereStatements.append("(");
				for (DatabaseField attribute : pkeyAttributes) {
					if (i>0) { 
						params.append(" , ");
						whereStatements.append(" AND ");
					}
					
					String paramName = "p_"+attribute.getFieldName();
					params.append(paramName);
					params.append(" "+attribute.getFieldType());
					
					whereStatements.append(" ( ");
					whereStatements
							.append(paramName)
							.append(" is null ")
								.append(" OR ")
							.append(attribute.getFieldName())
							.append("=")
							.append(paramName);
					
					whereStatements.append(" ) ");
					i++;
				}
			whereStatements.append(")");
			
			code=code.replace("#params#", params.toString());
			code=code.replace("#whereStatements#", whereStatements.toString());
		return code.toString();
	}
	public String pathToUpdate(DatabasePath dbPath) throws Exception{
		if (dbPath.table+dbPath.schema == null || (dbPath.table+dbPath.schema).equals("")) throw new Exception("Update functions cannot be created without proper path!");
		if (!dbPath.table.equals("")){
			DatabaseTable table = dbModel.getTableFromPath(dbPath);
			return tableToUpdate(table);
		}
		if (!dbPath.schema.equals("")){
			DatabaseSchema dbSchema =  dbModel.getSchemaFromPath(dbPath);
			return schemaToUpdate(dbSchema);
		}
		throw new Exception("Something went wrong in update functions generation!");
	}	
	public String pathToDelete(DatabasePath dbPath) throws Exception{
		if (dbPath.table+dbPath.schema == null || (dbPath.table+dbPath.schema).equals("")) throw new Exception("Delete functions cannot be created without proper path!");
		if (!dbPath.table.equals("")){
			DatabaseTable table = dbModel.getTableFromPath(dbPath);
			return tableToDelete(table);
		}
		if (!dbPath.schema.equals("")){ //negacija naknadno dodana
			DatabaseSchema dbSchema =  dbModel.getSchemaFromPath(dbPath);
			return schemaToDelete(dbSchema);
		}
		throw new Exception("Something went wrong in delete functions generation!");
	}
	public String pathToInsert(DatabasePath dbPath) throws Exception{
		if (dbPath.table+dbPath.schema == null || (dbPath.table+dbPath.schema).equals("")) throw new Exception("Insert funcions cannot be created without proper path!");
		if (!dbPath.table.equals("")){
			DatabaseTable table = dbModel.getTableFromPath(dbPath);
			return tableToInsert(table);
		}
		if (!dbPath.schema.equals("")){
			DatabaseSchema dbSchema =  dbModel.getSchemaFromPath(dbPath); //TODO provjerit ovo jer kako ce trazit semu iz staze ako je sema prazna? mislin daj enegacija prije ipak potrebna
			return schemaToInsert(dbSchema);
		}
		throw new Exception("Something went wrong in insert generation!");
	}
	public String ToInsert() throws Exception{
		if (dbModel == null) throw new Exception("Model not set!");
		List<DatabaseSchema> schemas =  dbModel.getSchemas();
		StringBuilder code = new StringBuilder();
		for (DatabaseSchema schema : schemas) 
		{
			DatabasePath schemaPath = new DatabasePath();
			schemaPath.schema=schema.getSchemaName();
			code.append(pathToInsert(schemaPath));
		} 
		return code.toString();
	}
	public String ToDelete() throws Exception{
		if (dbModel == null) throw new Exception("Model not set!");
		List<DatabaseSchema> schemas =  dbModel.getSchemas();
		StringBuilder code = new StringBuilder();
		for (DatabaseSchema schema : schemas) 
		{
			DatabasePath schemaPath = new DatabasePath();
			schemaPath.schema=schema.getSchemaName();
			code.append(pathToDelete(schemaPath));
		} 
		return code.toString();
	}
	public String ToUpdate() throws Exception{
		if (dbModel == null) throw new Exception("Model not set!");
		List<DatabaseSchema> schemas =  dbModel.getSchemas();
		StringBuilder code = new StringBuilder();
		for (DatabaseSchema schema : schemas) 
		{
			DatabasePath schemaPath = new DatabasePath();
			schemaPath.schema=schema.getSchemaName();
			code.append(pathToUpdate(schemaPath));
		} 
		return code.toString();
	}
	public void writeToFile(String pgSqlFilePath) throws Exception{
		StringBuilder code = new StringBuilder();
		code.append(ToInsert());
		code.append(ToUpdate());
		code.append(ToDelete());
		code.append(ToSwitch());

        string2fileOverWrite(code.toString(), pgSqlFilePath);
	}

	public boolean isValidationIncluded() {
		return validationIncluded;
	}

	public void setValidationIncluded(boolean validationIncluded) {
		this.validationIncluded = validationIncluded;
	}
 
	@Override
	public String ToSwitch() throws Exception {
		if (dbModel == null) throw new Exception("Model not set!");
		List<DatabaseSchema> schemas =  dbModel.getSchemas();
		StringBuilder code = new StringBuilder();
		for (DatabaseSchema schema : schemas) 
		{
			DatabasePath schemaPath = new DatabasePath();
			schemaPath.schema=schema.getSchemaName();
			code.append(pathToSwitch(schemaPath));
		} 
		return code.toString();
	}

	@Override
	public String pathToSwitch(DatabasePath dbPath) throws Exception {
		if (dbPath.table+dbPath.schema == null || (dbPath.table+dbPath.schema).equals("")) throw new Exception("Switch functions cannot be created without proper path!");
		if (!dbPath.table.equals("")){
			DatabaseTable table = dbModel.getTableFromPath(dbPath);
			return tableToSwitchFields(table);
		}
		if (!dbPath.schema.equals("")){
			DatabaseSchema dbSchema =  dbModel.getSchemaFromPath(dbPath);
			return schemaToSwitchFields(dbSchema);
		}
		throw new Exception("Something went wrong in switch functions generation!");
	}

	@Override
	public String schemaToSwitchFields(DatabaseSchema dbSchema)
			throws Exception {
		List<DatabaseTable> tables = dbSchema.getTables();
		StringBuilder code = new StringBuilder();
		for (DatabaseTable table : tables) {
			code.append(tableToSwitchFields(table));
		}
		return code.toString();
	}


}

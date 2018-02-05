package com.manuga.streamCodeGenerator.codeGen;

import java.io.File;
import java.util.List;

import com.manuga.streamCodeGenerator.helpers.helper.GenericHelper;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabasePath;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseSchema;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseTable;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseView;
import com.manuga.streamCodeGenerator.helpers.GeneratorBuilder;

public class PhpGeneratorOld4Pg extends GeneratorFromDbModel {
	private String outputPath;
	public void generateDirectories() throws Exception {
		if (outputPath == null || outputPath.isEmpty()) throw new Exception("Output directory not set!");
		if (!GenericHelper.fileOrDirectoryExists(this.outputPath)) new File(outputPath).mkdir();
		for (DatabaseSchema sch : this.getDbModel().getSchemas()) {
			new File(outputPath+ "/"+sch.getSchemaName()).mkdir();
			for (DatabaseTable table : sch.getTables()){
				String schName  =  sch.getSchemaName();
				String tableName = table.getName();
				String directoryPath = outputPath+ "/"+schName+"/"+tableName;
				new File(directoryPath).mkdir();
			}
			for (DatabaseView view : sch.getViews()) {
				String schName  =  sch.getSchemaName();
				String viewName = view.getName();
				String directoryPath = outputPath+ "/"+schName+"/"+viewName;
				new File(directoryPath).mkdir();
			}
		}
	}
	public String dbPath2dir(DatabasePath dbPath){
		StringBuilder dir = new StringBuilder();
		dir.append(this.outputPath).append("/")
		.append(dbPath.schema).append("/");
		if (dbPath.table == null || dbPath.table.equals("")){
			dir.append(dbPath.view);
		}else{
			dir.append(dbPath.table);
		}
		return dir.toString();
	}  
	public String generateMainMenu()throws Exception {
		GeneratorBuilder php = new GeneratorBuilder();
		php.append("<?php", 0, 1);
		php.append("$mainMenu=array(", 0, 1);
			int i =0;
			
			for (DatabaseSchema sch : this.getDbModel().getSchemas()) {
				for (DatabaseView view : sch.getViews()) {
					StringBuilder line = new StringBuilder();
					if (i>0) line.append(",");
					line.append("'")
						.append(sch.getSchemaName()).append(".")
						.append(view.getName()).append("_list")
						.append("'=>'")
						.append(view.getName()).append(" list'");
					php.append(line.toString(), 1, 1);						
					i++;
				}
				for (DatabaseTable table : sch.getTables()) {
					StringBuilder line = new StringBuilder();
					if (i>0) line.append(",");
					line.append("'")
						.append(sch.getSchemaName()).append(".")
						.append(table.getName()).append("_list")
						.append("'=>'")
						.append(table.getName()).append(" list'");
					php.append(line.toString(), 1, 1);						
					i++; 
				}
			}
			
		php.append(");", 0, 1);
		php.append("?>", 0, 0);
		return php.getCode();
	}

	public String generateSelectLists() throws Exception{ //sortirat cu po prvom polju koje je tipa string
		GeneratorBuilder php = new GeneratorBuilder();
		php.append("<?php class SqlSelectLists extends dbSql{", 0, 1);
		String delimiter = "\"\n\t\t,";
		for (DatabaseSchema sch : this.getDbModel().getSchemas()) {
			for (DatabaseView view : sch.getViews()) {
				php.append("function "+sch.getSchemaName()+"_"+view.getName()+"_list(){", 1, 1);
					List<String> fieldNames = view.getFieldNames();
					
					String fieldsStr = "\""+GenericHelper.join(fieldNames, delimiter)+"\"\n";
					//String
					//TODO fieldocvima treba dodat navodnike i treba dodat limitaciju
					php.append("$sql= \"select "+fieldsStr, 2, 1);
						php.append(" from "+sch.getSchemaName()+"."+view.getName(), 3, 1);
						php.append(" order by "+fieldsStr+"\";", 3, 1); 
				php.append("}", 1, 1);
			}
			for (DatabaseTable table : sch.getTables()) {
				php.append("function "+sch.getSchemaName()+"_"+table.getName()+"_list(){", 1, 1);
				List<String> fieldNames = table.getFieldNames();
				
				String fieldsStr = "\""+GenericHelper.join(fieldNames, delimiter)+"\"\n";
				//String
				//TODO fieldocvima treba dodat navodnike i treba dodat limitaciju
				php.append("$sql= \"select "+fieldsStr, 2, 1);
					php.append(" from "+sch.getSchemaName()+"."+table.getName(), 3, 1);
					php.append(" order by "+fieldsStr+"\";", 3, 1);
					php.append("return $sql;", 2, 1);
				php.append("}", 1, 1);
			}
		}
		
		php.append("} ?>", 0, 0);
		return php.getCode();
	}
	/*
	 * 	public static String table2sqlInsert(DatabasePath dbPath ,DatabaseTable table){
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		if (dbPath.schema != null && !dbPath.schema.equals("")) sql.append(dbPath.schema).append(".");
		sql.append(table.getName())
			.append("(");
		
		//List<DataField> fields= table.getFields();
		List<String> fieldNAmes = table.getFieldNames();
		
		List<DataField> pKeys = table.getPrimaryKeyAttributes();
		//sta ako nemam pkey??
		if (pKeys==null | pKeys.size()==0 | !pKeys.get(0).isAutoincrement()){
			sql.append('"')
				.append(GenericHelper.join(fieldNAmes, "\",\""))
				.append("\") values ('$")
				.append(GenericHelper.join(fieldNAmes, "','$"));
		}else{ //ovo je znak da imamo primarni kljuc i da je autoinc!
			String pkeyFieldName = pKeys.get(0).getFieldName(); 
			List<String> fieldNAmesNoPkey = table.getFieldNames();
			fieldNAmesNoPkey.remove(pkeyFieldName);
			sql.append('"')
				.append(GenericHelper.join(fieldNAmesNoPkey, "\",\""))
				.append("\") values ('$")
				.append(GenericHelper.join(fieldNAmesNoPkey, "','$"));
		}
		sql.append("');");
		//sql+=") values ()";//TODO napravit da se ugnajzdjuju vridnosti
		return sql.toString();
	}
	
	 * */
	public String generateInserts() throws Exception{ //sortirat cu po prvom polju koje je tipa string
		GeneratorBuilder php = new GeneratorBuilder();
		php.append("<?php class SqlInserts extends dbSql{", 0, 1);
		String delimiter = "\"\n\t\t,";
		for (DatabaseSchema sch : this.getDbModel().getSchemas()) {
			//TODO nije gotovo..treba kod iskoristit odgore
			for (DatabaseTable table : sch.getTables()) {
				php.append("function "+sch.getSchemaName()+"_"+table.getName()+"_insert(){", 1, 1);
				List<String> fieldNames = table.getFieldNames();
				
				String fieldsStr = ",\""+GenericHelper.join(fieldNames, delimiter)+"\"\n";
				//String
				//TODO fieldocvima treba dodat navodnike i treba dodat limitaciju
				php.append("$sql= \" insert into ",2,1);
				php.append(sch.getSchemaName()+"."+table.getName(), 3, 1);
				php.append("("+fieldsStr+") values (", 2, 1);
					
					php.append("\""+fieldsStr+"\");", 3, 1);
					php.append("return $sql;", 2, 1);
				php.append("}", 1, 1);
			}
		}
		
		php.append("} ?>", 0, 0);
		return php.getCode();
	}
	public String generateActions() throws Exception {
		GeneratorBuilder php = new GeneratorBuilder();
		php.append("<?php", 0, 1);
		php.append("switch ($action){", 1, 1);
		for (DatabaseSchema sch : this.getDbModel().getSchemas()) {
			for (DatabaseView view : sch.getViews()) {
				php.append("case '" + view.getName() + "_select':", 2, 2);
				php.append("include_once('./"
						+ sch.getSchemaName()
						+"." + view.getName()
						+ "_select')", 3, 1);
				php.append("break;", 2, 1);				
			}
			//insert, edit, edit_save
			for (DatabaseTable table : sch.getTables()) {
				php.append("case '" + table.getName() + "_insert':", 2, 1);
				php.append("include_once('./"
						+ sch.getSchemaName()
						+"."+ table.getName()
						+ "_insert.php')", 3, 1);
				php.append("break;", 2, 1);
				
				php.append("case '" + table.getName() + "_edit':", 2, 1);
				php.append("include_once('./"
						+ sch.getSchemaName()
						+"."+ table.getName()
						+ "_edit.php')", 3, 1);
				php.append("break;", 2, 1);
				
				php.append("case '" + table.getName() + "_update':", 2, 1);
				php.append("include_once('./"
						+ sch.getSchemaName()
						+"."+ table.getName()
						+ "_update.php')", 3, 1);
				php.append("break;", 2, 1);
				
				php.append("case '" + table.getName() + "_delete':", 2, 1);
				php.append("include_once('./"
						+ sch.getSchemaName()
						+"."+ table.getName()
						+ "_delete.php')", 3, 1);
				php.append("break;", 2, 1);
			}
		}
		php.append("}", 1, 1);
		php.append("?>", 0, 1);
		return php.getCode();
	}

	public void setOutputPath(String outputPath) {
		if (!GenericHelper.fileOrDirectoryExists(outputPath))
			new File(outputPath).mkdir();
		this.outputPath = outputPath;
	}
	public void writeActions() throws Exception{
		String actions = this.generateActions();
		GenericHelper.string2fileOverWrite(actions, this.outputPath+"/actions.php");
	}
	public void writeMainMenu() throws Exception{
		String menu = this.generateMainMenu();
		GenericHelper.string2fileOverWrite(menu, this.outputPath+"/mainMenu.php");
	}
	public void writeSelectListsToDbSql() throws Exception{
		String dbSql = this.generateSelectLists();
		GenericHelper.string2fileOverWrite(dbSql, this.outputPath+"/sqlSelectLists.php");
		//TODO treba dodat append a ne 
	}
	public void writeInsertsToDbSql() throws Exception{
		String dbSql = this.generateInserts();
		GenericHelper.string2fileOverWrite(dbSql, this.outputPath+"/sqlInsert.php");
		//TODO treba dodat append a ne 
	}
	public String getOutputPath() {
		return outputPath;
	}
	public void writeDbSql() throws Exception{
		//throw new Exception("Writting sqlDb not yet implemented!");
	}
	public void writeAll() throws Exception{
		
		this.generateDirectories();
		this.writeActions();
		writeMainMenu();
		writeSelectListsToDbSql();
		writeInsertsToDbSql();
		writeDbSql();
		
	}

    @Override
    public void setPath() {

    }
}

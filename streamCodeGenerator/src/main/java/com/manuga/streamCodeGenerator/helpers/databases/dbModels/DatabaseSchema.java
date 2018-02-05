/**
 * 
 */
package com.manuga.streamCodeGenerator.helpers.databases.dbModels;

import com.manuga.streamCodeGenerator.helpers.helper.GenericHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suncica
 *
 */
public class DatabaseSchema {
	private String schemaName;
	private String comment;
	private String DDL;
	private List<DatabaseTable> tables;
	private List<DatabaseFunction> functions;
	private List<DatabaseView> views;
	private List<DatabaseProcedure> procedures;
    private List<DatabaseSequence> sequences;
	public List<DatabaseTable> getTables() {
		return tables;
	}
	public void setTables(List<DatabaseTable> tables) {
		this.tables = tables;
	}
	public List<DatabaseFunction> getFunctions() {
		return functions;
	}
	public void setFunctions(List<DatabaseFunction> functions) {
		this.functions = functions;
	}
	public List<DatabaseView> getViews() {
		return views;
	}
	public void setViews(List<DatabaseView> views) {
		this.views = views;
	}
	public List<DatabaseProcedure> getProcedures() {
		return procedures;
	}
	public void setProcedures(List<DatabaseProcedure> procedures) {
		this.procedures = procedures;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDDL() {
		return DDL;
	}
	public void setDDL(String DDL) {
		this.DDL = DDL;
	}
	public List<IDatabaseDataObject> getTablesAndViews(){
		List<IDatabaseDataObject> objectList = new ArrayList<IDatabaseDataObject>();
		for (DatabaseTable table : this.getTables()) {
			objectList.add(table);
		}
		for (DatabaseView view : this.getViews()) {
			objectList.add(view);
		}
		return 	objectList;
	}
	public void saveToFile(String xmlFilePath) throws Exception {
        GenericHelper.object2xml(this,xmlFilePath);
	}
	public DatabaseSchema loadFromFile(String xmlFilePath) throws Exception {
        return  (DatabaseSchema) GenericHelper.xml2object(xmlFilePath);
	}

    public List<DatabaseSequence> getSequences() {
        return sequences;
    }

    public void setSequences(List<DatabaseSequence> sequences) {
        this.sequences = sequences;
    }
}

package com.timanaga.streamCodeGenerator.databases.dbModels;

import java.util.List;

public class DatabaseForeignKey extends DatabaseKey {
	private DatabasePath referencingTable;
	private List<DatabaseField> referencingFields;
	public List<DatabaseField> getReferencingFields() {
		return referencingFields;
	}
	public void setReferencingFields(List<DatabaseField> referencingFields) {
		this.referencingFields = referencingFields;
	}
	public DatabasePath getReferencingTablePath() {
		return referencingTable;
	}
	public void setReferencingTablePath(DatabasePath referencingTable) {
		this.referencingTable = referencingTable;
	}
	
}

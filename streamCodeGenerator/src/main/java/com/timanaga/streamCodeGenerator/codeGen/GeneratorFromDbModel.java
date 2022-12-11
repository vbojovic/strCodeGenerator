package com.timanaga.streamCodeGenerator.codeGen;


import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.DatabaseModel;

public abstract class GeneratorFromDbModel implements IDbModelGenerator{
	private DatabaseModel dbModel;

	public void setDbModel(DatabaseModel dbModel) {
		this.dbModel = dbModel;
	}

	public DatabaseModel getDbModel() {
		return dbModel;
	} 
}

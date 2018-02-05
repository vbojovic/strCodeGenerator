package com.manuga.streamCodeGenerator.codeGen;


import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseModel;

public abstract class GeneratorFromDbModel implements IDbModelGenerator{
	private DatabaseModel dbModel;

	public void setDbModel(DatabaseModel dbModel) {
		this.dbModel = dbModel;
	}

	public DatabaseModel getDbModel() {
		return dbModel;
	} 
}

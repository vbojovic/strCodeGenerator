package com.manuga;

import com.manuga.streamCodeGenerator.helpers.databases.dbClasses.IDb;
import com.manuga.streamCodeGenerator.helpers.databases.dbClasses.PgDb;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseModel;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseSettings;
import com.manuga.streamCodeGenerator.codeGen.PhpGeneratorOld4Pg;

import java.io.File;


public class GeneratorsClass {

	/**
	 * @param args
	 * @throws Exception 
	 */
	//private IDb db;
	
	//public void runPhpGeneratorOld(String projectPath) throws Exception{
	public static void runPhpGeneratorOld(String projectPath) throws Exception{
		IDb db;
		db = new PgDb();
		PgDb p = new PgDb();
		DatabaseSettings settings = new DatabaseSettings();
			String configPath = projectPath+"\\dbSettings.xml";
			settings.loadFromFile(configPath);
		db.setSettings(settings);
		//TODO iz nekog razloga settinzi se ne prosljedjuju dalje. to treban popporavit!
		DatabaseModel dbModel = new DatabaseModel();
		dbModel = db.getModel();
		
		
		
		PhpGeneratorOld4Pg phpOldGen = new PhpGeneratorOld4Pg();
		phpOldGen.setDbModel(dbModel);
		phpOldGen.setOutputPath(projectPath+File.separator+"out");
		phpOldGen.writeAll();
	}
	

}


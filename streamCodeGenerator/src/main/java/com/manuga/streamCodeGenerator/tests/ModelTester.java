package com.manuga.streamCodeGenerator.tests; /**
 * 
 */


import com.manuga.streamCodeGenerator.helpers.databases.dbClasses.IDb;
import com.manuga.streamCodeGenerator.helpers.databases.dbClasses.MysqDb;
import com.manuga.streamCodeGenerator.helpers.databases.dbClasses.PgDb;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseModel;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseSchema;
import com.manuga.streamCodeGenerator.helpers.Helper;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseSettings;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseTypeEnum;
import com.manuga.streamCodeGenerator.helpers.helper.GenericHelper;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class ModelTester {
//	@Test
//	public void testInnerModelExport() throws Exception {
//		PgDb db = new PgDb();
//		//db.setSettings(Helper.homeSettings());
//        db.setSettings(Helper.toptalSettings());
//		//db.setSettings(Helper.zoharSettings());
//		db.connect();
//
//		String outPath = "/tmp/map.xml";
//		//String outPath = GenericHelper.getTempDir()+"\\map.xml";
//		DatabaseModel dbm = new DatabaseModel();
////        List<DatabaseSchema> schemas =  db.getElementReader().readSchemas();
//		List<DatabaseSchema> schemas = new ArrayList<DatabaseSchema>();
//		schemas.add(db.getElementReader().readSchema(db.getSettings().getSchema()));
//        dbm.setSchemas(schemas);
//		dbm.exportToXml(outPath);
//        dbm.saveToJson(outPath+".json");
//		db.disconnect();
//		Assert.assertTrue(true);
//	}
	//@Test
	public void testInnerModelImport() throws Exception {
		String importFileName = "C:\\temp\\map.xml";
		DatabaseModel dbm = new DatabaseModel();
		dbm.loadFromXml(importFileName);
	}
//	@Test
	public void testMysqlInnerModelExportAll() throws Exception {
		MysqDb db = new MysqDb();
		//db.setSettings(Helper.homeSettings());
		db.setSettings(Helper.vmSettingsMysql());
		//db.setSettings(Helper.zoharSettings());
		db.connect();

		String outPath = "/tmp/mapMy.xml";
		DatabaseModel dbm = new DatabaseModel();
		List<DatabaseSchema> schemas =  db.getElementReader().readSchemas();
		dbm.setSchemas(schemas);
		dbm.exportToXml(outPath);
		dbm.saveToJson(outPath+".json");
		db.disconnect();
		Assert.assertTrue(true);
	}

//	@Test
	public void testInnerModelExportAll() throws Exception {
		PgDb db = new PgDb();
		//db.setSettings(Helper.homeSettings());
		db.setSettings(Helper.homeSettings());
		//db.setSettings(Helper.zoharSettings());
		db.connect();

		String outPath = "/tmp/map.xml";
		//String outPath = GenericHelper.getTempDir()+"\\map.xml";
		DatabaseModel dbm = new DatabaseModel();
        List<DatabaseSchema> schemas =  db.getElementReader().readSchemas();
		dbm.setSchemas(schemas);
		dbm.exportToXml(outPath);
		dbm.saveToJson(outPath+".json");
		db.disconnect();
		Assert.assertTrue(true);
	}

//	@Test
//	public  void  testRunApp() throws Exception{
//		String login = "toptal";
//		String pass = "toptal";
//		String database = "toptal";
//		String hostname = "localhost";
//		String outFile =  "/tmp/outTest.json";
//		String schema = "public";
//		String databaseType = "pg";
//		int port = 0;
//
//		if (databaseType.equals("pg")) port=5432;
//
//		int followDepth = -1;
//
//		DatabaseSettings settings = new DatabaseSettings();
//
//		settings.setDataBase(database);
//
//		settings.setDataBaseType(DatabaseTypeEnum.postgres);
//
//		settings.setHost(hostname);
//		settings.setLogin(login);
//		settings.setPass(pass);
//		settings.setPort(port);
//		settings.setSchema(schema);
//
//		IDb db = new PgDb();
//
//		db.setSettings(settings);
//		if (db.getConnection()==null || db.getConnection().isClosed()) db.connect();
//
//		DatabaseModel model = db.getModel();
//
//		List<DatabaseSchema> schemas =  db.getElementReader().readSchemas();
//
//		model.setSchemas(schemas);
//
//		model.saveToJson(outFile);
//
//		db.disconnect();
//
//	}
}

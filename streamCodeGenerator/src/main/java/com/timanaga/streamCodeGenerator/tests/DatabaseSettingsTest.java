package com.timanaga.streamCodeGenerator.tests; /**
 * 
 */

import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.DatabaseSettings;
import com.timanaga.streamCodeGenerator.helpers.helper.GenericHelper;
import com.timanaga.streamCodeGenerator.helpers.Helper;
import org.junit.Test;

/**
 * @author x
 *
 */
public class DatabaseSettingsTest {
	//@Test
	public void testDbSettingsSaveAndLoad() throws Exception{
		DatabaseSettings dbs =  Helper.homeSettings();
		dbs.setLogin("aaaaaaaaa");
		String fName = "c:\\temp\\test.xml";
		dbs.saveToFile(fName);
		GenericHelper.object2xmlX(dbs, fName+".1");
		/*
		XMLEncoder encoder = new XMLEncoder(
                new BufferedOutputStream(
	                       new FileOutputStream(fName)));
		encoder.setPersistenceDelegate(dbs.getClass(), encoder.getPersistenceDelegate(DatabaseSettings.class));
		encoder.writeObject(dbs);
		encoder.close();
		*/
		DatabaseSettings dbs2 = new DatabaseSettings();
		dbs2.loadFromFile(fName);
		int i =0 ;
	}
}

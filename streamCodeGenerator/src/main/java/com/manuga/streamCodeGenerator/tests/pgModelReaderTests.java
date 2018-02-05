package com.manuga.streamCodeGenerator.tests; /**
 * 
 */

import com.manuga.streamCodeGenerator.helpers.databases.dbClasses.IDb;
import com.manuga.streamCodeGenerator.helpers.databases.dbClasses.PgDb;
import com.manuga.streamCodeGenerator.helpers.Helper;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author antonioseric
 *
 */
public class pgModelReaderTests {

	//@Test
	public void test() throws Exception {
		IDb db = new PgDb();
		db.setSettings(Helper.homeSettings());
		try {
			db.connect();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Error connecting to databese");
		}
	      db.getElementReader().readSchemasList();
	}

}

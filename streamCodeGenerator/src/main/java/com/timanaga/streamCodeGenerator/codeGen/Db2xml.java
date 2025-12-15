/**
 * 
 */
package com.timanaga.streamCodeGenerator.codeGen;

import java.util.List;

import com.timanaga.streamCodeGenerator.databases.dbClasses.IDb;
import com.timanaga.streamCodeGenerator.databases.dbClasses.MysqlDb;
import com.timanaga.streamCodeGenerator.databases.dbClasses.PgDb;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseTypeEnum;


/**
 * @author bv
 * skripta sluzi da bazu bilokojeg tipa preslika u JSON format 
 */
public class Db2xml {
	private String fullPath;
	private List<String> schemaNames;
	private DatabaseTypeEnum dbType = DatabaseTypeEnum.postgres;
	private IDb dataBase;
	public Db2xml(final String FullPath, final DatabaseTypeEnum dbType) throws Error, Exception {
		this.setFullPath(FullPath);
		/*this.setDbType(dbType);*/
		switch(dbType)
		{
			case postgres:
				dataBase = new PgDb();
				break;
				
			case mysql:
				dataBase = new MysqlDb();
				break;
			/*case ora:
				dataBase = new OraDb();
				*/
		}
	}
	
	
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public List<String> getSchemaNames() {
		return schemaNames;
	}
	public void setSchemaNames(List<String> schemaNames) {
		this.schemaNames = schemaNames;
	}
	
	
 

}

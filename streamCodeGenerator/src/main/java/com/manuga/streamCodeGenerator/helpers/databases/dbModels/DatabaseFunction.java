/**
 * 
 */
package com.manuga.streamCodeGenerator.helpers.databases.dbModels;



/**
 * @author Suncica
 *
 */
public class DatabaseFunction extends DatabaseProcedure{

	private DatabaseTypeEnum returnValue;
	public DatabaseTypeEnum getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(DatabaseTypeEnum returnValue) {
		this.returnValue = returnValue;
	}

}

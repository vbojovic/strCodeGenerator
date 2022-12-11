/**
 * 
 */
package com.timanaga.streamCodeGenerator.helpers.databases.dbModels;


/**
 * @author Suncica
 *
 */
public class DatabaseView extends ADatabaseDataObject{
	private boolean isMaterialized;

	public boolean isMaterialized() {
		return isMaterialized;
	}
	public void setMaterialized(boolean isMaterialized) {
		this.isMaterialized = isMaterialized;
	} 
	public void saveToFile(String xmlFilePath){
		//TODO
	}
	public DatabaseView loadFromFile(String xmlFilePath){
		return null;
		//TODO
	} 
}

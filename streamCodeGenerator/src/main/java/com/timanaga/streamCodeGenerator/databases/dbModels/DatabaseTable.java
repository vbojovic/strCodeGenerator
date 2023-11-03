/**
 * 
 */
package com.timanaga.streamCodeGenerator.databases.dbModels;


import com.timanaga.streamCodeGenerator.helpers.helper.GenericHelper;

import java.util.List;

/**
 * @author Suncica
 *
 */
public class DatabaseTable extends ADatabaseDataObject{
	private boolean isRecursive = false;
	private DatabasePath parentPath;

	public DatabasePath getParentPath() {
		return parentPath;
	}

	public void setParentPath(DatabasePath parentPath) {
		this.parentPath = parentPath;
	}
	
	public void saveToFile(String xmlFilePath) throws Exception {
        GenericHelper.object2xml(this, xmlFilePath);
	}

	public DatabaseTable loadFromFile(String xmlFilePath) throws Exception {
		return (DatabaseTable) GenericHelper.xml2object(xmlFilePath);
	}

    public void saveToJSONFile(String jsonFilePath) throws Exception {
        GenericHelper.object2JsonFile(this,jsonFilePath);
    }

    public DatabaseTable loadFromJSONFile(String jsonFilePath) throws Exception {
        return (DatabaseTable) GenericHelper.jsonFile2object(jsonFilePath);
    }


    public boolean isRecursive() {
        if (isRecursive == true) return isRecursive;
        List<DatabaseForeignKey> fKeys= this.getForeignKeys();
        if (fKeys==null || fKeys.size()==0) return false;
        for (DatabaseForeignKey fKey : fKeys){
            DatabasePath rTblPath = fKey.getReferencingTablePath();
            if (rTblPath.schema.equalsIgnoreCase(parentPath.schema)
                && rTblPath.table.equalsIgnoreCase(this.getName())) {
                this.setRecursive(true);
                return true;
            }
        }


        return isRecursive;
    }

    public void setRecursive(boolean recursive) {
        isRecursive = recursive;
    }
}

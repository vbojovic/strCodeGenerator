/**
 * 
 */
package com.manuga.streamCodeGenerator.helpers.databases.dbModels;

import java.util.List;

/**
 * @author x
 *
 */
public class DatabaseKey extends ADatabaseFieldsSet{ //key can be primary/unique and foreign(other case)
	private boolean isPrimary = false;
    private boolean isForeign  = false;
	public boolean isPrimary() {
		return isPrimary;
	}
	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	public boolean isForeign(){
		return ! isPrimary();
	}
	public boolean isUnique() {
		return (isPrimary) ? true: this.getIsUnique();
	}
	public boolean containsAutoincremenField() throws Exception{
		List<DatabaseField> fields = this.getFields();
		for (DatabaseField field : fields) {
			if (field.isAutoincrement() == true ) return true;
		}
		return false;
	}
	                 /*
	public boolean containsTimestampWithDefaultFunc() throws Exception{
		List<DatabaseField> fields = this.getFields();
		for (DatabaseField field : fields) 
			if (field.isTimestampWithDefaultFunc() == true ) return true;
		return false;
	}
                       */
    public void setForeign(boolean foreign) {
        isForeign = foreign;
    }
}

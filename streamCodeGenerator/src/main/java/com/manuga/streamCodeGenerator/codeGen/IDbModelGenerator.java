package com.manuga.streamCodeGenerator.codeGen;

import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseModel;

/**
 * Created by x on 6/16/2014.
 */
public interface IDbModelGenerator {
    public void setDbModel(DatabaseModel dbModel) ;
    public DatabaseModel getDbModel();
    public void setPath();
}

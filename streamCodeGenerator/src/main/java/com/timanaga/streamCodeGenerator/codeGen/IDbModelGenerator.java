package com.timanaga.streamCodeGenerator.codeGen;

import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.DatabaseModel;

/**
 * Created by x on 6/16/2014.
 */
public interface IDbModelGenerator {
    public void setDbModel(DatabaseModel dbModel) ;
    public DatabaseModel getDbModel();
    public void setPath();
}

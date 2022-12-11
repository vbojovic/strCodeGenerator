package com.timanaga.streamCodeGenerator.helpers.databases.dbModels;

public class DatabasePath implements Cloneable {
	public String table;
	public String database;
	public String schema;
	public String view;
	public String function;
	public String procedure;
	public String sequence;
    public String field;
    public String index;
    public DatabasePath(){super();}
    public DatabasePath(String schema,String table){
        super();
        this.schema=schema;
        this.table=table;
    }
    public DatabasePath(String schema,String table, String field){
        super();
        this.schema=schema;
        this.table=table;
        this.field = field;
    }

    public DatabasePath duplicate()throws Exception {
        return (DatabasePath)clone();
    }
}

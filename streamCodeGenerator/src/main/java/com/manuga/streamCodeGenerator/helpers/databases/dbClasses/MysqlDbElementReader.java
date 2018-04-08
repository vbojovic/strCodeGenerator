package com.manuga.streamCodeGenerator.helpers.databases.dbClasses;

import com.manuga.streamCodeGenerator.helpers.databases.DataFormatConverter;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.*;
import com.manuga.streamCodeGenerator.helpers.helper.GenericHelper;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlDbElementReader extends ADbElemetReader implements IDbElementReader {
    private static Logger logger = Logger.getLogger(MysqlDbElementReader.class);

	/* Constructor */
	public MysqlDbElementReader(MysqDb dataBase) throws Exception {
		m_DataBase = dataBase;
        loadDataTypes();
	}
    private void loadDataTypes() throws Exception {
         this.converter = new DataFormatConverter("/mysqlDataTypes.txt");
    }



    /* (non-Javadoc)
         * @see com.streamCodeGenerator.codeGen.IDbElementReader#ReadFromDataBase()
         */
	@Override
	public void ReadFromDataBase() throws Exception {
		//Read Schemas
		List<DatabaseSchema> schemas = new ArrayList<DatabaseSchema>();
		schemas = readSchemas();
	}

    private boolean isFieldInPrimaryKey(final String schemaName,final String relName, final String field) throws Exception {
        try {
            List<String> pkeyKeys = getPkeyFields(schemaName,relName);
            return pkeyKeys.contains(field);
        }catch (Exception e){
            return false;
        }
    }
	
	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getSchemas()
	 */

    @Override
    public List<DatabaseSchema> readTargetSchemas() throws Exception {
        Statement sql;
        if (this.getTargetSchemas()==null || this.getTargetSchemas().size()==0) return readSchemas();

        ResultSet results = null;
        List<DatabaseSchema> schemas = new ArrayList<DatabaseSchema>();

        //sql = m_DataBase.getConnection().createStatement();
        for (String schemaName : getTargetSchemas()){
            DatabaseSchema sch = new DatabaseSchema();
            sch.setSchemaName(schemaName);
            sch.setDDL("N/A");
            DatabasePath dbPath = new DatabasePath();
            dbPath.schema = sch.getSchemaName();
            sch.setComment(this.readComment(dbPath));

            sch.setTables(this.readTables(dbPath));
            sch.setProcedures(this.readProcedures(dbPath));

            sch.setViews(this.readViews(dbPath));

            sch.setFunctions(this.readFunctions(dbPath));
            sch.setSequences(this.readSequences(dbPath));
            schemas.add(sch);

        }

        return schemas;
    }
	@Override
	public List<DatabaseSchema> readSchemas() throws Exception {
		Statement sql;
		String sqlTxt = "SELECT s.schema_name as `schema`\n" +
                "FROM INFORMATION_SCHEMA.schemata s " +
                "WHERE s.schema_name not in ('information_schema','mysql','performance_schema') "+
                "order by s.schema_name ";
        ResultSet results = null;
		List<DatabaseSchema> schemas = new ArrayList<DatabaseSchema>();

		sql = m_DataBase.getConnection().createStatement();
		results = sql.executeQuery(sqlTxt);
		if (results != null)
			while (results.next()) {
				DatabaseSchema sch = new DatabaseSchema();
				sch.setSchemaName(results.getString("schema"));
				sch.setDDL("N/A");
				DatabasePath dbPath = new DatabasePath();
				dbPath.schema = sch.getSchemaName();
                sch.setComment(this.readComment(dbPath));
//System.out.println(dbPath.schema);

                sch.setTables(this.readTables(dbPath));
                sch.setProcedures(this.readProcedures(dbPath));

                sch.setViews(this.readViews(dbPath));

                sch.setFunctions(this.readFunctions(dbPath));
                sch.setSequences(this.readSequences(dbPath));
				schemas.add(sch);
			}
		return schemas;
	}
    public  DatabaseSchema readSchema(final String schemaName) throws Exception {
        //TODO nadopunit sve tipove koji fale
        DatabaseSchema sch = new DatabaseSchema();
        sch.setSchemaName(schemaName);
        sch.setDDL("N/A");

        DatabasePath dbPath = new DatabasePath();
        dbPath.schema = sch.getSchemaName();
        sch.setComment(this.readComment(dbPath));
//System.out.println(dbPath.schema);

        sch.setTables(this.readTables(dbPath));
        sch.setProcedures(this.readProcedures(dbPath));

        sch.setViews(this.readViews(dbPath));

        sch.setFunctions(this.readFunctions(dbPath));
        sch.setSequences(this.readSequences(dbPath));

        return sch;
    }
	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getSchemasList()
	 */
	@Override
	public List<String> readSchemasList() {
		Statement sql;
        String sqlTxt = "SELECT s.schema_name as schema\n" +
                "FROM INFORMATION_SCHEMA.schemata s " +
                "WHERE s.schema_name not in ('information_schema','mysql','performance_schema') "+
                "order by s.schema_name ";
		ResultSet results = null;
		List<String> schemas = new ArrayList<String>();

		try {
			sql = m_DataBase.getConnection().createStatement();
			results = sql.executeQuery(sqlTxt);
			if (results != null)
				while (results.next())
					schemas.add(results.getString("schema"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return schemas;
	}
	
	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getComment(com.streamCodeGenerator.dbModels.DatabasePath)
	 */
	public String readComment(DatabasePath dbPath) throws Exception {
        String sql = "";
        String schema = (dbPath.schema.isEmpty())? "public" : dbPath.schema;
        if ( dbPath.field != null){
            if (dbPath.table==null && dbPath.view==null) throw new Exception("No table/view given");
            sql=String.format("N/A");
        }else if (dbPath.table!=null){
            sql=String.format(
                    "SELECT table_comment as description \n" +
                    "    FROM INFORMATION_SCHEMA.TABLES \n" +
                    "    WHERE table_schema='%s' \n" +
                    "        AND table_name='%s';\n", schema , dbPath.table);
        }else if (dbPath.view != null){
            sql=String.format(
                    "SELECT table_comment as description \n" +
                            "    FROM INFORMATION_SCHEMA.TABLES \n" +
                            "    WHERE table_schema='%s' \n" +
                            "        AND table_name='%s';\n", schema , dbPath.table);
        }  else if (dbPath.function != null){
            sql=String.format("SELECT 'N/A' as description");
        }  else if (dbPath.sequence != null){
            sql=String.format("SELECT 'N/A' as description");
        }  else if (dbPath.schema != null){
            sql=String.format("SELECT 'N/A' as description");
        } else if (dbPath.database != null){
            sql=String.format("SELECT 'N/A' as description");
        }
        try {
		    return m_DataBase.sql2string(sql);
        } catch (Exception e){
            return "";
        }
	}

	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getKeys(com.streamCodeGenerator.dbModels.DatabasePath)
	 */
	@Override
	public List<DatabaseKey> readKeys(DatabasePath dbPath) throws Exception {
        List<DatabaseKey> keys = new ArrayList<DatabaseKey>();

        String sql = String.format(
            " select distinct conname,contype  \n" +
            " from\n" +
            " pg_constraint  c\n" +
            " inner join pg_namespace n\n" +
            " on n.oid = c.connamespace\n" +
            " inner join pg_class r\n" +
            " on r.oid = c.conrelid" +
            " where nspname ~ '%s'"    , dbPath.schema);

        if (dbPath.table!=null) sql=sql+String.format(" and relname = '%s'", dbPath.table);
        ResultSet rows;
        try{
            rows = m_DataBase.sql2resultSet(sql);
        } catch (Exception e){
            return keys;
        }
        while (rows.next()){
            String key = rows.getString("conname");
            String type = rows.getString("contype");
            DatabaseKey dbKey = new DatabaseKey();
            dbKey.setName(key);

            List<String> keyFields = new ArrayList<String>();
            dbKey.setPrimary(false);
            dbKey.setIsUnique(false);
            dbKey.setForeign(false);

            if  (type.equalsIgnoreCase("p"))   {
                keyFields =  getPkeyFields(dbPath.schema,dbPath.table);
                dbKey.setPrimary(true);
                dbKey.setIsUnique(true);
            }
            if  (type.equalsIgnoreCase("u"))   {
                DatabasePath path = (DatabasePath)dbPath.duplicate();
                path.index = key;
                keyFields = getIndexFields(path);
                dbKey.setIsUnique(true);
            }
            if  (type.equalsIgnoreCase("f"))   {
                keyFields =  getForeignKeysFields(dbPath.schema, key, false);
                dbKey.setForeign(true);
            }
            for (String field : keyFields){
                DatabaseField f = this.getFieldData(dbPath.schema,dbPath.table,field);
                dbKey.getFields().add(f);
            }
            for (String keyField : keyFields){
                DatabaseField fieldData = getFieldData(dbPath.schema,dbPath.table,keyField);
                dbKey.getFields().add(fieldData)  ;
            }
            keys.add(dbKey);
        }

        return keys;
	}

	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getForeignKeys(com.streamCodeGenerator.dbModels.DatabasePath)
	 */
	@Override
	public List<DatabaseForeignKey> readForeignKeys(DatabasePath dbPath)
			throws Exception {
        if (dbPath.schema.isEmpty() || dbPath.table.isEmpty()) throw  new Exception("Path not complete");
        List<String> fKeyNames =  this.getForeignKeys(dbPath);
        List<DatabaseForeignKey> fKeys = new ArrayList<DatabaseForeignKey>();
        for (String fkeyName : fKeyNames){
            DatabaseForeignKey fkey = new DatabaseForeignKey();
            fkey.setName(fkeyName);
            fkey.setPrimary(this.isConstraintPkey(dbPath.schema,fkeyName));


            if (!fkey.isPrimary()){
                fkey.setForeign(true);
                    fkey.setReferencingFields(getSourceFieldNames(dbPath.schema, fkeyName));
                    //TODO ovdi san sta. treban punit polja referncna i indeksna
                    String srcTable = this.getForeignKeySourceTableName(dbPath.schema, fkeyName);
                    //System.out.println(dbPath.schema+"\t"+fkeyName);
                    String srcShema = this.getSrcSchemaFromFkey(dbPath.schema, fkeyName);

                    fkey.setReferencingTablePath(new DatabasePath(srcShema, srcTable));
            }
            fkey.setFields(this.getDestFieldNames(dbPath.schema, dbPath.table,fkeyName));

            fKeys.add(fkey);
        }
		return  fKeys;
	}

    private List<DatabaseField> getSourceFieldNames(String destSchema, String fkeyName) throws Exception {
        List<String> fieldNames = this.getForeignKeysFields(destSchema,fkeyName,true);
        List<DatabaseField> fields = new ArrayList<DatabaseField>();
        for (String fieldName : fieldNames){
            String table = getForeignKeySourceTableName(destSchema,fkeyName);
            DatabaseField field = this.getFieldData(destSchema,table,fieldName);
            fields.add(field);
        }
        return fields;
    }
    private List<DatabaseField> getDestFieldNames(String schema, String table, String fkeyName) throws Exception {
	    String sql = String.format(" SELECT REFERENCED_COLUMN_NAME\n" +
            "FROM\n" +
            "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n" +
            "WHERE\n" +
                "  REFERENCED_TABLE_SCHEMA = '%s' AND\n" +
                "  REFERENCED_TABLE = '%s' AND\n" +
            "  CONSTRAINT_NAME = '%s' " , schema,table,fkeyName);
        ResultSet rows;
        List<DatabaseField> fields = new ArrayList<DatabaseField>();;
        try{
            rows = m_DataBase.sql2resultSet(sql);
        } catch (Exception e){
            return fields;
        }
        while (rows.next()) {
            String fieldName = rows.getString("REFERENCED_COLUMN_NAME");
            DatabaseField field = this.getFieldData(schema,table,fieldName);
            fields.add(field);
        }
        return fields;
    }

	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getIndexes(com.streamCodeGenerator.dbModels.DatabasePath)
	 */
	@Override
	public List<DatabaseIndex> readIndexes(DatabasePath dbPath) throws Exception {
        final List<String> indexNames = getIndices(dbPath.schema,dbPath.table);
        List<DatabaseIndex> indices = new ArrayList<DatabaseIndex>();
        for (final String ndxName : indexNames){
//            final DatabaseIndex ndx = getIndex(dbPath.schema,dbPath.table,ndxName);
            DatabasePath path = (DatabasePath)dbPath.duplicate();
            path.index = ndxName;
            final DatabaseIndex ndx = getIndex(path);
            indices.add(ndx);
        }
		return indices;
	}

	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getFields(com.streamCodeGenerator.dbModels.DatabasePath)
	 */
	@Override
	public List<DatabaseField> readFields(DatabasePath dbPath) throws Exception { // TODO
		if (dbPath.schema == null || dbPath.schema == "" ) throw new Exception("Cannot get fields, schema not set!");
		if (dbPath.view == null && dbPath.table == null ) throw new Exception("Cannot get fields, table or view not set!");
		
		List<DatabaseField> fields = new ArrayList<DatabaseField>();
		
		StringBuilder sqlTxt = new StringBuilder();
		sqlTxt.append(
				"select  TABLE_CATALOG\n" +
                        ",TABLE_SCHEMA \n" +
                        ",TABLE_NAME\n" +
                        ",COLUMN_NAME \n" +
                        ",ORDINAL_POSITION\n" +
                        ",COLUMN_DEFAULT\n" +
                        ",IS_NULLABLE\n" +
                        ",DATA_TYPE\n" +
                        ",CHARACTER_MAXIMUM_LENGTH\n" +
                        ",CHARACTER_OCTET_LENGTH\n" +
                        ",NUMERIC_PRECISION\n" +
                        ",NUMERIC_SCALE\n" +
                        ",DATETIME_PRECISION\n" +
                        ",CHARACTER_SET_NAME\n" +
                        ",COLLATION_NAME\n" +
                        ",COLUMN_TYPE\n" +
                        ",COLUMN_KEY\n" +
                        ",EXTRA\n" +
                        ",PRIVILEGES\n" +
                        ",COLUMN_COMMENT ")
				.append("from INFORMATION_SCHEMA.COLUMNS ")
				.append("where table_schema = '" + dbPath.schema + "' ")
				.append("and table_name='" + dbPath.table + "'");
        if (dbPath.field != null)
            sqlTxt.append(" and column_name='"+dbPath.field+"'");
		ResultSet fieldsRes = m_DataBase.sql2resultSet(sqlTxt.toString());
		if (fieldsRes != null)
			while (fieldsRes.next()) {
				DatabaseField field =  this.getFieldData(
                        dbPath.schema
                        ,(dbPath.table != null)?dbPath.table : dbPath.view
                        ,fieldsRes.getString("column_name"));

				fields.add(field);
			}

		return fields;
	}
	

	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getTableNames(java.lang.String)
	 */
	@Override
	public List<String> readTableNames(String schema) throws Exception {
		if (schema.equals(""))  schema = "mysql";
		String sqlTxt = "select table_name as `tablename`  " +
                "from INFORMATION_SCHEMA.tables  " +
                "where TABLE_SCHEMA = '"+ schema + "' " +
                "order by tablename ";
		List<String> tableNames = new ArrayList<String>();
		ResultSet tablesResult = m_DataBase.sql2resultSet(sqlTxt);
		tableNames = GenericHelper.resultSetToList(tablesResult);
		return tableNames;
	}
	
	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getViews(com.streamCodeGenerator.dbModels.DatabasePath)
	 */
	@Override
	public List<DatabaseView> readViews(DatabasePath dbPath) throws Exception {
		String sqlTxt =String.format( "select TABLE_NAME , VIEW_DEFINITION " +
                "from information_schema.views " +
                "where TABLE_SCHEMA = '%s' order by TABLE_NAME ",dbPath.schema);
		List<DatabaseView> views = new ArrayList<DatabaseView>();
		ResultSet tablesResult = m_DataBase.sql2resultSet(sqlTxt);
		if (tablesResult != null)
			while (tablesResult.next()){
				//r.add(tablesResult.getString(1));
				DatabaseView view = new DatabaseView();
				view.setDDL(tablesResult.getString("VIEW_DEFINITION"));
				view.setName(tablesResult.getString("TABLE_NAME"));
				DatabasePath tablePath = dbPath;
				tablePath.view = view.getName();
				view.setFields(this.readFields(dbPath));  
				views.add(view);
			}
		return views;
	}
	
	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getTables(com.streamCodeGenerator.dbModels.DatabasePath)
	 */
    public DatabaseKey getPrimaryKey(DatabasePath dbPath) throws Exception {
        if (dbPath.table==null) throw  new Exception("No table given");
        for (DatabaseKey key : this.readKeys(dbPath)){
            if (key.isPrimary()) return key;
        }
        return null;
    }
	@Override
	public List<DatabaseTable> readTables(DatabasePath dbPath) throws Exception {
		String sqlTxt = "select table_name as tablename  " +
                "from INFORMATION_SCHEMA.tables  " +
                "where TABLE_SCHEMA = '"+dbPath.schema + "' " +
                "order by tablename ";
		List<DatabaseTable> tables = new ArrayList<DatabaseTable>();
		ResultSet tablesResult = m_DataBase.sql2resultSet(sqlTxt);
		if (tablesResult != null)
			while (tablesResult.next()){
				//r.add(tablesResult.getString(1));
				DatabaseTable table = new DatabaseTable();
				table.setDDL("table DDL not set");
				table.setName(tablesResult.getString("tablename"));
				DatabasePath tablePath = dbPath;
				tablePath.table = table.getName();
				table.setFields(readFields(tablePath));
                table.setForeignKeys(this.readForeignKeys(tablePath));
                table.setPrimaryKey(this.getPrimaryKey(tablePath));
                table.setIndices(this.readIndexes(tablePath));
                for (DatabaseKey key : this.readKeys(tablePath)){
                    if (table.getUniqueKeys()==null)
                        table.setUniqueKeys(new ArrayList<DatabaseKey>());
                    if (key.isUnique()) table.getUniqueKeys().add(key);
                }
				tables.add(table);
			}
		return tables;
	}

    @Override
    public List<DatabaseProcedure> readProcedures(DatabasePath dbPath) throws Exception {
        List<DatabaseProcedure> procedures = new ArrayList<DatabaseProcedure>();
        for (DatabaseProcedure func : readFunctions(dbPath)){
            DatabaseProcedure proc = (DatabaseProcedure)func;
            procedures.add(proc);
        }
        return  procedures;
    }

    private String getFunctionDDL(String schema, String functionName){
        return "N/A";
    }
    @Override
    public List<DatabaseFunction> readFunctions(DatabasePath dbPath) throws Exception {
        List<String> functionNames = readFunctionNames(dbPath.schema);
        List<DatabaseFunction> functions = new ArrayList<DatabaseFunction>();
        for (String funcName : functionNames){
            DatabaseFunction f = new DatabaseFunction();
            f.setDDL(getFunctionDDL(dbPath.schema,funcName));
            DatabasePath path = (DatabasePath)dbPath.duplicate();
            path.function = funcName;
            f.setComment(this.readComment(path));
            //f.setArguments();
            //f.setReturnValue();
            //f.setOid();
            f.setName(funcName);
            //TODO morma postavit funkciji osm DDLa i parametre i return value itd
        }
        return functions;
    }

    @Override
	public List<String> readTableColumns(String schema, String table) throws Exception {
		if (schema.equals(""))
			schema = "public";
		String sqlTxt = "SELECT column_name " +
				//"|| '(' || data_type || ')' " +
				" FROM information_schema.columns" +
                " WHERE  table_schema= '" +schema +
                "' and table_name = '" + table + "';";
		List<String> tableNames = new ArrayList<String>();
		ResultSet tablesResult = m_DataBase.sql2resultSet(sqlTxt);
		tableNames = GenericHelper.resultSetToList(tablesResult);
		return tableNames;
	}

    @Override
    public List<String> readFunctionNames(String schema) throws Exception {
        String sql = "select routine_name\n" +
                " from information_schema.routines   \n" +
                " where routine_catalog='DB'\n" +
                " and routine_schema='SCH'";
        sql=sql.replace("SCH",schema)
                .replace("DB",this.getM_DataBase().getSettings().getDataBase());
        ResultSet tablesResult = m_DataBase.sql2resultSet(sql);
        return GenericHelper.resultSetToList(tablesResult);
    }

    @Override
    public List<String> readProcedureNames(String schema) throws Exception {
        return readFunctionNames(schema);
    }

    public String getSequenceDDL(final String schema, final String seqName){
        return "N/A";
    }

    @Override
    public List<DatabaseSequence> readSequences(DatabasePath dbPath) throws Exception {
        return  null;
    }

    @Override
    public List<String> readConstraints(String schema, String tableName) throws Exception {
        String sqlTxt = "SELECT keyUsage.constraint_name " +
                " FROM information_schema.key_column_usage keyUsage " +
                " INNER JOIN information_schema.constraint_column_usage columnUsage " +
                " ON keyUsage.constraint_name = columnUsage.constraint_name " +
                " WHERE keyUsage.table_schema = '" + schema + "' " +
                " AND keyUsage.table_name='"+tableName+"' " ;
        List<String> constraints = new ArrayList<String>();
        ResultSet tablesResult = m_DataBase.sql2resultSet(sqlTxt);

        constraints = GenericHelper.resultSetToList(tablesResult);
        return constraints;
    }


    @Override
    public String getPkey(String schema, String table) throws Exception {
        String sql ="select c.conname::varchar as pkey " +
                " from pg_constraint c              " +
                " inner join pg_namespace n         " +
                " on n.oid = c.connamespace         " +
                " inner join pg_class r             " +
                " on r.oid=c.conrelid               " +
                " where contype='p'                 " +
                " and nspname='sch'            " +
                " and relname='tbl'               " ;
        sql = sql.replace("sch",schema).replace("tbl",table);
        try {
            return m_DataBase.sql2string(sql);
        }catch (Exception e){
            return null;
        }
    }

    private boolean isConstraintPkey(String schema, String constraintName) throws Exception {
        return (boolean) constraintName.equalsIgnoreCase("primary");
    }
    @Override
    public List<String> getPkeyFields(String schema, String table) throws Exception {
        String pkeyName = getPkey(schema,table);
        String sql = String.format("SELECT \n" +
                "  COLUMN_NAME as `column_name`\n" +
                "FROM\n" +
                "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n" +
                "WHERE\n" +
                " CONSTRAINT_NAME like 'PRIMARY' "+
                "  AND TABLE_SCHEMA = '%s' \n" +
                "  AND TABLE_NAME = '%s'",schema,table);


        ResultSet rows = m_DataBase.sql2resultSet(sql);
        List<String> pkeyFields = new ArrayList<String>();
        if (rows == null) return pkeyFields;
        while (rows.next())
            pkeyFields.add(rows.getString("column_name"))  ;

        return pkeyFields;
    }

    @Override
    public List<String> getIndices(String schema, String table) throws Exception {
        String sql = String.format(" SELECT DISTINCT\n" +
                "    INDEX_NAME as relname\n" +
                "FROM INFORMATION_SCHEMA.STATISTICS\n" +
                "WHERE TABLE_SCHEMA = '%s' " +
                "and table_name='%s'",schema,table);
        ResultSet rows = m_DataBase.sql2resultSet(sql);
        List<String> indices = GenericHelper.resultSetToList(rows);
        return indices;
    }

    @Override
    public List<String> getIndexFields(DatabasePath path/*String indexSchema,String indexName*/) throws Exception {
        DatabaseIndex ndx = this.getIndex(path);
        List<String> indexFields = new ArrayList<String>();
        for (DatabaseField field : ndx.getFields())
            indexFields.add(field.getFieldName());
        return indexFields;
    }

    @Override
    public List<String> getForeignKeys(DatabasePath path) throws Exception {
        String sql = String.format(
            " SELECT \n" +
            "  distinct CONSTRAINT_NAME\n" +
            " FROM\n" +
            "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n" +
            " WHERE\n"+
            " REFERENCED_TABLE_SCHEMA is not null"+
            " AND TABLE_NAME ='%s' " +
            " AND  CONSTRAINT_SCHEMA = '%s' ",path.table,path.schema);

        List<String> fkeys = GenericHelper.resultSetToList(m_DataBase.sql2resultSet(sql));
        return fkeys;
    }

    public DatabaseField getFieldData(String schemaName, String relName, String fieldName) throws Exception {
        DatabaseField field = new DatabaseField();
        field.setFieldName(fieldName);
        String sql =
                " select * " +
                " from information_schema.columns \n" +
                " where table_schema='SCH'\n" +
                " and table_name = 'TBL'\n" +
                " and column_name = 'COL'"  +
                " and table_catalog = 'CAT'";

        sql = sql.replace("SCH", schemaName)
                .replace("TBL" , relName)
                .replace("COL" , fieldName)
                .replace("CAT" , this.m_DataBase.getSettings().getDataBase());

        ResultSet row = m_DataBase.sql2resultSet(sql);
        while (row.next()){
            if (row.getString("column_default")!=null) field.setDefaultValue(row.getString("column_default"));
            field.setNullable((row.getString("is_nullable").trim().equalsIgnoreCase("NO")) ? false : true);

           // field.setDimensionCount(-1);

            String pgDataType = row.getString("udt_name").trim().toLowerCase();
            String genericDataType = this.toGenericDataType(pgDataType);
            //dbType.setType(row.getString("data_type").trim().toLowerCase());
            field.setGenericDataType(genericDataType);
            String topLevelDataType = this.converter.nativeToTopLevel(pgDataType);
            field.setPrecision(-1);
            if (row.getString("data_type").equalsIgnoreCase("character varying"))
                field.setSize(row.getInt("character_maximum_length"));

            //System.out.println("staza: "+schemaName +';' +relName+';' + fieldName+" tip :"+genericDataType+":"+pgDataType);

            if (genericDataType.equalsIgnoreCase("double")||genericDataType.equalsIgnoreCase("float"))                           {
                field.setPrecision(row.getInt("numeric_precision_radix"));
                if (row.getString("numeric_scale")!=null)
                    field.setSize(row.getInt("numeric_scale"));
            }
            //boolean isInPrimaryKey = (row.getString("is_identity").trim().equalsIgnoreCase("NO")) ? false : true;
            //field.setInPrimaryKey(isInPrimaryKey);
            field.setInPrimaryKey(this.isFieldInPrimaryKey(schemaName,relName,fieldName));
            //System.out.println(String.format("table: %s, field: %s, inPk:%s, inDb: %s",relName,fieldName,field.isInPrimaryKey(),row.getString("is_identity")));
            //logger.debug(String.format("table: %s, field: %s, inPk:%s",relName,fieldName,field.isInPrimaryKey()));
            field.setTableOrder(row.getInt("ordinal_position"));
            field.setFieldType(pgDataType);
        }
        return field;
    }
    @Override
    public DatabaseIndex getIndex(DatabasePath path) throws Exception {
        String sql = String.format("SELECT \n" +
                "     group_concat(COLUMN_NAME) as `indkey_names`\n" +
                "     ,NON_UNIQUE\n" +
                "     ,TABLE_NAME\n" +
                "FROM INFORMATION_SCHEMA.STATISTICS\n" +
                "WHERE TABLE_SCHEMA = '%s'\n" +
                "and index_name='%s'\n" +
                        ((path.table!=null)? "and TABLE_NAME = '%s'\n":"") +
                "group by TABLE_NAME"
        , path.schema,path.index, path.table);

        ResultSet resultSet =   m_DataBase.sql2resultSet(sql);

        if (resultSet == null)  return null;

        List<DatabaseField> fields = new ArrayList<DatabaseField>();
        boolean isIndexUnique = false;
        while(resultSet.next()){
            String[] fieldList = resultSet
                    .getString("indkey_names").split(",");
            String[] relationShema  = (resultSet.getString("TABLE_NAME").split("\\."));
            isIndexUnique = !resultSet.getBoolean("NON_UNIQUE");
            String relation = (relationShema.length>1)?relationShema[1]:relationShema[0];
            for (String fieldName : fieldList)    {
                DatabaseField field = getFieldData( path.schema,   relation, fieldName);
                fields.add(field);
            }
        }
        //TODO moram napunit informacije o indeksu. za sada su mi bitna samo polja
        DatabaseIndex ndx = new DatabaseIndex();
        ndx.setName(path.index);
        ndx.setParallel(false);
        ndx.setDDL("N/A");
        ndx.setIsUnique(isIndexUnique);
        ndx.setIndexType("N/A");
        ndx.setFields(fields);
        return ndx;
    }

    public String getIndexDDL(final String schemaName, final String indexName){
        return "N/A";
    }

    public String getSrcSchemaFromFkey(String fkSchema, String fKey) throws Exception {
        String sql= String.format("select distinct unique_constraint_schema  \n" +
                " from information_schema.referential_constraints " +
                " where constraint_schema='%s' " +
                " and constraint_name='%s'" , fkSchema, fKey);
        return m_DataBase.sql2string(sql);
    }
    public String getPkeyFromFkey(String fkSchema, String fKey) throws Exception {
        String sql= "select distinct unique_constraint_name,unique_constraint_schema  \n" +
                " from information_schema.referential_constraints " +
                " where constraint_schema='fkSchema' " +
                " and constraint_name='fKey' "
                .replace("fkSchema", fkSchema)
                .replace("fKey", fKey);

        ResultSet rows = m_DataBase.sql2resultSet(sql);
        String srcShema, srcKey="";
        while (rows.next()){
            srcShema = rows.getString("unique_constraint_schema");
            srcKey  =  rows.getString("unique_constraint_name");
            if (this.isConstraintPkey(srcShema,srcKey)) return srcKey;
        }
        return srcKey;
    }
    public String getTableFromPkey(String schemaName, String pkeyName) throws Exception {
        String sql = String.format(
            //" select r1.relname as  dest_table,n.nspname as dest_schema,\n" +
                " select r1.relname as  dest_table\n" +
            //"  conname ,contype\n" +
            " \n" +
            " from\n" +
            " pg_constraint  c\n" +
            " inner join pg_namespace n\n" +
            " on n.oid = c.connamespace\n" +
            " \n" +
            " left join pg_class r1 \n" +
            " on r1.oid = c.conrelid \n" +
            "  \n" +
            " where contype='p'" +
            " and conname='%s' " +
            " and n.nspname='%s'",pkeyName,schemaName);
        return m_DataBase.sql2string(sql);
    }
    public String getForeignKeyDestinationTable(String fkSchema, String fKeyName) throws Exception {
        String srcShema = this.getSrcSchemaFromFkey(fkSchema,fKeyName);
        String pkey = this.getPkeyFromFkey(fkSchema,fKeyName);

        String sql=String.format("select min(table_name) from \n" +
                " information_schema.key_column_usage " +
                " where table_catalog='%s' " +
                " and table_schema='%s' " +
                " and constraint_name='%s' "
                , this.m_DataBase.getSettings().getDataBase()
                ,fkSchema
                ,fKeyName) ;
        return m_DataBase.sql2string(sql);
    }

    @Override
    public List<String> getForeignKeysFields(String schema, String fKeyName, boolean sourceTable) throws Exception {
        String dbName = this.m_DataBase.getSettings().getDataBase();
        if (this.isConstraintPkey(schema,fKeyName)) throw new Exception("Primary key is supplied");
        String sql="";
        if (sourceTable){
            sql=String.format("SELECT \n" +
                    "  COLUMN_NAME\n" +
                    "FROM\n" +
                    "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n" +
                    "WHERE\n" +
                    "  TABLE_SCHEMA = '%s' AND\n" +
                    "  CONSTRAINT_NAME = '%s'",schema,fKeyName);
        }else{
            sql=String.format("SELECT \n" +
                    "  COLUMN_NAME\n" +
                    "FROM\n" +
                    "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n" +
                    "WHERE\n" +
                    "  REFERENCED_TABLE_SCHEMA = '%s' AND\n" +
                    "  CONSTRAINT_NAME = '%s' " +
                    "order by position_in_unique_constraint,ORDINAL_POSITION",schema,fKeyName);

//            sql=String.format(
//                " select column_name \n" +
//                " from information_schema.key_column_usage\n" +
//                " where constraint_name='%s'\n" +
//                " and constraint_schema='%s'\n" +
//                " and constraint_catalog='%s'" +
//                " order by position_in_unique_constraint",
//                fKeyName,schema,m_DataBase.getSettings().getDataBase()
//            );
//            m_DataBase.getSettings().getDataBase()
        }
        List<String> columns = GenericHelper.resultSetToList(m_DataBase.sql2resultSet(sql));
        return columns;
    }

    @Override
    public String getForeignKeySourceTableName(String destSchemaName, String fKeyName) throws Exception {
        if (this.isConstraintPkey(destSchemaName,fKeyName)) return null;
        String sql = String.format(
                "  SELECT \n" +
                "  distinct TABLE_NAME \n" +
                "FROM\n" +
                "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n" +
                "WHERE\n" +
                "  REFERENCED_TABLE_SCHEMA = '%s' AND\n" +
                "  CONSTRAINT_NAME = '%s'; ",destSchemaName,fKeyName);
        return m_DataBase.sql2string(sql);
    }

    private DatabaseFunction getFunctionData(String schema, String functionName) throws SQLException {
        String sql="";
        ResultSet row = m_DataBase.sql2resultSet(sql);
        DatabaseFunction func = new DatabaseFunction();
        func.setName(functionName);
        func.setDDL("N/A");
        //func.setReturnValue(DatabaseTypeEnum);
        //TODO tu san sta..treba nac upit odgovarajuci i popunit sve informacije
        return func;
    }
    @Override
    public List<DatabaseFunction> getFunctions(String schema) throws Exception {
        List<String> functionNames = readFunctionNames(schema);
        List<DatabaseFunction> functions = new ArrayList<DatabaseFunction>();
        for (String func : functionNames){
            DatabaseFunction f = getFunctionData(schema,func);
            functions.add(f);
        }
        return functions;
    }

}

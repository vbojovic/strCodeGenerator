package com.timanaga.streamCodeGenerator.helpers.databases.dbClasses;

import com.timanaga.streamCodeGenerator.helpers.databases.DataFormatConverter;
import com.timanaga.streamCodeGenerator.helpers.databases.dbModels.*;
import com.timanaga.streamCodeGenerator.helpers.helper.GenericHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PgDbElementReader extends ADbElemetReader implements IDbElementReader {

	/* Constructor */
	public PgDbElementReader(PgDb dataBase) throws Exception {
		m_DataBase = dataBase;
        loadDataTypes();
	}
    private void loadDataTypes() throws Exception {
         this.converter = new DataFormatConverter("/pgDataTypes.txt");
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
		String sqlTxt = "select nspname as schema from pg_namespace order by nspname";
		ResultSet results = null;
		List<DatabaseSchema> schemas = new ArrayList<DatabaseSchema>();

		sql = m_DataBase.getConnection().createStatement();
		results = sql.executeQuery(sqlTxt);
		if (results != null)
			while (results.next()) {
                //TODO nadopunit sve tipove koji fale
				DatabaseSchema sch = new DatabaseSchema();
				sch.setSchemaName(results.getString("schema"));
                if (sch.getSchemaName().equalsIgnoreCase("information_schema")
                        || sch.getSchemaName().equalsIgnoreCase("pg_catalog")
                        || sch.getSchemaName().equalsIgnoreCase("pg_temp_1")
                        || sch.getSchemaName().equalsIgnoreCase("pg_temp_2")
                        || sch.getSchemaName().equalsIgnoreCase("pg_toast")
                        || sch.getSchemaName().equalsIgnoreCase("pg_toast_temp_1") ) continue;

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
		String sqlTxt = "select nspname as schema from pg_namespace order by nspname";
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
            sql=String.format("select description from pg_description\n" +
                    " join pg_class on pg_description.objoid = pg_class.oid\n" +
                    " where relname = '%s'" +
                    " and relnamespace in (select oid from pg_namespace where nspname='%s')", dbPath.table, schema);
        }else if (dbPath.view != null){
            sql=String.format("select description from pg_description\n" +
                    " join pg_class on pg_description.objoid = pg_class.oid\n" +
                    " where relname = '%s'" +
                    " and relnamespace in (select oid from pg_namespace where nspname='%s')", dbPath.view, schema);
        }  else if (dbPath.function != null){
            sql=String.format("SELECT description  \n" +
                " FROM   pg_catalog.pg_proc c\n" +
                " inner join pg_description d\n" +
                " on d.objoid = c.oid\n" +
                " inner join pg_namespace n\n" +
                " on n.oid = c.pronamespace\n" +
                " where nspname='%s'    \n" +
                " and proname='%s'",schema,dbPath.function);
        }  else if (dbPath.sequence != null){
            sql=String.format("SELECT description \n" +
                    " FROM   pg_catalog.pg_class c\n" +
                    " inner join pg_description d\n" +
                    " on d.objoid = c.oid\n" +
                    " inner join pg_namespace n\n" +
                    " on n.oid = c.relnamespace\n" +
                    " where nspname='%s'   \n" +
                    " and relname='%s'\n",schema, dbPath.sequence);
        }  else if (dbPath.schema != null){
            sql=String.format("SELECT description\n" +
                " FROM   pg_catalog.pg_namespace n\n" +
                " inner join pg_description d\n" +
                " on d.objoid = n.oid\n" +
                " where nspname='%s' ", dbPath.schema);
        } else if (dbPath.database != null){
            sql = String.format("SELECT pg_catalog.shobj_description(d.oid, 'pg_database') as description" +
                    " FROM   pg_catalog.pg_database d WHERE  datname = '%s'",dbPath.database);
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

            DatabasePath path = (DatabasePath)dbPath.duplicate();
            path.index = key;
            if  (type.equalsIgnoreCase("p"))   {
                keyFields =  getPkeyFields(path.schema,path.table);
                dbKey.setPrimary(true);
                dbKey.setIsUnique(true);
            }
            if  (type.equalsIgnoreCase("u"))   {
                keyFields = getIndexFields(path);
                dbKey.setIsUnique(true);
            }
            if  (type.equalsIgnoreCase("f"))   {
                keyFields =  getForeignKeysFields(path.schema, key, false);
                dbKey.setForeign(true);
            }
            for (String field : keyFields){
                DatabaseField f = this.getFieldData(path.schema,path.table,field);
                dbKey.getFields().add(f);
            }
//            for (String keyField : keyFields){
//                DatabaseField fieldData = getFieldData(path.schema,path.table,keyField);
//                dbKey.getFields().add(fieldData)  ;
//            }
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
            fkey.setFields(getDestFieldNames(dbPath.schema,fkeyName));

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
    private List<DatabaseField> getDestFieldNames(String schema, String fkeyName) throws Exception {
        List<String> fieldNames = this.getForeignKeysFields(schema,fkeyName,false);
        List<DatabaseField> fields = new ArrayList<DatabaseField>();
        for (String fieldName : fieldNames){
            String table = getForeignKeyDestinationTable(schema,fkeyName);
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
            DatabasePath path = dbPath.duplicate();
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
				"select "
						+ " table_catalog,table_schema,table_name,column_name,ordinal_position,"
						+ " column_default,is_nullable,data_type,character_maximum_length,"
						+ " character_octet_length,numeric_precision,numeric_precision_radix,"
						+ " numeric_scale,datetime_precision,interval_type,interval_precision,character_set_catalog,"
						+ " character_set_schema,character_set_name,collation_catalog,collation_schema,collation_name,"
						+ " domain_catalog,domain_schema,domain_name,udt_catalog,udt_name,scope_catalog,"
						+ " scope_schema,scope_name,maximum_cardinality,dtd_identifier,is_self_referencing,"
						+ " is_identity,identity_generation,identity_start,identity_increment,identity_maximum,"
						+ " identity_minimum,identity_cycle,is_updatable ")
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
		if (schema.equals(""))
			schema = "public";
		String sqlTxt = "select tablename  from pg_tables  where schemaname = '"
				+ schema + "' order by tablename ";
		List<String> tableNames = new ArrayList<String>();
		ResultSet tablesTesult = m_DataBase.sql2resultSet(sqlTxt);
		tableNames = GenericHelper.resultSetToList(tablesTesult);
		return tableNames;
	}
	
	/* (non-Javadoc)
	 * @see com.streamCodeGenerator.codeGen.IDbElementReader#getViews(com.streamCodeGenerator.dbModels.DatabasePath)
	 */
	@Override
	public List<DatabaseView> readViews(DatabasePath dbPath) throws Exception {
		String sqlTxt = "select viewname,definition  from pg_views  where schemaname = '"
				+dbPath.schema + "' order by viewname ";
		List<DatabaseView> views = new ArrayList<DatabaseView>();
		ResultSet tablesResult = m_DataBase.sql2resultSet(sqlTxt);
		if (tablesResult != null)
			while (tablesResult.next()){
				//r.add(tablesResult.getString(1));
				DatabaseView view = new DatabaseView();
				view.setDDL(tablesResult.getString("definition"));
				view.setName(tablesResult.getString("viewname"));
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
		String sqlTxt = "select tablename  from pg_tables  where schemaname = '"
				+dbPath.schema + "' order by tablename ";
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
        String sql = String.format("select \n" +
                " s.cycle_option,\n" +
                " s.data_type,\n" +
                " s.\"increment\",\n" +
                " s.maximum_value,\n" +
                " s.minimum_value,\n" +
                " s.numeric_precision,\n" +
                " s.numeric_scale,\n" +
                " s.numeric_precision_radix,\n" +
                " s.sequence_catalog,\n" +
                " s.sequence_name,\n" +
                " s.sequence_schema\n" +
                " from information_schema.sequences s\n" +
                " where \n" +
                " s.sequence_schema = '%s'\n",dbPath.schema) ;
        ResultSet rows =(m_DataBase.sql2resultSet(sql));
        List<DatabaseSequence> sequences = new ArrayList<DatabaseSequence>();
        while (rows.next()){
            DatabasePath seqPath = (DatabasePath)dbPath.duplicate();
            DatabaseSequence seq = new DatabaseSequence();
            seq.setName(rows.getString("sequence_name"));

            seq.setComment(readComment(dbPath));
            //TODO nadopunit za sekvencu sta je potrebno. za sada preskocit sekvence jer nisu toliko bitne

            seq.setCycle((rows.getString("cycle_option")==null) ? false : true);

            seq.setDDL(getSequenceDDL(dbPath.schema,dbPath.sequence));
            seq.setIncrement(rows.getLong("increment"));
            seq.setMaxvalue(rows.getLong("maximum_value"));
            seq.setMinvalue(rows.getLong("minimum_value"));

            sequences.add(seq);
        }

        return sequences;
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
        String sql ="select count(1) as cnt " +
                " from pg_constraint c              " +
                " inner join pg_namespace n         " +
                " on n.oid = c.connamespace         " +
                " inner join pg_class r             " +
                " on r.oid=c.conrelid               " +
                " where contype='p'                 " +
                " and nspname='sch'            " +
                " and  c.conname::varchar='CONSTR'               " ;
        sql = sql.replace("sch",schema).replace("CONSTR",constraintName);
        return (Integer.parseInt(m_DataBase.sql2string(sql))>0);
    }
    @Override
    public List<String> getPkeyFields(String schema, String table) throws Exception {
        String pkeyName = getPkey(schema,table);
        String sql = "select column_name                   "+
                " from information_schema.key_column_usage  "+
                " where constraint_catalog='DB'             "+
                " and constraint_schema='schName'           "+
                " and constraint_name like 'pkey'           "+
                " order by ordinal_position                 ";
        sql=sql.replace("DB",this.getM_DataBase().getSettings().getDataBase())
                .replace("schName",schema)
                .replace("pkey",pkeyName);

        ResultSet rows = m_DataBase.sql2resultSet(sql);
        List<String> pkeyFields = new ArrayList<String>();
        if (rows == null) return pkeyFields;
        while (rows.next())
            pkeyFields.add(rows.getString("column_name"))  ;

        return pkeyFields;
    }

    @Override
    public List<String> getIndices(String schema, String table) throws Exception {
        String sql   = "select 		r.relname               " +
                " from 			pg_index i              " +
                "                                       "  +
                " inner join 	pg_class r              " +
                " on 			r.oid = i.indexrelid    " +
                "                                       "  +
                " inner join 	pg_class r2             " +
                " on 			r2.oid = i.indrelid     " +
                "                                       "  +
                " inner join      pg_namespace n        "  +
                " on 			n.oid = r2.relnamespace " +
                " where 			n.nspname = 'sch'   "     +
                " and				r2.relname = 'tbl'  "
                ;
        sql = sql.replace("sch",schema).replace("tbl",table);
        ResultSet rows = m_DataBase.sql2resultSet(sql);
        List<String> indices = GenericHelper.resultSetToList(rows);
        return indices;
    }

    @Override
    public List<String> getIndexFields(DatabasePath path) throws Exception {
        if (path.index == null) throw new Exception("Index name missing");
        DatabaseIndex ndx = this.getIndex(path);
        List<String> indexFields = new ArrayList<String>();
        for (DatabaseField field : ndx.getFields())
            indexFields.add(field.getFieldName());
        return indexFields;
    }

    @Override
    public List<String> getForeignKeys(DatabasePath path) throws Exception {
        String sql ="select c.conname::varchar as fkey " +
                " from pg_constraint c              " +
                " inner join pg_namespace n         " +
                " on n.oid = c.connamespace         " +
                " inner join pg_class r             " +
                " on r.oid=c.conrelid               " +
                " where contype='f'                 " +
                " and nspname='sch'            " +
                " and relname='tbl'               " ;
        sql = sql.replace("sch", path.schema)
                .replace("tbl",path.table);
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
            String genericDataType = this.toGenericDataType(pgDataType).trim();
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
        String sql =
            " SELECT i.relname as indname,                               "+
            "        i.relowner as indowner,                             "+
            "        idx.indrelid::regclass::varchar as relation,        "+
            "        am.amname as indam,                                 "+
            "        idx.indkey,                                         "+
            "        ARRAY(                                              "+
            "        SELECT pg_get_indexdef(idx.indexrelid, k + 1, true) "+
            "        FROM generate_subscripts(idx.indkey, 1) as k        "+
            "        ORDER BY k                                          "+
            "        ) as indkey_names,                                  "+
            "        idx.indexprs IS NOT NULL as indexprs,               "+
            "        idx.indpred IS NOT NULL as indpred,                 "+
            "        idx.indisunique                                    "+
            " FROM   pg_index as idx                                     "+
            " JOIN   pg_class as i                                       "+
            " ON     i.oid = idx.indexrelid                              "+
            " JOIN   pg_am as am                                         "+
            " ON     i.relam = am.oid                                    "+
            " JOIN   pg_namespace as ns                                  "+
            " ON     ns.oid = i.relnamespace                             "+
            " AND    ns.nspname = 'sch'                                  "+
            " and    i.relname='ndxName'                                 ";
        sql=sql.replace("sch",path.schema)
                .replace("ndxName", path.index);
        ResultSet resultSet =   m_DataBase.sql2resultSet(sql);

        if (resultSet == null)  return null;

        List<DatabaseField> fields = new ArrayList<DatabaseField>();
        boolean isIndexUnique = false;
        while(resultSet.next()){
            String[] fieldList = (String[])resultSet
                    .getArray("indkey_names")
                    .getArray();
            String[] relationShema  = (resultSet.getString("relation").split("\\."));
            isIndexUnique = resultSet.getBoolean("indisunique");
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
        ndx.setDDL(this.getIndexDDL(path));
        ndx.setIsUnique(isIndexUnique);
        ndx.setIndexType("N/A");
        ndx.setFields(fields);
        return ndx;
    }

    public String getIndexDDL(final DatabasePath path){
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
            //ovaj upit vraca ime tablice iz koje uzimam kljuc i koja je kolona u kljucu
            sql = "select column_name " +
                " from information_schema.constraint_column_usage  " +
                " where table_catalog='DB' " +
                " and   constraint_name='fKey' " +
                " and  table_schema='SCH'";
            sql = sql.replace("DB",dbName)
                    .replace("SCH",schema)
                    .replace("fKey", fKeyName)   ;
        }else{
            sql=String.format(
                " select column_name \n" +
                " from information_schema.key_column_usage\n" +
                " where constraint_name='%s'\n" +
                " and constraint_schema='%s'\n" +
                " and constraint_catalog='%s'" +
                " order by position_in_unique_constraint",
                fKeyName,schema,m_DataBase.getSettings().getDataBase()
            );
        }
        List<String> columns = GenericHelper.resultSetToList(m_DataBase.sql2resultSet(sql));
        return columns;
    }

    @Override
    public String getForeignKeySourceTableName(String destSchemaName, String fKeyName) throws Exception {
        String sql = String.format("" +
                " select " +
                " r2.relname as src_table " +
                //"r1.relname as  dest_table,n.nspname as dest_schema,\n" +
                //"r2.relname as src_table,n2.nspname as  src_schema,conname \n" +
                " \n" +
                " from\n" +
                " pg_constraint  c\n" +
                " inner join pg_namespace n\n" +
                " on n.oid = c.connamespace\n" +
                " \n" +
                " inner join pg_class r1 \n" +
                " on r1.oid = c.conrelid\n" +
                " inner join pg_class r2 \n" +
                " on r2.oid = c.confrelid\n" +
                " \n" +
                " inner join pg_namespace n2\n" +
                " on n2.oid = r2.relnamespace\n" +
                " \n" +
                " where contype='f' " +
                " and conname='%s' " +
                " and n.nspname='%s' ",fKeyName,destSchemaName);
        return m_DataBase.sql2string(sql);  //To change body of implemented methods use File | Settings | File Templates.
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

package com.timanaga.streamCodeGenerator.databases.dbClasses;

import com.timanaga.streamCodeGenerator.databases.DataFormatConverter;
import com.timanaga.streamCodeGenerator.databases.dbModels.*;
import com.timanaga.streamCodeGenerator.helpers.helper.GenericHelper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlServerDbElementReader extends ADbElemetReader{
    public SqlServerDbElementReader(SqlServerDb dataBase) throws Exception {
        m_DataBase = dataBase;
        loadDataTypes();
        throw new Exception("N/A");
    }
    private void loadDataTypes() throws Exception {
        this.converter = new DataFormatConverter("/mssqlDataTypes.txt");
    }
    @Override
    public void ReadFromDataBase() throws Exception {
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
        String sqlTxt = "SELECT name AS [schema] FROM sys.schemas ORDER BY name";
        ResultSet results = null;
        List<DatabaseSchema> schemas = new ArrayList<DatabaseSchema>();

        sql = m_DataBase.getConnection().createStatement();
        results = sql.executeQuery(sqlTxt);
        if (results != null) {
            while (results.next()) {
                // TODO: Populate missing types if needed
                DatabaseSchema sch = new DatabaseSchema();
                sch.setSchemaName(results.getString("schema"));

                // Skip system schemas
                if (sch.getSchemaName().equalsIgnoreCase("dbo")
                        || sch.getSchemaName().equalsIgnoreCase("guest")
                        || sch.getSchemaName().equalsIgnoreCase("sys")
                        || sch.getSchemaName().equalsIgnoreCase("information_schema")) {
                    continue;
                }

                sch.setDDL("N/A");

                DatabasePath dbPath = new DatabasePath();
                dbPath.schema = sch.getSchemaName();
                sch.setComment(this.readComment(dbPath));
                // System.out.println(dbPath.schema);

                sch.setTables(this.readTables(dbPath));
                sch.setProcedures(this.readProcedures(dbPath));

                sch.setViews(this.readViews(dbPath));

                sch.setFunctions(this.readFunctions(dbPath));
                sch.setSequences(this.readSequences(dbPath));
                schemas.add(sch);
            }
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

    @Override
    public List<String> readSchemasList() {
        Statement sql;
        String sqlTxt = "SELECT name AS [schema] FROM sys.schemas ORDER BY name";
        ResultSet results = null;
        List<String> schemas = new ArrayList<String>();

        try {
            sql = m_DataBase.getConnection().createStatement();
            results = sql.executeQuery(sqlTxt);
            if (results != null) {
                while (results.next()) {
                    schemas.add(results.getString("schema"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return schemas;
    }

    public String readComment(DatabasePath dbPath) throws Exception {
        String sql = "";
        String schema = (dbPath.schema.isEmpty()) ? "dbo" : dbPath.schema;  // Use "dbo" for the default schema in SQL Server

        if (dbPath.field != null) {
            if (dbPath.table == null && dbPath.view == null) {
                throw new Exception("No table/view given");
            }
            sql = "N/A";
        } else if (dbPath.table != null) {
            sql = String.format("SELECT [value] FROM fn_listextendedproperty(NULL, 'schema', '%s', 'table', '%s', 'column', default);", schema, dbPath.table);
        } else if (dbPath.view != null) {
            sql = String.format("SELECT [value] FROM fn_listextendedproperty(NULL, 'schema', '%s', 'view', '%s', 'view', default);", schema, dbPath.view);
        } else if (dbPath.function != null) {
            // Note: SQL Server doesn't have direct support for function comments in the same way as PostgreSQL
            sql = "N/A";
        } else if (dbPath.sequence != null) {
            // Note: SQL Server doesn't have sequences, so there's no direct equivalent
            sql = "N/A";
        } else if (dbPath.schema != null) {
            sql = String.format("SELECT [value] FROM fn_listextendedproperty(NULL, 'schema', '%s', 'schema', default, default, default);", schema);
        } else if (dbPath.database != null) {
            // Note: SQL Server doesn't have direct support for database comments
            sql = "N/A";
        }

        try {
            return m_DataBase.sql2string(sql);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public List<DatabaseKey> readKeys(DatabasePath dbPath) throws Exception {
        List<DatabaseKey> keys = new ArrayList<DatabaseKey>();

        String sql = String.format(
                "SELECT distinct [name] AS conname, [type] AS contype " +
                        "FROM sys.key_constraints kc " +
                        "INNER JOIN sys.schemas s ON s.schema_id = kc.schema_id " +
                        "INNER JOIN sys.tables t ON t.object_id = kc.parent_object_id " +
                        "WHERE s.name = '%s'", dbPath.schema);

        if (dbPath.table != null) {
            sql += String.format(" AND t.name = '%s'", dbPath.table);
        }

        ResultSet rows;
        try {
            rows = m_DataBase.sql2resultSet(sql);
        } catch (Exception e) {
            return keys;
        }

        while (rows.next()) {
            String key = rows.getString("conname");
            String type = rows.getString("contype");
            DatabaseKey dbKey = new DatabaseKey();
            dbKey.setName(key);

            List<String> keyFields = new ArrayList<String>();
            dbKey.setPrimary(false);
            dbKey.setIsUnique(false);
            dbKey.setForeign(false);

            DatabasePath path = (DatabasePath) dbPath.duplicate();
            path.index = key;

            if (type.equalsIgnoreCase("PK")) {
                keyFields = getPkeyFields(path.schema, path.table);
                dbKey.setPrimary(true);
                dbKey.setIsUnique(true);
            } else if (type.equalsIgnoreCase("UQ")) {
                keyFields = getIndexFields(path);
                dbKey.setIsUnique(true);
            } else if (type.equalsIgnoreCase("F")) {
                keyFields = getForeignKeysFields(path.schema, key, false);
                dbKey.setForeign(true);
            }

            for (String field : keyFields) {
                DatabaseField f = this.getFieldData(path.schema, path.table, field);
                dbKey.getFields().add(f);
            }
            keys.add(dbKey);
        }

        return keys;
    }

    @Override
    public List<DatabaseForeignKey> readForeignKeys(DatabasePath dbPath) throws Exception {
        if (dbPath.schema.isEmpty() || dbPath.table.isEmpty()) {
            throw new Exception("Path not complete");
        }

        List<DatabaseForeignKey> fKeys = new ArrayList<DatabaseForeignKey>();
        List<String> fKeyNames = this.getForeignKeys(dbPath);

        for (String fkeyName : fKeyNames) {
            DatabaseForeignKey fkey = new DatabaseForeignKey();
            fkey.setName(fkeyName);

            if (isConstraintPkey(dbPath.schema, fkeyName)) {
                fkey.setPrimary(true);
            } else {
                fkey.setForeign(true);
                fkey.setReferencingFields(getSourceFieldNames(dbPath.schema, fkeyName));

                // TODO: Set the referencing and referenced table names and fields.
                // The exact approach may vary depending on your SQL Server schema and constraints.
                // You may need to adapt this part of the code to match your specific schema.

                String srcTable = ""; // Set the source table name
                String srcSchema = ""; // Set the source schema name

                fkey.setReferencingTablePath(new DatabasePath(srcSchema, srcTable));
            }

            fkey.setFields(getDestFieldNames(dbPath.schema, fkeyName));
            fKeys.add(fkey);
        }

        return fKeys;
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

    //TODO ispitat jel ovo radi sa sql serverom ili triba drugi kod pisat
    @Override
    public List<DatabaseField> readFields(DatabasePath dbPath) throws Exception {
        if (dbPath.schema == null || dbPath.schema.isEmpty()) {
            throw new Exception("Cannot get fields, schema not set!");
        }

        if (dbPath.view == null && dbPath.table == null) {
            throw new Exception("Cannot get fields, table or view not set!");
        }

        List<DatabaseField> fields = new ArrayList<DatabaseField>();

        StringBuilder sqlTxt = new StringBuilder();
        sqlTxt.append(
                "SELECT " +
                        "   TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, ORDINAL_POSITION,  \n" +
                        "   COLUMN_DEFAULT, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH,  \n" +
                        "   CHARACTER_OCTET_LENGTH, NUMERIC_PRECISION, NUMERIC_PRECISION_RADIX,  \n" +
                        "   NUMERIC_SCALE, DATETIME_PRECISION, \n" +
                        "   CHARACTER_SET_CATALOG, CHARACTER_SET_SCHEMA, CHARACTER_SET_NAME,  \n" +
                        "   COLLATION_CATALOG, COLLATION_SCHEMA, COLLATION_NAME,  \n" +
                        "   DOMAIN_CATALOG, DOMAIN_SCHEMA, DOMAIN_NAME, * " +
                        "FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE TABLE_SCHEMA = '" + dbPath.schema + "' " +
                        "AND TABLE_NAME = '" + (dbPath.table != null ? dbPath.table : dbPath.view) + "'");

        if (dbPath.field != null) {
            sqlTxt.append(" AND COLUMN_NAME = '" + dbPath.field + "'");
        }

        ResultSet fieldsRes = m_DataBase.sql2resultSet(sqlTxt.toString());
        if (fieldsRes != null) {
            while (fieldsRes.next()) {
                DatabaseField field = this.getFieldData(
                        dbPath.schema,
                        (dbPath.table != null) ? dbPath.table : dbPath.view,
                        fieldsRes.getString("COLUMN_NAME"));

                fields.add(field);
            }
        }

        return fields;
    }

    @Override
    public List<String> readTableNames(String schema) throws Exception {
        if (schema.isEmpty()) {
            schema = "dbo"; // Default SQL Server schema
        }

        String sqlTxt = "SELECT table_name FROM information_schema.tables WHERE table_schema = '"
                + schema + "' AND table_type = 'BASE TABLE' ORDER BY table_name";

        List<String> tableNames = new ArrayList<String>();
        ResultSet tablesResult = m_DataBase.sql2resultSet(sqlTxt);
        tableNames = GenericHelper.resultSetToList(tablesResult);
        return tableNames;
    }

    @Override
    public List<DatabaseView> readViews(DatabasePath dbPath) throws Exception {
        String sqlTxt = "SELECT TABLE_NAME AS viewname, VIEW_DEFINITION AS definition " +
                "FROM INFORMATION_SCHEMA.VIEWS " +
                "WHERE TABLE_SCHEMA = '" + dbPath.schema + "' " +
                "ORDER BY TABLE_NAME";

        List<DatabaseView> views = new ArrayList<DatabaseView>();
        ResultSet viewsResult = m_DataBase.sql2resultSet(sqlTxt);

        if (viewsResult != null) {
            while (viewsResult.next()) {
                DatabaseView view = new DatabaseView();
                view.setDDL(viewsResult.getString("definition"));
                view.setName(viewsResult.getString("viewname"));

                // TODO: Set the view's fields based on your SQL Server schema and naming conventions.
                // The exact approach may vary depending on your schema and views.

                DatabasePath tablePath = dbPath;
                tablePath.view = view.getName();
                view.setFields(this.readFields(dbPath));
                views.add(view);
            }
        }

        return views;
    }

    public DatabaseKey getPrimaryKey(DatabasePath dbPath) throws Exception {
        if (dbPath.table==null) throw  new Exception("No table given");
        for (DatabaseKey key : this.readKeys(dbPath)){
            if (key.isPrimary()) return key;
        }
        return null;
    }

    @Override
    public List<DatabaseTable> readTables(DatabasePath dbPath) throws Exception {
        String sqlTxt = "SELECT TABLE_NAME AS tablename " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_SCHEMA = '" + dbPath.schema + "' " +
                "AND TABLE_TYPE = 'BASE TABLE' " +
                "ORDER BY TABLE_NAME";

        List<DatabaseTable> tables = new ArrayList<DatabaseTable>();
        ResultSet tablesResult = m_DataBase.sql2resultSet(sqlTxt);

        if (tablesResult != null) {
            while (tablesResult.next()) {
                DatabaseTable table = new DatabaseTable();
                table.setDDL("table DDL not set");
                table.setName(tablesResult.getString("tablename"));

                DatabasePath tablePath = dbPath;
                tablePath.table = table.getName();

                table.setFields(this.readFields(tablePath));

                table.setForeignKeys(this.readForeignKeys(tablePath));

                table.setPrimaryKey(this.getPrimaryKey(tablePath));

                table.setIndices(this.readIndexes(tablePath));

                List<DatabaseKey> keys = this.readKeys(tablePath);
                for (DatabaseKey key : keys) {
                    if (table.getUniqueKeys() == null) {
                        table.setUniqueKeys(new ArrayList<DatabaseKey>());
                    }
                    if (key.isUnique()) {
                        table.getUniqueKeys().add(key);
                    }
                }

                tables.add(table);
            }
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


    private String getFunctionDDL(String schema, String functionName) throws Exception {
        String sql = "SELECT OBJECT_DEFINITION(OBJECT_ID('" + schema + "." + functionName + "')) AS functionDDL";
        try {
            ResultSet resultSet = m_DataBase.sql2resultSet(sql);
            if (resultSet.next()) {
                return resultSet.getString("functionDDL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
            f.setDDL(this.getFunctionDDL(dbPath.schema,dbPath.function));
            //TODO morma postavit funkciji osm DDLa i parametre i return value itd
        }
        return functions;
    }

    @Override
    public List<String> readTableColumns(String schema, String table) throws Exception {
        if (schema.equals("")) schema = "dbo";
        String sqlTxt = "SELECT COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = '" + schema + "' " +
                "AND TABLE_NAME = '" + table + "';";

        List<String> columnNames = new ArrayList<String>();

        try (PreparedStatement statement = m_DataBase.getConnection().prepareStatement(sqlTxt);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                columnNames.add(resultSet.getString("COLUMN_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return columnNames;
    }

    @Override
    public List<String> readFunctionNames(String schema) throws Exception {
        String sql = "SELECT name AS routine_name\n" +
                "FROM sys.objects\n" +
                "WHERE type_desc = 'SQL_SCALAR_FUNCTION'\n" +
                "AND schema_id = SCHEMA_ID(?)";

        List<String> functionNames = new ArrayList<String>();

        try (PreparedStatement statement = m_DataBase.getConnection().prepareStatement(sql)) {
            statement.setString(1, schema);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    functionNames.add(resultSet.getString("routine_name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return functionNames;
    }

    @Override
    public List<String> readProcedureNames(String schema) throws Exception {
        String sql = "SELECT name AS procedure_name\n" +
                "FROM sys.objects\n" +
                "WHERE type_desc = 'SQL_STORED_PROCEDURE'\n" +
                "AND schema_id = SCHEMA_ID(?)";

        List<String> procedureNames = new ArrayList<String>();

        try (PreparedStatement statement = m_DataBase.getConnection().prepareStatement(sql)) {
            statement.setString(1, schema);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    procedureNames.add(resultSet.getString("procedure_name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return procedureNames;
    }

    public String getSequenceDDL(final String schema, final String seqName) {
        String sql = "SELECT definition\n" +
                "FROM sys.sequences\n" +
                "WHERE name = ?\n" +
                "AND schema_id = SCHEMA_ID(?)";

        try (PreparedStatement statement = m_DataBase.getConnection().prepareStatement(sql)) {
            statement.setString(1, seqName);
            statement.setString(2, schema);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("definition");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "N/A";
    }

    @Override
    public List<DatabaseSequence> readSequences(DatabasePath dbPath) throws Exception {
        String sql = String.format(
                "SELECT is_cycling AS cycle_option,\n" +
                        "data_type,\n" +
                        "increment AS \"increment\",\n" +
                        "maximum_value,\n" +
                        "minimum_value,\n" +
                        "numeric_precision,\n" +
                        "numeric_scale,\n" +
                        "numeric_precision_radix,\n" +
                        "sequence_catalog,\n" +
                        "sequence_name,\n" +
                        "sequence_schema\n" +
                        "FROM information_schema.sequences s\n" +
                        "WHERE s.sequence_schema = '%s'", dbPath.schema);

        ResultSet rows = m_DataBase.sql2resultSet(sql);
        List<DatabaseSequence> sequences = new ArrayList<DatabaseSequence>();
        while (rows.next()) {
            DatabasePath seqPath = (DatabasePath) dbPath.duplicate();
            DatabaseSequence seq = new DatabaseSequence();
            seq.setName(rows.getString("sequence_name"));

            seq.setComment(readComment(seqPath));
            seq.setDDL(getSequenceDDL(dbPath.schema, seqPath.sequence));
            seq.setCycle(rows.getBoolean("cycle_option"));
            seq.setIncrement(rows.getLong("increment"));
            seq.setMaxvalue(rows.getLong("maximum_value"));
            seq.setMinvalue(rows.getLong("minimum_value"));

            sequences.add(seq);
        }

        return sequences;
    }

    @Override
    public List<String> readConstraints(String schema, String tableName) throws Exception {
        String sqlTxt =
                "SELECT c.CONSTRAINT_NAME " +
                        "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu " +
                        "INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS c " +
                        "ON kcu.CONSTRAINT_NAME = c.CONSTRAINT_NAME " +
                        "WHERE kcu.TABLE_SCHEMA = '" + schema + "' " +
                        "AND kcu.TABLE_NAME = '" + tableName + "'";


        ResultSet constraintsResult = m_DataBase.sql2resultSet(sqlTxt);
        List<String> constraints =  GenericHelper.resultSetToList(constraintsResult);
        return constraints;
    }

    @Override
    public String getPkey(String schema, String table) throws Exception {
        String sql =
                "SELECT ccu.CONSTRAINT_NAME AS pkey " +
                        "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc " +
                        "INNER JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE ccu " +
                        "ON tc.CONSTRAINT_NAME = ccu.CONSTRAINT_NAME " +
                        "WHERE tc.CONSTRAINT_TYPE = 'PRIMARY KEY' " +
                        "AND tc.TABLE_SCHEMA = '" + schema + "' " +
                        "AND tc.TABLE_NAME = '" + table + "'";

        try {
            return m_DataBase.sql2string(sql);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isConstraintPkey(String schema, String constraintName) throws Exception {
        String sql =
                "SELECT CASE WHEN EXISTS (" +
                        "    SELECT 1 " +
                        "    FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc " +
                        "    WHERE tc.CONSTRAINT_TYPE = 'PRIMARY KEY' " +
                        "    AND tc.TABLE_SCHEMA = '" + schema + "' " +
                        "    AND tc.CONSTRAINT_NAME = '" + constraintName + "'" +
                        ") THEN 1 ELSE 0 END AS cnt";

        int count = Integer.parseInt(m_DataBase.sql2string(sql));
        return count > 0;
    }

    @Override
    public List<String> getPkeyFields(String schema, String table) throws Exception {
        String pkeyName = getPkey(schema, table);
        String sql =
                "SELECT ku.COLUMN_NAME " +
                        "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc " +
                        "JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE ku " +
                        "ON tc.CONSTRAINT_NAME = ku.CONSTRAINT_NAME " +
                        "AND tc.CONSTRAINT_SCHEMA = ku.CONSTRAINT_SCHEMA " +
                        "WHERE tc.CONSTRAINT_TYPE = 'PRIMARY KEY' " +
                        "AND tc.TABLE_SCHEMA = '" + schema + "' " +
                        "AND tc.TABLE_NAME = '" + table + "' " +
                        "AND tc.CONSTRAINT_NAME = '" + pkeyName + "' " +
                        "ORDER BY ku.ORDINAL_POSITION";

        ResultSet rows = m_DataBase.sql2resultSet(sql);
        List<String> pkeyFields = new ArrayList<String>();

        if (rows != null) {
            while (rows.next()) {
                pkeyFields.add(rows.getString("COLUMN_NAME"));
            }
        }

        return pkeyFields;
    }


    @Override
    public List<String> getIndices(String schema, String table) throws Exception {
        String sql =
                "SELECT si.name " +
                        "FROM sys.indexes si " +
                        "INNER JOIN sys.objects so " +
                        "ON si.object_id = so.object_id " +
                        "WHERE so.schema_id = SCHEMA_ID('" + schema + "') " +
                        "AND so.name = '" + table + "' " +
//                        "AND si.is_primary_key = 0 " +
//                        "AND si.is_unique_constraint = 0 " +
//                        "AND si.type_desc = 'NONCLUSTERED' " +
                        "AND si.name IS NOT NULL";

        ResultSet rows = m_DataBase.sql2resultSet(sql);
        List<String> indices = GenericHelper.resultSetToList(rows);
        return indices;
    }
    public List<String> getPkeyIndices(String schema, String table) throws Exception {
        String sql =
                "SELECT si.name " +
                        "FROM sys.indexes si " +
                        "INNER JOIN sys.objects so " +
                        "ON si.object_id = so.object_id " +
                        "WHERE so.schema_id = SCHEMA_ID('" + schema + "') " +
                        "AND so.name = '" + table + "' " +
                        "AND si.is_primary_key = 1 " + // Exclude primary key indices
//                        "AND si.is_unique_constraint = 0 " + // Exclude unique constraints
//                        "AND si.type_desc = 'NONCLUSTERED' " + // Exclude clustered indices
                        "AND si.name IS NOT NULL";

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
        String sql = "SELECT fk.name AS fkey " +
                "FROM sys.foreign_keys AS fk " +
                "INNER JOIN sys.tables AS t " +
                "ON fk.parent_object_id = t.object_id " +
                "INNER JOIN sys.schemas AS s " +
                "ON t.schema_id = s.schema_id " +
                "WHERE fk.type = 'F' " +
                "AND s.name = '" + path.schema + "' " +
                "AND t.name = '" + path.table + "';";

        List<String> fkeys = GenericHelper.resultSetToList(m_DataBase.sql2resultSet(sql));
        return fkeys;
    }

    public DatabaseField getFieldData(String schemaName, String relName, String fieldName) throws Exception {
        DatabaseField field = new DatabaseField();
        field.setFieldName(fieldName);

        String sql =
                " SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION, NUMERIC_SCALE, IS_NULLABLE " +
                        " FROM INFORMATION_SCHEMA.COLUMNS " +
                        " WHERE TABLE_SCHEMA = 'SCH' AND TABLE_NAME = 'TBL' AND COLUMN_NAME = 'COL' AND TABLE_CATALOG = 'CAT'";

        sql = sql.replace("SCH", schemaName)
                .replace("TBL", relName)
                .replace("COL", fieldName)
                .replace("CAT", this.m_DataBase.getSettings().getDataBase());

        ResultSet row = m_DataBase.sql2resultSet(sql);

        if (row.next()) {
            if (row.getString("IS_NULLABLE").trim().equalsIgnoreCase("NO")) {
                field.setNullable(false);
            } else {
                field.setNullable(true);
            }

            String dataType = row.getString("DATA_TYPE").trim().toLowerCase();
            field.setFieldType(dataType);

            if (dataType.equals("character varying") || dataType.equals("character")) {
                field.setSize(row.getInt("CHARACTER_MAXIMUM_LENGTH"));
            }

            if (dataType.equals("double") || dataType.equals("float")) {
                field.setPrecision(row.getInt("NUMERIC_PRECISION"));
                if (row.getString("NUMERIC_SCALE") != null) {
                    field.setSize(row.getInt("NUMERIC_SCALE"));
                }
            }

            return field;
        } else {
            throw new Exception("Field not found.");
        }
    }

    public DatabaseIndex getIndex(DatabasePath path) throws Exception {
        String sql =
                " SELECT i.name AS indname,                   " +
                        "        u.name AS indowner,                 " +
                        "        OBJECT_NAME(i.object_id) AS relation," +
                        "        i.type_desc AS indam,               " +
                        "        c.name AS index_column_name,        " +
                        "        ic.key_ordinal AS index_column_ordinal, " +
                        "        i.is_unique,                       " +
                        "        i.is_unique_constraint              " +
                        " FROM   sys.indexes AS i                    " +
                        " JOIN   sys.index_columns AS ic             " +
                        " ON     i.object_id = ic.object_id           " +
                        " AND    i.index_id = ic.index_id             " +
                        " JOIN   sys.columns AS c                    " +
                        " ON     ic.object_id = c.object_id           " +
                        " AND    ic.column_id = c.column_id           " +
                        " JOIN   sys.schemas AS s                    " +
                        " ON     s.schema_id = c.schema_id            " +
                        " JOIN   sys.database_principals AS u         " +
                        " ON     u.sid = s.principal_id               " +
                        " WHERE  u.name = 'sch'                      " +
                        " AND    OBJECT_NAME(i.object_id) = 'ndxName'";

        sql = sql.replace("sch", path.schema)
                .replace("ndxName", path.index);

        ResultSet resultSet = m_DataBase.sql2resultSet(sql);

        if (resultSet == null) {
            return null;
        }

        List<DatabaseField> fields = new ArrayList<DatabaseField>();
        boolean isIndexUnique = false;

        while (resultSet.next()) {
            String indexColumnName = resultSet.getString("index_column_name");
            int indexColumnOrdinal = resultSet.getInt("index_column_ordinal");
            isIndexUnique = resultSet.getBoolean("is_unique");

            DatabaseField field = getFieldData(path.schema, path.index, indexColumnName);
            field.setTableOrder(indexColumnOrdinal);
            fields.add(field);
        }

        // TODO: Fill in more information about the index. For now, we are focusing on the fields.
        DatabaseIndex ndx = new DatabaseIndex();
        ndx.setName(path.index);
        ndx.setParallel(false);
        ndx.setDDL(this.getIndexDDL(path));
        ndx.setIsUnique(isIndexUnique);
        ndx.setIndexType("N/A");
        ndx.setFields(fields);
        return ndx;
    }


    public String getIndexDDL(final DatabasePath path) throws Exception {
        String sql =
                "SELECT definition AS index_ddl " +
                "FROM sys.indexes " +
                "WHERE object_id = OBJECT_ID('sch.tbl') " +
                "AND name = 'ndxName'";

        sql = sql.replace("sch", path.schema)
                .replace("tbl", path.table)
                .replace("ndxName", path.index);

        ResultSet resultSet = m_DataBase.sql2resultSet(sql);

        if (!resultSet.next()) return null;
        return resultSet.getString("index_ddl");
    }

    public String getSrcSchemaFromFkey(String fkSchema, String fKey) throws Exception {
        String sql = String.format(
                "SELECT DISTINCT unique_constraint_schema " +
                "FROM information_schema.referential_constraints " +
                "WHERE constraint_schema = '%s' " +
                "AND constraint_name = '%s'",
                fkSchema, fKey);

        return m_DataBase.sql2string(sql);
    }

    //TODO provjerit jel mi ovo uopce treba
    public String getPkeyFromFkey(String fkSchema, String fKey) throws Exception {
        String sql = "SELECT DISTINCT uc.name AS unique_constraint_name, " +
                "sc.name AS unique_constraint_schema " +
                "FROM sys.foreign_keys fk " +
                "INNER JOIN sys.schemas sc ON fk.schema_id = sc.schema_id " +
                "INNER JOIN sys.objects t ON fk.parent_object_id = t.object_id " +
                "INNER JOIN sys.objects r ON fk.referenced_object_id = r.object_id " +
                "INNER JOIN sys.objects u ON fk.unique_index_id = u.object_id " +
                "INNER JOIN sys.key_constraints uc ON u.object_id = uc.parent_object_id " +
                "WHERE sc.name = N'fkSchema' " +
                "AND fk.name = N'fKey'";

        sql = sql.replace("fkSchema", fkSchema).replace("fKey", fKey);

        ResultSet rows = m_DataBase.sql2resultSet(sql);
        String srcSchema, srcKey = "";

        while (rows.next()) {
            srcSchema = rows.getString("unique_constraint_schema");
            srcKey = rows.getString("unique_constraint_name");

            if (this.isConstraintPkey(srcSchema, srcKey)) {
                return srcKey;
            }
        }

        return srcKey;
    }


    public String getTableFromPkey(String schemaName, String pkeyName) throws Exception {
        String sql = String.format(
                "SELECT t.name AS dest_table\n" +
                        "FROM sys.key_constraints kc\n" +
                        "INNER JOIN sys.schemas s ON s.schema_id = kc.schema_id\n" +
                        "INNER JOIN sys.tables t ON t.object_id = kc.parent_object_id\n" +
                        "WHERE kc.type = 'PK'\n" +
                        "AND kc.name = '%s'\n" +
                        "AND s.name = '%s'", pkeyName, schemaName);

        return m_DataBase.sql2string(sql);
    }

    public String getForeignKeyDestinationTable(String fkSchema, String fKeyName) throws Exception {
        String sql = String.format("SELECT MIN(table_name) FROM information_schema.key_column_usage\n" +
                        "WHERE table_catalog = '%s'\n" +
                        "AND table_schema = '%s'\n" +
                        "AND constraint_name = '%s'",
                m_DataBase.getSettings().getDataBase(),
                fkSchema,
                fKeyName);

        String result = m_DataBase.sql2string(sql);
        return result;
    }

    @Override
    public List<String> getForeignKeysFields(String schema, String fKeyName, boolean sourceTable) throws Exception {
        String dbName = m_DataBase.getSettings().getDataBase();
        if (isConstraintPkey(schema, fKeyName)) {
            throw new Exception("Primary key is supplied");
        }
        String sql = "";
        if (sourceTable) {
            // This query retrieves the column names from the source table's foreign key constraint.
            sql = "SELECT column_name " +
                    "FROM information_schema.constraint_column_usage  " +
                    "WHERE table_catalog = '" + dbName + "' " +
                    "AND constraint_name = '" + fKeyName + "' " +
                    "AND table_schema = '" + schema + "'";
        } else {
            // This query retrieves the column names from the target table's primary key constraint.
            sql = String.format(
                    "SELECT column_name " +
                            "FROM information_schema.key_column_usage " +
                            "WHERE constraint_name = '%s' " +
                            "AND constraint_schema = '%s' " +
                            "AND constraint_catalog = '%s' " +
                            "ORDER BY position_in_unique_constraint",
                    fKeyName, schema, dbName
            );
        }

        ResultSet resultSet = m_DataBase.sql2resultSet(sql);

        List<String> columns = GenericHelper.resultSetToList(resultSet);
        return columns;
    }

    @Override
    public String getForeignKeySourceTableName(String destSchemaName, String fKeyName) throws Exception {
        String sql = String.format(
                "SELECT " +
                "    r2.name AS src_table " +
                "FROM " +
                "    sys.foreign_keys AS fk " +
                "JOIN " +
                "    sys.objects AS r2 ON fk.parent_object_id = r2.object_id " +
                "JOIN " +
                "    sys.schemas AS s2 ON r2.schema_id = s2.schema_id " +
                "WHERE " +
                "    fk.name = '%s' " +
                "    AND s2.name = '%s'", fKeyName, destSchemaName);

        return m_DataBase.sql2string(sql);
    }

    private DatabaseFunction getFunctionData(String schema, String functionName) throws Exception {
        String sql="";
        ResultSet row = m_DataBase.sql2resultSet(sql);
        DatabaseFunction func = new DatabaseFunction();
        func.setName(functionName);
        func.setDDL(this.getFunctionDDL(schema,functionName));
        //func.setReturnValue(DatabaseTypeEnum);
        //TODO tu san sta..treba nac upit odgovarajuci i popunit sve informacije
//        throw new Exception("N/A");
        return null;
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

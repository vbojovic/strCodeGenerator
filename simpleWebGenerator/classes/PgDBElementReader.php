<?php

class PgDbElementReader extends ADbElemetReader implements IDbElementReader {

    public function __construct(PgDb $dataBase) {
        $this->m_DataBase = $dataBase;
        $this->loadDataTypes();
    }

    private function loadDataTypes() {
        $this->converter = new DataFormatConverter("/pgDataTypes.txt");
    }

    private function isFieldInPrimaryKey($schemaName, $relName, $field) {
        try {
            $pkeyKeys = $this->getPkeyFields($schemaName, $relName);
            return in_array($field, $pkeyKeys);
        } catch (Exception $e) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.streamCodeGenerator.codeGen.IDbElementReader#getSchemas()
     */

    public function readTargetSchemas() {
        $sql = null;
        if ($this->getTargetSchemas() == null || count($this->getTargetSchemas()) == 0) {
            return $this->readSchemas();
        }

        $results = null;
        $schemas = array();

        //sql = m_DataBase.getConnection().createStatement();
        foreach ($this->getTargetSchemas() as $schemaName) {
            $sch = new DatabaseSchema();
            $sch->setSchemaName($schemaName);
            $sch->setDDL("N/A");
            $dbPath = new DatabasePath();
            $dbPath->schema = $sch->getSchemaName();
            $sch->setComment($this->readComment($dbPath));

            $sch->setTables($this->readTables($dbPath));
            $sch->setProcedures($this->readProcedures($dbPath));
            $sch->setViews($this->readViews($dbPath));
            $sch->setFunctions($this->readFunctions($dbPath));
            $sch->setSequences($this->readSequences($dbPath));
            $schemas[] = $sch;
        }

        return $schemas;
    }

    public function readSchemas() {
        $sqlTxt = "select nspname as schema from pg_namespace order by nspname";
        $results = $this->m_DataBase->query($sqlTxt);
        $schemas = [];

        while ($row = $results->fetch(PDO::FETCH_ASSOC)) {
            $schemaName = $row['schema'];

            // Ignore system schemas
            if (in_array($schemaName, ['information_schema', 'pg_catalog', 'pg_temp_1', 'pg_temp_2', 'pg_toast', 'pg_toast_temp_1'])) {
                continue;
            }

            $schema = new DatabaseSchema();
            $schema->setSchemaName($schemaName);
            $schema->setDDL("N/A");

            $dbPath = new DatabasePath();
            $dbPath->schema = $schema->getSchemaName();
            $schema->setComment($this->readComment($dbPath));

            $schema->setTables($this->readTables($dbPath));
            $schema->setProcedures($this->readProcedures($dbPath));
            $schema->setViews($this->readViews($dbPath));
            $schema->setFunctions($this->readFunctions($dbPath));
            $schema->setSequences($this->readSequences($dbPath));

            $schemas[] = $schema;
        }

        return $schemas;
    }

    public function readSchema($schemaName) {
        // TODO: nadopunit sve tipove koji fale
        $sch = new DatabaseSchema();
        $sch->setSchemaName($schemaName);
        $sch->setDDL("N/A");

        $dbPath = new DatabasePath();
        $dbPath->schema = $sch->getSchemaName();
        $sch->setComment($this->readComment($dbPath));

        $sch->setTables($this->readTables($dbPath));
        $sch->setProcedures($this->readProcedures($dbPath));
        $sch->setViews($this->readViews($dbPath));
        $sch->setFunctions($this->readFunctions($dbPath));
        $sch->setSequences($this->readSequences($dbPath));

        return $sch;
    }

    public function readSchemasList() {
        $sql = "select nspname as schema from pg_namespace order by nspname";
        $results = pg_query($this->m_DataBase->getConnection(), $sql);
        $schemas = array();

        if ($results) {
            while ($row = pg_fetch_assoc($results)) {
                $schemas[] = $row["schema"];
            }
        }

        return $schemas;
    }

    function readComment($dbPath) {
        $schema = empty($dbPath->schema) ? "public" : $dbPath->schema;
        if (isset($dbPath->field)) {
            if (empty($dbPath->table) && empty($dbPath->view)) {
                throw new Exception("No table/view given");
            }
            $sql = "N/A";
        } else if (!empty($dbPath->table)) {
            $sql = sprintf("select description from pg_description
                    join pg_class on pg_description.objoid = pg_class.oid
                    where relname = '%s'
                    and relnamespace in (select oid from pg_namespace where nspname='%s')",
                    $dbPath->table, $schema);
        } else if (!empty($dbPath->view)) {
            $sql = sprintf("select description from pg_description
                    join pg_class on pg_description.objoid = pg_class.oid
                    where relname = '%s'
                    and relnamespace in (select oid from pg_namespace where nspname='%s')",
                    $dbPath->view, $schema);
        } else if (!empty($dbPath->function)) {
            $sql = sprintf("SELECT description
                    FROM pg_catalog.pg_proc c
                    inner join pg_description d
                    on d.objoid = c.oid
                    inner join pg_namespace n
                    on n.oid = c.pronamespace
                    where nspname='%s'
                    and proname='%s'",
                    $schema, $dbPath->function);
        } else if (!empty($dbPath->sequence)) {
            $sql = sprintf("SELECT description
                    FROM pg_catalog.pg_class c
                    inner join pg_description d
                    on d.objoid = c.oid
                    inner join pg_namespace n
                    on n.oid = c.relnamespace
                    where nspname='%s'
                    and relname='%s'",
                    $schema, $dbPath->sequence);
        } else if (!empty($dbPath->schema)) {
            $sql = sprintf("SELECT description
                    FROM pg_catalog.pg_namespace n
                    inner join pg_description d
                    on d.objoid = n.oid
                    where nspname='%s'",
                    $dbPath->schema);
        } else if (!empty($dbPath->database)) {
            $sql = sprintf("SELECT pg_catalog.shobj_description(d.oid, 'pg_database') as description
                    FROM pg_catalog.pg_database d WHERE datname = '%s'",
                    $dbPath->database);
        }
        try {
            return $m_DataBase->sql2string($sql);
        } catch (Exception $e) {
            return "";
        }
    }

    public function readKeys($dbPath) {
        $keys = array();
        $schema = empty($dbPath->schema) ? "public" : $dbPath->schema;

        $sql = sprintf("select distinct conname, contype 
                    from pg_constraint c 
                    inner join pg_namespace n 
                    on n.oid = c.connamespace 
                    inner join pg_class r 
                    on r.oid = c.conrelid 
                    where nspname ~ '%s'", $schema);

        if (!empty($dbPath->table)) {
            $sql .= sprintf(" and relname = '%s'", $dbPath->table);
        }

        try {
            $rows = $this->m_DataBase->sql2resultSet($sql);
        } catch (Exception $e) {
            return $keys;
        }

        while ($row = pg_fetch_assoc($rows)) {
            $key = $row['conname'];
            $type = $row['contype'];

            $dbKey = new DatabaseKey();
            $dbKey->setName($key);
            $dbKey->setPrimary(false);
            $dbKey->setIsUnique(false);
            $dbKey->setForeign(false);

            $keyFields = array();
            $path = clone $dbPath;
            $path->index = $key;

            if ($type == 'p') {
                $keyFields = $this->getPkeyFields($schema, $dbPath->table);
                $dbKey->setPrimary(true);
                $dbKey->setIsUnique(true);
            } elseif ($type == 'u') {
                $keyFields = $this->getIndexFields($path);
                $dbKey->setIsUnique(true);
            } elseif ($type == 'f') {
                $keyFields = $this->getForeignKeysFields($schema, $key, false);
                $dbKey->setForeign(true);
            }

            foreach ($keyFields as $field) {
                $f = $this->getFieldData($schema, $dbPath->table, $field);
                $dbKey->getFields()->add($f);
            }

            $keys[] = $dbKey;
        }

        return $keys;
    }

    public function readForeignKeys($dbPath) {
        if (empty($dbPath['schema']) || empty($dbPath['table']))
            throw new Exception("Path not complete");
        $fKeyNames = $this->getForeignKeys($dbPath);
        $fKeys = array();
        foreach ($fKeyNames as $fkeyName) {
            $fkey = new DatabaseForeignKey();
            $fkey->setName($fkeyName);
            $fkey->setPrimary($this->isConstraintPkey($dbPath['schema'], $fkeyName));

            if (!$fkey->isPrimary()) {
                $fkey->setForeign(true);
                $fkey->setReferencingFields($this->getSourceFieldNames($dbPath['schema'], $fkeyName));
                //TODO ovdi san sta. treban punit polja referncna i indeksna
                $srcTable = $this->getForeignKeySourceTableName($dbPath['schema'], $fkeyName);
                //echo $dbPath['schema']."\t".$fkeyName;
                $srcShema = $this->getSrcSchemaFromFkey($dbPath['schema'], $fkeyName);
                $fkey->setReferencingTablePath(new DatabasePath($srcShema, $srcTable));
            }
            $fkey->setFields($this->getDestFieldNames($dbPath['schema'], $fkeyName));
            $fKeys[] = $fkey;
        }
        return $fKeys;
    }

    private function getSourceFieldNames($destSchema, $fkeyName) {
        $fieldNames = $this->getForeignKeysFields($destSchema, $fkeyName, true);
        $fields = array();
        foreach ($fieldNames as $fieldName) {
            $table = $this->getForeignKeySourceTableName($destSchema, $fkeyName);
            $field = $this->getFieldData($destSchema, $table, $fieldName);
            $fields[] = $field;
        }
        return $fields;
    }

    public function getDestFieldNames(string $schema, string $fkeyName): array {
        $fieldNames = $this->getForeignKeysFields($schema, $fkeyName, false);
        $fields = [];
        foreach ($fieldNames as $fieldName) {
            $table = $this->getForeignKeyDestinationTable($schema, $fkeyName);
            $field = $this->getFieldData($schema, $table, $fieldName);
            $fields[] = $field;
        }
        return $fields;
    }

    public function readIndexes(DatabasePath $dbPath): array {
        if (empty($dbPath->schema) || empty($dbPath->table)) {
            throw new Exception("Path not complete");
        }
        $indexNames = $this->getIndices($dbPath->schema, $dbPath->table);
        $indices = [];
        foreach ($indexNames as $ndxName) {
            $path = $dbPath->duplicate();
            $path->index = $ndxName;
            $ndx = $this->getIndex($path);
            $indices[] = $ndx;
        }
        return $indices;
    }

    public function readFields(DatabasePath $dbPath) {
        if (empty($dbPath->schema)) {
            throw new Exception("Cannot get fields, schema not set!");
        }
        if (empty($dbPath->view) && empty($dbPath->table)) {
            throw new Exception("Cannot get fields, table or view not set!");
        }

        $fields = [];

        $sqlTxt = "SELECT table_catalog, table_schema, table_name, column_name, ordinal_position, "
                . "column_default, is_nullable, data_type, character_maximum_length, "
                . "character_octet_length, numeric_precision, numeric_precision_radix, "
                . "numeric_scale, datetime_precision, interval_type, interval_precision, character_set_catalog, "
                . "character_set_schema, character_set_name, collation_catalog, collation_schema, collation_name, "
                . "domain_catalog, domain_schema, domain_name, udt_catalog, udt_name, scope_catalog, "
                . "scope_schema, scope_name, maximum_cardinality, dtd_identifier, is_self_referencing, "
                . "is_identity, identity_generation, identity_start, identity_increment, identity_maximum, "
                . "identity_minimum, identity_cycle, is_updatable "
                . "FROM INFORMATION_SCHEMA.COLUMNS "
                . "WHERE table_schema = '{$dbPath->schema}' "
                . "AND table_name='{$dbPath->table}'";

        if (!empty($dbPath->field)) {
            $sqlTxt .= " AND column_name='{$dbPath->field}'";
        }

        $fieldsRes = $this->m_DataBase->sql2resultSet($sqlTxt);
        if ($fieldsRes != null) {
            while ($fieldsRes->next()) {
                $field = $this->getFieldData(
                        $dbPath->schema,
                        !empty($dbPath->table) ? $dbPath->table : $dbPath->view,
                        $fieldsRes->getString("column_name")
                );
                $fields[] = $field;
            }
        }

        return $fields;
    }

    public function readTableNames(string $schema = ""): array {
        if ($schema == "") {
            $schema = "public";
        }
        $sqlTxt = "select tablename  from pg_tables  where schemaname = '"
                . $schema . "' order by tablename ";
        $tableNames = array();
        $tablesResult = $this->m_DataBase->sql2resultSet($sqlTxt);
        $tableNames = GenericHelper::resultSetToList($tablesResult);
        return $tableNames;
    }

    public function readViews(DatabasePath $dbPath): array {
        $sqlTxt = "select viewname,definition from pg_views where schemaname = '{$dbPath->schema}' order by viewname";
        $views = array();
        $tablesResult = $this->m_DataBase->sql2resultSet($sqlTxt);
        if ($tablesResult !== null) {
            while ($tablesResult->next()) {
                $view = new DatabaseView();
                $view->setDDL($tablesResult->getString("definition"));
                $view->setName($tablesResult->getString("viewname"));
                $tablePath = clone $dbPath;
                $tablePath->view = $view->getName();
                $view->setFields($this->readFields($tablePath));
                $views[] = $view;
            }
        }
        return $views;
    }

    public function getPrimaryKey($dbPath) {
        if ($dbPath['table'] == null) {
            throw new Exception("No table given");
        }
        $keys = $this->readKeys($dbPath);
        foreach ($keys as $key) {
            if ($key->isPrimary()) {
                return $key;
            }
        }
        return null;
    }

    public function readTables(DatabasePath $dbPath): array {
        $sqlTxt = "SELECT tablename FROM pg_tables WHERE schemaname = '{$dbPath->schema}' ORDER BY tablename";
        $tables = [];
        $tablesResult = $this->m_DataBase->sql2resultSet($sqlTxt);
        if ($tablesResult !== null) {
            while ($tablesResult->next()) {
                $table = new DatabaseTable();
                $table->setDDL("table DDL not set");
                $table->setName($tablesResult->getString("tablename"));
                $tablePath = clone $dbPath;
                $tablePath->table = $table->getName();
                $table->setFields($this->readFields($tablePath));
                $table->setForeignKeys($this->readForeignKeys($tablePath));
                $table->setPrimaryKey($this->getPrimaryKey($tablePath));
                $table->setIndices($this->readIndexes($tablePath));
                foreach ($this->readKeys($tablePath) as $key) {
                    if ($table->getUniqueKeys() === null) {
                        $table->setUniqueKeys([]);
                    }
                    if ($key->isUnique()) {
                        $table->getUniqueKeys()->add($key);
                    }
                }
                $tables[] = $table;
            }
        }
        return $tables;
    }

    public function readProcedures(DatabasePath $dbPath): array {
        $procedures = array();
        foreach ($this->readFunctions($dbPath) as $func) {
            $proc = new DatabaseProcedure();
            $proc->setName($func->getName());
            $proc->setParams($func->getParams());
            $proc->setReturnType($func->getReturnType());
            $proc->setDDL($func->getDDL());
            array_push($procedures, $proc);
        }
        return $procedures;
    }

    private function getFunctionDDL($schema, $functionName) {
        return "N/A";
    }

    public function readFunctions(DatabasePath $dbPath): array {
        $functionNames = $this->readFunctionNames($dbPath->schema);
        $functions = [];
        foreach ($functionNames as $funcName) {
            $f = new DatabaseFunction();
            $f->setDDL($this->getFunctionDDL($dbPath->schema, $funcName));
            $path = $dbPath->duplicate();
            $path->function = $funcName;
            $f->setComment($this->readComment($path));
            // $f->setArguments();
            // $f->setReturnValue();
            // $f->setOid();
            $f->setName($funcName);
            // TODO: set the function's DDL, parameters, return value, OID, etc.
            $functions[] = $f;
        }
        return $functions;
    }

    public function readTableColumns($schema, $table) {
        if ($schema == "") {
            $schema = "public";
        }
        $sqlTxt = "SELECT column_name " .
                " FROM information_schema.columns" .
                " WHERE  table_schema= '" . $schema .
                "' and table_name = '" . $table . "';";
        $tableNames = array();
        $tablesResult = $this->m_DataBase->sql2resultSet($sqlTxt);
        $tableNames = $this->resultSetToList($tablesResult);
        return $tableNames;
    }

    public function readFunctionNames($schema) {
        $database = $this->getM_DataBase()->getSettings()->getDataBase();
        $sql = "SELECT routine_name
            FROM information_schema.routines
            WHERE routine_catalog = '{$database}'
            AND routine_schema = '{$schema}'";
        $tablesResult = $this->getM_DataBase()->sql2resultSet($sql);
        return GenericHelper::resultSetToList($tablesResult);
    }

    public function readProcedureNames($schema) {
        return $this->readFunctionNames($schema);
    }

    private function getSequenceDDL($schema, $seqName) {
        return "N/A";
    }

    public function readSequences(DatabasePath $dbPath) {
        $sql = sprintf("select 
            s.cycle_option,
            s.data_type,
            s.\"increment\",
            s.maximum_value,
            s.minimum_value,
            s.numeric_precision,
            s.numeric_scale,
            s.numeric_precision_radix,
            s.sequence_catalog,
            s.sequence_name,
            s.sequence_schema
            from information_schema.sequences s
            where 
            s.sequence_schema = '%s'", $dbPath->schema);
        $rows = $this->m_DataBase->sql2resultSet($sql);
        $sequences = array();
        while ($rows->next()) {
            $seqPath = clone $dbPath;
            $seq = new DatabaseSequence();
            $seq->setName($rows->getString("sequence_name"));
            $seq->setComment($this->readComment($dbPath));
            // TODO: add any necessary sequence properties
            $seq->setCycle(($rows->getString("cycle_option") == null) ? false : true);
            $seq->setDDL($this->getSequenceDDL($dbPath->schema, $dbPath->sequence));
            $seq->setIncrement($rows->getLong("increment"));
            $seq->setMaxvalue($rows->getLong("maximum_value"));
            $seq->setMinvalue($rows->getLong("minimum_value"));
            $sequences[] = $seq;
        }
        return $sequences;
    }

    public function readConstraints(string $schema, string $tableName): array {
        $sqlTxt = "SELECT keyUsage.constraint_name " .
                " FROM information_schema.key_column_usage keyUsage " .
                " INNER JOIN information_schema.constraint_column_usage columnUsage " .
                " ON keyUsage.constraint_name = columnUsage.constraint_name " .
                " WHERE keyUsage.table_schema = '" . $schema . "' " .
                " AND keyUsage.table_name='" . $tableName . "' ";
        $constraints = array();
        $tablesResult = $this->m_DataBase->sql2resultSet($sqlTxt);

        $constraints = GenericHelper::resultSetToList($tablesResult);
        return $constraints;
    }

    public function getPkey(string $schema, string $table): ?string {
        $sql = "select c.conname::varchar as pkey " .
                " from pg_constraint c              " .
                " inner join pg_namespace n         " .
                " on n.oid = c.connamespace         " .
                " inner join pg_class r             " .
                " on r.oid=c.conrelid               " .
                " where contype='p'                 " .
                " and nspname='sch'            " .
                " and relname='tbl'               ";
        $sql = str_replace("sch", $schema, $sql);
        $sql = str_replace("tbl", $table, $sql);
        try {
            return $this->m_DataBase->sql2string($sql);
        } catch (Exception $e) {
            return null;
        }
    }

    private function isConstraintPkey(string $schema, string $constraintName): bool {
        $sql = "select count(1) as cnt " .
                " from pg_constraint c              " .
                " inner join pg_namespace n         " .
                " on n.oid = c.connamespace         " .
                " inner join pg_class r             " .
                " on r.oid=c.conrelid               " .
                " where contype='p'                 " .
                " and nspname='sch'            " .
                " and  c.conname::varchar='CONSTR'               ";
        $sql = str_replace("sch", $schema, $sql);
        $sql = str_replace("CONSTR", $constraintName, $sql);
        return (intval($this->m_DataBase->sql2string($sql)) > 0);
    }

    public function getPkeyFields($schema, $table) {
        $pkeyName = $this->getPkey($schema, $table);
        $sql = "SELECT column_name FROM information_schema.key_column_usage WHERE constraint_catalog = 'DB' AND constraint_schema = 'schName' AND constraint_name LIKE 'pkey' ORDER BY ordinal_position";
        $sql = str_replace("DB", $this->getM_DataBase()->getSettings()->getDataBase(), $sql);
        $sql = str_replace("schName", $schema, $sql);
        $sql = str_replace("pkey", $pkeyName, $sql);
        $rows = $this->getM_DataBase()->sql2resultSet($sql);
        $pkeyFields = array();
        if (!$rows) {
            return $pkeyFields;
        }
        while ($row = $rows->fetch_assoc()) {
            $pkeyFields[] = $row["column_name"];
        }
        return $pkeyFields;
    }

    public function getIndices(string $schema, string $table): array {
        $sql = "select r.relname from pg_index i
            inner join pg_class r on r.oid = i.indexrelid
            inner join pg_class r2 on r2.oid = i.indrelid
            inner join pg_namespace n on n.oid = r2.relnamespace
            where n.nspname = '{$schema}' and r2.relname = '{$table}'";

        $rows = $this->m_DataBase->sql2resultSet($sql);
        $indices = [];

        if ($rows) {
            while ($row = $rows->fetch_assoc()) {
                $indices[] = $row['relname'];
            }
        }

        return $indices;
    }

    public function getIndexFields($path) {
        if (!isset($path['index'])) {
            throw new Exception("Index name missing");
        }

        $ndx = $this->getIndex($path);
        $indexFields = array();
        foreach ($ndx->getFields() as $field) {
            $indexFields[] = $field->getFieldName();
        }
        return $indexFields;
    }

    public function getForeignKeys(DatabasePath $path) {
        $sql = "select c.conname::varchar as fkey " .
                " from pg_constraint c " .
                " inner join pg_namespace n " .
                " on n.oid = c.connamespace " .
                " inner join pg_class r " .
                " on r.oid=c.conrelid " .
                " where contype='f' " .
                " and nspname='" . $path->schema . "' " .
                " and relname='" . $path->table . "'";

        $sqlResult = $this->m_DataBase->sql2resultSet($sql);
        $fkeys = GenericHelper::resultSetToList($sqlResult);
        return $fkeys;
    }

    public function getFieldData($schemaName, $relName, $fieldName) {
        $field = new DatabaseField();
        $field->setFieldName($fieldName);
        $sql = "select * " .
                "from information_schema.columns " .
                "where table_schema='SCH' " .
                "and table_name = 'TBL' " .
                "and column_name = 'COL' " .
                "and table_catalog = 'CAT'";

        $sql = str_replace("SCH", $schemaName, $sql);
        $sql = str_replace("TBL", $relName, $sql);
        $sql = str_replace("COL", $fieldName, $sql);
        $sql = str_replace("CAT", $this->m_DataBase->getSettings()->getDataBase(), $sql);

        $row = $this->m_DataBase->sql2resultSet($sql);
        while ($row->next()) {
            if ($row->getString("column_default") !== null) {
                $field->setDefaultValue($row->getString("column_default"));
            }

            $isNullable = $row->getString("is_nullable");
            if (trim($isNullable) === "NO") {
                $field->setNullable(false);
            } else {
                $field->setNullable(true);
            }

            $pgDataType = strtolower(trim($row->getString("udt_name")));
            $genericDataType = $this->toGenericDataType($pgDataType);
            $field->setGenericDataType(trim($genericDataType));

            $topLevelDataType = $this->converter->nativeToTopLevel($pgDataType);
            $field->setPrecision(-1);
            if ($row->getString("data_type") === "character varying") {
                $field->setSize($row->getInt("character_maximum_length"));
            }

            if ($genericDataType === "double" || $genericDataType === "float") {
                $field->setPrecision($row->getInt("numeric_precision_radix"));
                if ($row->getString("numeric_scale") !== null) {
                    $field->setSize($row->getInt("numeric_scale"));
                }
            }

            $field->setInPrimaryKey($this->isFieldInPrimaryKey($schemaName, $relName, $fieldName));
            $field->setTableOrder($row->getInt("ordinal_position"));
            $field->setFieldType($pgDataType);
        }

        return $field;
    }

    public function getIndex(DatabasePath $path) {
        $sql = "SELECT i.relname as indname,
            i.relowner as indowner,
            idx.indrelid::regclass::varchar as relation,
            am.amname as indam,
            idx.indkey,
            ARRAY(SELECT pg_get_indexdef(idx.indexrelid, k + 1, true)
                FROM generate_subscripts(idx.indkey, 1) as k
                ORDER BY k) as indkey_names,
            idx.indexprs IS NOT NULL as indexprs,
            idx.indpred IS NOT NULL as indpred,
            idx.indisunique
        FROM pg_index as idx
        JOIN pg_class as i ON i.oid = idx.indexrelid
        JOIN pg_am as am ON i.relam = am.oid
        JOIN pg_namespace as ns ON ns.oid = i.relnamespace
            AND ns.nspname = 'sch'
            AND i.relname='ndxName'";
        $sql = str_replace("sch", $path->schema, $sql);
        $sql = str_replace("ndxName", $path->index, $sql);
        $resultSet = $this->m_DataBase->sql2resultSet($sql);
        if (!$resultSet) {
            return null;
        }
        $fields = array();
        $isIndexUnique = false;
        while ($row = $resultSet->fetch_assoc()) {
            $fieldList = $row["indkey_names"];
            $relationShema = explode(".", $row["relation"]);
            $isIndexUnique = $row["indisunique"];
            $relation = count($relationShema) > 1 ? $relationShema[1] : $relationShema[0];
            foreach ($fieldList as $fieldName) {
                $field = $this->getFieldData($path->schema, $relation, $fieldName);
                $fields[] = $field;
            }
        }
        //TODO: Fill in index information. For now, only fields are important.
        $ndx = new DatabaseIndex();
        $ndx->setName($path->index);
        $ndx->setParallel(false);
        $ndx->setDDL($this->getIndexDDL($path));
        $ndx->setIsUnique($isIndexUnique);
        $ndx->setIndexType("N/A");
        $ndx->setFields($fields);
        return $ndx;
    }

    public function getIndexDDL(DatabasePath $path) {
        return "N/A";
    }

    public function getSrcSchemaFromFkey($fkSchema, $fKey) {
        $sql = sprintf("SELECT DISTINCT unique_constraint_schema
                    FROM information_schema.referential_constraints
                    WHERE constraint_schema='%s'
                    AND constraint_name='%s'", $fkSchema, $fKey);
        return $this->m_DataBase->sql2string($sql);
    }

    public function getPkeyFromFkey($fkSchema, $fKey) {
        $sql = "select distinct unique_constraint_name,unique_constraint_schema \n" .
                "from information_schema.referential_constraints " .
                "where constraint_schema='%s' " .
                "and constraint_name='%s'";
        $sql = sprintf($sql, $fkSchema, $fKey);
        $rows = $this->m_DataBase->sql2resultSet($sql);
        $srcShema = "";
        $srcKey = "";
        while ($row = $rows->fetch(PDO::FETCH_ASSOC)) {
            $srcShema = $row['unique_constraint_schema'];
            $srcKey = $row['unique_constraint_name'];
            if ($this->isConstraintPkey($srcShema, $srcKey)) {
                return $srcKey;
            }
        }
        return $srcKey;
    }

    public function getTableFromPkey($schemaName, $pkeyName) {
        $sql = sprintf(
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
                " where contype='p'" .
                " and conname='%s' " .
                " and n.nspname='%s'",
                $pkeyName,
                $schemaName
        );
        return $this->m_DataBase->sql2string($sql);
    }

    public function getForeignKeyDestinationTable($fkSchema, $fKeyName) {
        $srcShema = $this->getSrcSchemaFromFkey($fkSchema, $fKeyName);
        $pkey = $this->getPkeyFromFkey($fkSchema, $fKeyName);

        $sql = sprintf("SELECT MIN(table_name) FROM information_schema.key_column_usage WHERE table_catalog='%s' AND table_schema='%s' AND constraint_name='%s'",
                $this->m_DataBase->getSettings()->getDataBase(),
                $fkSchema,
                $fKeyName
        );
        return $this->m_DataBase->sql2string($sql);
    }

    public function getForeignKeysFields($schema, $fKeyName, $sourceTable) {
        $dbName = $this->m_DataBase->getSettings()->getDataBase();
        if ($this->isConstraintPkey($schema, $fKeyName)) {
            throw new Exception("Primary key is supplied");
        }
        if ($sourceTable) {
            $sql = "select column_name " .
                    " from information_schema.constraint_column_usage  " .
                    " where table_catalog='DB' " .
                    " and   constraint_name='fKey' " .
                    " and  table_schema='SCH'";
            $sql = str_replace("DB", $dbName, $sql);
            $sql = str_replace("SCH", $schema, $sql);
            $sql = str_replace("fKey", $fKeyName, $sql);
        } else {
            $sql = sprintf(
                    " select column_name \n" .
                    " from information_schema.key_column_usage\n" .
                    " where constraint_name='%s'\n" .
                    " and constraint_schema='%s'\n" .
                    " and constraint_catalog='%s'" .
                    " order by position_in_unique_constraint",
                    $fKeyName,
                    $schema,
                    $this->m_DataBase->getSettings()->getDataBase()
            );
        }
        $rows = $this->m_DataBase->sql2array($sql);
        $columns = array_map(function ($row) {
            return $row['column_name'];
        }, $rows);
        return $columns;
    }

    public function getForeignKeySourceTableName($destSchemaName, $fKeyName) {
        $sql = sprintf("" .
                " select " .
                " r2.relname as src_table " .
                //"r1.relname as  dest_table,n.nspname as dest_schema,\n" +
                //"r2.relname as src_table,n2.nspname as  src_schema,conname \n" +
                " \n" .
                " from\n" .
                " pg_constraint  c\n" .
                " inner join pg_namespace n\n" .
                " on n.oid = c.connamespace\n" .
                " \n" .
                " inner join pg_class r1 \n" .
                " on r1.oid = c.conrelid\n" .
                " inner join pg_class r2 \n" .
                " on r2.oid = c.confrelid\n" .
                " \n" .
                " inner join pg_namespace n2\n" .
                " on n2.oid = r2.relnamespace\n" .
                " \n" .
                " where contype='f' " .
                " and conname='%s' " .
                " and n.nspname='%s' ", $fKeyName, $destSchemaName);
        return $this->m_DataBase->sql2string($sql);
    }

    private function getFunctionData(string $schema, string $functionName): DatabaseFunction {
        $sql = ""; // Replace with the SQL query to fetch the function data
        $row = $this->m_DataBase->sql2resultSet($sql);
        $func = new DatabaseFunction();
        $func->setName($functionName);
        $func->setDDL("N/A");
        //func->setReturnValue(DatabaseTypeEnum);
        //TODO: Fill in all the information by finding the appropriate query
        return $func;
    }

    public function getFunctions(string $schema): array {
        $functionNames = $this->readFunctionNames($schema);
        $functions = array();
        foreach ($functionNames as $func) {
            $f = $this->getFunctionData($schema, $func);
            $functions[] = $f;
        }
        return $functions;
    }

}

<?php

abstract class ADbElemetReader implements IDbElementReader {
    protected $converter;

    protected $targetSchemas = [];

    protected $m_DataBase;

    private $followDepth;

    public function getTargetSchemas() {
        return $this->targetSchemas;
    }

    public function setTargetSchemas($targetSchemas) {
        $this->targetSchemas = $targetSchemas;
    }

    public function getM_DataBase() {
        return $this->m_DataBase;
    }

    public function fromGenericDataType($dataType) {
        return $this->converter->genericToNatives($dataType)[0];
    }

    public function toGenericDataType($dataType) {
        return $this->converter->nativeToGeneric($dataType);
    }

    public function setFollowConstraintsToOtherSchemasDepth($followDepth) {
        $this->followDepth = $followDepth;
    }

    public function getFollowConstraintsToOtherSchemasDepth() {
        return $this->followDepth;
    }

    public abstract function ReadFromDataBase();

    public abstract function readSchemas();

    public abstract function readSchema($schemaName);

    public abstract function readTargetSchemas();

    public abstract function readKeys($dbPath);

    public abstract function readForeignKeys($dbPath);

    public abstract function readIndexes($dbPath);

    public abstract function readFields($dbPath);

    public abstract function readViews($dbPath);

    public abstract function readTables($dbPath);

    public abstract function readProcedures($dbPath);

    public abstract function readFunctions($dbPath);

    public abstract function readComment($dbPath);

    public abstract function readSchemasList();

    public abstract function readTableNames($schema);

    public abstract function readTableColumns($schema, $table);

    public abstract function readFunctionNames($schema);

    public abstract function readProcedureNames($schema);

    public abstract function readConstraints($schema, $table);

    public abstract function readSequences($dbPath);

    public abstract function getPkey($schema, $table);

    public abstract function getPkeyFields($schema, $table);

    public abstract function getIndices($schema, $table);

    public abstract function getForeignKeys($path);

    public abstract function getIndex($path);

    public abstract function getForeignKeysFields($schema, $fKeyName, $sourceTable);

    public abstract function getForeignKeySourceTableName($schemaName, $fKeyName);

    public abstract function getFunctions($schema);

    public abstract function getIndexFields($path);
}

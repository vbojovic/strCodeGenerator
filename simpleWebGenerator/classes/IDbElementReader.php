<?php

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Scripting/PHPInterface.php to edit this template
 */

/**
 *
 * @author IRB
 */
interface IDbElementReader {
    public function fromGenericDataType($dataType);
    public function toGenericDataType($dataType);
    public function getTargetSchemas();
    public function setTargetSchemas($targetSchemas);
    public function ReadFromDataBase();

    public function readSchemas();
    public function readSchema($schemaName);
    public function readTargetSchemas();
    public function readKeys($dbPath);
    public function readForeignKeys($dbPath);
    public function readIndexes($dbPath);
    public function readFields($dbPath);
    public function readViews($dbPath);
    public function readTables($dbPath);
    public function readProcedures($dbPath);
    public function readFunctions($dbPath);
    public function readComment($dbPath);
    public function readSchemasList();
    public function readTableNames($schema);
    public function readTableColumns($schema, $table);
    public function readFunctionNames($schema);
    public function readProcedureNames($schema);
    public function readConstraints($schema, $table);
    public function readSequences($dbPath);
    public function getPkey($schema, $table);
    public function getPkeyFields($schema, $table);
    public function getIndices($schema, $table);
    public function getForeignKeys($path);
    public function getIndex($path);
    public function getForeignKeysFields($schema, $fKeyName, $sourceTable);
    public function getForeignKeySourceTableName($schemaName, $fKeyName);
    public function setFollowConstraintsToOtherSchemasDepth($followDepth);
    public function getFollowConstraintsToOtherSchemasDepth();
    public function getFunctions($schema);
    public function getIndexFields($path);
}
?>


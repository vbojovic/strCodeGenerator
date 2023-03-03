<?php
interface IDbManager{
    public function getTables($schema,$regex);
    public function getFields($schema,$table);
    public function getIndices($schema,$table);
    public function getPKeys($schema,$table);
    public function getFKeys($schema,$table);
    public function getSchemas($schema,$table);
    public function getFunctions($schema);
    public function getProcedures($schema);
    public function dropTable($schema,$table);
    public function createTable($fields,$types);
    public function createModel();
    public function serializeModel();
}
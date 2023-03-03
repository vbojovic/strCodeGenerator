<?php 
class DatabasePath {
    public $table;
    public $database;
    public $schema;
    public $view;
    public $function;
    public $procedure;
    public $sequence;
    public $field;
    public $index;

//    public function __construct() {}


    public function __construct($schema, $table, $field) {
        if ($schema != null && $table!=null){
            $this->schema = $schema;
            $this->table = $table;
        }
        if ($field != null) $this->field = $field;
    }

    public function duplicate() {
        return clone $this;
    }
}

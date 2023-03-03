<?php

class DatabaseSettings {

    private $dataBaseType = "postgres";
    private $login;
    private $pass;
    private $port = 0;
    private $host;
    private $dataBase;
    private $schema;

    public function getDataBaseType() {
        return $this->dataBaseType;
    }

    public function setDataBaseType($dataBaseType) {
        $this->dataBaseType = $dataBaseType;
    }

    public function getLogin() {
        return $this->login;
    }

    public function setLogin($login) {
        $this->login = $login;
    }

    public function getPass() {
        return $this->pass;
    }

    public function setPass($pass) {
        $this->pass = $pass;
    }

    public function getPort() {
        if ($this->port == 0) {
            $this->port = $this->getDataBaseType()->getDefaultPort();
        }

        return $this->port;
    }

    public function setPort($port) {
        $this->port = $port;
    }

    public function getHost() {
        return $this->host;
    }

    public function setHost($host) {
        $this->host = $host;
    }

    public function getDataBase() {
        return $this->dataBase;
    }

    public function setDataBase($dataBase) {
        $this->dataBase = $dataBase;
    }

    public function saveToFile($fName) {
        file_put_contents($fName, serialize($this));
    }

    public function loadFromFile($fName) {
        $dbs = unserialize(file_get_contents($fName));
        $this->login = $dbs->login;
        $this->pass = $dbs->pass;
        $this->port = $dbs->port;
        $this->host = $dbs->host;
        $this->dataBase = $dbs->dataBase;
    }

    public function getSchema() {
        return $this->schema;
    }

    public function setSchema($schema) {
        $this->schema = $schema;
    }

}

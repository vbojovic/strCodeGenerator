<?php

class StreamCodeGenerator {

    public static function main($args) {
        try {
            $paramHelp = self::getHelp();
            $keys = self::loadParams($args, "--", $paramHelp, true);

            if (empty($args) || count($args) === 0) {
                echo $paramHelp;
                //GeneratorsClass gc = new GeneratorsClass();
                //gc.runPhpGeneratorOld("C:\\temp\\out");
                return;
            }

            self::runApp2($keys);
        } catch (Exception $ex) {
            echo "Error: " . $ex->getMessage();
        }
    }

    private static function getHelp() {
        return <<<HELP
            java -jar streamCodeGenerator.jar <param1>...<paramN>
                --format        [json|xml]         export schema to template (default=JSON)
                --file          <filename>         export to filename (default = out.json)
                --sch           [schema|all]       included schema (default = all)
                --public        [true/false]       autoinclude public    (N/A)
                --db            <database name>    database
                --login         <username>         login
                --pass          <password>         password
                --port          <port>             port
                --dtype         [mysql,pg]         database type  (default = pg)
                --host          <hostname>         server name or address (default = localhost)
                --folowDepth    <true/false>       folow constraints to tables in another schemas depth
                                                (0 = follow all,
                                                1= first table,
                                                2 first and second) N/A
HELP;
    }

    private static function loadParams($args, $prefix, $paramHelp, $throwException) {
        $keys = array();
        $i = 0;
        while ($i < count($args)) {
            if (substr($args[$i], 0, strlen($prefix)) === $prefix) {
                $key = substr($args[$i], strlen($prefix));
                if (isset($keys[$key])) {
                    throw new Exception(sprintf("Duplicate argument: %s", $key));
                }
                $i++;
                if ($i >= count($args)) {
                    if ($throwException) {
                        throw new Exception(sprintf("Missing value for argument: %s", $key));
                    }
                    break;
                }
                $value = $args[$i];
                $keys[$key] = $value;
            }
            $i++;
        }
        if ($throwException && $i === count($args)) {
            throw new Exception("Missing arguments");
        }
        return $keys;
    }

    function getSettingsFromKeys($keys) {
        if (!array_key_exists('login', $keys)) {
            throw new Exception('login needed');
        }
        $login = $keys['login'];

        if (!array_key_exists('pass', $keys)) {
            throw new Exception('pass needed');
        }
        $pass = $keys['pass'];

        if (!array_key_exists('db', $keys)) {
            throw new Exception('database needed');
        }
        $database = trim($keys['db']);

        $hostname = array_key_exists('host', $keys) ? strtolower(trim($keys['host'])) : 'localhost';

        $outFile = array_key_exists('file', $keys) ? trim($keys['file']) : 'out.json';
        if (file_exists($outFile)) {
            throw new Exception('output file already exists');
        }

        $schema = 'public';
        if (array_key_exists('sch', $keys)) {
            $schema = trim($keys['sch']);
        }

        $databaseType = 'pg';
        if (array_key_exists('dtype', $keys)) {
            $databaseType = strtolower(trim($keys['dtype']));
            $types = explode(',', 'mysql,pg');
            if (!in_array($databaseType, $types)) {
                throw new Exception('Unknown database type ' . $databaseType);
            }
        }

        if ($databaseType == 'mysql') {
            $schema = $database;
        }

        $port = 0;
        if (array_key_exists('port', $keys)) {
            $port = intval($keys['port']);
        } else {
            if ($databaseType == 'pg') {
                $port = 5432;
            } else if ($databaseType == 'mysql') {
                $port = 3306;
            }
        }

        $settings = new DatabaseSettings();
        $settings->setDataBase($database);

        if (strcasecmp($databaseType, 'pg') === 0) {
            $settings->setDataBaseType(DatabaseTypeEnum::postgres);
        } else if (strcasecmp($databaseType, 'mysql') === 0) {
            $settings->setDataBaseType(DatabaseTypeEnum::mysql);
        } else {
            throw new Exception('not supported database type');
        }

        $settings->setHost($hostname);
        $settings->setLogin($login);
        $settings->setPass($pass);
        $settings->setPort($port);
        $settings->setSchema($schema);

        return $settings;
    }

    public static function runApp2(array $keys) : void {
        try {
            $settings = self::getSettingsFromKeys($keys);
            $format = "json";
            if (array_key_exists("format", $keys)) {
                $format = strtolower(trim($keys["format"]));
                $formats = ["json", "xml"];
                if (!in_array($format, $formats)) {
                    throw new Exception("Unknown format " . $format);
                }
            }

            $db = null;
            if ($settings->getDataBaseType() == DatabaseTypeEnum::postgres()) {
                $db = new PgDb();
            } else if ($settings->getDataBaseType() == DatabaseTypeEnum::mysql()) {
                $db = new MysqDb();
            } else {
                throw new Exception("not supported database type");
                //TODO N/A treban implementirat ostale baze
            }

            $db->setSettings($settings);
            $db->connect();

            $dbm = new DatabaseModel();

            $schema = "";
            $schema = $settings->getSchema();
            $schemas = array();
            if ($schema !== "") {
                $schemas = $db->getElementReader()->readSchemas();
            } else {
                $schemas[] = $db->getElementReader()->readSchema($schema);
            }
            $dbm->setSchemas($schemas);

            $outFile = array_key_exists("file", $keys) ? trim($keys["file"]) : "out.json";
            if (file_exists($outFile)) {
                throw new Exception("output file allready exists");
            }

            if ($format == "json") {
                $dbm->saveToJson($outFile);
            } else {
                $dbm->saveToXml($outFile);
            }

            $db->disconnect();
        } catch (Exception $e) {
            echo $e->getMessage();
        }
    }

    public static function loadSchema(IDb $db, DatabaseSettings $settings): DatabaseSchema {
        $schema = new DatabaseSchema();
        $schemaName = $settings->getSchema();
        $schema->setSchemaName($schemaName);
        $schema->setComment("");
        $schema->setDDL("");

        $dbPath = new DatabasePath();
        $dbPath->schema = $schemaName;
        $dbPath->database = $settings->getDataBase();

        $schema->setSchemaName($schemaName);
        $schema->setTables($db->getElementReader()->readTables($dbPath));
        $schema->setComment("N/A");
        $schema->setDDL("N/A");
        $schema->setFunctions($db->getElementReader()->getFunctions($dbPath->schema));
        if (!($db->getDbType() === DatabaseTypeEnum::pgOld || $db->getDbType() === DatabaseTypeEnum::postgres)) {
            $schema->setProcedures($db->getElementReader()->readProcedures($dbPath));
        }
        $schema->setFunctions($db->getElementReader()->readFunctions($dbPath));
        return $schema;
    }

}

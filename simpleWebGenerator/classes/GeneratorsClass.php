<?php

class GeneratorsClass {
    
    public static function runPhpGeneratorOld($projectPath) {
        $db = new PgDb();
        $p = new PgDb();
        $settings = new DatabaseSettings();
        $configPath = $projectPath . "\\dbSettings.xml";
        $settings->loadFromFile($configPath);
        $db->setSettings($settings);
        
        $dbModel = $db->getModel();
        
        $phpOldGen = new PhpGeneratorOld4Pg();
        $phpOldGen->setDbModel($dbModel);
        $phpOldGen->setOutputPath($projectPath . DIRECTORY_SEPARATOR . "out");
        $phpOldGen->writeAll();
    }
}
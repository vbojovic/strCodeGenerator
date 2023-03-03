<?php
class PropertiesReader {
//    private static Logger logger = Logger.getLogger(PropertiesReader.class);
    private static $instance = null;

    private $propertiesHash = array();

    // podatak kad je pojedini file zadnji put mijenjan
    private $propertiesLastModifiedHash = array();

    private function __construct() {}

    public static function getInstance()
    {
        if (self::$instance == null)
        {
            self::$instance = new PropertiesReader();
        }
        return self::$instance;
    }

    public function getProperties($propFilePath)
    {
        // cuvaj podatke kad je svaki od property file-ova zadnji put mijenjan
        $datePropFileModified = $this->propertiesLastModifiedHash[$propFilePath];
        // dohvati URL trazenog file-a
        $url = $this->getClass()->getResource($propFilePath);
        if ($url == null)
        {
            throw new RuntimeException("Property file " . $propFilePath . " nije pronadjen!");
        }

        $propFile = null;
        $prop = $this->propertiesHash[$propFilePath];
        try
        {
            $propFile = new File($url.toString());
//            logger.debug("URL:" + url.toString());
            $lastModified = filemtime($propFile);

            // ako u memoriji nema tog file-a ili se u medjuvremenu promijenio na disku, ucitaj file ponovo
            if ($prop == null || $datePropFileModified != null && $datePropFileModified != $lastModified)
            {
                // ovo je singleton klasa, ne smije se desiti da vise threadova ide istovremeno citati file
                // i mijenjati prop objekt. ovo se eventualno moze desiti ako vise threadova istovremeno pozove
                // metode ali samo kod prije nego je prvi put kreirana
                synchronized ($this)
                {
                    $is = fopen($url, 'r');
                    $prop = array();
                    try
                    {
                        while (($line = fgets($is)) !== false) {
                            $line = trim($line);
                            if (substr($line, 0, 1) == '#' || substr($line, 0, 1) == ';') {
                                continue;
                            }
                            $pos = strpos($line, '=');
                            if ($pos === false) {
                                continue;
                            }
                            $key = trim(substr($line, 0, $pos));
                            $value = trim(substr($line, $pos+1));
                            $prop[$key] = $value;
                        }
                    }
                    catch (IOException $e)
                    {
                        throw new RuntimeException($e);
                    }
                    try
                    {
                        fclose($is);
                    }
                    catch (IOException $e)
                    {
//                        logger.error(e.getMessage(), e);
                    }
                }

                // hashtable je synchronized pa ovo ne mora biti u syncronized bloku
                $this->propertiesHash[$propFilePath] = $prop;
                $this->propertiesLastModifiedHash[$propFilePath] = $lastModified;
            }
        }
        catch (IOException $e)
        {
            throw new RuntimeException("Greska pri dohvatu property file-a: " . $propFilePath, $e);
        }

        return $prop;
    }
}
?>
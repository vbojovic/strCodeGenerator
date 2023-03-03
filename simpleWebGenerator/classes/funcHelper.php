<?php

class funcHelper {

    public static function getCanonicalDateFromStringAndLogMessage($srcVar,&$destVar,$dateFormat,&$boolStatus){
        $regexFormat = $dateFormat;
        $regexFormat = str_replace('y', '\d', $regexFormat);
        $regexFormat = str_replace('m', '\d', $regexFormat);
        $regexFormat = str_replace('d', '\d', $regexFormat);
        $regexFormat = str_replace('.', '\.', $regexFormat);
        //ovdi moze bit konflikt izmedju js i php radi razlicitog imenovanja
        //http://php.net/manual/en/datetime.createfromformat.php
        if (preg_match("/^".$regexFormat."$/",$srcVar)){
            //sad kad iman datum string moram ga pocupat vanka
        }else{
            $boolStatus = false;
        }
    }

    public static function isAssoc($arr)
    {
        return array_keys($arr) !== range(0, count($arr) - 1);
    }

    public static function csvToArray($pathToCsv,$toMap = false,$separator,$cellQuote){
        ini_set('auto_detect_line_endings',false);
        $file = fopen($pathToCsv,"r");
        $rowNames = array();
        $rowNum = 0;
        while(! feof($file)) {
            $row = fgetcsv($file,0,$separator,$cellQuote);

            if ($rowNum == 0 && $toMap==true) {
                $rowNames = $row;
            }
            else 
            {
                $map  = array();
                if ($toMap==true){
                    for ($i =0 ; $i < count($rowNames) ; $i++){
                        $rowName = $this->rowNames[$i];
                        $map[$rowName] = $row[$i];
                    }
                } else{
                    $map=$row;
                }

                $csvMatrix[]=$map;
            }
            $rowNum++;

        }
        fclose($file);
        return $csvMatrix;
    }

    public static function arrayToCsv($arr,$pathToCsv,$overWrite = true,$headerNamesArr){
        if ($arr == null || count($arr)==0) return;
        if ($overWrite) unlink($pathToCsv);

        $fp = fopen($pathToCsv, 'w');
            if ($headerNamesArr != null && count($headerNamesArr)>0) 
                fputcsv($fp, $headerNamesArr,$separator,$cellQuote);

            foreach ($arr as $fields) {
                fputcsv($fp, $fields,$separator,$cellQuote);
            }

        fclose($fp);
    }
    /**
     * [dateTimePickerDefaultToDateTime converts from format "2015/02/12 22:46" to yyyy-mm-dd or HH:mm]
     * @param  [type] $str    [description]
     * @param  [type] $toTime [description]
     * @return [type]         [description]
     */
    public static function dateTimePickerDefaultToDateTime($str, $toTime){
        if ($toTime == true){
            $time = explode(' ',$str);
            $time = trim($time[1]);
            return (preg_match("/^\d\d:\d\d$/",$time))? $time : null;
        }else{
            $date = explode(' ',$str);
            $date = trim($date[0]);
            $date = str_replace('/', '-', $date);
            return (preg_match("/^\d\d\d\d-\d\d-\d\d$/",$date))? $date : null;
        }
    }
    public static function uploadFile($componentName, $destination) {
        if (!isset($_FILES[$componentName]['name']))
            return;
        move_uploaded_file($_FILES[$componentName]['tmp_name'], $destination);
    }
    public static function getKeysMatching($map,$regex){
        $myMap = array();
        // print_r($map);
        foreach ($map as $key => $value) {
            // print_r( preg_match($regex,$key));
            if (preg_match($regex,$key)==1){
                $myMap[$key] = $value;
            }
        }
        return $myMap;
    }     
    
    public static function addPrefixAndSufixToKeyAndValue($map,$prefix,$sufix){
        $newMap = array();
        foreach ($map as $key => $value) {
            $newMap[$prefix.$key.$sufix] = $prefix.$value.$sufix;
        }
        return $newMap;
    }
    public static function addPrefixAndSufixToKeyOrValue($map,$prefix,$sufix,$isKey){
        $newMap = array();
        if ($isKey){
            foreach ($map as $key => $value) {
                $newMap[$prefix.$key.$sufix] = $value;
            }
        }else{
            foreach ($map as $key => $value) {
                $newMap[$key] = $prefix.$value.$sufix;
            }
        }
        return $newMap;
    }
    public static function removeKeysMatching($map,$regex){
        // return $map;
        foreach ($map as $key => $value) {
            // print_r( preg_match($regex,$key));
            if (preg_match($regex,$key)==1){
                unset($map[$key]);
            }
        }
        return $map;
    }
    public static function sqlBoolAttribs2PgCases($attribnamesArray){
        $translatedElems = array();
        foreach ($attribnamesArray as $element) {
            $translatedElems[]=" case when {$element}='f' then 'false' when {$element}='t' then 'true' else null end as $element ";
        }
        return join(',',$translatedElems);
    }

    //TODO ova metoda ne radi.!
    public static function cloneArray($old){
        $new = array();
        foreach ($old as $k => $v) {
            $new[$k] = clone $v;
        }
        return $new;
        // $new =  ArrayObject::getArrayCopy($old);
        // // print_r($new);
        // return $new;
    }
    public static function hashMapTo2Options($hm){
        $cols = array(); 
        
        foreach(array_keys($hm) as $key){
            $cols[]= array ( "Value" => $key , "DisplayText" => $hm[$key] );
        }

        return $cols;
    }
    public static function hashMapTo2Cols($hm){
        $cols = array();
        // foreach(array_keys($hm) as $key){
        //     $cols['key'][]=$key;
        //     $cols['value'][]=$hm[$key];
        // }
        
        foreach(array_keys($hm) as $key){
            $cols[]= array ( "key" => $key , "value" => $hm[$key] );
        }

        return $cols;
    }
    // public static function optionPurgeEmptyDisplayText($hm){
    //     foreach ($hm as $key => $value) {
    //         if ($value['DisplayText'] == '' || $value['DisplayText'] == null) unset($hm[$key]);
    //         // echo "{$key}->'{$value}'";
    //     }
    //     // print_r($hm);
    //     return $hm;
    // }

    public static function hashMapPurgeEmptyValues($hm){
        foreach ($hm as $key => $value) {
            if ($value == '' || $value == null) unset($hm[$key]);
            // echo "{$key}->'{$value}'";
        }
        // print_r($hm);
        return $hm;
    }

    public static function getExtensionOfUploadingFile($componentName) {
//        print_r($_FILES);
        if (!isset($_FILES[$componentName]['name']))
            return null;
        
        $type = $_FILES[$componentName]['type'];
        
        switch ($type){
            case 'image/jpeg':
                return 'jpg';
                break;
            case 'image/png':
                return 'png';
                break;
            case 'image/gif':
                return 'gif';
                break;
        }
        return;
    }

    public static function isFileImage($filePath) {
        return (@!is_array(getimagesize($filePath)));
    }

    public static function isFileJpg($filePath) {
        //return (exif_imagetype($filePath) == IMAGETYPE_JPEG);
        $ext = pathinfo($filePath, PATHINFO_EXTENSION);
        return ($ext == 'jpg');
    }

    public static function isFilePng($filePath) {
//            echo "jeli png:".exif_imagetype($filePath);
//            echo "$filePath";
        $ext = pathinfo($filePath, PATHINFO_EXTENSION);
        return ($ext == 'png');
//        return (exif_imagetype($filePath) == IMAGETYPE_PNG);
    }

    public static function png2jpeg($inputFile, $outFile) {
        $im = imagecreatefrompng($inputFile);
        imagejpeg($im, $outFile);
    }

    public static function resizeImage($newWidth, $targetFile, $originalFile) {

        $info = getimagesize($originalFile);
        $mime = $info['mime'];
//        echo $originalFile;
//        echo $targetFile;
//        print_r($info);
        switch ($mime) {
            case 'image/jpeg':
                $image_create_func = 'imagecreatefromjpeg';
                $image_save_func = 'imagejpeg';
                $new_image_ext = 'jpg';
                break;

            case 'image/png':
                $image_create_func = 'imagecreatefrompng';
                $image_save_func = 'imagepng';
                $new_image_ext = 'png';
                break;

            case 'image/gif':
                $image_create_func = 'imagecreatefromgif';
                $image_save_func = 'imagegif';
                $new_image_ext = 'gif';
                break;

            default:
                throw Exception('Unknown image type.');
        }

        $img = $image_create_func($originalFile);
        list($width, $height) = getimagesize($originalFile);

        $newHeight = ($height / $width) * $newWidth;
        $tmp = imagecreatetruecolor($newWidth, $newHeight);
        imagecopyresampled($tmp, $img, 0, 0, 0, 0, $newWidth, $newHeight, $width, $height);

        if (file_exists($targetFile)) {
            unlink($targetFile);
        }
        $image_save_func($tmp, "$targetFile.$new_image_ext");
    }

//
//    public static function resizeJpgImage($imgPath, $w, $h, $destFile) {
//        if (!self::isFileImage($imgPath))
//            return;
//        if (!self::isFileJpg($imgPath))
//            return;
//        $src = imagecreatefromjpeg($imgPath);
//        list($width, $height) = getimagesize($imgPath);
//
////        $tmp = imagecreatetruecolor($w, $h); 
//        imagecopyresampled($tmp, $src, 0, 0, 0, 0, 800, 600, $width, $height);
//    }

    public static function getTempDir() {
        return sys_get_temp_dir();
    }

    /**
     * 
     * @param type $imgPath
     * @param type $w
     * @param type $h
     * @param type $destFile
     * @return type
     */
    public static function resizeJpgOrPngAndSaveToPNG($imgPath, $w, $h, $destFile) {
//        if (!self::isFileImage($imgPath)) //TODO funkcija ne radi
//            return;
        if (!self::isFilePng($imgPath) && !self::isFileJpg($imgPath))
            return;
        if (self::isFilePng($imgPath)) {
            $randomName = self::getTempDir() . DIRECTORY_SEPARATOR . rand(100, 1000) . ".png";
            self::png2jpeg($imgPath, $randomName);
            self::resizeImage($w, $destFile, $randomName);
            //self::resizeJpgImage($randomName, $w, $h, $destFile);
            unlink($randomName);
        } else {
//            self::resizeJpgImage($randomName, $w, $h, $destFile);
            self::resizeImage($w, $destFile, $imgPath);
        }
    }

    public static function isUid($uid) {
        return preg_match('/^\{?[A-Z0-9]{8}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{12}\}?$/', strtoupper($uid));
//        if (preg_match('/^\{?[A-Z0-9]{8}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{12}\}?$/', strtoupper($uid))) {
//            return TRUE;
//        } else {
//            return FALSE;
//        }
    }

    public static function intFromPost($varName, $defaultVal) {
        return (int) ((isset($_POST[$varName]) && (is_numeric($_POST[$varName])) ? $_POST[$varName] : $defaultVal));
    }

    public static function intFromPostOrDie($varName, $msgTemplateStr) {
        if (!isset($_POST[$varName]) or is_numeric($_POST[$varName]) == FALSE)
            die(str_replace("{{msg}}", $varName, $msgTemplateStr));
        return (int) ($_POST[$varName]);
    }

    public static function boolFromPost($varName, $defaultVal) { //TODO moram vidit kako handlat null jer ne znan jel null=false!
        if (!isset($_POST[$varName]))
            return $defaultVal;
        $postedValue = strtolower(trim($_POST[$varName]));
        if ($postedValue == '1' || $postedValue == 1 || $postedValue == 't' || $postedValue == 'true' || $postedValue == 'y' || $postedValue == 'yes')
            return TRUE;
        if ($postedValue == '0' || $postedValue == 0 || $postedValue == 'f' || $postedValue == 'false' || $postedValue == 'n' || $postedValue == 'no')
            return FALSE;
        return $defaultVal;
    }

    public static function boolFromPostOrDie($varName, $message) { //TODO moram vidit kako handlat null jer ne znan jel null=false!
        if (!isset($_POST[$varName]))
            die($message);
        $postedValue = strtolower(trim($_POST[$varName]));
        if ($postedValue == '1' || $postedValue == 1 || $postedValue == 't' || $postedValue == 'true' || $postedValue == 'y' || $postedValue == 'yes')
            return TRUE;
        if ($postedValue == '0' || $postedValue == 0 || $postedValue == 'f' || $postedValue == 'false' || $postedValue == 'n' || $postedValue == 'no')
            return FALSE;
        die($message);
    }

    public static function strFromPost($varName) {
        if (!isset($_POST[$varName]))
            return '';
        return addslashes(trim($_POST[$varName]));
    }

    public static function strFromPostOrDie($varName, $msgTemplateStr) {
        if (!isset($_POST[$varName]))
            die(str_replace("{{msg}}", $varName, $msgTemplateStr));
        return addslashes(trim($_POST[$varName]));
    }

    public static function getTimeInSeconds() {
        
    }

    public static function getUnixTimeInSeconds() {
        
    }

    public static function validateIpAddress($ip_addr) {
        //first of all the format of the ip address is matched
        if (preg_match("/^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/", $ip_addr)) {
            //now all the intger values are separated
            $parts = explode(".", $ip_addr);
            //now we need to check each part can range from 0-255
            foreach ($parts as $ip_parts) {
                if (intval($ip_parts) > 255 || intval($ip_parts) < 0)
                    return false; //if number is not within range of 0-255
            }
            return true;
        } else
            return false; //if format of ip address doesn't matches
    }

    public static function getFilesInDirectory($pathToDirectory) {
        $files = array();
        if ($handle = opendir($pathToDirectory)) {
            while (false !== ($file = readdir($handle))) {
                if (!is_dir($pathToDirectory . "/" . $file)) {
                    $files[] = $file;
                }
            }
            closedir($handle);
        }
        return $files;
    }

    public static function getSpecificFilesFromDirectory($dir, $extension) {
        $files = preg_grep("/{$extension}/", scandir($dir));
        return $files;
    }

    public static function get_keys($ar) {
        foreach ($ar as $k => $v) {
            $keys[] = $k;
        }
        return $keys;
    }

    public static function objectToArray($d) {
        if (is_object($d)) {
            $d = get_object_vars($d);
        }
        if (is_array($d)) {
            return array_map(__FUNCTION__, $d);
        } else {
            return $d;
        }
    }

    public static function isIntArray($arr) {
        $ret = true;
        if (count($arr) == 0 || $arr == '')
            return false;
        foreach ($arr as $val)
            if (!is_int($val))
                return false;
        return $ret;
    }
    
    public static function setBooleanDataFromPOST($fieldNameArr,&$msg,&$resultValid){
        for ($x =0 ; $x<count($fieldNameArr); $x++){
            $fieldName= $fieldNameArr[$x];
            if (isset($_POST[$fieldName]) && $_POST[$fieldName] != "") {
                $inputData[$fieldName] = $_POST[$fieldName];
                $inputData[$fieldName] = ($inputData[$fieldName] == '' || $inputData[$fieldName] == null || $inputData[$fieldName] == '0' || $inputData[$fieldName] == false || strtolower($inputData[$fieldName]) == 'f' || strtolower($inputData[$fieldName]) == 'false') ? FALSE : TRUE;
                ;
            } else {
                $resultValid = false;
                $msg=" field {$fieldName} is not valid";
            }
        }
    }
    
    /**
     * 
     * @param type $variableName
     * @param type $arrayToStore
     * @param type $skipIfEmpty
     * @param type $format
     * @param type $isRequired
     * @param type $booleanStatus
     * @param type $message
     */
    public static function getData($variableName, &$arrayToStore, $skipIfEmpty, $format, $isRequired, &$booleanStatus, &$message, $isPost) {
        if ($format == '' || $format == null) {
            $booleanStatus = false;
            $message = "format not defined";
            return;
        } elseif ($variableName == null || $variableName == '') {
            $booleanStatus = false;
            $message = "variable name not set";
            return;
        }
        if ($isPost == true) {
            $exists = (isset($_POST[$variableName]));
            $varValue = (isset($_POST[$variableName])) ? $_POST[$variableName] : null;
        } else {
            $exists = (isset($_GET[$variableName]));
            $varValue = (isset($_GET[$variableName])) ? $_GET[$variableName] : null;
        }
        if ($exists == false && $isRequired == false && $skipIfEmpty == FALSE) {
            $arrayToStore[$variableName] = null;
        } elseif ($isRequired == true && $exists == false) {
            $booleanStatus = false;
            $message = 'No value provided';
            return;
        } elseif ($exists == false && $skipIfEmpty == true && $isRequired == FALSE) {
            return;
        } elseif ($exists == true) {
//            $booleanStatus = TRUE;
            $format = strtolower($format);
            if ($format == "text" || $format == "varchar" || $format == 'char' || $format == "string") {
                $varValue = addslashes($varValue);
                $arrayToStore[$variableName] = $varValue;
                return;
            } elseif ($format == 'int' || $format == 'smallint' || $format == 'integer' || $format == 'bigint') {
                $varValue = trim($varValue);
                if ($varValue != '0' && ((int) ($varValue)) == 0) {
                    $booleanStatus = FALSE;
                    $message = 'Wrong integer format';
                } else {
                    $varValue = (int) ($varValue);
                    $arrayToStore[$variableName] = $varValue;
                }
                return;
            } elseif ($format == 'bool' || $format == 'boolean') {
                $varValue = ($varValue == '' || $varValue == null || $varValue == '0' || $varValue == false || strtolower($inputData['is_metric_system']) == 'f' || $varValue == 'false') ? FALSE : TRUE;
                return;
            } elseif ($format == 'float' || $format == 'numeric' || $format == 'number') {
                $varValue = trim($varValue);
                if ($varValue == '' || (floatval($varValue) == 0 && $varValue != '')) {
                    $booleanStatus = FALSE;
                    $message = 'Wrong integer format';
                } else {
                    $varValue = floatval($varValue);
                    $arrayToStore[$variableName] = $varValue;
                }
                return;
            }

            return;
        }
    }

}

?>
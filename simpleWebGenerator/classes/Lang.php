<?php
	class Lang{
		var $langCode;
		var $langData;
		var $db;
		var $path;
		var $translationMap = array();
		var $lookupMapFile;
		var $separator=';';
		var $cellQuote='"';
		var $auth=null;
		var $defaultLang='en';
		public function __construct($langCode,$db,$path,$auth,$defaultLang){
			$this->path = $path;
			$this->db = $db;
			$this->auth = $auth;
			$this->defaultLang = $defaultLang;
			if ($langCode == null){

				$this->langCode = (($this->auth != null))
					? $this->auth->CurrentUser()->langcode
					: $this->defaultLang;
			    if ( $this->langCode == null ) 
			    	$this->langCode = $this->defaultLang;
			} else {
				$this->langCode = $langCode;
			}
			$this->lookupMapFile = $this->path."/".$this->langCode.".map.php";

			$this->loadLang();
			$this->lookupTranslationLoad();
		}

		private function loadTranslationMapFromFile(){
			if (!file_exists($this->lookupMapFile)) 
				$this->flushLookupTranslationsToFile();
			$this->translationMap = array();
			$data  = funcHelper::csvToArray($pathToCsv,$toMap = false,$this->separator,$this->cellQuote);
			foreach ($data as $row) {
				$this->translationMap[$row[0]]=	$row[1];
			}
		}

		public  function lookupTranslationLoad(){
			if (!file_exists($this->lookupMapFile)) {
				$this->flushLookupTranslationsToFile();
			}else {
				$this->loadTranslationMapFromFile();
			}
		}

	    public function loadAndTranslateFile($filePath){
	        if (!(file_exists($filePath) && $filePath != '')) 
	            return null ;

	        $content = file_get_contents($filePath);
	        $content =  $this->translateJs($content,'LANG',true);
	        return $content;
	    }

		public function translateOptionValuesFromLookupFile($options,$prefix,$sufix){
			$translatedOptions = array();
			foreach ($options as $option) {
				$key = $this->translateByKeyFileMap(strtoupper($option["Value"]),$prefix,$sufix);
				$translatedOptions[] = array('DisplayText' => $option['DisplayText'], 'Value' => $key);
			}
			return $translatedOptions;
		}

		public function flushLookupTranslationsToFile(){
			if ($this->translationMap==null || count($this->translationMap)==0) return;
			funcHelper::arrayToCsv($this->translationMap,$this->lookupMapFile,$overWrite = true,null);
		}

		public static function getLangFromCountryCode($cc){
			return (!in_array(strtolower($cc), Array('hr','ba','si','me','rs','mk'))) 
				? 'en' 
				: 'hr';//strtolower($cc);
		}
		public static function getLangFromLangCode($lc){
			return (!in_array(strtolower($lc), Array('hr','bs','sl','sr','mk'))) 
				? 'en' 
				: 'hr';//strtolower($lc);
		}
		public static function setBrowserLangToSession($lang){
			if ( !isset($_SESSION['browser_lang']) &&  isset($_SERVER["HTTP_ACCEPT_LANGUAGE"])){
				$_SESSION['browser_lang'] = Lang::getLangFromLangCode(strtolower(substr($_SERVER["HTTP_ACCEPT_LANGUAGE"], 0, 2)));
			}elseif (isset($_SESSION['browser_lang'])) {
				return "OK";
			}elseif (!isset($_SESSION['browser_lang'])){
				$_SESSION['browser_lang'] = Lang::getLangFromLangCode($lang);
			}
			return $_SESSION['browser_lang'];
		}
		//TODO not tested!
		public  function autoDetectLanguage(){
			$lang = 'en';

			if (isset($_SESSION['lang_code'])) {
                $lang = $_SESSION['lang_code'];
                if (!file_exists($this->path."/{$lang}.js")) {
                    $lang = "en";
                    $_SESSION['lang_code'] = $lang;  
                }

            } else {
                if (class_exists("User") && User::ValidSession()) {
                    $lang = User::CurrentUser()->langcode;
                /*print_r($_SESSION);*/
                }   else {
                    $_SESSION['lang_code'] = $lang;  
                } 
            }
            return $lang;
		}
		public function translatePhp($txt){

		}
		public function translateHtml($jsData,$varName){
			 return $this->translateJs($jsData,$varName,false);
		}				
		public function translateJs($jsData,$varName,$quoteValues){
			// $myLangData = funcHelper::cloneArray($this->langData);
			$myContent = $jsData;
			$myLangData = array();
			$quote =  ($quoteValues == true)? "'" :"";
			foreach ($this->langData as $key => $value) {
				$newKey = $varName.'["'.$key.'"]';
				$myLangData[$newKey] = $quote.$value.$quote;
			}
			// print_r($this->langData);
			$myContent = strtr($myContent,$myLangData);
			return $myContent; 
		}		
		public function translateDashboard($fileContent){
			$myContent = $fileContent;
			$myLangData = funcHelper::getKeysMatching($this->langData,'/.*_LIST$|.*_LIST_DESCRIPTION$/');
			$myLangData = funcHelper::addPrefixAndSufixToKeyAndValue($myLangData,'>','</');
			$myContent = strtr($myContent,$myLangData);

			return $myContent; 
		}		
		public function translateLeftMenu($menuData){
			$myContent = $menuData;
			$myLangData = funcHelper::getKeysMatching($this->langData,'/.*_MENU_ITEM$/');
			$myLangData = funcHelper::addPrefixAndSufixToKeyAndValue($myLangData,'>','</');
			$myContent = strtr($myContent,$myLangData);
			return $myContent; 
		}
		public function translateOptionValues($options,$prefix,$sufix){
			$translatedOptions = array();
			foreach ($options as $option) {
				$key = $this->translateByKey(strtoupper($option["Value"]),$prefix,$sufix);
				$translatedOptions[] = array('DisplayText' => $option['DisplayText'], 'Value' => $key);
			}
			return $translatedOptions;
		}
		public function translateFieldOptionValues($options){
			$translatedOptions = array();
			foreach ($options as $option) {
				$key = $this->translateByKey(strtoupper($option["Value"]),'FIELD_','');
				$translatedOptions[] = array('DisplayText' => $option['DisplayText'], 'Value' => $key);
			}
			return $translatedOptions;
		}
		public function translateByKeyFileMap($key,$prefix,$sufix){
			if (isset($this->translationMap[$key])){
				return $this->translationMap[$key];
			} elseif (isset($this->translationMap[strtoupper($key)])) {
				return $this->translationMap[strtoupper($key)];
			} elseif (isset($this->translationMap[$prefix.strtoupper($key).$sufix])) {
				return $this->translationMap[$prefix.strtoupper($key)];
			}else{
				return $key;
			}
		}		
	    private function sql2matrix($sql){
	        $result = pg_query($sql);
	        $matrix = array();
	        while ($row = pg_fetch_assoc($result)){
	            $matrix[]=$row;
	        }
	        return $matrix;
	    }
	    public function getLanguagesPDO() 
	    {
	    	if ($this->db == null){
	    		require_once ("dbClass.php");
	    		$this->db = new DbClass();
	    		$this->db->connect();
	    	}
			$sql='SELECT  * from languages';
			$this->db->setQuery($sql);
			return $this->db->sql2matrix();
/*
	        try
	        {
	        	$connectionStr = "pgsql:host=".Variables::$PG_SERVER.";port=".Variables::$PG_PORT.";dbname=".Variables::$PG_DB;
	            $db = new PDO($connectionStr, Variables::$PG_LOGIN, Variables::$PG_PASS);
	            $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);

	            $stmt = $db->prepare('SELECT  * from languages');
	            if($stmt->execute())
	            {
	                $row = $stmt->fetch(PDO::FETCH_OBJ);
	                if(!is_null($row) )
	                {
	                    //$key = $row->session_key;
	                    $matrix[]=$row;
	                    //echo  $row->name;
	                }
	            }
	            
	            $db = null;
	        }
	        catch(PDOException $e) {  
	            $response->status = ResponseCode::ServiceUnavailable;
	            print_r($response);
	        } 
*/

	        return $matrix;
	    }
		public function getLanguages(){
	        $sql = "SELECT id,name,lang_code
					FROM public.languages
					ORDER BY order by name ";

        	return $this->sql2matrix(sql);
		}
		public function translateByKey($key,$prefix,$sufix){
			// if ($this->db == null) return;
			// echo $key;
			// print_r($this->langData);
			if (isset($this->langData[$key])){
				return $this->langData[$key];
			} elseif (isset($this->langData[strtoupper($key)])) {
				return $this->langData[strtoupper($key)];
			} elseif (isset($this->langData[$prefix.strtoupper($key).$sufix])) {
				return $this->langData[$prefix.strtoupper($key)];
			}else{
				return $key;
			}
		}
		public static function removeEmptyOptions($optArr){
			$newArr = array();
			for ($i=0 ; $i< count($optArr); $i++){
				if ($optArr[$i]['DisplayText']==null || $optArr[$i]['DisplayText']=='' ) continue;
				$newArr[]=$optArr[$i];
			}

			return $newArr;
		}
		public function translateWord($key){

		}

		public function translateData($key,$pKeyValue){

		}

		private function loadLang(){
			// $content = file_get_contents($this->path.'/'.$this->langCode.'.js');
			// $content = str_replace("var LANG = ", "", $content);
			// // print_r($content);

			// $this->langData = json_decode($content,true);
			// echo json_last_error();
			// var_dump($this->langData);
			$this->langData = Array();
			$langPath = $this->path.'/'.$this->langCode.'.js';
			$lines = file($langPath, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
			foreach ($lines as $line_num => $line) {
				$lineArr = explode(":", $line);
				if ($lineArr == null || count($lineArr)<=1) {
					continue;
				} elseif (count($lineArr)==2) {
					$key = trim($lineArr[0]);
					$key = trim($key,'"');
					$key = ltrim($key,"\t");
					$val = ltrim($lineArr[1]);
				} else {
					$key = trim($lineArr[0]);
					$key = trim($key,'"');
					$key = ltrim($key,"\t");
					$key = ltrim($key);
					unset($lineArr[0]);
					$val = ltrim(join(':',$lineArr));
				}
				$val = rtrim($val,',');
				$val = trim($val,'"');
				$val = trim($val,"'");
			    $this->langData[$key] = $val;
			}
			// print_r($this->langData);
		}

		public function getLangData(){
			// print_r($this->langData);
			return $this->langData;
		}


	}

?>
<?php
error_reporting(9);
require_once dirname(__FILE__).'/SessionEncoderConf.php';
class SessionEncoder extends SessionEncoderConf {
    var $constructorExecuted = false;
    public function SessionEncoder(){ 
        if ($this->constructorExecuted==true)            return;
        //if (session_status() == PHP_SESSION_NONE) {session_start();}
        if (!isset($_SESSION)) session_start ();
        $this->constructorExecuted=TRUE;
    }
    
    public function __construct() {
        if ($this->constructorExecuted==true)            return;
        //if (session_status() == PHP_SESSION_NONE) {session_start();}
        if (!isset($_SESSION)) session_start ();
        $this->constructorExecuted=TRUE;
    }
    public  function valueToSession($key,$value){
        $encryptedKey = $this->encodeKey($key);
        $encryptedValue = $this->encryptData($value);
        $_SESSION[$encryptedKey] = $encryptedValue;
    }
    public  function valueFromSession($key){
        $encodedKey = $this->encodeKey($key);
        if (!isset($_SESSION[$encodedKey]))  {return null;}
        $encryptedValue= $_SESSION[$encodedKey];
        $decryptedValue = $this->decryptData($encryptedValue);
        return $decryptedValue;
    }
    public function removeKeyFromSession($key){
        $encodedKey = $this->encodeKey($key);
        if (!isset($_SESSION[$encodedKey]))  {return ;}
        unset($_SESSION[$encodedKey]);
    }

    private function encodeKey($keyName){
        $encodedKey =  base64_encode($this->encryptWithXor($keyName));
        $encodedKey = str_replace("==", "", $encodedKey);  
        return $encodedKey;
    }
    
    private function decodeKey($keyName){
        $encodedKey =  base64_decode($keyName);
        $encodedKey = $this->encryptWithXor($keyName);
        return $encodedKey;
    }

    public  function keyExists($key){
        $encodedKey = $this->encodeKey($key);
        return isset($_SESSION[$encodedKey]);
    }
    private function encryptWithXor($str) {  //http://stackoverflow.com/questions/14673551/encrypt-decrypt-with-xor-in-php
        $encrypted = '';
        for($i=0;$i<strlen($str);)
        {
            for($j=0;($j<strlen($this->keyPass) && $i<strlen($str));$j++,$i++)
            { $encrypted .= $str{$i} ^ $this->keyPass{$j}; }
        }  
        return $encrypted;
    }
    
    public function getAllKeys(){
        $keys=array_keys($_SESSION);
        
        for ($x=0; $x < count($keys); $x++) {
            $keys[$x]=  $this->decodeKey($keys[$x]);
        }
         
        return $keys;
    }
     private function decryptData($data){ //http://us3.php.net/mcrypt 
         $key = hash('SHA256', $this->salt . $this->pass, true);
         $iv = base64_decode(substr($data, 0, 22) . '==');
         $data = substr($data, 22);
         $decrypted = rtrim(mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, base64_decode($data), MCRYPT_MODE_CBC, $iv), "\0\4");
         $hash = substr($decrypted, -32);
         $decrypted = substr($decrypted, 0, -32);
         if (md5($decrypted) != $hash) return false;
         return $decrypted;
    }
      private function encryptData($data){ //http://us3.php.net/mcrypt
        $decrypted=$data;  
        $key = hash('SHA256', $this->salt . $this->pass, true);
        srand(); 
        $iv = mcrypt_create_iv(mcrypt_get_iv_size(MCRYPT_RIJNDAEL_128, MCRYPT_MODE_CBC), MCRYPT_RAND);
        if (strlen($iv_base64 = rtrim(base64_encode($iv), '=')) <> 22) return false;
        $encrypted = base64_encode(mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, $data . md5($data), MCRYPT_MODE_CBC, $iv));
        return $iv_base64 . $encrypted;
    }
}
?>
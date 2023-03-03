<?php
/**
    signelton se mora nakacit na bazu cija mu konekcija mora bit prosljedjena
*/

    class AuthClass{
        // const userSessionKey = 'current_user_session_storage_key_index';
        private  $userSessionKey = 'juzer_nekakvi';
        private $LoggedIn;      
        private $locked;

        private $dbLogin;
        private $dbPass;
        private $connectionString;
        private $connection;
        private $ezyDB;
        private $disableEmailAuthentication = false;
        private $hasUsername = false;

        function __construct($PDOconnection, $ezyConnection,$connectionString, $dbLogin,$dbPass,$disableEmailAuthentication,$hasUsername)
        {
            $this->connectionString = $connectionString;            
            $this->dbLogin          = $dbLogin          ;       
            $this->dbPass           = $dbPass           ;   
            $this->disableEmailAuthentication = $disableEmailAuthentication;
            $this->hasUsername = $hasUsername;
            if ($PDOconnection != null){
                $this->connection = $PDOconnection;
            } else if ($ezyConnection!=null){
                $this->ezyDB = $ezyDB;
            }else{
                $this->connection = new PDO($this->connectionString, $this->dbLogin, $this->dbPass);
            }
        }       

        private function CreatePassword($password)
        {
            $hasher = new PasswordHash(8, TRUE);
            $noviPass = $hasher->HashPassword($password);
            return $noviPass;
        }
        public  function CurrentUser()
        {
            // print_r($_SESSION);
            $user = null;
            if(isset($_SESSION[$this->userSessionKey]))  
                $user = $_SESSION[$this->userSessionKey];
            return $user;
        }

        public  function SetUser($user)
        {
            $_SESSION[$this->userSessionKey] = $user;
        }

        public  function ValidSession()
        {
            $user = $this->CurrentUser();
            return (isset($user) && !is_null($user) && $user->LoggedIn);
        }

        public  function Admin()
        {
            $user = $this->CurrentUser();
            return (isset($user) && !is_null($user) && $user->admin == 1);
        }
        public  function Authorized()
        {
            $user = $this->CurrentUser();
            return (isset($user) && !is_null($user) && $user->LoggedIn && $user->locked == 0);
        }
        public  function Logout()
        {
            $user = $this->CurrentUser();
            if(!is_null($user))
            {
                $user->LoggedIn = false;
                $this->SetUser($user);
                unset($_SESSION[$this->userSessionKey]);
            }
        }
        public  function GetProfile($userID, $forUserID)
        {
            $response['status'] = 'OK';
            try {
                $db= $this->connection; 
                $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);

                $sql="SELECT u.id as userId,
                        u.email,
                        u.first_name as firstName,
                        u.login as userName,
                        u.last_name as lastName,
                        u.loc_reason as lockedReason,
                        u.locked,
                        u.about_myself as bio,
                        u.created,
                        u.last_login as lastLogin,
                        u.nick_name as nickName,
                        u.admin,
                        u.country_id as countryID,
                        pc.place as countryName,
                        u.lang_id as langID,
                        l.lang_code as langCode,
                        u.role_id as roleID,
                        u.place_id as placeID,
                        p.place as cityName,
                        u.valute_id as valuteID,
                        u.use_metric_system as useMetricSystem                  
                        FROM users u
                        LEFT JOIN places p on p.id = u.place_id
                        LEFT JOIN places pc on pc.id = u.country_id
                        LEFT JOIN place_types pt on p.id = p.place_type_id
                        LEFT JOIN languages l on l.id = u.lang_id
                        WHERE u.id = :forUserID";
            $stmt = $db->prepare($sql);
            
            $sqlResult = $stmt->execute(array('forUserID' => $forUserID));
            // print_r($sqlResult);
            if ($sqlResult) {
                $row = $stmt->fetch(PDO::FETCH_OBJ);
                
                if ($this->hasUsername)
                    $row->displayName = $row->username;
                if (!empty($row->firstname) && !empty($row->lastname)) {
                    $row->displayName = "{$row->firstname} {$row->lastname}";
                }

                // $row->registerDate = Helper::TimeAgo($row->created);
                $response['item'] = $row;
            } else {
                $response['status'] = 'NotFound';
            }

            $db = null;
        } catch (PDOException $e) {
            $response['status'] = "ServiceUnavailable";
            // $log->logError($e->getMessage());
        }

        return $response;
    }



    public function Login($username, $password)
    {
        $hasher = new PasswordHash(8, TRUE);
        $response = array();
        $response['status'] = 'OK';

        try {
            $db = $this->connection;
            $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);


            if ($this->hasUsername) {
                $sqlAddition = " or u.login = :login" ;
                $arr = array( 'email' => $username , 'login' => $username);
            } else {
                $sqlAddition = "";
                $arr = array( 'email' => $username );
            }
            $sql = "SELECT u.id, u.pass, u.locked FROM users u WHERE ( u.email = :email $sqlAddition)";
            // echo "$sql";
            // print_r($arr);
            // throw new Exception("Error Processing Request", 1);
            
            $stmt = $db->prepare($sql);
            $queryResult = $stmt->execute($arr);
            if ($queryResult) {
                $row = $stmt->fetch(PDO::FETCH_OBJ);
                // print_r($row);
                if ($hasher->CheckPassword($password, $row->pass)) {
                    $response = $this->GetProfile(0, $row->id);
                    $response['item']->LoggedIn = true;
                    $this->SetUser($response['item']); //TODO viktor doda
                } else {
                    $response['status'] = 'Authentication failed';
                }
            } else {
                $response['status'] = 'Authentication failed'; 
            }

            $db = null;
        } catch (PDOException $e) {
            $response['status'] = 'Database problem';
        }

        return $response;
    }


    private function userExists($loginOrEmail){
        $val = addslashes($loginOrEmail);
        $sql = "select count(1) as cnt from users where '{$val}' in (email)";
        $db = $this->connection;
        $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
        $stmt = $db->prepare($sql);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_OBJ);
        return ($row != null && $row->cnt != 0);
    }

    public function getUserIdFromEmail($email){
        if (!$this->userExists($email)) return null;
        $val = addslashes($loginOrEmail);
        $sql = "select id from users where '{$val}' in (email)";
        $db = $this->connection;
        $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
        $stmt = $db->prepare($sql);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_OBJ);
        return $row->id;
    }

    public  function ConfirmRegistration($username, $token)
    {
        $response = array();
        $response['status'] = 'OK';
        try {
            $exists = $this->userExists($username);
            $userId = $this->getUserIdFromEmail($username);
            $exists = TRUE;
            if ($userId == null ) $exists= false;

            if ($exists) {
                try {
                    $params = array('userID' => $userId);

                    $db = $this->connection;
                    $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);

                    $stmt = $db->prepare("SELECT confirm_token FROM users WHERE id = :userID");
                    if ($stmt->execute(array('userID' => $userId))) {
                        $row = $stmt->fetch(PDO::FETCH_OBJ);
                        error_log( "user id={$userId}; token= {$token}");
                        if ($row->confirm_token == $token) {
                            $stmt2 = $db->prepare("UPDATE users SET confirm_token = NULL, loc_reason = NULL, locked = NULL WHERE id = :userID");
                            if (!$stmt2->execute($params)) {
                                $response['status'] = 'Conflict';
                                error_log( "Problem authenticating user: {$username}");
                            }
                        } else {
                            $response['status'] = 'Conflict';
                            error_log( "Wrong token: {$username},{$token}");
                        }
                    } else {
                        $response['status'] = 'NotFound';
                    }

                    $db = null;
                } catch (PDOException $e) {
                    $response['status'] = 'ServiceUnavailable';
                    error_log( $e->getMessage());
                }
            } else {
                $response['status'] = 'NotFound';
                error_log( "user not found: {$username}");
            }
        } catch (PDOException $e) {
            $response['status'] = 'ServiceUnavailable';
            error_log( $e->getMessage());
        }

        // $response->item = $user;
        return true;
    }



    private function GetCustomStatus($statusCode, $server)
    {
        $statusCodes[200]="OK";
        $statusCodes[401]="UNAUTHORIZED";
        $statusCodes[404]="NOT_FOUND";
        $statusCodes[400]="BAD_REQUEST";
        $ret= (isset($statusCodes[$statusCode])) 
            ? $statusCodes[$statusCode]
            : str_replace(" ", "_", strtoupper($server->getStatus($statusCode)));
        return $ret;
    }

  public function APIKeyValid($userID = 0)
  {
    //TODO ovde ne kuzin vezanost API keya sa userom
    return (isset($_SERVER['HTTP_API_KEY']) && !empty($_SERVER['HTTP_API_KEY'])) ;
  }
  public function getUserId(){
    // print_r($this->CurrentUser());
        $user = $this->CurrentUser();
        // print_r($user->userid);
        // throw new Exception("Error Processing Request", 1);
        
        return $user->userid;
  }
  public function isLoggedIn(){
        return ($this->CurrentUser() != null && $this->getUserId() != null);
  }
  private function setLastLogin(){
    if (! $this->isLoggedIn()) return;
    $params = array('userID' => $this->getUserId());
    $db = $this->connection;
    $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
    $stmt2 = $db->prepare("UPDATE users SET last_login = now() WHERE id = :userID");
    $stmt2->execute($params);
  }
  
//                $mail = new Email($email, $username, EMAIL, EMAIL_FROM, "Users - Confirm registration",
//                    "<!DOCTYPE html>
//                     <html>
//                        <head>
//                            <meta charset=\"utf-8\" />
//                            <style type=\"text/css\">
//                                    body {font: 12px sans-serif;}
//                            </style>
//                        </head>
//                        <body>
//                            <a href=\"" . BASE_WEB_URL . "/users/confirm/{$username}/{$token}\" alt=\"Confirm registration\">"
//                    . BASE_WEB_URL . "/users/confirm/{$username}/{$token}/
//                            </a>
//                        </body>
//                    </html>");

//                if (!$mail->Send()) {
//                    $response['status'] = $ret['status'];
//                    $log->logError($mail->ErrorMessage());
//                } else {
   private function GetRandomToken()
    {   
        $underscores = 2; // Maximum number of underscores allowed in password
        $length = 10; // Length of password
         
        $p ="";
        for ($i=0;$i<$length;$i++)
        {   
          $c = mt_rand(1,7);
          switch ($c)
          {
            case ($c<=2):
              // Add a number
              $p .= mt_rand(0,9);   
            break;
            case ($c<=4):
              // Add an uppercase letter
              $p .= chr(mt_rand(65,90));   
            break;
            case ($c<=6):
              // Add a lowercase letter
              $p .= chr(mt_rand(97,122));   
            break;
            case 7:
               $len = strlen($p);
              if ($underscores>0&&$len>0&&$len<($length-1)&&$p[$len-1]!="_")
              {
                $p .= "_";
                $underscores--;   
              }
              else
              {
                $i--;
                continue;
              }
            break;       
          }
        }
        return $p;
    }

    private function generateInsertSql($table,$hmFieldsValues,$returnField){
        if ($table == null || $table == '') return null;
        $sql = 'insert into '.$table.'(';

        $x=0;
        foreach ($hmFieldsValues as $key => $value) {
            if ($x>0)       $sql.=",";
            $sql.=$key;
            $x++;
        }

        $sql .= ") values (";
        $x=0;
        foreach ($hmFieldsValues as $key => $value) {
            if ($x>0)       $sql.=",";
            $sql.=$value;
            $x++;
        }
        $sql .= ")";

        if ($returnField != null)
            $sql.=" returning {$returnField}";

        return $sql;
    }

    public function sql2str($sql,$arr){
        $db=$this->connection;
        $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
        $stmt = $db->prepare($sql);
        // print_r($stmt);
        if ($arr != null){
            $sqlResult = $stmt->execute($arr);
            print_r($sqlResult);
        } else {
            $sqlResult = $stmt->execute();
        }
        if (! $sqlResult) return null;
// $stmt->debugDumpParams();
        // echo "string";
// print_r($sqlResult);
        $row = $stmt->fetch(PDO::FETCH_BOTH);


        return $row[0];
    }
    
    private function sendVerificationLink($userNameOrEmail,$token,$passChanging){
        $path = ($passChanging == true) 
            ? "messages/"
            : "messages/";

        $content=file_get_contents($path);
        $content = str_replace("#token#", $token, $content);
        $content = str_replace("#username#", $userNameOrEmail, $content);
        //TODO
    }

    public function forgotPassword($usernameOrEmail){
        //TODO
    }

    public function changePasswordWarn($usernameOrEmail,$token){
        //TODO
    }

    public function Register($username, $password, $email, $firstName, $lastName,$placeId)
    {
        $response = array();
        $response['status'] = 'OK';
        $hasher = new PasswordHash(8, TRUE);
        $user = new stdClass;
        if ($this->hasUsername) $user->username = $username;
        $user->email = $email;


        // $token = md5("{$username} | {$email}" . $this->GetRandomToken());
        $token = md5("{$username} | {$email}"). md5(uniqid(rand(), true));

        try {
            $exists = $this->userExists($username);
            if (! $exists) {

                    $db = $this->connection;
                    $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
                     
                    if($this->disableEmailAuthentication)
                    {
                        $hmUserData = array( 
                            "pass"    =>':password'
                            ,"email"   =>':email'
                            ,"first_name"  =>':firstName'
                            ,"last_name"   =>':lastName'
                            ,"place_id"   =>$placeId
                            ,"picture_available"   => 'FALSE'
                            // is_seller   =>FALSE
                        );                        
                    } else{
                        $hmUserData = array( 
                            "pass"    =>':password'
                            ,"email"   =>':email'
                            ,"first_name"  =>':firstName'
                            ,"last_name"   =>':lastName'
                            ,"locked"  =>'now()'
                            ,"loc_reason"  =>"'Awaiting email confirmation'"
                            ,"place_id"   =>$placeId
                            ,"picture_available"   => 'FALSE'
                            // is_seller   =>FALSE
                        );
                    }

                    if ($this->hasUsername) $hmUserData["login"]=':username';

                    $sql = $this->generateInsertSql("users",$hmUserData,'id');

                    $insertArr = array(
                        'password' => $hasher->HashPassword($password), 
                        'email' => $email,
                        'firstName' => $firstName, 
                        'lastName' => $lastName);

                    if ($this->hasUsername) $insertArr['username'] = $username;

                    $insertID = $this->sql2str($sql,$insertArr);
                    // echo "$sql $insertID";
                    $response['status'] = 'Conflict';

                    if ($insertID) 
                    {
                        // echo "aaaaaaaa";
                        $user = $this->GetProfile(0, $insertID);
                        $response['status'] = $user['status'];
                        // print_r($user);
                        $user = $user['item'];
                        // print_r($response);

                    }
            } else {
                $response['status'] = $exists['status'];
            }

        } catch (PDOException $e) {
            $response['status'] = 'ServiceUnavailable';
            error_log($e->getMessage());
        }

        $response['item'] = $user;
        return $response;

    }

}

?>
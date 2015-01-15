<?php
 require_once("BeanDataStore.php");
 require_once($ConstantsArray['dbServerUrl']. "BusinessObjects/User.php");
 define("UPDATE_DETAIL","update users set fullname = :fullname, password = :password where seq = :userseq");
 define("UPDATE_FULLNAME","update users set fullname = :fullname where seq = :userseq");
 class UserDataStore extends BeanDataStore{

    private static $userDataStore ;
    public static function getInstance()
    {
        if (!self::$userDataStore)
        {
            self::$userDataStore = new UserDataStore("User",User::$tableName);
                return self::$userDataStore;
        }
        return self::$userDataStore;
    }
    public function findByEmail($email){
        $colValuePair = array();
        $colValuePair["email"] = $email;
        $userList = self::executeConditionQuery($colValuePair);
        if(sizeof($userList) > 0){
            return $userList[0];
        }
        return null;
    }

    public function findByMobile($mobile){
        $colValuePair = array();
        $colValuePair["mobile"] = $mobile;
        $userList = self::executeConditionQuery($colValuePair);
         if(sizeof($userList) > 0){
            return $userList[0];
        }
        return null;
    }
    
    public function getExistingMobile($mobileNumbers){
        $params = '"'. implode('","', explode(',', $mobileNumbers)) .'"';
        $query = "select mobile from users where mobile in(" .$params .")";
        $mobiles = "";
        $existingArray = self::executeQuery($query);
        $mobiles = array();
        if(!empty($existingArray)){                     
           foreach($existingArray as $arr){
               array_push($mobiles,$arr[0]); 
           }
           $mobiles = implode(",",$mobiles);  
        } 
        return  $mobiles;
    }
    
    
    function updateDetail($userSeq,$fullName,$password){         
         $params = array();
         $params[":userseq"] = $userSeq;
         $params[":fullname"] = $fullName;
         $query = UPDATE_FULLNAME;
         if($password != null && $password != ""){
            $params[":password"] = $password;
            $query = UPDATE_DETAIL;    
         }
         self::executeParameterizedQuery($query,$params);        
    }
 }
?>

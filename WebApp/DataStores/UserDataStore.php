<?php
 require_once("BeanDataStore.php");
 require_once($ConstantsArray['dbServerUrl']. "BusinessObjects/User.php");

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

	
 }
?>

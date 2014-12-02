<?php
    require_once($ConstantsArray['dbServerUrl']. "DataStores/UserDataStore.php5");
    require_once($ConstantsArray['dbServerUrl']. "DataStores/UserCustomFieldsDataStore.php5");

class UserMgr{

    private static $userMgr;

    public static function getInstance()
    {
        if (!self::$userMgr)
        {
            self::$userMgr = new UserMgr();
            return self::$userMgr;
        }
        return self::$userMgr;
    }

    public function logInUser($username, $password){
        $userDataStore = UserDataStore::getInstance();
        $user = $userDataStore->findByUserName($username);
        if ($user != null){
            $userObj = new User();
            $userObj = $user;
            if($userObj->getPassword() == $password){
                return $userObj;
            }
        }
        return null;
    }





}

?>

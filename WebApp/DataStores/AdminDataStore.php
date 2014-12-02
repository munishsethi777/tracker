<?php
 require_once("BeanDataStore.php");
 require_once($ConstantsArray['dbServerUrl']. "BusinessObjects/Admin.php");

 class AdminDataStore extends BeanDataStore{
    private static $adminDataStore;

    public static function getInstance()
    {
        if (!self::$adminDataStore)
        {
            self::$adminDataStore = new AdminDataStore("Admin",Admin::$tableName);
                return self::$adminDataStore;
        }
        return self::$adminDataStore;
    }

    public function findByEmailId($emailId){
        $colValuePair = array();
        $colValuePair["email"] = $emailId;
        $adminList = self::executeConditionQuery($colValuePair);
        if(sizeof($adminList) > 0){
            return $adminList[0];
        }
        return null;
    }
 }
?>

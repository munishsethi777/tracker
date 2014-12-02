<?php
require_once($ConstantsArray['dbServerUrl']. "DataStores/AdminDataStore.php");
require_once($ConstantsArray['dbServerUrl']. "DataStores/UserDataStore.php");
class AdminMgr{

    private static $adminMgr;

    public static function getInstance()
    {
        if (!self::$adminMgr)
        {
            self::$adminMgr = new AdminMgr();
            return self::$adminMgr;
        }
        return self::$adminMgr;
    }
    public function logInAdmin($username, $password){
        $adminDataStore = AdminDataStore::getInstance();
        $admin = new Admin();
        $admin = $adminDataStore->findByEmailId($username);
        return $admin;
    }

    public function saveSubmitter(User $submitter){
        $userDataStore = UserDataStore::getInstance();
        return $userDataStore->save($submitter);
    }

    public static function getModulesDataJson($companySeq){
        $modules = AdminMgr::getModulesByCompany($companySeq);
        $fullArr = array();
        foreach($modules as $module1){
            $moduleObj = new Module();
            $moduleObj = $module1;

            $arr = array();
            $arr['id'] = $moduleObj->getSeq();
            $arr['title'] = $moduleObj->getTitle();
            $arr['description'] = $moduleObj->getDescription();
            array_push($fullArr,$arr);
        }
        return json_encode($fullArr);
    }

    public function getUsersGridJSON($companySeq){
        $customFieldsJSON = $this->getUserAllFieldsJsonByCompany($companySeq);
        $users =  $this->getUsersByCompany($companySeq);
        $usersJson = self::getUsersDataJson($users);

        $mainJsonArray = array();
        $mainJsonArray["columns"] = $customFieldsJSON;
        $mainJsonArray["data"] = $usersJson;
        return json_encode($mainJsonArray);
    }



}
?>

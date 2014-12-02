<?php
    require_once($ConstantsArray['dbServerUrl']. "DataStores/ModuleDataStore.php5");

class ModuleMgr{
    private static $moduleMgr;

    public static function getInstance()
    {
        if (!self::$moduleMgr)
        {
            self::$moduleMgr = new ModuleMgr();
            return self::$moduleMgr;
        }
        return self::$moduleMgr;
    }

    public static function getModulesDataJson($modules){
        $fullArr = array();
        foreach($modules as $module){
            $moduleObj = new Module();
            $moduleObj = $module;
            $arr = array();
            $arr['id'] = $moduleObj->getSeq();
            $arr['title'] = $moduleObj->getTitle();
            $arr['description'] = $moduleObj->getDescription();
            $arr['uploadedby'] = $moduleObj->getUploadedBy();
            $arr['dateofexpiry'] = $moduleObj->getDateOfDateOfExpiry();
            $arr['createdon'] = $moduleObj->getCreatedOn();
            //$arr['isenabled'] = $moduleObj->getIsEnabled();
            array_push($fullArr,$arr);
        }
        return json_encode($fullArr);
    }

    public function getModulesByCompany($companySeq){
        $moduleDataStore = ModuleDataStore::getInstance();
        $modules = $moduleDataStore->findByCompanySeq($companySeq);
        return $modules;
    }
     /*JSON Methods for Grids*/
    public function getModuleGridJSON($companySeq){
        $headerJSON = self::getHeadersJSON();
        $modules =  $this->getModulesByCompany($companySeq);
        $moduleJson = self::getModulesDataJson($modules);

        $mainJsonArray = array();
        $mainJsonArray["columns"] = $headerJSON;
        $mainJsonArray["data"] = $moduleJson;
        return json_encode($mainJsonArray);
    }

    public static function getHeadersJSON(){
        $fullArr = array();
        $arr = array();
        $arr['text'] = "Title";
        $arr['datafield'] = "title";
        array_push($fullArr,$arr);

        $arr = array();
        $arr['text'] = "Description";
        $arr['datafield'] = "description";
        array_push($fullArr,$arr);

        $arr = array();
        $arr['text'] = "Stakeholder";
        $arr['datafield'] = "uploadedby";
        array_push($fullArr,$arr);

        $arr = array();
        $arr['text'] = "Expiry Date";
        $arr['datafield'] = "dateofexpiry";
        array_push($fullArr,$arr);

        $arr = array();
        $arr['text'] = "Created On";
        $arr['datafield'] = "createdon";
        array_push($fullArr,$arr);

        //$arr = array();
//        $arr['text'] = "Enabled";
//        $arr['datafield'] = "isenabled";
//        array_push($fullArr,$arr);

        return json_encode($fullArr);

    }

    public function getModule($moduleSeq){
        $moduleDataStore = ModuleDataStore::getInstance();
        $module = $moduleDataStore->findBySeq($moduleSeq);
        return $module;
    }

}
?>

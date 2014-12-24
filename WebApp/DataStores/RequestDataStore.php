<?php
  require_once("BeanDataStore.php");
   
    require_once($ConstantsArray['dbServerUrl']. "BusinessObjects/Request.php");
    require_once($ConstantsArray['dbServerUrl']. "DataStores/GroupDataStore.php"); 
    define('UPDATE_STATUS', "update requests set status = :status where touser=:touser and groupseq = :groupseq");
   

 class RequestDataStore extends BeanDataStore{
    private static $requestDataStore;
    public static function getInstance()
    {
        if (!self::$requestDataStore)
        {
            self::$requestDataStore = new RequestDataStore(Request::$className,Request::$tableName);
                return self::$requestDataStore;
        }
        return self::$requestDataStore;
    }    
    public function updateRequest($status,$userseq,$groupseq){
      $params = array();
      $params[":status"] = $status;
      $params[":touser"] = $userseq;
      $params[":groupseq"] = $groupseq;
      self::executeParameterizedQuery(UPDATE_STATUS,$params); 
      if($status == RequestStatus::ACCEPTED){
         $gds = GroupDataStore::getInstance();
         $gds->setUserOnGroup($userseq,$groupseq); 
      }
    }
 }
?>

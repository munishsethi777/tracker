<?php
  require_once('../IConstants.inc');
  require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/User.php");
  require_once($ConstantsArray['dbServerUrl']. "DataStores/UserDataStore.php");
  require_once($ConstantsArray['dbServerUrl']. "DataStores/GroupDataStore.php");

  $response = new ArrayObject();
  $response["success"] = 1;
  $response["message"]  = "";
  $response["matches"] = 0;
  try{
        $seq = $_GET["seq"];
        $password = $_GET["pss"];
        $userDataStore = UserDataStore::getInstance();
        $user = $userDataStore->findBySeq($seq);
        if($user != null){
            if($user->getPassword() == $password){
                $response["matches"]  = 1;
                $response["useq"]  = intval($user->getSeq());
                $response["email"]  = $user->getEmail();
                $response["mobile"]  = $user->getMobile();
                $response["fullname"]  = $user->getFullName();
                $GDS = GroupDataStore::getInstance();
                $groups = $GDS->getGroupInformation($seq);
                $response["groups"] =  $groups;
            }
        }
  }catch(Exception $e){
        $response["message"]  = $e->getMessage();
        $response["success"] = 0;
  }
   
   
    header('Access-Control-Allow-Origin: *');
    header("Access-Control-Allow-Credentials: true");
    header('Access-Control-Allow-Methods: GET, PUT, POST, DELETE, OPTIONS');
    header('Access-Control-Max-Age: 1000');
    header('Access-Control-Allow-Headers: Content-Type, Content-Range, Content-Disposition, Content-Description');
    header("Content-type: application/json"); 
        
    echo json_encode($response);
    return;
?>
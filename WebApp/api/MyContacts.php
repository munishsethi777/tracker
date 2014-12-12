<?php
  require_once('../IConstants.inc');
  require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/User.php");
  require_once($ConstantsArray['dbServerUrl']. "DataStores/UserDataStore.php");
  $response = new ArrayObject();
  $response["success"] = 1;
  $response["message"]  = "";
  $response["numbers"] = 0;
  try{
    $mobileNumbers = $_GET["numbers"];
    $userDataStore = UserDataStore::getInstance();
    $existingMobiles = $userDataStore->getExistingMobile($mobileNumbers);
    $response["numbers"] = $existingMobiles;
  }catch(Exception $e){
     $response["success"] = 0;
     $response["message"]  = $e->getMessage(); 
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

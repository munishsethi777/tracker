<?php
  require_once('IConstants.inc');
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
  echo json_encode($response);
  return;
?>

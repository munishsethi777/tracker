<?php
    require_once('../IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/Group.php");
    require_once($ConstantsArray['dbServerUrl']. "DataStores/LocationDataStore.php");
    $response = new ArrayObject();
    $response["success"] = 0;
    $response["message"] = "";
    try{
        if(!isset($_GET["groupseq"])){
              throw new RuntimeException("Group seq is null !") ;
        } 
        $groupSeq = $_GET["groupseq"];
        $locationDataStore = LocationDataStore::getInstance(); 
        $locations = $locationDataStore->getLocationsByGroup($groupSeq);
        $mainJson = array();
         if(!empty($locations)){
            foreach($locations as $location){
             $arr = array();
             $arr["userseq"] = $location["userseq"];
             $arr["username"] = $location["fullname"];
             $arr["dated"] = $location["dated"];
             $arr["longitude"] = $location["longitude"];
             $arr["latitude"] = $location["latitude"];
             array_push($mainJson,$arr);
             }
         }
       
         $response["success"] = 1;
         $response["data"] = $mainJson;   
    }catch(Exception $e){
        $response["message"] = $e->getMessage(); 
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

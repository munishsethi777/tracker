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
        if(isset($_GET["dated"])){
            $datedStr  = $_GET["dated"];
            $date = new DateTime($datedStr);            
        }else{
            $date = new DateTime();
        }
        $dated = $date->format("m/d/Y");
        $locationDataStore = LocationDataStore::getInstance(); 
        $locations = $locationDataStore->getLocationsByGroup($groupSeq,$dated);
        $mainJson = array();
         if(!empty($locations)){
            foreach($locations as $location){
             $arr = array();
             $arr["userseq"] = $location->getUserSeq();
             $arr["dated"] = $location->getDated();
             $arr["longitude"] = $location->getLongitude();
             $arr["latitude"] = $location->getLatitude();
             array_push($mainJson,$arr);
             }
         }
         $json = json_encode($mainJson);
         $response["success"] = 1;
         $response["data"] = $json;   
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

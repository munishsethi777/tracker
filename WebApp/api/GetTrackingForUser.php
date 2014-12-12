<?php
    require_once('../IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/Location.php");
    require_once($ConstantsArray['dbServerUrl']. "DataStores/LocationDataStore.php");
    $response = new ArrayObject();
    $response["success"] = 1;
    $response["message"] = "";
    $json = "";
    try{
        if(!isset($_GET["userSeq"])){
            throw new RuntimeException("User Seq null !");          
        }
        $userSeq = $_GET["userSeq"];
        $locationDataStore = LocationDataStore::getInstance();  
        if(isset($_GET["dated"])){
            $datedStr  = $_GET["dated"];
            $date = new DateTime($datedStr);
            $dated =   $date->format("m/d/Y");
            $locations = $locationDataStore->getLocations($userSeq,$dated);  
        } else{
             $locations = $locationDataStore->getCurrentLocations($userSeq);
        }
         $mainJson = array();
         if(!empty($locations)){
            foreach($locations as $location ){
             $arr = array();
             $arr["dated"] = $location->getDated();
             $arr["longitude"] = $location->getLongitude();
             $arr["latitude"] = $location->getLatitude();
             array_push($mainJson,$arr);
             }
         }
         $json = json_encode($mainJson);
         $response["data"] = $json;   
         
    }catch(Exception $e){
        $response["success"] = 0;
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

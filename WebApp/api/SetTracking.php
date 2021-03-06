<?php
   require_once('../IConstants.inc');  
   require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/Location.php");
   require_once($ConstantsArray['dbServerUrl']. "DataStores/BeanDataStore.php");
   require_once($ConstantsArray['dbServerUrl']. "DataStores/GroupDataStore.php");
   $response = new ArrayObject();
   $response["success"] = 1;
   $response["message"]  = "";
    try{
        $data = $_GET['data'];
        //$data = '{"useq":1,"locations":[{"long":1212.12222221,"lat":121.343222243,"dated":"2014-12-02 12:15:19"},{"long":134324.1232323,"lat":34.2342224234,"dated":"2014-12-03 12:15:19"}]}';   
        $jsonArr = json_decode($data,true);
        if(count($jsonArr) > 0){
        $useq = $jsonArr['useq'];
        $locationArray = array();
        foreach($jsonArr['locations'] as $locationJson){
            $location = new Location();
            $location->setDated($locationJson['dated']);
            $location->setLatitude($locationJson['lat']);
            $location->setLongitude($locationJson['long']);
            $location->setUserSeq($useq);
            array_push($locationArray,$location);
        }
        $locationDataStore = new BeanDataStore("Location",Location::$tableName);
        $locationDataStore->saveList($locationArray);
        $response["groupRequests"] = getGroupRequest($useq);
    }   
    }catch(Exception $e){
       $response["success"] = 0;
       $response["message"]  = "Exception saving tracking : - " . $e->getMessage(); 
    }

    header('Access-Control-Allow-Origin: *');
    header("Access-Control-Allow-Credentials: true");
    header('Access-Control-Allow-Methods: GET, PUT, POST, DELETE, OPTIONS');
    header('Access-Control-Max-Age: 1000');
    header('Access-Control-Allow-Headers: Content-Type, Content-Range, Content-Disposition, Content-Description');
    header("Content-type: application/json");
    echo json_encode($response);
    return;
    
    
    
   function getGroupRequest($userSeq){
    try{
       $groupDataStore = GroupDataStore::getInstance();
       $objlist =  $groupDataStore->getPendingGroupRequests($userSeq);
       $grpJson = array();
       foreach($objlist as $obj){
          $json = array();
          $json["groupseq"] = $obj["groupSeq"];
          $json["groupname"] = $obj["groupName"];
          $json["grouprequester"] = $obj["groupRequester"];
          array_push($grpJson,$json);
       }
       return  $grpJson;
    }catch(Exception $e){
        return "";    
    }
    
   } 
?>

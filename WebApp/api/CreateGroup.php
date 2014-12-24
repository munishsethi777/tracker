<?php
    require_once('../IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/Group.php");
    require_once($ConstantsArray['dbServerUrl']. "DataStores/GroupDataStore.php");
    $response = new ArrayObject();
    $response["success"] = 0;
    $response["message"] = "";
    try{
        if(isset($_GET["name"]) && isset($_GET["userseq"])){
            $name = $_GET["name"];
            $userSeq = $_GET["userseq"];
            $group = new Group();
            $group->setName($name);
            $group->setAdminUserSeq($userSeq);
            $group->setCreatedOn(new DateTime());
            $group->setIsEnabled(true); 
            $GDS = GroupDataStore::getInstance();
            $id = $GDS->createGroup($group);
            $response["success"] = 1;
            $response["seq"] = $id; 
        }else{          
            $response["message"] = "Enter Data !";
        }
    } catch(Exception $e){       
        $response["message"] = $e-getMessage();
    }
    header('Access-Control-Allow-Origin: *');
    header("Access-Control-Allow-Credentials: true");
    header('Access-Control-Allow-Methods: GET, PUT, POST, DELETE, OPTIONS');
    header('Access-Control-Max-Age: 1000');
    header('Access-Control-Allow-Headers: Content-Type, Content-Range, Content-Disposition, Content-Description');
    header("Content-type: application/json"); 
    echo json_encode($response);
    return
?>

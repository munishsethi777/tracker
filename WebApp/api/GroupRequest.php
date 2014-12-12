<?php
    require_once('../IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/Request.php");
    require_once($ConstantsArray['dbServerUrl']. "DataStores/BeanDataStore.php");
    $response = new ArrayObject();
    $response["success"] = 0;
    $response["message"] = "";
    try{
        if(isset($_GET["byuserseq"]) && 
                isset($_GET["touserseq"]) && isset($_GET["groupseq"]) ){
              $byUserSeq = $_GET["byuserseq"];
              $toUserSeq = $_GET["touserseq"];
              $groupSeq  = $_GET["groupseq"];
              $request = new Request();
              $request->setByuser($byUserSeq);
              $request->setTouser($toUserSeq);
              $request->setCircleSeq($groupSeq);
              $request->setRequestDate(new DateTime()); 
              $request->setStatus("pending");
              $RDS = new BeanDataStore(Request::$className,Request::$tableName);
              $id = $RDS->save($request);
              $response["success"] = 1;
              $response["seq"] = $id;
       }else{
         $response["message"] = "Invalid Request data!";  
       }
    }catch(Exception $e){
       $response["message"] = $e->getMessage(); 
    }
    echo json_encode($response);
    return;
?>

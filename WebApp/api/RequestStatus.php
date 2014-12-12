<?php
    require_once('../IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/Request.php");
    require_once($ConstantsArray['dbServerUrl']. "DataStores/BeanDataStore.php");
    $response = new ArrayObject();
    $response["success"] = 0;
    $response["message"] = "";
    try{
        if(isset($_GET["requestseq"])) {
            $requestSeq = $_GET["requestseq"];
            $RDS = new BeanDataStore(Request::$className,Request::$tableName);
            $request = $RDS->findBySeq($requestSeq);
            if($request != null and $request != ""){
                $response["status"] = $request->getStatus();
            }else{
                throw new RuntimeException("No Request data exist for reuestSeq : -" .$requestSeq);
            }
                  
        }else{
           $response["message"] = "Request Seq null !";
        }
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

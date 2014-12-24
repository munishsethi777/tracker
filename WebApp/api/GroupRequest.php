<?php
    require_once('../IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/Request.php");
    require_once($ConstantsArray['dbServerUrl']. "DataStores/BeanDataStore.php");
    require_once($ConstantsArray['dbServerUrl']. "DataStores/UserDataStore.php");
    $response = new ArrayObject();
    $response["success"] = 0;
    $response["message"] = "";

    $UDS = UserDataStore::getInstance();
    $RDS = new BeanDataStore(Request::$className,Request::$tableName);

    try{
        if(isset($_GET["byuserseq"]) &&
                isset($_GET["tomobilenumber"]) && isset($_GET["groupseq"]) ){
              $byUserSeq = $_GET["byuserseq"];
              $toNumbers = $_GET["tomobilenumber"];
              $groupSeq  = $_GET["groupseq"];
              $numbersArr = explode(",",$toNumbers);
              foreach($numbersArr as $number){//looping over numbers to send requests
                try{
                    $user = $UDS->findByMobile($number);
                    if($user != null){
                        $request = new Request();
                        $request->setByuser($byUserSeq);
                        $request->setTouser($user->getSeq());
                        $request->setGroupSeq($groupSeq);
                        $request->setRequestDate(new DateTime());
                        $request->setStatus("pending");
                        $RDS->save($request);
                    }
                }catch(Exception $e){//while requesting
                    $duplicateErrorArr = explode('UniqueRequest', $e->getMessage());
                    if(!is_null($duplicateErrorArr[1])){
                        $response["alreadysent"] = $response["alreadysent"]. ",". $number;
                    }else{
                        $response["failure"] = $response["failure"]. ",". $number;
                    }


                }
              }

              $response["success"] = 1;
       }else{
         $response["message"] = "Invalid Request data!";
       }
    }catch(Exception $e){//while complete call
       $response["message"] = $e->getMessage();
    }
    echo json_encode($response);
    return;
?>

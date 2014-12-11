<?php
  require_once('../IConstants.inc');
  require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/User.php");
  require_once($ConstantsArray['dbServerUrl']. "DataStores/UserDataStore.php");
  $response = new ArrayObject();
  $response["success"] = 1;
  $response["message"]  = "";
  $response["seq"] = 0;
  try{
    $mobile = $_GET["mobile"];
    $email = $_GET["email"];
    $password = $_GET["password"];
    $fullName = $_GET["fullName"];
    $user = new User();
    $user->setCreatedOn(new DateTime());
    $user->setEmail($email);
    $user->setIsActivated(true);
    $user->setIsEnabled(true);
    $user->setPassword($password);
    $user->setFullName($fullName);
    $user->setMobile($mobile);
    $userDataStore = UserDataStore::getInstance();
    $seq = $userDataStore->save($user);
    $response["seq"] = $seq;
    if($seq != null){

    }
  }catch(Exception $e){
    if($e->getCode() == 23000){
        $response["success"] = 0;
        $response["message"]  = getMessage($e);
    }else{
       $response["message"]  = $e->getMessage();
    }
  }
   echo json_encode($response);
   return;

   function getMessage($e){
       $arr = explode('key', $e->getMessage());
       $key = $arr["1"];
       if(trim($key) ==  "'mobile_2'"){
           return "Mobile no is already exist";
       }else if(trim($key) == "'email_1'"){
           return "Email is alreadty exist";
       }
      return "";
   }


   function

?>

<?php
  require_once('IConstants.inc');
  require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/User.php");

  require_once($ConstantsArray['dbServerUrl'] ."Managers/AdminMgr.php");
  require_once($ConstantsArray['dbServerUrl'] ."Utils/SessionUtil.php");
  $HTTP_POST = $_GET;

  $call = $HTTP_POST["call"];
  $LOGIN = "loginAdmin";
  $CREATE_SUBMITTER = "createSubmitter";


  if($call == $LOGIN){
    $username = $HTTP_POST["username"];
    $password = $HTTP_POST["password"];
    $adminMgr = AdminMgr::getInstance();
    $admin = $adminMgr->logInAdmin($username,$password);
    if($admin){
      $sessionUtil = SessionUtil::getInstance();
      $sessionUtil->createAdminSession($admin);
      echo 1;
    }else{
      echo 0;
    }
    return;
  }

  if($call = $CREATE_SUBMITTER){
    $isdCode = $HTTP_POST['isdCode'];
    $mobile = $HTTP_POST['mobile'];
    $email = $HTTP_POST['email'];
    $fullName = $HTTP_POST['fullName'];

    $submitter = new User();
    $submitter->setMobile($isdCode.$mobile);
    $submitter->setEmail($email);
    $submitter->setFullName($fullName);
    $submitter->setIsEnabled(true);
    $submitter->setIsSubmitter(true);
    $submitter->setCreatedOn(new DateTime());
    $adminMgr = AdminMgr::getInstance();
    $response = $adminMgr->saveSubmitter($submitter);
    if($response[1]= null){
        echo 1 ;
    }else{
        echo 0;
    }
    return;
  }

?>

<?php
  require_once('../IConstants.inc');
  require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/User.php");
  require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects/Group.php");
  require_once($ConstantsArray['dbServerUrl']. "DataStores/UserDataStore.php");
  require_once($ConstantsArray['dbServerUrl']. "Utils/StringUtil.php"); 

  $response = new ArrayObject();
  $response["success"] = 1;
  $response["message"]  = "";
  $response["seq"] = 0;
  $response["isexists"]  = 0;
  try{
    $mobile = StringUtil::checkVal($_GET["mbl"]);
    $email = StringUtil::checkVal($_GET["eml"]);
    $password = StringUtil::checkVal($_GET["pss"]);
    $fullName = StringUtil::checkVal($_GET["fnm"]);
    
    
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
    $response["seq"] = intval($seq);
    $user->setSeq($seq);
    if($seq != null){
        $group = new Group();
        $group = createDefaultGroup($user);
        $response["groupseq"]= intval($group->getSeq());
        $response["groupname"]= $group->getName();
    }
  }catch(Exception $e){
        $bool = isDuplicateMobileError($e);
        if($bool == true){
            $earlierUser = $userDataStore->findByMobile($mobile);
            $response["seq"] = intval($earlierUser->getSeq());
            $response["message"]  = $e->getMessage();
            $response["isexists"]  = 1;
            $response["fullname"]  = $earlierUser->getFullName();
        }else{
            $response["success"] = 0;
            $response["message"]  = $e->getMessage();
        }
  }
    header('Access-Control-Allow-Origin: *');
    header("Access-Control-Allow-Credentials: true");
    header('Access-Control-Allow-Methods: GET, PUT, POST, DELETE, OPTIONS');
    header('Access-Control-Max-Age: 1000');
    header('Access-Control-Allow-Headers: Content-Type, Content-Range, Content-Disposition, Content-Description');
    header("Content-type: application/json"); 
    
    echo json_encode($response);
    return;
   
   function isDuplicateMobileError($e){
        $arr = explode('Duplicate', $e->getMessage());
        return !is_null($arr[1]);   
   }
   
   function createDefaultGroup(User $user){
        $userFullNameArr = explode(" ",$user->getFullName());
        $groupName = $userFullNameArr[0];
        $groupDS = new BeanDataStore("Group",Group::$tableName);
        $group = new Group();
        $group->setAdminUserSeq($user->getSeq());
        $group->setCreatedOn(new DateTime());
        $group->setIsEnabled(true);
        $group->setName($groupName."'s Group");
        $seq = $groupDS->save($group);
        $group->setSeq($seq);
        return $group;
   }
?>
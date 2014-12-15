<?php
    require_once("BeanDataStore.php");
    require_once($ConstantsArray['dbServerUrl']. "BusinessObjects/Group.php");
    define('GET_GROUPS_SQL', "select groups.name,groups.seq,groups.adminuserseq,users.seq as userseq,users.fullname " .
        "from (select * from groupusers where userseq=:userseq)g " . 
        "inner join groupusers on g.groupseq = groupusers.groupseq " . 
        "inner join groups on g.groupseq = groups.seq " .
        "inner join users on groupusers.userseq = users.seq");
 class GroupDataStore extends BeanDataStore{
    private static $groupDataStore;
    public static function getInstance()
    {
        if (!self::$groupDataStore)
        {
            self::$groupDataStore = new GroupDataStore(Group::$className,Group::$tableName);
                return self::$groupDataStore;
        }
        return self::$groupDataStore;
    }
    
    public function getGroupInformation($userSeq){
        $params = array();
        $params[":userseq"] = $userSeq; 
        $groupInfoList = self::executeParameterizedQuery(GET_GROUPS_SQL,$params);
        if(empty($groupInfoList)){
           return ""; 
        }
        $groupJson = array();
        $mainJson = array();
        $userArray = ""; 
         
        foreach($groupInfoList as $groupInfo){
           $grpName = $groupInfo["name"];           
           if(array_key_exists($grpName,$mainJson)){
                 $groupJson =  $mainJson[$grpName];                
           }else{
               $groupJson["gname"] = $groupInfo["name"];
               $groupJson["gseq"] = $groupInfo["seq"];
               $groupJson["adminSeq"] = intval($groupInfo["adminuserseq"]);
               $userArray = array();  
           }
           $userJson = array();
           $userJson["useq"] =   $groupInfo["userseq"];
           $userJson["uname"] =   $groupInfo["fullname"];
           array_push($userArray,$userJson);          
           $groupJson["users"] = $userArray;  
           $mainJson[$grpName] =  $groupJson;
           
        }
        $mainJsonArr = array_values($mainJson);
        return $mainJsonArr;    
    }
 }
?>

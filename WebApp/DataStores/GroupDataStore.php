<?php
    require_once("BeanDataStore.php");
    require_once($ConstantsArray['dbServerUrl']. "BusinessObjects/Group.php");
    define('GET_GROUPS_SQL', "select  groups.name,groups.seq from groupusers " .
    "inner join groups on groupusers.groupseq = groups.seq " .
    "where groupusers.userseq=:userseq");
    define ('GET_PENDING_REQUESTS',"select groups.seq as groupSeq, users.fullname as groupRequester,groups.name as groupName " .
        "from requests left join users on users.seq = requests.byuser left join groups on groups.seq = requests.groupseq " .
        "where requests.status = 'pending' and requests.touser = :userseq");

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
        $mainJson = array();
        foreach($groupInfoList as $groupInfo){
            $groupJson = array();
            $groupJson["gname"] = $groupInfo["name"];
            $groupJson["gseq"] = $groupInfo["seq"];
               array_push($mainJson,$groupJson);
        }
        $mainJsonArr = array_values($mainJson);
        return $mainJsonArr;
    }

    public function getPendingGroupRequests($userSeq){
        $params = array();
        $params[":userseq"] = $userSeq;
        $groupsRequestsList = self::executeParameterizedQuery(GET_PENDING_REQUESTS,$params);
        return $groupsRequestsList;
    }

 }
?>

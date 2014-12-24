<?php
  require_once("BeanDataStore.php");
  require_once($ConstantsArray['dbServerUrl']. "BusinessObjects/Location.php");

 class LocationDataStore extends BeanDataStore{
    private static $locationDataStore;
    public static function getInstance()
    {
        if (!self::$locationDataStore)
        {
            self::$locationDataStore = new LocationDataStore("Location",Location::$tableName);
            return self::$locationDataStore;
        }
        return self::$locationDataStore;
    }
    
    public function getLocations($userSeq,$dated){
        $sql = "select * from locations where DATE_FORMAT(dated,'%m/%d/%Y') = '".$dated."'";
        $locationList = self::executeObjectQuery($sql);
        if(sizeof($locationList) > 0){
            return $locationList;
        }
        return null;
    } 
    public function getLocationsByGroup($groupSeq){
        $SQL = "select l.*,users.fullname from (select * from locations order by locations.seq desc)as l " .
            "inner join groupusers on l.userseq = groupusers.userseq " .
            "inner join users on l.userseq = users.seq " .  
            " where groupusers.groupseq = " 
            . $groupSeq .  " GROUP BY l.userseq";
        $locationArr = self::executeQuery($SQL);
        if(!empty($locationArr)){
            return $locationArr;
        }
        return null; 
    }
    
    public function getCurrentLocations($userSeq){
        $date = new DateTime();
        $dated =   $date->format("m/d/Y"); 
        $sql = "select * from locations where DATE_FORMAT(dated,'%m/%d/%Y') = '".$dated."' ORDER BY seq DESC LIMIT 1";
        $locationList = self::executeObjectQuery($sql);
        if(sizeof($locationList) > 0){
            return $locationList;
        }
        return null;
    }
 }
?>

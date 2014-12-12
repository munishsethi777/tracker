<?php
  class Group{
      public static $tableName = "groups";
      private $seq,$name,$adminuserseq,$createdon,$isenabled;

      public function setSeq($seq_){
          $this->seq = $seq_;
      }
      public function getSeq(){
        return $this->seq;
      }

      public function setName($name){
          $this->name = $name;
      }
      public function getName(){
          return $this->name;
      }

      public function setAdminUserSeq($userSeq){
          $this->adminuserseq = $userSeq;
      }
      public function getAdminUserSeq(){
          return $this->adminuserseq;
      }
      public function setCreatedOn($createdon_){
        $this->createdon = $createdon_;
      }
      public function getCreatedOn(){
            return $this->createdon;
      }

      public function setIsEnabled($isEnabled_){
        $this->isenabled = $isEnabled_;
      }
      public function getIsEnabled(){
         return $this->isenabled;
      }
  }
?>
